/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashKeywordDefaultImpl.java, Class: BashKeywordDefaultImpl
 * Last modified: 2013-05-02
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
package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.lang.psi.api.BashKeyword;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.document.util.TextRange;
import consulo.localize.LocalizeValue;
import consulo.navigation.ItemPresentation;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.ast.IElementType;
import consulo.language.util.IncorrectOperationException;
import consulo.ui.image.Image;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.Nullable;

/**
 * @author Joachim Ansorg
 * @since 2009-05-06
 */
public abstract class BashKeywordDefaultImpl extends BashCompositeElement implements PsiReference, BashKeyword {
    private static final Object[] EMPTY = new Object[0];

    protected BashKeywordDefaultImpl(IElementType type) {
        super(type);
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    @Override
    @RequiredReadAction
    public PsiElement getElement() {
        return this;
    }

    @Override
    @RequiredReadAction
    public boolean canNavigate() {
        return false;
    }

    @Override
    @RequiredReadAction
    public boolean canNavigateToSource() {
        return false;
    }

    @Override
    @RequiredReadAction
    public TextRange getRangeInElement() {
        PsiElement keyword = keywordElement();
        if (keyword != null) return TextRange.from(keyword.getStartOffsetInParent(), keyword.getTextLength());

        return TextRange.from(0, getTextLength());
    }

    @Override
    @RequiredReadAction
    public PsiElement resolve() {
        return this;
    }

    @Nonnull
    @Override
    @RequiredReadAction
    public String getCanonicalText() {
        return getText();
    }

    @Override
    @RequiredWriteAction
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    @Override
    @RequiredWriteAction
    public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException("bindToElement not implemented");
    }

    @Override
    @RequiredReadAction
    public boolean isReferenceTo(PsiElement element) {
        return element == this;
    }

    @Nonnull
    @Override
    @RequiredReadAction
    public Object[] getVariants() {
        return EMPTY;
    }

    @Override
    @RequiredReadAction
    public boolean isSoft() {
        return false;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            @RequiredReadAction
            public LocalizeValue getPresentableText() {
                PsiElement element = keywordElement();
                return element != null ? LocalizeValue.of(element.getText()) : LocalizeValue.empty();
            }

            @Override
            public @Nullable String getLocationString() {
                return null;
            }

            @Override
            public @Nullable Image getIcon() {
                return null;
            }
        };
    }
}
