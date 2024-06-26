/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: UnregisterGlobalVarInspection.java, Class: UnregisterGlobalVarInspection
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

import com.ansorgit.plugins.bash.editor.inspections.quickfix.UnregisterGlobalVariableQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashReference;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.settings.BashProjectSettings;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;
import jakarta.annotation.Nonnull;

import java.util.Set;

/**
 * This inspection marks global variables and offers a unregister quickfix.
 * <p/>
 * Of course, there is a chance of false positives as both inclusions may be in conditional
 * statements.
 */
@ExtensionImpl
public class UnregisterGlobalVarInspection extends AbstractBashInspection {
    @Nls
    @Nonnull
    @Override
    public String getDisplayName() {
        return "Unregister as a global variable";
    }

    @Pattern("[a-zA-Z_0-9.]+")
    @Nonnull
    @Override
    public String getID() {
        return "unregisterGlobalVariableInspection";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Unregister global variable";
    }

    @Override
    public String getStaticDescription() {
        return "Unknown variables can be registered as global variables to remove the error highlighting. This inspection provides an unregister action for already registered variables.";
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly) {
        return new UnresolvedVarVisitor(holder);
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.ERROR;
    }

    private static class UnresolvedVarVisitor extends BashVisitor {
        private final ProblemsHolder holder;
        private final Set<String> globalVars;

        public UnresolvedVarVisitor(ProblemsHolder holder) {
            this.holder = holder;
            this.globalVars = BashProjectSettings.storedSettings(holder.getProject()).getGlobalVariables();
        }

        @Override
        public void visitVarUse(BashVar bashVar) {
            BashReference ref = bashVar.getReference();
            if (!bashVar.isBuiltinVar() && ref.resolve() == null) {
                String varName = ref.getReferencedName();

                boolean isRegisteredAsGlobal = globalVars.contains(varName);

                if (isRegisteredAsGlobal) {
                    holder.registerProblem(bashVar, "This variable is currently registered as a global variable",
                            ProblemHighlightType.INFO,
                            ref.getRangeInElement(),
                            new UnregisterGlobalVariableQuickfix(bashVar));
                }
            }
        }
    }
}