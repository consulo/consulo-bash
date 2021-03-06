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

import javax.annotation.Nonnull;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.intellij.codeHighlighting.TextEditorHighlightingPass;
import com.intellij.codeHighlighting.TextEditorHighlightingPassFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.psi.PsiFile;

/**
 * Factory which provides text editor highlighters for the Bash file type.
 */
public class RemoveHighlightingPassFactory implements TextEditorHighlightingPassFactory
{
	@Override
	public void register(@Nonnull Registrar registrar)
	{
		registrar.registerTextEditorHighlightingPass(this, TextEditorHighlightingPassFactory.Registrar.Anchor.AFTER, HighlighterLayer.ADDITIONAL_SYNTAX, true);
	}

	public TextEditorHighlightingPass createHighlightingPass(@Nonnull PsiFile file, @Nonnull Editor editor)
	{
		if(file instanceof BashFile)
		{
			return new RemoveHighlightingPass(file.getProject(), (BashFile) file, editor);
		}

		return null;
	}
}
