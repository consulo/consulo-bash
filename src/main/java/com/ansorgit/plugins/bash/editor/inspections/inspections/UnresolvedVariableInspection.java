/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: UnresolvedVariableInspection.java, Class: UnresolvedVariableInspection
 * Last modified: 2013-05-09
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
package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.RegisterGlobalVariableQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

import java.util.Set;

/**
 * This inspection marks unresolved variables.
 *
 * @author jansorg
 * @since 2010-01-25
 */
@ExtensionImpl
public class UnresolvedVariableInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "unresolvedVariableInspection";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Unresolved variable";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Unresolved variable");
    }

    @Override
    public String getStaticDescription() {
        return "An unresolved variable has not been declared in earlier parts of the script";
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly) {
        return new UnresolvedVariableVisitor(holder);
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }

    private static class UnresolvedVariableVisitor extends BashVisitor {
        private final ProblemsHolder holder;
        private Set<String> globalVariables;

        public UnresolvedVariableVisitor(ProblemsHolder holder) {
            this.holder = holder;
            this.globalVariables = BashProjectSettings.storedSettings(holder.getProject()).getGlobalVariables();
        }

        @Override
        public void visitVarUse(BashVar bashVar) {
            BashReference ref = bashVar.getReference();

            PsiElement resolved = ref.resolve();
            if (!bashVar.isBuiltinVar() && resolved == null) {
                String varName = ref.getReferencedName();

                boolean isRegisteredAsGlobal = globalVariables.contains(varName);
                if (!isRegisteredAsGlobal) {
                    holder.registerProblem(bashVar,
                            "Unresolved variable",
                            ProblemHighlightType.LIKE_UNKNOWN_SYMBOL,
                            ref.getRangeInElement(),
                            new RegisterGlobalVariableQuickfix(bashVar));
                }
            }
        }
    }
}
