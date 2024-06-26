/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: ArrayUseOfSimpleVarInspection.java, Class: ArrayUseOfSimpleVarInspection
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

package com.ansorgit.plugins.bash.editor.inspections.inspections;

import consulo.annotation.component.ExtensionImpl;
import jakarta.annotation.Nonnull;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.editor.inspection.ProblemsHolder;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;

/**
 * This inspection detects use of array variables without array element qualifiers.
 */
@ExtensionImpl
public class ArrayUseOfSimpleVarInspection extends AbstractBashInspection {
    @Pattern("[a-zA-Z_0-9.]+")
    @Nonnull
    @Override
    public String getID() {
        return "ArrayUseOfSimple";
    }

    @Nonnull
    public String getShortName() {
        return "Array use of a simple variable";
    }

    @Nls
    @Nonnull
    public String getDisplayName() {
        return "Array use of a simple, non-array variable";
    }

    @Override
    public String getStaticDescription() {
        return "Detects array element references to simple variables which are defined as array variables.";
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
            public void visitVarUse(BashVar var) {
                if (var.isArrayUse()) {
                    BashVarDef definition = (BashVarDef) var.getReference().resolve();
                    if (definition != null && !definition.isArray()) {
                        holder.registerProblem(var, "Array use of non-array variable", ProblemHighlightType.WEAK_WARNING);
                    }
                }
            }
        };
    }
}