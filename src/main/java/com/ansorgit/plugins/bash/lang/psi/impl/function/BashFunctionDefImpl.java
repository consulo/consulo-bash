/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFunctionDefImpl.java, Class: BashFunctionDefImpl
 * Last modified: 2013-05-05
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

package com.ansorgit.plugins.bash.lang.psi.impl.function;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashElementSharedImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashGroupImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFunctionDefStub;
import com.ansorgit.plugins.bash.lang.psi.util.BashChangeUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.google.common.collect.Lists;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.scope.util.PsiScopesUtilCore;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import consulo.ide.IconDescriptorUpdaters;
import consulo.ui.image.Image;
import consulo.util.dataholder.Key;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * @author Joachim Ansorg
 */
public class BashFunctionDefImpl extends BashBaseStubElementImpl<BashFunctionDefStub> implements BashFunctionDef, StubBasedPsiElement<BashFunctionDefStub> {
    private static final Logger log = Logger.getInstance("#Bash.BashFunctionDefImpl");

    public BashFunctionDefImpl(ASTNode astNode) {
        super(astNode, "bash function()");
    }

    public BashFunctionDefImpl(@Nonnull BashFunctionDefStub stub, @Nonnull IStubElementType nodeType) {
        super(stub, nodeType, null);
    }

    public PsiElement setName(@Nonnull @NonNls String name) throws IncorrectOperationException {
        if (StringUtil.isEmpty(name)) {
            return null;
        }

        //fixme validate name

        final PsiElement nameNode = getNameSymbol();
        final PsiElement newNameSymbol = BashChangeUtil.createSymbol(getProject(), name);

        if (log.isDebugEnabled()) {
            log.debug("renamed to symbol " + newNameSymbol);
        }

        getNode().replaceChild(nameNode.getNode(), newNameSymbol.getNode());
        return this;
    }

    @Override
    public String getName() {
        return getDefinedName();
    }

    public BashBlock body() {
        BashGroupImpl bashGroup = findChildByClass(BashGroupImpl.class);

        if (log.isDebugEnabled()) {
            log.debug("found commandGroup: " + bashGroup);
        }

        return bashGroup;
    }

    public boolean hasCommandGroup() {
        log.debug("hasCommandGroup");

        final BashBlock body = body();

        return body != null && body.isCommandGroup();
    }

    private static final Key<BashFunctionDefName> NAME_SYMBOL_KEY = Key.create("nameSymbol");

    public BashFunctionDefName getNameSymbol() {
        final BashFunctionDefName nameWord = findChildByClass(BashFunctionDefName.class);

        if (log.isDebugEnabled()) {
            log.debug("getNameSymbole result: " + nameWord);
        }

        putUserData(NAME_SYMBOL_KEY, nameWord);

        return nameWord;
    }

    public List<PsiComment> findAttachedComment() {
        return BashPsiUtils.findDocumentationElementComments(this);
    }

    @Nonnull
    public List<BashVar> findReferencedParameters() {
        //call the visitor to find all uses of the parameter varaiables
        Collection<BashVar> usedVariables = PsiTreeUtil.collectElementsOfType(this, BashVar.class);

        List<BashVar> parameters = Lists.newLinkedList();

        for (BashVar var : usedVariables) {
            if (var.isParameterReference()) {
                parameters.add(var);
            }
        }

        return parameters;
    }

    private Key<String> DEFINED_NAME_KEY = Key.create("definedName");

    public String getDefinedName() {
        BashFunctionDefName symbol = getNameSymbol();
        String result = symbol == null ? "" : symbol.getNameString();

        putUserData(DEFINED_NAME_KEY, result);

        return result;
    }

    public int getTextOffset() {
        final ASTNode name = getNameSymbol().getNode();
        return name != null ? name.getStartOffset() : super.getTextOffset();
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                return getName() + "()";
            }

            public String getLocationString() {
                return null;
            }

            public Image getIcon() {
                return IconDescriptorUpdaters.getIcon(BashFunctionDefImpl.this, 0);
            }
        };
    }

    public PsiElement getNameIdentifier() {
        return getNameSymbol();
    }

    @Override
    public void accept(@Nonnull PsiElementVisitor visitor) {
        if (visitor instanceof BashVisitor) {
            ((BashVisitor) visitor).visitFunctionDef(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement place) {
        if (!processor.execute(this, state)) {
            return false;
        }

        //the group does its own processing, do not duplicate it here
        if (!hasCommandGroup()) {
            return BashElementSharedImpl.walkDefinitionScope(this, processor, state, lastParent, place);
        }

        return PsiScopesUtilCore.walkChildrenScopes(this, processor, state, lastParent, place);
    }
}
