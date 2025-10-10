/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: UnusedFunctionParameterInspection.java, Class: UnusedFunctionParameterInspection
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

package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import consulo.util.collection.ContainerUtil;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Inspects function calls and checks whether the given parameters are actually used in the function definition.<p/>
 *
 * <p>This inspection is not capable to evaluate the control flow, e.g. parameter references in unreachable if
 * statements are still evaluated.<p/>
 *
 * @author jansorg
 * @since 2010-12-28
 */
@ExtensionImpl
public class UnusedFunctionParameterInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "UnusedFunctionParams";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Unused function parameter");
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Unused parameter";
    }

    @Override
    public String getStaticDescription() {
        return "Detects unused function parameter values. " +
                "If the caller passes a parameter value " +
                "which is not used inside of the function then it is highlighted.";
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitGenericCommand(BashCommand bashCommand) {
                if (bashCommand.isFunctionCall()) {
                    BashFunctionDef functionDef = (BashFunctionDef) bashCommand.getReference().resolve();
                    if (functionDef != null) {
                        List<BashPsiElement> callerParameters = bashCommand.parameters();
                        List<BashVar> usedParameters = functionDef.findReferencedParameters();

                        Set<String> definedParamNames = new HashSet<>(ContainerUtil.map(usedParameters, var -> var.getReference().getReferencedName()));

                        //if the all parameter expansion feature is used consider all params as used
                        if (definedParamNames.contains("*") || definedParamNames.contains("@")) {
                            return;
                        }

                        int paramsCount = callerParameters.size();
                        for (int i = 0; i < paramsCount; i++) {
                            String paramName = String.valueOf(i + 1);

                            if (!definedParamNames.contains(paramName)) {
                                holder.registerProblem(callerParameters.get(i), getShortName(), LocalQuickFix.EMPTY_ARRAY);
                            }
                        }
                    }
                }

            }
        };
    }

}
