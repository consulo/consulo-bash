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
public class BashLightAdditionalTextAttributesProvider implements EditorColorSchemeExtender {
    @Override
    public void extend(Builder builder) {
        builder.add(BashSyntaxHighlighter.FUNCTION_CALL, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0xC5, 0x76, 0x33))
            .withItalicFont()
            .build());

        builder.add(BashSyntaxHighlighter.FUNCTION_DEF_NAME, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0xC5, 0x76, 0x33))
            .withBoldFont()
            .build());

        builder.add(BashSyntaxHighlighter.HERE_DOC, AttributesFlyweightBuilder.create()
            .withBackground(new RGBColor(0xE7, 0xFF, 0xB3))
            .build());

        builder.add(BashSyntaxHighlighter.INTERNAL_COMMAND, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0xB0, 0xC9, 0x05))
            .build());

        builder.add(BashSyntaxHighlighter.REDIRECTION, AttributesFlyweightBuilder.create()
            .withForeground(new RGBColor(0x5A, 0x5A, 0x5A))
            .withBoldFont()
            .withEffect(EffectType.WAVE_UNDERSCORE, null)
            .build());

        builder.add(BashSyntaxHighlighter.SHEBANG_COMMENT, AttributesFlyweightBuilder.create()
            .withBoldFont()
            .build());
    }

    @Nonnull
    @Override
    public String getColorSchemeId() {
        return EditorColorsScheme.DEFAULT_SCHEME_NAME;
    }
}
