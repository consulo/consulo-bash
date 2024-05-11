/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: RemoveHighlightingPassFactory.java, Class: RemoveHighlightingPassFactory
 * Last modified: 2011-03-31 20:06
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

package com.ansorgit.plugins.bash.editor.highlighting.codeHighlighting;

/**
 * Factory which provides text editor post highlighter for the Bash file type.
 * <p>
 * This code is based on IntelliJ's PostHighlightingPassFactory .
 */
public class BashPostHighlightingPassFactory //implements TextEditorHighlightingPassFactory
{
//	private static final Key<Long> LAST_POST_PASS_TIMESTAMP = Key.create("BASH_LAST_POST_PASS_TIMESTAMP");
//
//	@Override
//	public void register(@Nonnull Registrar registrar)
//	{
//		registrar.registerTextEditorHighlightingPass(this, new int[]{Pass.UPDATE_ALL}, null, true, Pass.UPDATE_ALL);
//	}
//
//	public static void markFileUpToDate(@Nonnull PsiFile file)
//	{
//		long lastStamp = PsiModificationTracker.SERVICE.getInstance(file.getProject()).getModificationCount();
//		file.putUserData(LAST_POST_PASS_TIMESTAMP, lastStamp);
//	}
//
//	public consulo.language.editor.impl.highlight.TextEditorHighlightingPass createHighlightingPass(@Nonnull PsiFile file, @Nonnull Editor editor)
//	{
//		TextRange textRange = FileStatusMap.getDirtyTextRange(editor, Pass.UPDATE_ALL);
//		if(textRange == null)
//		{
//			Long lastStamp = file.getUserData(LAST_POST_PASS_TIMESTAMP);
//			long currentStamp = PsiModificationTracker.SERVICE.getInstance(file.getProject()).getModificationCount();
//			if(lastStamp != null && lastStamp == currentStamp || !ProblemHighlightFilter.shouldHighlightFile(file))
//			{
//				return null;
//			}
//		}
//
//		if(file instanceof BashFile)
//		{
//			return new PostHighlightingPass(file.getProject(), file, editor, editor.getDocument());
//		}
//
//		return null;
//	}
}
