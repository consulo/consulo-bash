package consulo.bash.module.extension;

import com.ansorgit.plugins.bash.settings.facet.ui.BashFacetUI;
import consulo.disposer.Disposable;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.MutableModuleExtension;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 19.11.13.
 */
public class BashMutableModuleExtension extends BashModuleExtension implements MutableModuleExtension<BashModuleExtension> {
    public BashMutableModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer moduleRootLayer) {
        super(id, moduleRootLayer);
    }

    @RequiredUIAccess
    @Nullable
    @Override
    public Component createConfigurationComponent(@Nonnull Disposable disposable, @Nonnull Runnable runnable) {
        return BashFacetUI.create(this, disposable);
    }

    @Override
    public void setEnabled(boolean b) {
        myIsEnabled = b;
    }

    @Override
    public boolean isModified(@Nonnull BashModuleExtension extension) {
        return myIsEnabled != extension.isEnabled() ||
            myOperationMode != extension.getOperationMode() ||
            !myMapping.equals(extension.myMapping);
    }
}
