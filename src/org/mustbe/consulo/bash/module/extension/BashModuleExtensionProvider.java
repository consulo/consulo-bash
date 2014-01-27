package org.mustbe.consulo.bash.module.extension;

import javax.swing.Icon;

import org.consulo.module.extension.ModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ansorgit.plugins.bash.util.BashIcons;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 19.11.13.
 */
public class BashModuleExtensionProvider implements ModuleExtensionProvider<BashModuleExtension, BashMutableModuleExtension>
{
	@Nullable
	@Override
	public Icon getIcon()
	{
		return BashIcons.BASH_FILE_ICON;
	}

	@NotNull
	@Override
	public String getName()
	{
		return "Bash";
	}

	@NotNull
	@Override
	public BashModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new BashModuleExtension(s, module);
	}

	@NotNull
	@Override
	public BashMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module)
	{
		return new BashMutableModuleExtension(s, module);
	}
}
