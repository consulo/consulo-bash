/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: RecursiveIncludeFileInspection.java, Class: RecursiveIncludeFileInspection
 * Last modified: 2013-05-12
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

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.FileInclusionManager;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;

import java.util.Set;

/**
 * This inspection detects recursive file inclusion.
 * It can detect whether another file actually back-includes this file.
 *
 * @author jansorg
 * @since 2009-11-02
 */
@ExtensionImpl
public class RecursiveIncludeFileInspection extends AbstractBashInspection {
    @Nonnull
    @Override
    @Pattern("[a-zA-Z_0-9.]+")
    public String getID() {
        return "RecursiveInclusion";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Recursive file inclusion";
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Recursive file inclusion");
    }

    @Override
    public String getStaticDescription() {
        return "Checks for recursive file inclusion. Currently it can highlight the inclusion of a file in itself..";
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitIncludeCommand(BashIncludeCommand includeCommand) {
                BashFileReference fileReference = includeCommand.getFileReference();
                if (fileReference == null) {
                    return;
                }

                PsiFile referencedFile = fileReference.findReferencedFile();
                if (includeCommand.getContainingFile().equals(referencedFile)) {
                    holder.registerProblem(fileReference, "A file should not include itself.");
                } else if (referencedFile instanceof BashFile) {
                    //check for deep recursive inclusion
                    //fixme
                    Set<PsiFile> includedFiles = FileInclusionManager.findIncludedFiles(referencedFile, true, true);
                    if (includedFiles.contains(includeCommand.getContainingFile())) {
                        holder.registerProblem(fileReference, "Possible recursive inclusion");
                    }
                }
            }
        };
    }
}