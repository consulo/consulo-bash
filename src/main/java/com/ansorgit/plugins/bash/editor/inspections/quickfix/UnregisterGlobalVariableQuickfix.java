/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: UnregisterGlobalVariableQuickfix.java, Class: UnregisterGlobalVariableQuickfix
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
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import consulo.application.ApplicationManager;
import consulo.codeEditor.Editor;
import consulo.document.ReadOnlyFragmentModificationException;
import consulo.document.ReadOnlyModificationException;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiElementWithSubtreeChangeNotifier;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;

import jakarta.annotation.Nonnull;

/**
 * Unregisters a global variable.
 * <p/>
 * User: jansorg
 * Date: Jan 25, 2010
 * Time: 10:30:22 PM
 */
public class UnregisterGlobalVariableQuickfix extends AbstractBashQuickfix {
    private final BashVar variable;

    public UnregisterGlobalVariableQuickfix(BashVar variable) {
        this.variable = variable;
    }

    @Nonnull
    public String getName() {
        return "Unregister as global variable";
    }

    public void invoke(@Nonnull Project project, Editor editor, final PsiFile file) throws IncorrectOperationException {
        String variableName = variable.getReference().getReferencedName();
        TextRange textRange = variable.getTextRange();
        BashProjectSettings.storedSettings(project).removeGlobalVariable(variableName);

        //replace this position with the same value, we have to trigger a reparse somehow
        try {
            editor.getDocument().replaceString(textRange.getStartOffset(), textRange.getEndOffset(), variable.getText());
        } catch (ReadOnlyModificationException e) {
            //ignore
        } catch (ReadOnlyFragmentModificationException e) {
            //ignore
        }

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
                ApplicationManager.getApplication().saveSettings();
				((PsiElementWithSubtreeChangeNotifier)file).subtreeChanged();
            }
        });

    }
}
