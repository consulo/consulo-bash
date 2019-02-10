package consulo.bash.module.extension;

import javax.annotation.Nonnull;
import javax.swing.JComponent;
import javax.swing.JPanel;

import javax.annotation.Nullable;
import com.ansorgit.plugins.bash.settings.facet.ui.BashFacetUI;
import com.intellij.openapi.ui.VerticalFlowLayout;
import consulo.ui.RequiredUIAccess;
import consulo.module.extension.MutableModuleExtension;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 19.11.13.
 */
public class BashMutableModuleExtension extends BashModuleExtension implements MutableModuleExtension<BashModuleExtension>
{
	public BashMutableModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer moduleRootLayer)
	{
		super(id, moduleRootLayer);
	}

	@RequiredUIAccess
	@Nullable
	@Override
	public JComponent createConfigurablePanel(@Nullable Runnable runnable)
	{
		JPanel panel = new JPanel(new VerticalFlowLayout(true, false));
		panel.add(new BashFacetUI(this));
		return panel;
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
}
