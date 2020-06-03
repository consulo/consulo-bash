package consulo.bash.module.extension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;

import com.ansorgit.plugins.bash.settings.facet.ui.BashFacetUI;
import com.intellij.openapi.ui.VerticalFlowLayout;
import consulo.disposer.Disposable;
import consulo.module.extension.MutableModuleExtension;
import consulo.module.extension.swing.SwingMutableModuleExtension;
import consulo.roots.ModuleRootLayer;
import consulo.ui.Component;
import consulo.ui.Label;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;

/**
 * @author VISTALL
 * @since 19.11.13.
 */
public class BashMutableModuleExtension extends BashModuleExtension implements MutableModuleExtension<BashModuleExtension>, SwingMutableModuleExtension
{
	public BashMutableModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer moduleRootLayer)
	{
		super(id, moduleRootLayer);
	}

	@RequiredUIAccess
	@Nullable
	@Override
	public Component createConfigurationComponent(@Nonnull Disposable disposable, @Nonnull Runnable runnable)
	{
		return VerticalLayout.create().add(Label.create("Unsupported UI"));
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified(@Nonnull BashModuleExtension extension)
	{
		return myIsEnabled != extension.isEnabled() ||
				myOperationMode != extension.getOperationMode() ||
				!myMapping.equals(extension.myMapping);
	}

	@RequiredUIAccess
	@Nullable
	@Override
	public JComponent createConfigurablePanel(@Nonnull Disposable disposable, @Nonnull Runnable runnable)
	{
		JPanel panel = new JPanel(new VerticalFlowLayout(true, false));
		panel.add(new BashFacetUI(this));
		return panel;
	}
}
