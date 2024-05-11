package consulo.bash;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.AdditionalTextAttributesProvider;
import consulo.colorScheme.EditorColorsScheme;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 11.05.2024
 */
@ExtensionImpl
public class BashLightAdditionalTextAttributesProvider implements AdditionalTextAttributesProvider {
    @Nonnull
    @Override
    public String getColorSchemeName() {
        return EditorColorsScheme.DEFAULT_SCHEME_NAME;
    }

    @Nonnull
    @Override
    public String getColorSchemeFile() {
        return "/colors/BashDefault.xml";
    }
}
