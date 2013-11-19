package org.mustbe.consulo.bash.module.extension;

import javax.swing.JComponent;

import org.consulo.module.extension.MutableModuleExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ansorgit.plugins.bash.settings.facet.ui.BashFacetUI;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;

/**
 * @author VISTALL
 * @since 19.11.13.
 */
public class BashMutableModuleExtension extends BashModuleExtension implements MutableModuleExtension<BashModuleExtension>
{
	private BashModuleExtension myOriginal;

	public BashMutableModuleExtension(@NotNull String id, @NotNull Module module, @NotNull BashModuleExtension original)
	{
		super(id, module);
		myOriginal = original;
	}

	@Nullable
	@Override
	public JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable)
	{
		return wrapToNorth(new BashFacetUI(this));
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified()
	{
		return myIsEnabled != myOriginal.isEnabled() ||
				myOperationMode != myOriginal.getOperationMode() ||
				!myMapping.equals(myOriginal.myMapping);
	}

	@Override
	public void commit()
	{
		myOriginal.commit(this);
	}
}
