/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: AbstractWordWrapQuickfix.java, Class: AbstractWordWrapQuickfix
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
package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import consulo.codeEditor.Editor;
import consulo.document.Document;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;

import jakarta.annotation.Nonnull;

/**
 * Abstract base class to wrap words in something else.
 *
 * @author jansorg
 * @since 2009-05-21
 */
abstract class AbstractWordWrapQuickfix extends AbstractBashQuickfix {
    protected final BashWord word;

    public AbstractWordWrapQuickfix(BashWord word) {
        this.word = word;
    }

    public void invoke(@Nonnull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
        if (document != null) {
            int endOffset = word.getTextOffset() + word.getTextLength();
            document.replaceString(word.getTextOffset(), endOffset, wrapText(word.getText()));
            PsiDocumentManager.getInstance(project).commitDocument(document);
        }
    }

    protected abstract String wrapText(String text);
}
