/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: BashCompletionProvider.java, Class: BashCompletionProvider
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

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import consulo.codeInsight.completion.CompletionProvider;

/**
 * Abstract base class for completion providers in Bash files.
 */
abstract class BashCompletionProvider implements CompletionProvider
{
    public BashCompletionProvider() {
    }

    abstract void addTo(CompletionContributor contributor);

    protected Predicate<File> createFileFilter() {
        return Predicates.alwaysTrue();
    }

    @Override
    public final void addCompletions(@Nonnull CompletionParameters parameters,
                                        ProcessingContext context,
                                        @Nonnull CompletionResultSet resultWithoutPrefix) {

        addBashCompletions(findCurrentText(parameters, parameters.getPosition()), parameters, context, resultWithoutPrefix);
    }

    protected String findOriginalText(PsiElement element) {
        return element.getText();
    }

    protected String findCurrentText(CompletionParameters parameters, PsiElement element) {
        String originalText = findOriginalText(element);
        int elementOffset = parameters.getOffset() - element.getTextOffset();

        return (elementOffset >= 0) && (elementOffset < originalText.length())
                ? originalText.substring(0, elementOffset)
                : originalText;
    }

    protected abstract void addBashCompletions(String currentText, CompletionParameters parameters, ProcessingContext context, CompletionResultSet resultWithoutPrefix);

    protected int computeResultCount(List<String> completions, CompletionResultSet result) {
        PrefixMatcher prefixMatcher = result.getPrefixMatcher();

        int resultCount = 0;

        for (String c : completions) {
            if (prefixMatcher.prefixMatches(c)) {
                resultCount++;
            }
        }

        return resultCount;
    }
}
