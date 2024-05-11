package consulo.bash.module.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.bash.icon.BashIconGroup;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleExtensionProvider;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.MutableModuleExtension;
import consulo.ui.image.Image;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 11.05.2024
 */
@ExtensionImpl(id = "bash")
public class BashModuleExtensionProvider implements ModuleExtensionProvider<BashModuleExtension> {
    @Nonnull
    @Override
    public String getId() {
        return "bash";
    }

    @Override
    public boolean isAllowMixin() {
        return true;
    }

    @Nonnull
    @Override
    public LocalizeValue getName() {
        return LocalizeValue.localizeTODO("Bash");
    }

    @Nonnull
    @Override
    public Image getIcon() {
        return BashIconGroup.bash_icon();
    }

    @Nonnull
    @Override
    public ModuleExtension<BashModuleExtension> createImmutableExtension(@Nonnull ModuleRootLayer moduleRootLayer) {
        return new BashModuleExtension(getId(), moduleRootLayer);
    }

    @Nonnull
    @Override
    public MutableModuleExtension<BashModuleExtension> createMutableExtension(@Nonnull ModuleRootLayer moduleRootLayer) {
        return new BashMutableModuleExtension(getId(), moduleRootLayer);
    }
}
