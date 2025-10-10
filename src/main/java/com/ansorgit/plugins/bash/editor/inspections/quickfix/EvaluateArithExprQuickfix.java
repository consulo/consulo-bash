/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: EvaluateArithExprQuickfix.java, Class: EvaluateArithExprQuickfix
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
package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import consulo.codeEditor.Editor;
import consulo.language.psi.PsiFile;
import consulo.document.util.TextRange;
import consulo.project.Project;
import consulo.language.util.IncorrectOperationException;

/**
 * Replaces a static arithmetic expression with the evaluated result.
 *
 * @author jansorg
 * @since 2009-11-15
 */
public class EvaluateArithExprQuickfix extends AbstractBashQuickfix {
    private final ArithmeticExpression expression;

    public EvaluateArithExprQuickfix(ArithmeticExpression expression) {
        this.expression = expression;
    }

    @Nonnull
    @Override
    public LocalizeValue getName() {
        return LocalizeValue.localizeTODO("Replace '" + expression.getText() + "' with the result '" + expression.computeNumericValue() + "'");
    }

    public void invoke(@Nonnull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        TextRange r = expression.getTextRange();
        String replacement = String.valueOf(expression.computeNumericValue());
        editor.getDocument().replaceString(r.getStartOffset(), r.getEndOffset(), replacement);
    }
}