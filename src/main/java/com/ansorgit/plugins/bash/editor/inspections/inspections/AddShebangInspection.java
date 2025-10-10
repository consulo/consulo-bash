/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: AddShebangInspection.java, Class: AddShebangInspection
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

import com.ansorgit.plugins.bash.editor.inspections.quickfix.AddShebangQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

/**
 * This inspection detects a missing shebang line and offers a file-level quickfix to add one.
 *
 * @author Joachim Ansorg
 * @since 2009-05-15
 */
@ExtensionImpl
public class AddShebangInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "AddShebangLine";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Add Shebang line";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Add missing shebang line to file");
    }

    @Override
    public String getStaticDescription() {
        return "If a file does not yet have a shebang line this inspection offers a file wide quickfix to add one.";
    }

    @Override
    public ProblemDescriptor[] checkFile(@Nonnull PsiFile file, @Nonnull InspectionManager manager, boolean isOnTheFly) {
        PsiFile checkedFile = BashPsiUtils.findFileContext(file);
        if (checkedFile instanceof BashFile && !BashPsiUtils.isInjectedElement(file)) {
            BashFile bashFile = (BashFile) checkedFile;

            Boolean isLanguageConsole = checkedFile.getUserData(BashFile.LANGUAGE_CONSOLE_MARKER);

            if ((isLanguageConsole == null || !isLanguageConsole) && !bashFile.hasShebangLine()) {
                return new ProblemDescriptor[]{
                        manager.createProblemDescriptor(checkedFile, getShortName(), new AddShebangQuickfix(), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, isOnTheFly)
                };
            }
        }

        return null;
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitFile(BashFile file) {
                addDescriptors(checkFile(file, holder.getManager(), isOnTheFly));
            }

            private void addDescriptors(final ProblemDescriptor[] descriptors) {
                if (descriptors != null) {
                    for (ProblemDescriptor descriptor : descriptors) {
                        holder.registerProblem(descriptor);
                    }
                }
            }
        };
    }
}