package com.ansorgit.plugins.bash.lang;

import com.ansorgit.plugins.bash.editor.highlighting.BashSyntaxHighlighter;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.highlight.SingleLazyInstanceSyntaxHighlighterFactory;
import consulo.language.editor.highlight.SyntaxHighlighter;
import jakarta.annotation.Nonnull;

@ExtensionImpl
public class BashHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
    @Override
    @Nonnull
    protected SyntaxHighlighter createHighlighter() {
        return new BashSyntaxHighlighter();
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return BashLanguage.INSTANCE;
    }
}
