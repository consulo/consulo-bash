/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: BashCompletionContributor.java, Class: BashCompletionContributor
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

import com.ansorgit.plugins.bash.lang.BashLanguage;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.completion.CompletionContributor;
import consulo.language.editor.completion.CompletionInitializationContext;
import consulo.language.psi.PsiElement;
import consulo.language.editor.completion.OffsetMap;

import jakarta.annotation.Nonnull;

import static consulo.language.editor.completion.CompletionInitializationContext.IDENTIFIER_END_OFFSET;
import static consulo.language.editor.completion.CompletionInitializationContext.START_OFFSET;

/**
 * Bash completion contributor.
 */
@ExtensionImpl
public class BashCompletionContributor extends CompletionContributor {
    public BashCompletionContributor() {
        new VariableNameCompletionProvider().addTo(this);
        new CommandNameCompletionProvider().addTo(this);
        new AbsolutePathCompletionProvider().addTo(this);
        new ShebangPathCompletionProvider().addTo(this);
        new DynamicPathCompletionProvider().addTo(this);
    }

    @Override
    public void beforeCompletion(@Nonnull CompletionInitializationContext context) {
        context.setDummyIdentifier("ZZZ");
    }

    @Override
    public void duringCompletion(@Nonnull CompletionInitializationContext context) {
        fixComposedWordEndOffset(context);
    }

    /**
     * Fix the replace offset, for composed bash words use the full composed range
     *
     * @param context The current invocation context
     */
    protected void fixComposedWordEndOffset(CompletionInitializationContext context) {
        PsiElement element = context.getFile().findElementAt(context.getStartOffset());
        if (element == null) {
            return;
        }

        //try the parent if it's not already a BashWord
        if (!(element instanceof BashWord)) {
            element = element.getParent();
        }

        if (element instanceof BashWord) {
            int endOffset = element.getTextOffset() + element.getTextLength();

            OffsetMap offsetMap = context.getOffsetMap();
            offsetMap.addOffset(START_OFFSET, element.getTextOffset());
            offsetMap.addOffset(IDENTIFIER_END_OFFSET, endOffset);
        }
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return BashLanguage.INSTANCE;
    }
}
