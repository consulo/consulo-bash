/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: PreIncrementExpr.java, Class: PreIncrementExpr
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

package com.ansorgit.plugins.bash.lang.parser.arithmetic;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import consulo.language.parser.PsiBuilder;

/**
 * Parsing of prefix increment operators.
 * <p/>
 * User: jansorg
 * Date: Feb 6, 2010
 * Time: 4:27:55 PM
 */
class PreIncrementExpr implements ArithmeticParsingFunction {
    private ArithmeticParsingFunction next;

    PreIncrementExpr(ArithmeticParsingFunction next) {
        this.next = next;
    }

    public boolean isValid(BashPsiBuilder builder) {
        return arithmeticPreOps.contains(builder.getTokenType()) || next.isValid(builder);
    }

    public boolean parse(BashPsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        boolean mark = ParserUtil.conditionalRead(builder, arithmeticPreOps);

        boolean ok = next.parse(builder);

        if (mark) {
            marker.done(ARITH_PRE_INC_ELEMENT);
        } else {
            marker.drop();
        }

        return ok;
    }
}