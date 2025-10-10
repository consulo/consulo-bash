/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: SimpleVarUsageInspection.java, Class: SimpleVarUsageInspection
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

import com.ansorgit.plugins.bash.editor.inspections.quickfix.ReplaceVarWithParamExpansionQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.api.arithmetic.ArithmeticExpression;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

/**
 * This inspection detects simple variable usages and offers a quickfix to replace it with
 * the equivalent parameter expansion, e.g. $a would be replaced with ${a}.
 *
 * @author jansorg
 * @since 2009-05-21
 */
@ExtensionImpl
public class SimpleVarUsageInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "SimpleVarUsage";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Simple variable usage";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Replace with the equivalent parameter expansion");
    }

    @Override
    public String getStaticDescription() {
        return "Replaces the simple use of a variable with the equivalent parameter expansion form. For example $a is replaced by ${a}.";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WEAK_WARNING;
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitVarUse(BashVar var) {
                //only if the variable is not embedded inside of a string add the quickfix
                boolean validParent = !BashPsiUtils.hasParentOfType(var, BashString.class, 4)
                        && !BashPsiUtils.hasParentOfType(var, ArithmeticExpression.class, 4);

                if (!var.isParameterExpansion() && !var.isBuiltinVar() && validParent) {
                    holder.registerProblem(var, getShortName(), new ReplaceVarWithParamExpansionQuickfix(var));
                }
            }
        };
    }
}