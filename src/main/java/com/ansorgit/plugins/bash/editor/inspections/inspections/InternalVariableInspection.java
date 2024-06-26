/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: InternalVariableInspection.java, Class: InternalVariableInspection
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

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;

/**
 * This inspection marks unresolved variables.
 * <p/>
 * User: jansorg
 * Date: Jan 25, 2010
 * Time: 10:11:49 PM
 */
@ExtensionImpl
public class InternalVariableInspection extends AbstractBashInspection {
    //private static final Logger log = Logger.getInstance("#UnresolvedVariable");

    @Nls
    @Nonnull
    @Override
    public String getDisplayName() {
        return "Read-only shell variable";
    }

    @Pattern("[a-zA-Z_0-9.]+")
    @Nonnull
    @Override
    public String getID() {
        return "internalVariableInspection";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Change to a builtin, read-only shell variable";
    }

    @Override
    public String getStaticDescription() {
        return "Attempt to change an internal, read-only shell variable.";
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitVarDef(BashVarDef varDef) {
                String name = varDef.getName();

                if (LanguageBuiltins.readonlyShellVars.contains(name)) {
                    holder.registerProblem(varDef, getShortName(), LocalQuickFix.EMPTY_ARRAY);
                }
            }
        };
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }
}