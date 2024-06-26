/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: NewBashFileAction.java, Class: NewBashFileAction
 * Last modified: 2011-04-30 16:33
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.ansorgit.plugins.bash.actions;

import com.ansorgit.plugins.bash.util.BashIcons;
import com.ansorgit.plugins.bash.util.BashStrings;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;

/**
 * Date: 17.04.2009
 * Time: 20:19:17
 *
 * @author Joachim Ansorg
 */
public class NewBashFileAction extends NewBashActionBase
{
	public NewBashFileAction()
	{
		super(BashStrings.message("newfile.menu.action.text"), BashStrings.message("newfile.menu.action.description"), BashIcons.BASH_FILE_ICON);
	}


	protected String getDialogPrompt()
	{
		return BashStrings.message("newfile.dialog.prompt");
	}

	protected String getDialogTitle()
	{
		return BashStrings.message("newfile.dialog.title");
	}

	protected String getCommandName()
	{
		return BashStrings.message("newfile.command.name");
	}

	protected String getActionName(PsiDirectory directory, String newName)
	{
		return BashStrings.message("newfile.menu.action.text");
	}

	protected PsiElement[] doCreate(String newName, PsiDirectory directory)
	{
		PsiFile file = createFileFromTemplate(directory, newName, "bash-script.sh");
		PsiElement child = file.getLastChild();
		return child != null ? new PsiElement[]{
				file,
				child
		} : new PsiElement[]{file};
	}
}
