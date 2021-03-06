package com.ansorgit.plugins.bash.editor.highlighting.codeHighlighting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.ansorgit.plugins.bash.editor.inspections.inspections.UnusedFunctionDefInspection;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeHighlighting.TextEditorHighlightingPass;
import com.intellij.codeInsight.daemon.HighlightDisplayKey;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.UpdateHighlightersUtil;
import com.intellij.codeInspection.InspectionProfile;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.profile.codeInspection.InspectionProjectProfileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.Query;

public class PostHighlightingPass extends TextEditorHighlightingPass
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
		super(project, document, true);

		this.project = project;
		this.file = file;
		this.editor = editor;
		this.document = document;

		startOffset = 0;
		endOffset = file.getTextLength();
	}

	@Override
	public List<HighlightInfo> getInfos()
	{
		return highlights == null ? null : new ArrayList<HighlightInfo>(highlights);
	}

	public static HighlightInfo createUnusedSymbolInfo(@Nonnull PsiElement element, @Nullable String message)
	{
		return HighlightInfo.newHighlightInfo(HighlightInfoType.UNUSED_SYMBOL).range(element).descriptionAndTooltip(message)
				.create();
	}

	@Override
	public void doCollectInformation(@Nonnull final ProgressIndicator progress)
	{
		final List<HighlightInfo> highlights = new ArrayList<HighlightInfo>();

		collectHighlights(highlights, progress);
		this.highlights = highlights;
	}

	private void collectHighlights(@Nonnull final List<HighlightInfo> result, @Nonnull final ProgressIndicator progress)
	{
		ApplicationManager.getApplication().assertReadAccessAllowed();

		InspectionProfile profile = InspectionProjectProfileManager.getInstance(myProject).getInspectionProfile();

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

	@Override
	public void doApplyInformationToEditor()
	{
		if(highlights == null || highlights.isEmpty())
		{
			return;
		}

		UpdateHighlightersUtil.setHighlightersToEditor(myProject, myDocument, startOffset, endOffset, highlights, getColorsScheme(),
				Pass.UPDATE_ALL);
		BashPostHighlightingPassFactory.markFileUpToDate(file);
	}
}
