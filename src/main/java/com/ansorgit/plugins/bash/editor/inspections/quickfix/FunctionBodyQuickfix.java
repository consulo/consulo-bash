/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: FunctionBodyQuickfix.java, Class: FunctionBodyQuickfix
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

import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import consulo.codeEditor.Editor;
import consulo.document.Document;
import consulo.language.editor.FileModificationService;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.logging.Logger;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

/**
 * Wrap a function body in curly brackets.
 * <p/>
 * Date: 12.04.2009
 * Time: 15:22:48
 *
 * @author Joachim Ansorg
 */
public class FunctionBodyQuickfix extends AbstractBashQuickfix {
    private static final Logger log = Logger.getInstance("#bash.FunctionBodyQuickfix");
    private final BashFunctionDef functionDef;

    public FunctionBodyQuickfix(BashFunctionDef functionDef) {
        this.functionDef = functionDef;
    }

    @Nonnull
    public String getName() {
        return "Wrap function body in curly brackets";
    }

    public void invoke(@Nonnull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        if (!FileModificationService.getInstance().prepareFileForWrite(file)) {
            return;
        }

        log.assertTrue(functionDef != null);

        log.debug("Invoke for " + functionDef);
        log.debug("command group? " + functionDef.body());

        final BashBlock block = functionDef.body();
        if (block != null) {
            StringBuilder builder = new StringBuilder();
            final BashBlock body = functionDef.body();
            builder.append("{ ").append(body.getText()).append(" }");

            int startOffset = body.getTextOffset();
            int endOffset = startOffset + body.getTextLength();

            Document document = PsiDocumentManager.getInstance(project).getDocument(file);
            document.replaceString(startOffset, endOffset, builder);
            PsiDocumentManager.getInstance(project).commitDocument(document);
        }
    }
}
