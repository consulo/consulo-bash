/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: AbstractLoopParser.java, Class: AbstractLoopParser
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

package com.ansorgit.plugins.bash.lang.parser.shellCommand;

import com.ansorgit.plugins.bash.lang.parser.BashPsiBuilder;
import com.ansorgit.plugins.bash.lang.parser.Parsing;
import com.ansorgit.plugins.bash.lang.parser.ParsingFunction;
import com.ansorgit.plugins.bash.lang.parser.ParsingTool;
import com.ansorgit.plugins.bash.lang.parser.misc.ShellCommandParsing;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import consulo.language.parser.PsiBuilder;
import consulo.language.ast.IElementType;

/**
 * Abstract base class for loops.
 * <p/>
 * Date: 02.05.2009
 * Time: 17:39:14
 *
 * @author Joachim Ansorg
 */
public class AbstractLoopParser implements ParsingTool, ParsingFunction {
    private final IElementType startToken;
    private final IElementType commandMarker;

    public AbstractLoopParser(IElementType startToken, IElementType commandMarker) {
        this.startToken = startToken;
        this.commandMarker = commandMarker;
    }

    public boolean isValid(BashPsiBuilder builder) {
        return builder.getTokenType() == startToken;
    }

    public boolean parse(BashPsiBuilder builder) {
        /*  Grammar:
            WHILE compound_list DO compound_list DONE
            UNTIL compound_list DO compound_list DONE
         */

        final PsiBuilder.Marker loopMarker = builder.mark();
        builder.advanceLexer();

        if (!Parsing.list.parseCompoundList(builder, false)) {
            loopMarker.drop();
            return false;
        }

        if (!ParserUtil.checkNextOrError(builder, loopMarker, ShellCommandParsing.DO_KEYWORD, "parser.shell.expectedDo")) {
            return false;
        }

        if (!Parsing.list.parseCompoundList(builder, true)) {
            //builder.error("x");
            loopMarker.drop();
            return false;
        }

        if (!ParserUtil.checkNextOrError(builder, loopMarker, ShellCommandParsing.DONE_KEYWORD, "parser.shell.expectedDone")) {
            return false;
        }

        loopMarker.done(commandMarker);
        return true;
    }
}
