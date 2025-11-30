/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashStructureViewFactory.java, Class: BashStructureViewFactory
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

import com.ansorgit.plugins.bash.lang.BashLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.fileEditor.structureView.StructureViewBuilder;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.TreeBasedStructureViewBuilder;
import consulo.language.Language;
import consulo.language.editor.structureView.PsiStructureViewFactory;
import consulo.language.psi.PsiFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Creates the structure view builder for Bash files.
 * <p/>
 * Date: 12.04.2009
 * Time: 20:25:32
 *
 * @author Joachim Ansorg
 */
@ExtensionImpl
public class BashStructureViewFactory implements PsiStructureViewFactory {
    @Override
    public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile) {
        return new TreeBasedStructureViewBuilder() {
            @Override
            @Nonnull
            public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
                return new BashStructureViewModel(editor, psiFile);
            }

            @Override
            public boolean isRootNodeShown() {
                return false;
            }
        };
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return BashLanguage.INSTANCE;
    }
}
