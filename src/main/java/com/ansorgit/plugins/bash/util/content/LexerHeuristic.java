/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: LexerHeuristic.java, Class: LexerHeuristic
 * Last modified: 2010-02-28 19:53
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

package com.ansorgit.plugins.bash.util.content;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.BashLanguage;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import consulo.language.ast.IElementType;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.project.Project;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Lexes the file and evaluates the characteristics of the lexing process.
 * This operation is quite expensive in time and memory. If possible it should be executed very seldomly.
 * <p/>
 * User: jansorg
 * Date: Feb 20, 2010
 * Time: 5:32:41 PM
 */
class LexerHeuristic implements ContentHeuristic {
    private final double badCharacterWeight;
    private final double tokenLimitWeight;
    private final double tokenWeight;
    private final double modeWeight;

    public LexerHeuristic(double tokenWeight, double modeWeight) {
        badCharacterWeight = 0.3;
        tokenLimitWeight = 0.1;

        this.tokenWeight = tokenWeight;
        this.modeWeight = modeWeight;
    }

    @Override
	public double isBashFile(File file, String data, Project project) {
        ParserDefinition definition = ParserDefinition.forLanguage(BashFileType.BASH_LANGUAGE);

        Lexer lexer = definition.createLexer(BashLanguage.INSTANCE.getVersions()[0]);
        lexer.start(data);

        int tokenCount = 0;
        Set<IElementType> tokenSet = new HashSet<>();
        Set<Integer> modeSet = new HashSet<>();
        while (lexer.getTokenType() != BashTokenTypes.BAD_CHARACTER && lexer.getTokenType() != null) {
            tokenSet.add(lexer.getTokenType());
            modeSet.add(lexer.getState());

            lexer.advance();
            tokenCount++;
        }

        double score = 0;
        if (lexer.getTokenType() == BashTokenTypes.BAD_CHARACTER) {
            score -= badCharacterWeight;
        }

        if (tokenCount > 4) {
            score += tokenLimitWeight;
        }


        score += Math.min(0.45, (double) tokenSet.size() * tokenWeight);
        score += Math.min(0.45, (double) modeSet.size() * modeWeight);

        return score;
    }
}
