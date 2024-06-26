/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: ResolveProcessor.java, Class: ResolveProcessor
 * Last modified: 2010-07-04 21:24
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

package com.ansorgit.plugins.bash.lang.psi.api;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import jakarta.annotation.Nullable;

import java.util.Collection;

/**
 * Date: 12.04.2009
 * Time: 23:38:25
 *
 * @author Joachim Ansorg
 */
public interface ResolveProcessor extends PsiScopeProcessor {
    @Nullable
    PsiElement getBestResult(boolean firstResult, PsiElement referenceElement);

    @Nullable
    Collection<? extends PsiElement> getResults();

    boolean hasResults();

    void reset();
}
