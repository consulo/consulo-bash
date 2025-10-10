/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: RegisterGlobalVariableQuickfix.java, Class: RegisterGlobalVariableQuickfix
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

/*
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
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

/**
 * Quickfix to register an unknown / unresolved variable as a globally defined variable.
 *
 * @author jansorg
 * @since 2010-01-25
 */
public class RegisterGlobalVariableQuickfix extends AbstractBashQuickfix {
    private final BashVar bashVar;

    public RegisterGlobalVariableQuickfix(BashVar bashVar) {
        this.bashVar = bashVar;
    }

    @Nonnull
    @Override
    public LocalizeValue getName() {
        return LocalizeValue.localizeTODO("Register as global variable");
    }

    public void invoke(@Nonnull Project project, Editor editor, final PsiFile file) throws IncorrectOperationException {
        String variableName = bashVar.getReference().getReferencedName();
        TextRange textRange = bashVar.getTextRange();
        BashProjectSettings.storedSettings(project).addGlobalVariable(variableName);
        //replace this position with the same value, we have to trigger a reparse somehow
        try {
            file.getViewProvider().getDocument().replaceString(textRange.getStartOffset(), textRange.getEndOffset(), bashVar.getText());
        } catch (ReadOnlyModificationException e) {
            //ignore
        } catch (ReadOnlyFragmentModificationException e) {
            //ignore
        }

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
                ApplicationManager.getApplication().saveSettings();
				((PsiElementWithSubtreeChangeNotifier)file).subtreeChanged(); //FIXME RIGHT?
            }
        });
    }
}
