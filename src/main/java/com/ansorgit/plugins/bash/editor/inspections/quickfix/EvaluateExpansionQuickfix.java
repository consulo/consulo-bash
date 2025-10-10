/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: EvaluateExpansionQuickfix.java, Class: EvaluateExpansionQuickfix
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

import com.ansorgit.plugins.bash.lang.psi.api.word.BashExpansion;
import com.ansorgit.plugins.bash.lang.valueExpansion.ValueExpansionUtil;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import consulo.codeEditor.Editor;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import jakarta.annotation.Nonnull;

/**
 * Evaluates an expansion and replaces the placeholder with the evaluated result.
 *
 * @author jansorg
 * @since 2009-11-15
 */
public class EvaluateExpansionQuickfix extends AbstractBashQuickfix {
    private final BashExpansion expansion;
    private Project project;

    public EvaluateExpansionQuickfix(BashExpansion expansion, Project project) {
        this.expansion = expansion;
        this.project = project;
    }

    @Nonnull
    @Override
    public LocalizeValue getName() {
        boolean supportBash4 = BashProjectSettings.storedSettings(project).isSupportBash4();
        String replacement = ValueExpansionUtil.expand(expansion.getText(), supportBash4);

        if (replacement.length() < 20) {
            return LocalizeValue.localizeTODO("Replace with the result '" + replacement + "'");
        }

        return LocalizeValue.localizeTODO("Replace with evaluated expansion");
    }

    public void invoke(@Nonnull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        TextRange r = expansion.getTextRange();

        boolean supportBash4 = BashProjectSettings.storedSettings(project).isSupportBash4();
        String replacement = ValueExpansionUtil.expand(expansion.getText(), supportBash4);
        if (replacement != null) {
            editor.getDocument().replaceString(r.getStartOffset(), r.getEndOffset(), replacement);
        }
    }
}
