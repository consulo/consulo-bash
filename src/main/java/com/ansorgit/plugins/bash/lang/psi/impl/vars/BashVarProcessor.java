/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashVarProcessor.java, Class: BashVarProcessor
 * Last modified: 2013-04-30
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.Keys;
import com.ansorgit.plugins.bash.lang.psi.util.BashAbstractProcessor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.MultiMap;
import consulo.util.collection.Sets;
import consulo.util.dataholder.Key;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.Set;

/**
 * Date: 14.04.2009
 * Time: 17:34:42
 *
 * @author Joachim Ansorg
 */
public class BashVarProcessor extends BashAbstractProcessor implements Keys {
    private final boolean leaveInjectionHost;
    private BashVar startElement;
    private boolean checkLocalness;
    private String varName;
    private boolean startElementIsVarDef;
    private boolean ignoreGlobals;

    private final Set<PsiElement> visitedScopes = Sets.newIdentityHashSet();

    public BashVarProcessor(BashVar startElement, boolean checkLocalness) {
        this(startElement, checkLocalness, true);
    }

    public BashVarProcessor(BashVar startElement, boolean checkLocalness, boolean leaveInjectionHosts) {
        super(false);

        this.startElement = startElement;
        this.checkLocalness = checkLocalness;
        this.varName = startElement.getReference().getReferencedName();
        this.startElementIsVarDef = startElement instanceof BashVarDef;
        this.ignoreGlobals = false;
        this.leaveInjectionHost = leaveInjectionHosts;
    }

    public boolean execute(@Nonnull PsiElement psiElement, ResolveState resolveState) {
        if (visitedScopes.contains(psiElement)) {
            return true;
        }

        if (psiElement instanceof BashVarDef) {
            BashVarDef varDef = (BashVarDef) psiElement;

            if (!varName.equals(varDef.getName()) || startElement.equals(psiElement)) {
                //proceed with the search
                return true;
            }

            //we have the same name, so it's a possible hit
            //now check the scope
            boolean varDefIsLocal = checkLocalness && varDef.isFunctionScopeLocal();
            boolean isValid = varDefIsLocal
                    ? isValidLocalDefinition(varDef, resolveState)
                    : isValidDefinition(varDef, resolveState);

            //if we found a valid local variable definition we must ignore all (otherwise matching) global variable definitions
            ignoreGlobals = ignoreGlobals || (isValid && varDefIsLocal);

            if (isValid) {
                storeResult(varDef, BashPsiUtils.blockNestingLevel(varDef));
                return false;
            }
        }

        visitedScopes.add(psiElement);
        return true;
    }

    private boolean isValidDefinition(BashVarDef varDef, ResolveState resolveState) {
        if (varDef.isCommandLocal()) {
            return false;
        }

        if (!varDef.isStaticAssignmentWord()) {
            return false;
        }

        BashFunctionDef varDefScope = BashPsiUtils.findNextVarDefFunctionDefScope(varDef);
        if (ignoreGlobals && varDefScope == null) {
            return false;
        }

        //first case: the definition is before the start element -> the definition is valid
        //second case: the definition is after the start element:
        //  - if startElement and varDef do NOT share a common scope -> varDef is only valid if it's inside of a function definition, i.e. global
        //  - if startElement and varDef share a scope which different from the PsiFile -> valid if the startElement is inside of a function def
        //this check is only valid if both elements are in the same file

        boolean sameFiles = this.leaveInjectionHost
                ? BashPsiUtils.findFileContext(startElement).equals(BashPsiUtils.findFileContext(varDef))
                : startElement.getContainingFile().equals(varDef.getContainingFile());

        BashFunctionDef startElementScope = BashPsiUtils.findNextVarDefFunctionDefScope(startElement);

        if (sameFiles) {
            int textOffsetStart = BashPsiUtils.getFileTextOffset(startElement);
            int textOffsetDef = BashPsiUtils.getFileTextOffset(varDef);
            if (textOffsetStart >= textOffsetDef) {
                return isDefinitionOffsetValid(varDefScope);
            }

            //the found varDef is AFTER the startElement
            if (varDefScope == null) {
                //if varDef is on global level, then it is only valid if startElement is inside of a function definition
                return BashPsiUtils.findNextVarDefFunctionDefScope(startElement) != null;
            }

            //varDef has a valid function def scope AND comes after the start element
            //in this case it is only valid if start element is in a nested function definition inside of varDefScope
            if (startElementScope != null) {
                return PsiTreeUtil.isAncestor(varDefScope, startElementScope, true);
            }
        } else {
            //working on a definition in an included file (maybe even over several include-steps)
            MultiMap<VirtualFile, PsiElement> includedFiles = resolveState.get(visitedIncludeFiles);
            Collection<PsiElement> includeCommands = includedFiles != null ? includedFiles.get(varDef.getContainingFile().getVirtualFile()) : null;

            if (includeCommands == null || includeCommands.isEmpty()) {
                return false;
            }

            PsiElement includeCommand = includeCommands.iterator().next();
            BashFunctionDef includeCommandScope = BashPsiUtils.findNextVarDefFunctionDefScope(includeCommand);

            //now check the offset of the include command
            int startOffset = BashPsiUtils.getFileTextOffset(startElement);
            int endOffset = BashPsiUtils.getFileTextOffset(includeCommand);
            if (startOffset >= endOffset) {
                return isDefinitionOffsetValid(includeCommandScope);
            }

            //the include command comes AFTER the start element
            if (includeCommandScope == null) {
                return BashPsiUtils.findNextVarDefFunctionDefScope(includeCommand) != null;
            }

            if (startElementScope != null) {
                return PsiTreeUtil.isAncestor(varDefScope, includeCommandScope, true);
            }
        }

        return false;
    }

    private boolean isDefinitionOffsetValid(BashFunctionDef varDefScope) {
        //the var def is only valid if the varDef is NOT inside of a nested function (our rule is: more global is better)

        BashFunctionDef startElementScope = BashPsiUtils.findNextVarDefFunctionDefScope(startElement);
        if (startElementScope == null) {
            //if the start element is on global level, then the var def has to be global, too, if the start element is a var def, also
            //if it it just a variabale which references the definition, then varDef is a valid definition for it
            return varDefScope == null || !startElementIsVarDef;
        }

        return varDefScope == null || varDefScope.equals(startElementScope) || !PsiTreeUtil.isAncestor(startElementScope, varDefScope, true);
    }

    /**
     * A local var def is a valid definition for our start element if it's scope contains the start
     * element.
     * <p/>
     * Also, the checked variable definition has to appear before the start element.
     *
     * @param varDef       The variable definition in question
     * @param resolveState
     * @return True if varDef is a valid local definition for startElement
     */
    private boolean isValidLocalDefinition(BashVarDef varDef, ResolveState resolveState) {
        boolean validScope = PsiTreeUtil.isAncestor(BashPsiUtils.findEnclosingBlock(varDef), startElement, false);

        //fixme: this is not entirely true, think of a function with a var redefinition of a local variable of the inner functions
        //context (i.e. the outer function)
        //for now, this is ok
        return validScope && BashPsiUtils.getFileTextOffset(varDef) < BashPsiUtils.getFileTextOffset(startElement);
    }

    public <T> T getHint(Key<T> key) {
        if (key.equals(VISITED_SCOPES_KEY)) {
            return (T) visitedScopes;
        }

        return null;
    }
}
