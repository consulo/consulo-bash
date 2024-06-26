/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: HereDocParsing.java, Class: HereDocParsing
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

package com.ansorgit.plugins.bash.lang.parser.misc;

import com.ansorgit.plugins.bash.lang.parser.*;
import com.ansorgit.plugins.bash.lang.parser.util.ParserUtil;
import consulo.util.lang.Pair;
import consulo.language.parser.PsiBuilder;

import jakarta.annotation.Nullable;

/**
 * Here-docs are started in a command with a redirect.
 * <p/>
 * Date: 11.04.2009
 * Time: 20:57:24
 *
 * @author Joachim Ansorg
 */
public class HereDocParsing implements ParsingTool {
    public boolean parseOptionalHereDocs(BashPsiBuilder builder) {
        if (!builder.getHereDocData().expectsHereDoc()) {
            return false;
        }

        return doParsing(builder);
    }

    private boolean doParsing(BashPsiBuilder builder) {
        builder.eatOptionalNewlines(1, true);

        while (!builder.eof() && builder.getHereDocData().expectsHereDoc()) {
            final String expectedEnd = builder.getHereDocData().expectedDocEnd();

            //read lines until we found the end of the current here-doc
            final BashSmartMarker hereDocMarker = new BashSmartMarker(builder.mark());

            boolean foundPrefixedEnd = false;
            boolean foundExactEnd = false;

            Pair<String, BashSmartMarker> line = readLine(builder);

            int readLines = 1;

            while (line != null) {
                foundExactEnd = expectedEnd.equals(line.first);
                foundPrefixedEnd = !foundExactEnd && expectedEnd.equals(line.first.trim());

                if (foundPrefixedEnd || foundExactEnd) {
                    //we've found our end marker, the heredoc marker should end before this end marker, though
                    //so we need to rollback to the start of the line, finish the document marker and then
                    //read in the end marker line again

                    line.second.rollbackTo();

                    if (readLines > 1) {
                        builder.eatOptionalNewlines();
                        hereDocMarker.done(HEREDOC_ELEMENT);
                    } else {
                        hereDocMarker.drop();
                    }

                    line = readLine(builder);

                    line.second.done(HEREDOC_END_MARKER_ELEMENT);

                    //don't eat the newline after the end token, it's the command separator (needed in loops, etc)
                    break;
                }

                line.second.drop();

                line = readLine(builder);
                readLines++;
            }

            if (foundPrefixedEnd) {
                if (hereDocMarker.isOpen()) {
                    hereDocMarker.drop();
                }

                ParserUtil.error(builder, "parser.heredoc.expectedEnd");
                builder.getHereDocData().reset();//fixme check this

                return true;
            } else if (!foundExactEnd) {
                //ParserUtil.error(hereDocMarker, "parser.heredoc.expectedEnd");
                hereDocMarker.drop();

                ParserUtil.error(builder, "parser.heredoc.expectedEnd");
                builder.getHereDocData().reset();

                return true;
            }

            //this could happen if the users enters text at the end of a document
            //in this case the while loop is never entered and the marker is never closed
            if (hereDocMarker.isOpen()) {
                hereDocMarker.drop();
            }

            builder.getHereDocData().removeExpectedEnd();
        }

        return false;
    }

    /**
     * Returns the read line and a yet UNCLOSED marker
     *
     * @param builder The builder to read from
     * @return A pair of line text and UNCLOSED marker. Null if the builder has no more tokens.
     */
    @Nullable
    private Pair<String, BashSmartMarker> readLine(BashPsiBuilder builder) {
        if (builder.eof()) {
            return null;
        }

        StringBuilder string = new StringBuilder();

        PsiBuilder.Marker lineMarker = builder.mark();

        builder.eatOptionalNewlines(-1, true);

        PsiBuilder.Marker elementMarker = builder.mark();
        while (!builder.eof() && builder.getTokenType(true) != LINE_FEED) {
            //we have to do this because var.isValid does not preservere whitespace in all cases
            //we make sure that we rollback after the check
            boolean isValidVariable;

            PsiBuilder.Marker whitespaceMarker = builder.mark();
            isValidVariable = Parsing.var.isValid(builder);
            whitespaceMarker.rollbackTo();

            if (isValidVariable) {
                //collapse all elements before into a WORD token
                elementMarker.collapse(WORD);

                Parsing.var.parse(builder);

                elementMarker = builder.mark();
            }

            if (string.length() > 0) { //isEmpty is JDK6
                string.append(" ");
            }

            string.append(builder.getTokenText(true));

            if (!isValidVariable && builder.getTokenType() != WHITESPACE) {
                //if we had a valid variable on this line the variable parser already advanced
                //the token stream just after the variable tokens, i.e. we do not need to advance further
                //in case of variable parsing
                builder.advanceLexer();
            }
        }
        elementMarker.collapse(WORD);

        return Pair.create(string.toString(), new BashSmartMarker(lineMarker));
    }

    /**
     * Helper method to read a heredoc start marker from the psi document.
     *
     * @param builder WHere to read from
     * @return The text string of the marker. Does NOT include the optional string start/end markers
     */
    public static String readHeredocMarker(BashPsiBuilder builder) {
        String markerText;
        HereDocData.MarkerType evalMode;

        if (builder.getTokenType() == STRING_BEGIN) {
            ParserUtil.getTokenAndAdvance(builder); //string start
            markerText = builder.getTokenText();
            ParserUtil.markTokenAndAdvance(builder, HEREDOC_START_MARKER_ELEMENT);
            ParserUtil.getTokenAndAdvance(builder); //string end

            evalMode = HereDocData.MarkerType.NoEval;
        } else if (builder.getTokenType() == STRING2) {
            markerText = builder.getTokenText();
            if (markerText != null && markerText.startsWith("'") && markerText.endsWith("'")) {
                markerText = markerText.substring(1, markerText.length() - 1);
            }

            ParserUtil.markTokenAndAdvance(builder, HEREDOC_START_MARKER_ELEMENT);

            evalMode = HereDocData.MarkerType.NoEval;
        } else {
            markerText = builder.getTokenText();
            ParserUtil.markTokenAndAdvance(builder, HEREDOC_START_MARKER_ELEMENT);
            evalMode = HereDocData.MarkerType.Eval;
        }

        builder.getHereDocData().addExpectedDoc(markerText, evalMode, false);

        return builder.getTokenText();
    }
}
