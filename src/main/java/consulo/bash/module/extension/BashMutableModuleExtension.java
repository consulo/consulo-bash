package consulo.bash.module.extension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ansorgit.plugins.bash.settings.facet.ui.BashFacetUI;
import com.intellij.openapi.ui.VerticalFlowLayout;
import consulo.annotations.RequiredDispatchThread;
import consulo.module.extension.MutableModuleExtension;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 19.11.13.
 */
public class BashMutableModuleExtension extends BashModuleExtension implements MutableModuleExtension<BashModuleExtension>
{
	public BashMutableModuleExtension(@NotNull String id, @NotNull ModuleRootLayer moduleRootLayer)
	{
		super(id, moduleRootLayer);
	}

	@RequiredDispatchThread
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
	public boolean isModified(@NotNull BashModuleExtension extension)
	{
		return myIsEnabled != extension.isEnabled() ||
				myOperationMode != extension.getOperationMode() ||
				!myMapping.equals(extension.myMapping);
	}
}