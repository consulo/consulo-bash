/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: CompletionUtil.java, Class: CompletionUtil
 * Last modified: 2013-02-03
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

package com.ansorgit.plugins.bash.util;

import jakarta.annotation.Nonnull;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Class to help with file path completions.
 * It can provide the possible matches for absolute and relative paths.
 */
public class CompletionUtil {
    private CompletionUtil() {
    }

    /**
     * Provide a list of absolute paths on the current system which match the given prefix.
     * Prefix is path whose last entry may be a partial match. A match with "/etc/def" matches
     * all files and directories in /etc which start with "def".
     *
     * @param prefix A path which is used to collect matching files.
     * @param accept
     * @return A list of full paths which match the prefix.
     */
    @Nonnull
    public static List<String> completeAbsolutePath(@Nonnull String prefix, Predicate<File> accept) {
        File base = new File(prefix);

        //a dot tricks Java into thinking dir/. is equal to dir and thus exists
        boolean dotSuffix = prefix.endsWith(".") && !prefix.startsWith(".");
        if (!base.exists() || dotSuffix) {
            base = base.getParentFile();
            if (base == null || !base.exists()) {
                return Collections.emptyList();
            }
        }

        File basePath;
        String matchPrefix;
        if (base.isDirectory()) {
            basePath = base;
            matchPrefix = "";
        } else {
            basePath = base.getParentFile();
            matchPrefix = base.getName();
        }


        List<String> result = new LinkedList<>();

        for (File fileCandidate : collectFiles(basePath, matchPrefix)) {
            if (!accept.test(fileCandidate)) {
                continue;
            }

            if (fileCandidate.isDirectory()) {
                result.add(fileCandidate.getAbsolutePath() + "/");
            } else {
                result.add(fileCandidate.getAbsolutePath());
            }
        }

        return result;
    }

    /**
     * Collect a list of relative paths. The start directory for the match is given as separate parameter.
     *
     * @param baseDir      The directory which is used as a starting point for the relative path matching.
     * @param shownBaseDir The prefix which is used in the results instead of the path given as baseDir. Can be used to display $HOME as prefix instead of the actual value on the current system.
     * @param relativePath The relative path prefix used for the matching.
     * @return The list of files and directories which match an item in the subtree of baseDir. shownBaseDir is used as prefix, if set.
     */
    @Nonnull
    public static List<String> completeRelativePath(@Nonnull String baseDir, @Nonnull String shownBaseDir, @Nonnull String relativePath) {
        List<String> result = new LinkedList<>();

        for (String path : completeAbsolutePath(baseDir + "/" + relativePath, file -> true)) {
            if (path.startsWith(baseDir)) {
                result.add(shownBaseDir + path.substring(baseDir.length()));
            }
        }

        return result;
    }

    @Nonnull
    private static List<File> collectFiles(File basePath, @Nonnull final String matchPart) {
        File[] filtered = basePath.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return !".".equals(pathname.getName()) &&
                        (matchPart.isEmpty() || pathname.getName().startsWith(matchPart));

            }
        });

        return filtered == null ? Collections.<File>emptyList() : Arrays.asList(filtered);
    }
}
