/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: AbsolutePathCompletionProvider.java, Class: AbsolutePathCompletionProvider
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

package com.ansorgit.plugins.bash.editor.codecompletion;

import com.ansorgit.plugins.bash.util.CompletionUtil;
import consulo.application.util.matcher.PrefixMatcher;
import consulo.language.editor.completion.CompletionContributor;
import consulo.language.editor.completion.CompletionParameters;
import consulo.language.editor.completion.CompletionResultSet;
import consulo.language.editor.completion.CompletionType;
import consulo.language.pattern.StandardPatterns;
import consulo.language.psi.PsiElement;
import consulo.language.util.ProcessingContext;
import jakarta.annotation.Nonnull;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;

/**
 * This completion provider provides code completion for file / directory paths in the file.
 */
class AbsolutePathCompletionProvider extends BashCompletionProvider {
    public AbsolutePathCompletionProvider() {
    }

    @Override
    void addTo(CompletionContributor contributor) {
        //not in composed words, these will be completed by the DynamicPathCompletionProvider
        contributor.extend(CompletionType.BASIC, StandardPatterns.instanceOf(PsiElement.class), this);
    }

    protected void addBashCompletions(String currentText, CompletionParameters parameters, ProcessingContext context, CompletionResultSet resultWithoutPrefix) {
        if (currentText == null || !currentText.startsWith("/")) {
            return;
        }

        PsiElement parentElement = parameters.getPosition().getParent();
        String parentText = parentElement.getText();
        if (parentText.startsWith("$HOME") || parentText.startsWith("~")) {
            return;
        }

        CompletionResultSet result = resultWithoutPrefix.withPrefixMatcher(new PathPrefixMatcher(currentText));

        final int invocationCount = parameters.getInvocationCount();

        Predicate<File> incovationCountPredicate = new Predicate<File>() {
            public boolean test(File file) {
                //accept hidden file with more than one invocation
                //return file.isHidden() ? invocationCount >= 2 : true;
                return (file.isHidden() && (invocationCount >= 2)) || ((invocationCount >= 1) && !file.isHidden());
            }
        };

        List<String> completions = CompletionUtil.completeAbsolutePath(currentText, createFileFilter().and(incovationCountPredicate));
        result.addAllElements(CompletionProviderUtils.createPathItems(completions));

        int validResultCount = computeResultCount(completions, result);

        if (validResultCount == 0 && invocationCount == 1) {
            //do hidden-completion now
            List<String> secondCompletions = CompletionUtil.completeAbsolutePath(currentText, createFileFilter());
            result.addAllElements(CompletionProviderUtils.createPathItems(secondCompletions));
        }

        if (invocationCount == 1) {
            result.addLookupAdvertisement("Press twice for hidden files");
        }
    }

    private static class PathPrefixMatcher extends PrefixMatcher {
        protected PathPrefixMatcher(String prefix) {
            super(prefix);
        }

        @Override
        public boolean prefixMatches(@Nonnull String name) {
            return name.startsWith(getPrefix());
        }

        @Nonnull
        @Override
        public PrefixMatcher cloneWithPrefix(@Nonnull String prefix) {
            return new PathPrefixMatcher(prefix);
        }
    }
}
