/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: AbstractResolveTest.java, Class: AbstractResolveTest
 * Last modified: 2010-07-17
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ansorgit.plugins.bash.lang.psi.resolve;

import consulo.language.psi.PsiReference;
import org.jetbrains.annotations.NonNls;
import consulo.language.psi.PsiFile;
import com.intellij.testFramework.TestDataFile;
import com.intellij.testFramework.UsefulTestCase;

/**
 * User: jansorg
 * Date: 15.06.2010
 * Time: 21:09:35
 */
public abstract class AbstractResolveTest extends UsefulTestCase
{
	protected PsiFile myFile;
	protected PsiReference configure() throws Exception
	{
		//return configureByFile(getTestName(false) + ".bash");
		return null;
	}

	protected PsiFile addFile(@TestDataFile @NonNls String filePath) throws Exception
	{
		/*final String fullPath = getTestDataPath() + filePath;
        final VirtualFile vFile = LocalFileSystem.getInstance().findFileByPath(fullPath.replace(File.separatorChar, '/'));
        assertNotNull("file " + filePath + " not found", vFile);

        String fileText = StringUtil.convertLineSeparators(VfsUtil.loadText(vFile));

        final String fileName = vFile.getName();

        return createFile(myModule, myFile.getVirtualFile().getParent(), fileName, fileText);   */
		return null;
	}
}
