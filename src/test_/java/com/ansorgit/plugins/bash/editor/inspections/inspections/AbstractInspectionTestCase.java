/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: AbstractInspectionTestCase.java, Class: AbstractInspectionTestCase
 * Last modified: 2010-07-01
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

package com.ansorgit.plugins.bash.editor.inspections.inspections;

import java.util.Arrays;

import consulo.language.editor.inspection.LocalInspectionToolSession;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiNamedElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import com.ansorgit.plugins.bash.BashTestUtils;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import com.intellij.testFramework.UsefulTestCase;

/**
 * User: jansorg
 * Date: 01.07.2010
 * Time: 18:48:20
 */
public abstract class AbstractInspectionTestCase extends UsefulTestCase
{
	protected AbstractInspectionTestCase(Class<?> inspectionClass)
	{
		if(!Arrays.asList(new InspectionProvider().getInspectionClasses()).contains(inspectionClass))
		{
			throw new IllegalStateException("The inspection is not registered in the inspection provider");
		}
	}

	protected void doTest(String string, LocalInspectionTool inspectionTool)
	{
		throw new UnsupportedOperationException("");
	}

	protected String getTestDataPath()
	{
		return BashTestUtils.getBasePath() + "/psi/inspection/";
	}

	protected LocalInspectionTool withOnTheFly(final LocalInspectionTool delegate)
	{
		return new MyLocalInspectionTool(delegate);
	}

	private static class MyLocalInspectionTool extends LocalInspectionTool
	{
		private final LocalInspectionTool delegate;

		public MyLocalInspectionTool(LocalInspectionTool delegate)
		{
			this.delegate = delegate;
		}

		@Nls
		@Nonnull
		@Override
		public String getGroupDisplayName()
		{
			return delegate.getGroupDisplayName();
		}

		@Override
		@Nonnull
		public String[] getGroupPath()
		{
			return delegate.getGroupPath();
		}

		@Nls
		@Nonnull
		@Override
		public String getDisplayName()
		{
			return delegate.getDisplayName();
		}

		@Nonnull
		@Override
		public String getShortName()
		{
			return delegate.getShortName();
		}

		@Nonnull
		@Override
		public PsiElementVisitor buildVisitor(@Nonnull ProblemsHolder holder, boolean isOnTheFly)
		{
			return delegate.buildVisitor(holder, true);
		}

		@Override
		@Nullable
		public PsiNamedElement getProblemElement(PsiElement psiElement)
		{
			return delegate.getProblemElement(psiElement);
		}

		@Override
		public void inspectionStarted(LocalInspectionToolSession session, boolean isOnTheFly)
		{
			delegate.inspectionStarted(session, isOnTheFly);
		}

		@Override
		public void inspectionFinished(LocalInspectionToolSession session, ProblemsHolder problemsHolder)
		{
			delegate.inspectionFinished(session, problemsHolder);
		}

		@Override
		@Deprecated
		public void inspectionFinished(LocalInspectionToolSession session)
		{
			delegate.inspectionFinished(session);
		}

		@Override
		@Nonnull
		@NonNls
		public String getID()
		{
			return delegate.getID();
		}

		@Override
		@Nullable
		@NonNls
		public String getAlternativeID()
		{
			return delegate.getAlternativeID();
		}

		@Override
		public boolean runForWholeFile()
		{
			return delegate.runForWholeFile();
		}

		@Override
		@Nullable
		public ProblemDescriptor[] checkFile(@Nonnull PsiFile file, @Nonnull InspectionManager manager, boolean isOnTheFly)
		{
			return delegate.checkFile(file, manager, isOnTheFly);
		}

		@Override
		@Nonnull
		public PsiElementVisitor buildVisitor(@Nonnull ProblemsHolder holder, boolean isOnTheFly, LocalInspectionToolSession session)
		{
			return delegate.buildVisitor(holder, true, session);
		}
	}
}
