package consulo.bash;

import com.ansorgit.plugins.bash.editor.highlighting.BashSyntaxHighlighter;
import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.AttributesFlyweightBuilder;
import consulo.colorScheme.EditorColorSchemeExtender;
import consulo.colorScheme.EditorColorsScheme;
import consulo.colorScheme.EffectType;
import consulo.ui.color.RGBColor;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 11.05.2024
 */
@ExtensionImpl
public class BashDarkAdditionalTextAttributesProvider implements EditorColorSchemeExtender {
    @Override
    public void extend(Builder builder) {
        builder.add(BashSyntaxHighlighter.FUNCTION_CALL, AttributesFlyweightBuilder.create()
            .withItalicFont()
            .build());

        builder.add(BashSyntaxHighlighter.FUNCTION_DEF_NAME, AttributesFlyweightBuilder.create()
            .withBoldFont()
            .build());

        builder.add(BashSyntaxHighlighter.HERE_DOC, AttributesFlyweightBuilder.create()
            .withBackground(new RGBColor(0x5A, 0x5A, 0x5A))
            .withItalicFont()
            .build());

        builder.add(BashSyntaxHighlighter.HERE_DOC_START, AttributesFlyweightBuilder.create()
            .withBoldFont()
            .withItalicFont()
            .build());

        builder.add(BashSyntaxHighlighter.HERE_DOC_END, AttributesFlyweightBuilder.create()
            .withBoldFont()
            .withItalicFont()
            .build());

        builder.add(BashSyntaxHighlighter.EXTERNAL_COMMAND, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0xC5, 0x76, 0x33))
            .withItalicFont()
            .build());

        builder.add(BashSyntaxHighlighter.INTERNAL_COMMAND, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0xB0, 0xC9, 0x05))
            .build());

        builder.add(BashSyntaxHighlighter.REDIRECTION, AttributesFlyweightBuilder.create()
            .withItalicFont()
            .withEffect(EffectType.WAVE_UNDERSCORE, null)
            .build());

        builder.add(BashSyntaxHighlighter.SHEBANG_COMMENT, AttributesFlyweightBuilder.create()
            .withBoldFont()
            .build());
    }

    @Nonnull
    @Override
    public String getColorSchemeId() {
        return EditorColorsScheme.DARCULA_SCHEME_NAME;
    }
}
