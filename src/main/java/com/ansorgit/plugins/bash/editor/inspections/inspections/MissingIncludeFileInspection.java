/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: MissingIncludeFileInspection.java, Class: MissingIncludeFileInspection
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
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiElementVisitor;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;
import jakarta.annotation.Nonnull;

import java.io.File;

/**
 * User: jansorg
 * Date: Nov 2, 2009
 * Time: 8:15:59 PM
 */
@ExtensionImpl
public class MissingIncludeFileInspection extends AbstractBashInspection {
    @Pattern("[a-zA-Z_0-9.]+")
    @Nonnull
    @Override
    public String getID() {
        return "MissingInclude";
    }

    @Nonnull
    @Override
    public String getShortName() {
        return "Missing include file";
    }

    @Nls
    @Nonnull
    @Override
    public String getDisplayName() {
        return "Missing include file";
    }

    @Override
    public String getStaticDescription() {
        return "Checks the filenames of include directives. If a given file doesn't exist then" +
                "the element is highlighted as an error. Includes of files given as runtime values (e.g. variables) are not evaluated.";
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
            public void visitIncludeCommand(BashIncludeCommand bashCommand) {
                //fixme support $PATH evaluation

                BashFileReference fileReference = bashCommand.getFileReference();
                if (fileReference == null) {
                    return;
                }

                PsiFile file = fileReference.findReferencedFile();
                if (file == null && fileReference.isStatic()) {
                    String filename = fileReference.getFilename();

                    //check if it's an existing absolute file
                    File diskFile = new File(filename);
                    boolean absoluteAndExists = diskFile.isAbsolute() && diskFile.exists();
                    if (!absoluteAndExists) {
                        holder.registerProblem(fileReference, "The file '" + filename + "' does not exist.");
                    }

                    //print an error message if the given path is a directory
                    if (absoluteAndExists && diskFile.isDirectory()) {
                        holder.registerProblem(fileReference, "Unable to include a directory.");
                    }
                }
            }
        };
    }
}
