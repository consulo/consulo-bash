/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: FloatArithmeticInspection.java, Class: FloatArithmeticInspection
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

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ProductExpression;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;
import jakarta.annotation.Nonnull;

/**
 * This inspection detects floating point arithmetic using Bash arithmetic (so it probably does not produce
 * the intended results).
 * <p/>
 * Date: 15.05.2009
 * Time: 14:56:55
 *
 * @author Joachim Ansorg
 */
@ExtensionImpl
public class FloatArithmeticInspection extends AbstractBashInspection {

    @Pattern("[a-zA-Z_0-9.]+")
    @Nonnull
    @Override
    public String getID() {
        return "FloatArithmetic";
    }

    @Nonnull
    public String getShortName() {
        return "Integer division with remainder found.";
    }

    @Nls
    @Nonnull
    public String getDisplayName() {
        return "Integer division with remainder. Maybe you wanted floating point arithmetic (unsupported in Bash)?";
    }

    @Override
    public String getStaticDescription() {
        return "Detects floating point quotients in static expressions (e.g. '3/4') which give a probably unexpected result.";
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.INFO;
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitArithmeticExpression(ArithmeticExpression expression) {
                if (!expression.isStatic()) {
                    return;
                }

                if (expression instanceof ProductExpression) {
                    ProductExpression product = (ProductExpression) expression;

                    if (product.hasDivisionRemainder()) {
                        holder.registerProblem(expression, getShortName());
                    }
                }
            }
        };
    }
}