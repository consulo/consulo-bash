/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: EvaluateExpansionInspection.java, Class: EvaluateExpansionInspection
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
package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.EvaluateExpansionQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashExpansion;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

/**
 * This inspection detects expansions and offers a quickfix to calculate the expansion
 * and insert it instead of the original expansion placeholder.
 *
 * @author jansorg
 * @since 2009-11-15
 */
@ExtensionImpl
public class EvaluateExpansionInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "EvaluateExpression";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Evaluate expansion";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Evaluate an expansion");
    }

    @Override
    public String getStaticDescription() {
        return "Replaces a Bash expansion with the evaluated result. Only static value expansions are understood by this expansion, " +
            "i.e. if an expansion contains a variable then the quickfix can not be applied.";
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitExpansion(BashExpansion expansion) {
                if (isOnTheFly && expansion.isValidExpansion()) {
                    holder.registerProblem(expansion, getShortName(), new EvaluateExpansionQuickfix(expansion, expansion.getProject()));
                }
            }
        };
    }
}
