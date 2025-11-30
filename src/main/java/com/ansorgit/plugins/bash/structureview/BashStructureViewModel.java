/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashStructureViewModel.java, Class: BashStructureViewModel
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

package com.ansorgit.plugins.bash.structureview;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import consulo.codeEditor.Editor;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.fileEditor.structureView.tree.Filter;
import consulo.fileEditor.structureView.tree.Grouper;
import consulo.fileEditor.structureView.tree.Sorter;
import consulo.language.editor.structureView.TextEditorBasedStructureViewModel;
import consulo.language.psi.PsiFile;
import jakarta.annotation.Nonnull;

/**
 * Date: 12.04.2009
 * Time: 20:26:26
 *
 * @author Joachim Ansorg
 */
class BashStructureViewModel extends TextEditorBasedStructureViewModel implements StructureViewModel {
    private static final Class[] CLASSS = new Class[]{BashFunctionDef.class};
    private static final Sorter[] SORTERS = new Sorter[]{Sorter.ALPHA_SORTER};

    public BashStructureViewModel(Editor editor, PsiFile psiFile) {
        super(editor, psiFile);
    }

    @Override
    @Nonnull
    public StructureViewTreeElement getRoot() {
        return new BashStructureViewElement(getPsiFile());
    }

    @Override
    @Nonnull
    public Grouper[] getGroupers() {
        return Grouper.EMPTY_ARRAY;
    }

    @Nonnull
    @Override
    protected Class[] getSuitableClasses() {
        return CLASSS;
    }

    @Override
    @Nonnull
    public Sorter[] getSorters() {
        return SORTERS;
    }

    @Override
    @Nonnull
    public Filter[] getFilters() {
        return Filter.EMPTY_ARRAY;
    }
}
