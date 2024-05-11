/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: BashLineMarkerProvider.java, Class: BashLineMarkerProvider
 * Last modified: 2013-01-25
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

package com.ansorgit.plugins.bash.editor;

import com.ansorgit.plugins.bash.lang.BashLanguage;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.AllIcons;
import consulo.codeEditor.markup.GutterIconRenderer;
import consulo.language.Language;
import consulo.language.editor.Pass;
import consulo.language.editor.gutter.LineMarkerInfo;
import consulo.language.editor.gutter.LineMarkerProvider;
import consulo.language.psi.PsiElement;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Provides Bash line markers for a given element.
 *
 * @author Joachim Ansorg
 */
@ExtensionImpl
public class BashLineMarkerProvider implements LineMarkerProvider
{
	@Nullable
	@Override
	public LineMarkerInfo getLineMarkerInfo(@Nonnull PsiElement element)
	{
		if(element instanceof BashFunctionDefName && element.getParent() instanceof BashFunctionDef)
		{
			return new LineMarkerInfo<>((BashFunctionDefName) element, element.getTextRange(), AllIcons.Nodes.Function,
					Pass.UPDATE_ALL, null, null, GutterIconRenderer.Alignment.LEFT);
		}

		return null;
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return BashLanguage.INSTANCE;
	}
}
