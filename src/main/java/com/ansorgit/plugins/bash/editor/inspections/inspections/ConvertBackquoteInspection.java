/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: ConvertBackquoteInspection.java, Class: ConvertBackquoteInspection
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

import com.ansorgit.plugins.bash.editor.inspections.quickfix.BackquoteQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashBackquote;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

/**
 * This inspection can convert the backquote commands into subshell commands.
 * <p/>
 * @author jansorg
 * @since 2009-05-21
 */
@ExtensionImpl
public class ConvertBackquoteInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "ReplaceWithSubshell";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Replace with subshell";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Convert backquote to subshell commands");
    }

    @Override
    public String getStaticDescription() {
        return "Replaces a backtick command with a corresponding subshell expression. For example: `echo a` would be replaced by $(echo a).";
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.INFO;
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder problemsHolder, final boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitBackquoteCommand(BashBackquote backquote) {
                if (isOnTheFly) {
                    problemsHolder.registerProblem(backquote, getShortName(), new BackquoteQuickfix(backquote));
                }
            }
        };
    }
}
