/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: CompletionProviderUtils.java, Class: CompletionProviderUtils
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

import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.completion.lookup.PrioritizedLookupElement;
import consulo.language.psi.PsiNamedElement;
import consulo.ui.image.Image;
import consulo.util.collection.ContainerUtil;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * User: jansorg
 * Date: 07.02.11
 * Time: 18:38
 */
class CompletionProviderUtils {
    private CompletionProviderUtils() {
    }

    static Collection<LookupElement> createPsiItems(Collection<? extends PsiNamedElement> elements) {
        return ContainerUtil.map(elements, from -> LookupElementBuilder.create(from).withCaseSensitivity(true));
    }

    static Collection<LookupElement> createItems(Iterable<String> items, final Image icon) {
        return ContainerUtil.map(ContainerUtil.newArrayList(items), from -> LookupElementBuilder.create(from).withCaseSensitivity(true).withIcon(icon));
    }

    static Collection<LookupElement> wrapInGroup(final int groupId, Collection<LookupElement> elements) {
        return ContainerUtil.map(elements, lookupElement -> PrioritizedLookupElement.withGrouping(lookupElement, groupId));
    }

    static Collection<LookupElement> createPathItems(List<String> paths) {
        Function<String, LookupElement> transformationFunction = path -> new PathLookupElement(path, !path.endsWith("/"));

        Predicate<String> isRelativePath = path -> !path.startsWith("/");

        Collection<String> relativePaths = ContainerUtil.filter(paths, isRelativePath);
        Collection<LookupElement> relativePathItems = ContainerUtil.map(relativePaths, transformationFunction);

        Collection<String> absolutePaths = ContainerUtil.filter(paths, isRelativePath.negate());
        Collection<LookupElement> absolutePathItems = ContainerUtil.map(absolutePaths, transformationFunction);

        Collection<LookupElement> result = new LinkedList<>();
        result.addAll(wrapInGroup(CompletionGrouping.RelativeFilePath.ordinal(), relativePathItems));
        result.addAll(wrapInGroup(CompletionGrouping.AbsoluteFilePath.ordinal(), absolutePathItems));

        return result;
    }
}
