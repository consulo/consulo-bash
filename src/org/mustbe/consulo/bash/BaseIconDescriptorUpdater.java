package org.mustbe.consulo.bash;

import org.jetbrains.annotations.NotNull;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 19.11.13.
 */
public class BaseIconDescriptorUpdater implements IconDescriptorUpdater
{
	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int i)
	{
		if(element instanceof BashFunctionDef)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Function);
		}
	}
}
