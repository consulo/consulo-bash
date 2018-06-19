/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashLexer.java, Class: BashLexer
 * Last modified: 2013-05-02
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ansorgit.plugins.bash.lang.lexer;

import com.ansorgit.plugins.bash.lang.BashVersion;
import com.intellij.psi.tree.TokenSet;
import consulo.lang.LanguageVersion;

/**
 * The actual Bash lexer. This lexer is based on te JFlex lexer generated by the bash.flex lexer definition.
 * The class _BashLexer is created by running the "ant" build script in this project.
 * <p>
 * This lexer merges string characters into words, thus string parsing is made a bit easier and Ctrl+W
 * works a bit better for plain text words inside of strings.
 * <p>
 * Date: 22.03.2009
 * Time: 12:31:22
 *
 * @author Joachim Ansorg
 */
public final class BashLexer extends MergingLexer implements BashTokenTypes
{
	public BashLexer()
	{
		super(new _BashLexer(BashVersion.Bash_v4), MergeTuple.create(TokenSet.create(STRING_CHAR), WORD));
	}

	public BashLexer(LanguageVersion languageVersion)
	{
		super(new _BashLexer(BashVersion.Bash_v4), MergeTuple.create(TokenSet.create(STRING_CHAR), WORD));
	}
}
