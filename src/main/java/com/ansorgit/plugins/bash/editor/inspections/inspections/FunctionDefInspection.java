/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: FunctionDefInspection.java, Class: FunctionDefInspection
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

import com.ansorgit.plugins.bash.editor.inspections.quickfix.FunctionBodyQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.psi.PsiElementVisitor;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;

/**
 * In Bash a function definition must not have a command block as body. A body block is good style, though.
 * This inspection offers a quickfix to wrap the body in a block.
 * <p/>
 * Date: 15.05.2009
 * Time: 15:14:04
 *
 * @author Joachim Ansorg
 */
@ExtensionImpl
public class FunctionDefInspection extends AbstractBashInspection {
    @Pattern("[a-zA-Z_0-9.]+")
    @Nonnull
    @Override
    public String getID() {
        return "WrapFunction";
    }

    @Nonnull
    public String getShortName() {
        return "Wrap function body";
    }

    @Nls
    @Nonnull
    public String getDisplayName() {
        return "Wrap function body in {}";
    }

    @Override
    public String getStaticDescription() {
        return "If the body of a function is not yet wrapped in curly brackets {} this inspection" +
                "offers a quickfix to automatically do this.";
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitFunctionDef(BashFunctionDef functionDef) {
                if (isOnTheFly && !functionDef.hasCommandGroup() && functionDef.body() != null) {
                    holder.registerProblem(functionDef.body(), getShortName(), new FunctionBodyQuickfix(functionDef));
                }
            }
        };
    }
}
