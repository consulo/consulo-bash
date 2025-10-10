/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: DuplicateFunctionDefInspection.java, Class: DuplicateFunctionDefInspection
 * Last modified: 2013-01-25
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
package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.impl.command.BashFunctionProcessor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * This inspection highlights duplicate function definitions.
 *
 * @author jansorg
 * @since 2009-10-31
 */
@ExtensionImpl
public class DuplicateFunctionDefInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "DuplicateFunction";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Duplicate function";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Duplicate function definition");
    }

    @Override
    public String getStaticDescription() {
        return "Detects duplicate function definitions and highlights the double definitions. " +
            "There is a chance of false positives if the earlier definition is inside of a conditional command.";
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitFunctionDef(BashFunctionDef functionDef) {
                BashFunctionProcessor p = new BashFunctionProcessor(functionDef.getName(), true);

                boolean isOnGlobalLevel = BashPsiUtils.findNextVarDefFunctionDefScope(functionDef) == null;
                PsiElement start = functionDef.getContext() != null && !isOnGlobalLevel
                    ? functionDef.getContext()
                    : functionDef.getPrevSibling();

                if (start != null) {
                    PsiTreeUtil.treeWalkUp(p, start, functionDef.getContainingFile(), ResolveState.initial());

                    List<PsiElement> results = new ArrayList<>(p.getResults());
                    results.remove(functionDef);

                    if (results.size() > 0) {
                        //find the result which has the lowest textOffset in the file
                        PsiElement firstFunctionDef = results.get(0);
                        for (PsiElement e : results) {
                            if (e.getTextOffset() < firstFunctionDef.getTextOffset()) {
                                firstFunctionDef = e;
                            }
                        }


                        if (firstFunctionDef.getTextOffset() < functionDef.getTextOffset()) {
                            String message = "The function '" + functionDef.getName() +
                                "' is already defined at line " + BashPsiUtils.getElementLineNumber(firstFunctionDef) + ".";

                            BashFunctionDefName nameSymbol = functionDef.getNameSymbol();
                            if (nameSymbol != null) {
                                holder.registerProblem(
                                    nameSymbol,
                                    message,
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                                );
                            }

                        }
                    }
                }
            }
        };
    }
}
