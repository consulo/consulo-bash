package com.ansorgit.plugins.bash.editor.highlighting.codeHighlighting;

import com.ansorgit.plugins.bash.editor.inspections.inspections.UnusedFunctionDefInspection;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressIndicator;
import consulo.application.util.query.Query;
import consulo.codeEditor.Editor;
import consulo.document.Document;
import consulo.language.editor.inspection.scheme.InspectionProfile;
import consulo.language.editor.inspection.scheme.InspectionProjectProfileManager;
import consulo.language.editor.rawHighlight.HighlightDisplayKey;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.editor.rawHighlight.HighlightInfoType;
import consulo.language.psi.*;
import consulo.language.psi.search.ReferencesSearch;
import consulo.logging.Logger;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostHighlightingPass //extends TextEditorHighlightingPass
{
	private static final Logger LOG = Logger.getInstance("#com.intellij.codeInsight.daemon.impl.PostHighlightingPass");
	@Nonnull
	private final Project project;
	@Nonnull
	private final PsiFile file;
	@Nullable
	private final Editor editor;
	@Nonnull
	private final Document document;
	private HighlightDisplayKey unusedSymbolInspection;
	private int startOffset;
	private int endOffset;
	private Collection<HighlightInfo> highlights;

	PostHighlightingPass(@Nonnull Project project, @Nonnull PsiFile file, @Nullable Editor editor, @Nonnull Document document)
	{
		//super(project, document, true);

		this.project = project;
		this.file = file;
		this.editor = editor;
		this.document = document;

		startOffset = 0;
		endOffset = file.getTextLength();
	}

	//@Override
	public List<HighlightInfo> getInfos()
	{
		return highlights == null ? null : new ArrayList<HighlightInfo>(highlights);
	}

	public static HighlightInfo createUnusedSymbolInfo(@Nonnull PsiElement element, @Nullable String message)
	{
		return HighlightInfo.newHighlightInfo(HighlightInfoType.UNUSED_SYMBOL).range(element).descriptionAndTooltip(message)
				.create();
	}

	//@Override
	public void doCollectInformation(@Nonnull final ProgressIndicator progress)
	{
		final List<HighlightInfo> highlights = new ArrayList<HighlightInfo>();

		collectHighlights(highlights, progress);
		this.highlights = highlights;
	}

	private void collectHighlights(@Nonnull final List<HighlightInfo> result, @Nonnull final ProgressIndicator progress)
	{
		ApplicationManager.getApplication().assertReadAccessAllowed();

		InspectionProfile profile = InspectionProjectProfileManager.getInstance(project).getInspectionProfile();

		unusedSymbolInspection = HighlightDisplayKey.find(UnusedFunctionDefInspection.SHORT_NAME);

		boolean findUnusedFunctions = profile.isToolEnabled(unusedSymbolInspection, file);
		if(findUnusedFunctions)
		{
			final BashVisitor bashVisitor = new BashVisitor()
			{
				@Override
				public void visitFunctionDef(BashFunctionDef functionDef)
				{
					HighlightingKeys.IS_UNUSED.set(functionDef, null);

					if(!PsiUtilCore.hasErrorElementChild(functionDef))
					{
						HighlightInfo highlightInfo = processFunctionDef(functionDef, progress);
						if(highlightInfo != null)
						{
							result.add(highlightInfo);
						}
					}
				}
			};

			file.accept(new PsiRecursiveElementWalkingVisitor()
			{
				@Override
				public void visitElement(PsiElement element)
				{
					element.accept(bashVisitor);
					super.visitElement(element);
				}
			});
		}
	}

	private HighlightInfo processFunctionDef(BashFunctionDef functionDef, ProgressIndicator progress)
	{
		BashFunctionDefName nameSymbol = functionDef.getNameSymbol();
		if(nameSymbol != null)
		{
			Query<PsiReference> search = ReferencesSearch.search(functionDef, functionDef.getUseScope(), true);
			progress.checkCanceled();

			PsiReference first = search.findFirst();
			progress.checkCanceled();

			if(first == null)
			{
				HighlightingKeys.IS_UNUSED.set(functionDef, Boolean.TRUE);

				return createUnusedSymbolInfo(nameSymbol, null);
			}
		}

		return null;
	}

//	@Override
//	public void doApplyInformationToEditor()
//	{
//		if(highlights == null || highlights.isEmpty())
//		{
//			return;
//		}
//
//		UpdateHighlightersUtil.setHighlightersToEditor(myProject, myDocument, startOffset, endOffset, highlights, getColorsScheme(),
//				Pass.UPDATE_ALL);
//		BashPostHighlightingPassFactory.markFileUpToDate(file);
//	}
}
