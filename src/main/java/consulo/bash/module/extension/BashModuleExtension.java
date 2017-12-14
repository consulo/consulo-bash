package consulo.bash.module.extension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ansorgit.plugins.bash.settings.facet.OperationMode;
import com.ansorgit.plugins.bash.settings.facet.ui.FileMode;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.hash.HashMap;
import consulo.extension.impl.ModuleExtensionImpl;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 19.11.13.
 */
public class BashModuleExtension extends ModuleExtensionImpl<BashModuleExtension>
{
	protected OperationMode myOperationMode = OperationMode.IgnoreAll;

	protected Map<VirtualFile, FileMode> myMapping = new HashMap<VirtualFile, FileMode>();

	public BashModuleExtension(@NotNull String id, @NotNull ModuleRootLayer moduleRootLayer)
	{
		super(id, moduleRootLayer);
	}

	@NotNull
	public FileMode findMode(@Nullable VirtualFile file)
	{
		if(myOperationMode == OperationMode.AcceptAll)
		{
			return FileMode.accept();
		}

		if(myOperationMode == OperationMode.IgnoreAll)
		{
			return FileMode.ignore();
		}

		//custom mode
		if(myMapping.containsKey(file))
		{
			return myMapping.get(file);
		}

		VirtualFile parent = file.getParent();
		if(parent != null)
		{
			return findMode(parent);
		}

		return myMapping.containsKey(null) ? myMapping.get(null) : FileMode.defaultMode();
	}

	@Override
	protected void getStateImpl(@NotNull Element element)
	{
		super.getStateImpl(element);
		Element modeElement = new Element("operationMode");
		modeElement.setAttribute("type", myOperationMode.name());
		element.addContent(modeElement);

		List<VirtualFile> files = new ArrayList<VirtualFile>(myMapping.keySet());
		ContainerUtil.quickSort(files, new Comparator<VirtualFile>()
		{
			@Override
			public int compare(final VirtualFile o1, final VirtualFile o2)
			{
				if(o1 == null || o2 == null)
				{
					return o1 == null ? o2 == null ? 0 : 1 : -1;
				}
				return o1.getPath().compareTo(o2.getPath());
			}
		});

		for(VirtualFile file : files)
		{
			FileMode mode = myMapping.get(file);
			Element child = new Element("file");
			element.addContent(child);

			child.setAttribute("url", file == null ? "MODULE" : file.getUrl());
			child.setAttribute("mode", mode.getId());
		}
	}

	public OperationMode getOperationMode()
	{
		return myOperationMode;
	}

	public void setOperationMode(OperationMode operationMode)
	{
		this.myOperationMode = operationMode;
	}

	public Map<VirtualFile, FileMode> getMapping()
	{
		return myMapping;
	}

	public void setMapping(Map<VirtualFile, FileMode> newMapping)
	{
		myMapping.clear();
		myMapping.putAll(newMapping);
	}

	@Override
	public void commit(@NotNull BashModuleExtension mutableModuleExtension)
	{
		super.commit(mutableModuleExtension);

		setOperationMode(mutableModuleExtension.getOperationMode());
		setMapping(mutableModuleExtension.getMapping());
	}

	@Override
	protected void loadStateImpl(@NotNull Element element)
	{
		super.loadStateImpl(element);

		Element modeChild = element.getChild("operationMode");
		if(modeChild != null)
		{
			String modeString = modeChild.getAttributeValue("type", OperationMode.IgnoreAll.name());
			myOperationMode = OperationMode.valueOf(modeString);
		}

		List<Element> files = element.getChildren("file");
		for(Element fileElement : files)
		{
			String url = fileElement.getAttributeValue("url");

			String modeId = fileElement.getAttributeValue("mode");
			if(modeId == null)
			{
				continue;
			}

			FileMode mode = FileMode.forId(modeId);
			if(mode == null)
			{
				continue;
			}

			VirtualFile file = url.equals("MODULE") ? null : VirtualFileManager.getInstance().findFileByUrl(url);
			if(file != null || url.equals("MODULE"))
			{
				myMapping.put(file, mode);
			}
		}
	}
}
