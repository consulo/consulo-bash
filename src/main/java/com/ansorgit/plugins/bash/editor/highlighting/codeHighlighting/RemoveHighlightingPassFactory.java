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
 * Factory which provides text editor highlighters for the Bash file type.
 *
 * Fully disabled since using PassFactory not allowed from plugins
 */
public class RemoveHighlightingPassFactory //implements consulo.language.editor.impl.highlight.TextEditorHighlightingPassFactory
{
//	@Override
//	public void register(@Nonnull Registrar registrar)
//	{
//		registrar.registerTextEditorHighlightingPass(this, consulo.language.editor.impl.highlight.TextEditorHighlightingPassFactory.Registrar.Anchor.AFTER, HighlighterLayer.ADDITIONAL_SYNTAX, true);
//	}
//
//	public TextEditorHighlightingPass createHighlightingPass(@Nonnull PsiFile file, @Nonnull Editor editor)
//	{
//		if(file instanceof BashFile)
//		{
//			return new RemoveHighlightingPass(file.getProject(), (BashFile) file, editor);
//		}
//
//		return null;
//	}
}
