/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: UnknownFiledescriptorInspection.java, Class: UnknownFiledescriptorInspection
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

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.expression.BashFiledescriptor;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

/**
 * Detects invalid filedescriptors. Bash only supports the descriptors 0-9.
 *
 * @author Joachim Ansorg
 * @since 2010-05-09
 */
@ExtensionImpl
public class UnknownFiledescriptorInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "UnknownFiledescriptor";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Unknown filedescriptor";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Unknown filedescriptor. Only &0 to &9 are valid.");
    }

    @Override
    public String getStaticDescription() {
        return "Bash supports the numbers 0-9 to identify filedescriptors. 0 is stdin, 1 is stdout and 2 is stderr.";
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
            public void visitFiledescriptor(BashFiledescriptor descriptor) {
                Integer asInt = descriptor.descriptorAsInt();
                if (asInt != null) {
                    if (asInt < 0 || asInt > 9) {
                        holder.registerProblem(descriptor, BashPsiUtils.rangeInParent(descriptor, descriptor),
                                "Invalid file descriptor " + asInt);
                    }
                }
            }
        };
    }
}