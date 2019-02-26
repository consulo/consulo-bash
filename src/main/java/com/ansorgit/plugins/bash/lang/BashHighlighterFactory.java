package com.ansorgit.plugins.bash.lang;

import javax.annotation.Nonnull;

import com.ansorgit.plugins.bash.editor.highlighting.BashSyntaxHighlighter;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;

public class BashHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory
{
	@Override
	@Nonnull
	protected SyntaxHighlighter createHighlighter()
	{
		return new BashSyntaxHighlighter();
	}
}
