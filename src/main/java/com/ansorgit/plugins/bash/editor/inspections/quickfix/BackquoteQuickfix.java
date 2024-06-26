/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BackquoteQuickfix.java, Class: BackquoteQuickfix
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

import com.ansorgit.plugins.bash.lang.psi.api.BashBackquote;
import consulo.document.Document;
import consulo.codeEditor.Editor;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.language.util.IncorrectOperationException;

import jakarta.annotation.Nonnull;

/**
 * Quickfix to convert a backtick command into a subshell command.
 * <p/>
 * User: jansorg
 * Date: 21.05.2009
 * Time: 13:53:59
 */
public class BackquoteQuickfix extends AbstractBashQuickfix {
    private final BashBackquote backquote;

    public BackquoteQuickfix(BashBackquote backquote) {
        this.backquote = backquote;
    }

    @Nonnull
    public String getName() {
        return "Replace with subshell command";
    }

    public void invoke(@Nonnull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        Document document = PsiDocumentManager.getInstance(project).getDocument(file);
        if (document != null) {
            int endOffset = backquote.getTextRange().getEndOffset();
            String command = backquote.getCommandText();
            document.replaceString(backquote.getTextOffset(), endOffset, "$(" + command + ")");
            PsiDocumentManager.getInstance(project).commitDocument(document);
        }
    }
}
