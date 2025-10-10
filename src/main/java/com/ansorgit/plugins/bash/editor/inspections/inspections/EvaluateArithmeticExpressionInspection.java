/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: EvaluateArithmeticExpressionInspection.java, Class: EvaluateArithmeticExpressionInspection
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

import com.ansorgit.plugins.bash.editor.inspections.quickfix.EvaluateArithExprQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ParenthesesExpression;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

import java.util.List;

/**
 * Evaluate a static arithmetic expression. Offers a quickfix to insert the replacement value.
 *
 * @author jansorg
 * @since 2009-11-15
 */
@ExtensionImpl
public class EvaluateArithmeticExpressionInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "EvaluateArithmeticExpression";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Evaluate arithmetic expression";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Evaluate an arithmetic expression");
    }

    @Override
    public String getStaticDescription() {
        return "Replaces a static arithmetic expression with the result. " +
            "For example the expression 1 + 3 * 4 would be replaced with the evaluated result of 13.";
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
            public void visitArithmeticExpression(ArithmeticExpression expression) {
                if (!isOnTheFly) {
                    return;
                }

                boolean isParenthesisExpr = expression instanceof ParenthesesExpression;

                List<ArithmeticExpression> subexpressions = expression.subexpressions();
                if (subexpressions.size() > 1 && (expression.isStatic() || isParenthesisExpr)) {
                    ArithmeticExpression parent = expression.findParentExpression();

                    //run only if the parent is not a static expression itself
                    if (parent == null || !parent.isStatic()) {
                        String template =
                            "Replace '" + expression.getText() + "' with the evaluated result of '" + expression.computeNumericValue() + "'";
                        holder.registerProblem(expression, template, new EvaluateArithExprQuickfix(expression));
                    }
                }
            }
        };
    }
}