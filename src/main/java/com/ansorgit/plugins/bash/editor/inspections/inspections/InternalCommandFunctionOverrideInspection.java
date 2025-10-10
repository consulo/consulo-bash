/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: InternalCommandFunctionOverrideInspection.java, Class: InternalCommandFunctionOverrideInspection
 * Last modified: 2011-04-30 16:33
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

/**
 * This inspection detects function names which override internal bash commands.
 */
@ExtensionImpl
public class InternalCommandFunctionOverrideInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "InternalCommandFunctionOverride";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Function overrides internal command";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Function overrides internal command");
    }

    @Override
    public String getStaticDescription() {
        return "Detects function definitions which override built-in Bash commands.";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitFunctionDef(BashFunctionDef functionDef) {
                if (LanguageBuiltins.isInternalCommand(functionDef.getName())) {
                    PsiElement targetElement = functionDef.getNameSymbol();
                    if (targetElement == null) {
                        targetElement = functionDef.getNavigationElement();
                    }

                    holder.registerProblem(targetElement, "Function overrides internal Bash command", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}