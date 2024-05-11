/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFormattingModelBuilder.java, Class: BashFormattingModelBuilder
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

package com.ansorgit.plugins.bash.editor.formatting;

import com.ansorgit.plugins.bash.editor.formatting.noOpModel.NoOpBlock;
import com.ansorgit.plugins.bash.lang.BashLanguage;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.codeStyle.*;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import jakarta.annotation.Nonnull;

/**
 * This code is based on code taken from the Groovy plugin.
 *
 * @author ilyas, jansorg
 */
@ExtensionImpl
public class BashFormattingModelBuilder implements FormattingModelBuilder {
    @Nonnull
    public FormattingModel createModel(FormattingContext formattingContext) {
        PsiElement element = formattingContext.getPsiElement();
        CodeStyleSettings settings = formattingContext.getCodeStyleSettings();
        ASTNode node = element.getNode();
        assert node != null;

        PsiFile containingFile = element.getContainingFile();//.getViewProvider().getPsi(BashFileType.BASH_LANGUAGE);
        ASTNode astNode = containingFile.getNode();
        assert astNode != null;

        BashProjectSettings projectSettings = BashProjectSettings.storedSettings(containingFile.getProject());
        if (!projectSettings.isFormatterEnabled()) {
            return FormattingModelProvider.createFormattingModelForPsiFile(containingFile,
                    new NoOpBlock(astNode), settings);
        }

        return FormattingModelProvider.createFormattingModelForPsiFile(containingFile,
                new BashBlock(astNode, null, Indent.getAbsoluteNoneIndent(), null, settings), settings);
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return BashLanguage.INSTANCE;
    }
}
