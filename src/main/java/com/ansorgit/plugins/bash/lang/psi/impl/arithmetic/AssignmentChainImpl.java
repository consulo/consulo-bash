/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: AssignmentChainImpl.java, Class: AssignmentChainImpl
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

package com.ansorgit.plugins.bash.lang.psi.impl.arithmetic;

import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.AssignmentChain;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;

import java.util.List;

/**
 * User: jansorg
 * Date: Feb 6, 2010
 * Time: 12:13:49 PM
 */
public class AssignmentChainImpl extends AbstractExpression implements AssignmentChain {
    @Override
    protected Long compute(long currentValue, IElementType operator, Long nextExpressionValue) {
        throw new UnsupportedOperationException("compute is not supported");
    }

    public AssignmentChainImpl(final ASTNode astNode) {
        super(astNode, "ArithAssignmentExpr", Type.Unsupported);
    }

    @Override
    public long computeNumericValue() {
        //find the child after the assignment sign
        List<ArithmeticExpression> childs = subexpressions();
        if (childs.size() != 2) {
            throw new IllegalStateException("impossible state");
        }

        return childs.get(1).computeNumericValue();
    }
}