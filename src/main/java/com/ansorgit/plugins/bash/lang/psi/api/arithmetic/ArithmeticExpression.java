/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: ArithmeticExpression.java, Class: ArithmeticExpression
 * Last modified: 2011-01-26 19:51
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

package com.ansorgit.plugins.bash.lang.psi.api.arithmetic;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;

import jakarta.annotation.Nullable;
import java.util.List;

/**
 * User: jansorg
 * Date: Feb 6, 2010
 * Time: 10:56:51 AM
 */
public interface ArithmeticExpression extends BashPsiElement {


    enum Type {
        NoOperands,
        TwoOperands,
        PrefixOperand,
        PostfixOperand,
        Unsupported;
    }
    boolean isStatic();

    /**
     * The contained subexpressions.
     *
     * @return The list of contained subexpressions. Can be an empty list.
     */
    List<ArithmeticExpression> subexpressions();

    /**
     * Computes the numeric value of this expression. This only is possible if this expressions is static.
     *
     * @return The numeric value. If not isStatic an IllegalStateException is thrown.
     */
    long computeNumericValue();

    /**
     * Tries to find a parent expression.
     *
     * @return The parent arithmetic expression. If not available, null is returned.
     */
    @Nullable
    ArithmeticExpression findParentExpression();

    /**
     * Returns the operator element if an operator is found in this expression.
     * @return
     */
    @Nullable
    PsiElement findOperatorElement();

    @Nullable
    IElementType findOperator();
}
