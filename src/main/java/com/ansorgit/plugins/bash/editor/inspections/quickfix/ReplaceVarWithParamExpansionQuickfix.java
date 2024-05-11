/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: ReplaceVarWithParamExpansionQuickfix.java, Class: ReplaceVarWithParamExpansionQuickfix
 * Last modified: 2013-04-30
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

package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import consulo.codeEditor.Editor;
import consulo.document.Document;
import consulo.document.ReadOnlyFragmentModificationException;
import consulo.document.ReadOnlyModificationException;
import consulo.document.util.TextRange;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.psi.PsiElementWithSubtreeChangeNotifier;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

/**
 * THis quickfix replaces a simple variable usage with the equivalent parameter expansion form.
 * User: jansorg
 * Date: 28.12.10
 * Time: 12:19
 */
public class ReplaceVarWithParamExpansionQuickfix extends AbstractBashQuickfix implements LocalQuickFix {
    private final BashVar var;
    protected final String variableName;

    public ReplaceVarWithParamExpansionQuickfix(BashVar var) {
        this.var = var;
        this.variableName = var.getReference().getReferencedName();
    }

    @Nonnull
    public String getName() {
        if (variableName.length() > 10) {
            return "Replace with '${...}'";
        } else {
            return String.format("Replace '%s' with '${%s}'", variableName, variableName);
        }
    }

    public void invoke(@Nonnull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        TextRange textRange = var.getTextRange();

        //replace this position with the same value, we have to trigger a reparse somehow
        try {
            Document document = file.getViewProvider().getDocument();
            document.replaceString(textRange.getStartOffset(), textRange.getEndOffset(), "${" + variableName + "}");
			((PsiElementWithSubtreeChangeNotifier)file).subtreeChanged();
        } catch (ReadOnlyModificationException e) {
            //ignore
        } catch (ReadOnlyFragmentModificationException e) {
            //ignore
        }
    }
}
