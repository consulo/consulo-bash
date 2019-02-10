package consulo.bash;

import javax.annotation.Nonnull;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import consulo.ide.IconDescriptor;
import consulo.ide.IconDescriptorUpdater;

/**
 * @author VISTALL
 * @since 19.11.13.
 */
public class BashIconDescriptorUpdater implements IconDescriptorUpdater
{
	@Override
	public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement element, int i)
	{
		if(element instanceof BashFunctionDef)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Function);
		}
	}
}
