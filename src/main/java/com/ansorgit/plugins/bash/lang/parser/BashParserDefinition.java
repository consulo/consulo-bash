/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashParserDefinition.java, Class: BashParserDefinition
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

package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.BashLanguage;
import com.ansorgit.plugins.bash.lang.lexer.BashLexer;
import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.ansorgit.plugins.bash.lang.psi.BashPsiCreator;
import com.ansorgit.plugins.bash.lang.psi.impl.BashFileImpl;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;

import jakarta.annotation.Nonnull;

/**
 * Defines the implementation of the Bash parser. This is  the starting point for the parse.
 * This class is referenced in the plugin.xml file.
 *
 * @author Joachim Ansorg
 */
@ExtensionImpl
public class BashParserDefinition implements ParserDefinition, BashElementTypes
{
	@Nonnull
	@Override
	public Language getLanguage()
	{
		return BashLanguage.INSTANCE;
	}

	@Override
	@Nonnull
	public Lexer createLexer(@Nonnull LanguageVersion languageVersion)
	{
		return new BashLexer(languageVersion);
	}

	@Nonnull
	@Override
	public PsiParser createParser(@Nonnull LanguageVersion languageVersion)
	{
		return new BashParser();
	}

	@Nonnull
	@Override
	public IFileElementType getFileNodeType()
	{
		return FILE;
	}

	@Override
	@Nonnull
	public TokenSet getWhitespaceTokens(@Nonnull LanguageVersion languageVersion)
	{
		return BashTokenTypes.whitespaceTokens;
	}

	@Override
	@Nonnull
	public TokenSet getCommentTokens(@Nonnull LanguageVersion languageVersion)
	{
		return BashTokenTypes.commentTokens;
	}

	@Override
	@Nonnull
	public TokenSet getStringLiteralElements(@Nonnull LanguageVersion languageVersion)
	{
		return BashTokenTypes.editorStringLiterals;
	}

	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode leftAst, ASTNode rightAst)
	{
		final IElementType left = leftAst.getElementType();
		final IElementType right = rightAst.getElementType();

		if(left == BashTokenTypes.LINE_FEED || right == BashTokenTypes.LINE_FEED || left == BashTokenTypes.ASSIGNMENT_WORD)
		{
			return SpaceRequirements.MUST_NOT;
		}

		if(left == BashTokenTypes.LEFT_PAREN || right == BashTokenTypes.RIGHT_PAREN || left == BashTokenTypes.RIGHT_PAREN || right == BashTokenTypes.LEFT_PAREN

				|| left == BashTokenTypes.LEFT_CURLY || right == BashTokenTypes.LEFT_CURLY || left == BashTokenTypes.RIGHT_CURLY || right == BashTokenTypes.RIGHT_CURLY

				|| (left == BashTokenTypes.WORD && right == BashTokenTypes.PARAM_EXPANSION_OP_UNKNOWN)

				|| left == BashTokenTypes.LEFT_SQUARE || right == BashTokenTypes.RIGHT_SQUARE || left == BashTokenTypes.RIGHT_SQUARE || right == BashTokenTypes.LEFT_SQUARE

				|| left == BashTokenTypes.VARIABLE || right == BashTokenTypes.VARIABLE)
		{

			return SpaceRequirements.MAY;
		}

		return SpaceRequirements.MUST;
	}

	@Override
	@Nonnull
	public PsiElement createElement(ASTNode node)
	{
		return BashPsiCreator.createElement(node);
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new BashFileImpl(viewProvider);
	}
}
