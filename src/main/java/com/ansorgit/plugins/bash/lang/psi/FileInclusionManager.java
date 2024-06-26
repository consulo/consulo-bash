/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: FileInclusionManager.java, Class: FileInclusionManager
 * Last modified: 2011-07-17 20:06
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

package com.ansorgit.plugins.bash.lang.psi;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashIncludeCommandIndex;
import com.ansorgit.plugins.bash.lang.psi.stubs.index.BashIncludedFilenamesIndex;
import com.ansorgit.plugins.bash.lang.psi.util.BashSearchScopes;
import consulo.language.psi.PsiFile;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.StubIndex;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

import java.util.*;

/**
 * User: jansorg
 * Date: 06.02.11
 * Time: 20:36
 */
public class FileInclusionManager {
    private FileInclusionManager() {
    }

    @Nonnull
    public static Set<PsiFile> findIncludedFiles(@Nonnull PsiFile sourceFile, boolean diveDeep, boolean bashOnly) {
        if (!(sourceFile instanceof BashFile)) {
            return Collections.emptySet();
        }

        Set<PsiFile> includersTodo = new HashSet<>(Set.of(sourceFile.getContainingFile()));
        Set<PsiFile> includersDone = new HashSet<>();

        Set<PsiFile> allIncludedFiles = new HashSet<>();

        while (!includersTodo.isEmpty()) {
            Iterator<PsiFile> iterator = includersTodo.iterator();
            PsiFile file = iterator.next();
            iterator.remove();

            includersDone.add(file);

            GlobalSearchScope moduleScope = BashSearchScopes.moduleScope(file);
            Collection<BashIncludeCommand> commands = StubIndex.getInstance().get(BashIncludeCommandIndex.KEY, file.getName(), file.getProject(), moduleScope);
            for (BashIncludeCommand command : commands) {
                if (command.getFileReference().isStatic()) {
                    PsiFile referencedFile = command.getFileReference().findReferencedFile();
                    if (bashOnly && !(referencedFile instanceof BashFile)) {
                        continue;
                    }

                    if (referencedFile != null) {
                        allIncludedFiles.add(referencedFile);

                        if (!includersDone.contains(referencedFile)) {
                            //the include commands of this command have to be collected, too
                            includersTodo.add(referencedFile);
                        }
                    }
                }
            }

            if (!diveDeep) {
                //the first iteratopm is the original source
                break;
            }
        }

        return allIncludedFiles;
    }

    /**
     * Finds all files which include the given file.
     * The bash files of the module are checked if they include the file.
     *
     * @param project The project
     * @param file    The file for which the includers should be found.
     * @return
     */
    @Nonnull
    public static Set<BashFile> findIncluders(@Nonnull Project project, @Nonnull PsiFile file) {
        GlobalSearchScope searchScope = BashSearchScopes.moduleScope(file);

        Set<BashFile> includers = new HashSet<>();

        Collection<BashIncludeCommand> includeCommands = StubIndex.getInstance().get(BashIncludedFilenamesIndex.KEY, file.getName(), project, searchScope);
        for (BashIncludeCommand command : includeCommands) {
            BashFile includer = (BashFile) command.getContainingFile();

            if (!file.equals(includer)) {
                includers.add(includer);
            }
        }

        return includers;
    }
}
