package com.ansorgit.plugins.bash.codeInsight.completion;

import java.io.File;

import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NonNls;
import com.ansorgit.plugins.bash.BashTestUtils;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.ide.impl.idea.openapi.vfs.VfsUtil;
import com.intellij.testFramework.TestDataFile;
import com.intellij.testFramework.UsefulTestCase;

/**
 * User: jansorg
 * Date: 09.02.11
 * Time: 21:11
 */
public abstract class AbstractCompletionTest extends UsefulTestCase
{
	public static final String[] NO_COMPLETIONS = new String[0];

	protected LookupElement[] myItems;
	protected Project myProject;

	protected String getTestDataPath()
	{
		return BashTestUtils.getBasePath() + "/codeInsight/completion/" + getTestDir() + "/";
	}

	protected abstract String getTestDir();

	protected void configure() throws Exception
	{
		configure(1);
	}

	protected void configure(int invocationCount) throws Exception
	{
		//configureByFileNoCompletion(getTestName(false) + ".bash");

		complete(invocationCount);
	}

	protected void configure(String... files) throws Exception
	{
		configure(1, files);
	}

	protected void configure(int invocationCount, String... files) throws Exception
	{
		// configureByFileNoCompletion(getTestName(false) + ".bash");
		for(String file : files)
		{
			addFile(file);
		}

		complete(invocationCount);
	}

	protected PsiFile addFile(@TestDataFile @NonNls String filePath) throws Exception
	{
		final String fullPath = getTestDataPath() + filePath;
		final VirtualFile vFile = LocalFileSystem.getInstance().findFileByPath(fullPath.replace(File.separatorChar, '/'));
		// assertNotNull("file " + filePath + " not found", vFile);

		String fileText = StringUtil.convertLineSeparators(VfsUtil.loadText(vFile));

		final String fileName = vFile.getName();

		// return createFile(myModule, myFile.getVirtualFile().getParent(), fileName, fileText);
		return null;
	}

	protected void checkItems(String... values)
	{   /*
		if(myItems == null)
		{
			assertEquals(values.length, 0);
			return;
		}

		List<String> texts = Lists.transform(Lists.newArrayList(myItems), new Function<LookupElement, String>()
		{
			public String apply(LookupElement lookupElement)
			{
				return lookupElement.getLookupString();
			}
		});

		for(Iterator<String> iterator = texts.iterator(); iterator.hasNext(); )
		{
			String item = iterator.next();
			if(item.contains(".svn"))
			{
				iterator.remove();
			}
		}

		List<String> expected = Arrays.asList(values);

		assertEquals("Unexpected number of completions: " + texts, values.length, texts.size());

		ArrayList<String> remaining = Lists.newArrayList(values);
		remaining.removeAll(texts);

		assertTrue("Not all completions were found, left over: " + remaining, texts.containsAll(expected) && expected.containsAll(texts));
         */
		//assertEquals("Only the first index " + index + " matched of: " + Arrays.toString(myItems), values.length, index);
	}

	protected void complete(final int time)
	{
		//make sure with "false" that no auto-insertion of the completion is performed
		/*new CodeCompletionHandlerBase(CompletionType.BASIC, false, false, true).invokeCompletion(myProject, myEditor, time, false, false);

		LookupImpl lookup = (LookupImpl) LookupManager.getActiveLookup(myEditor);
		myItems = lookup == null ? null : lookup.getItems().toArray(LookupElement.EMPTY_ARRAY);
		myPrefix = lookup == null ? "" : lookup.itemMatcher(lookup.getItems().get(0)).getPrefix();    */
	}
}
