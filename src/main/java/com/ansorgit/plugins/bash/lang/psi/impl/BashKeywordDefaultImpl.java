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

import javax.annotation.Nonnull;

import com.ansorgit.plugins.bash.lang.psi.api.BashKeyword;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import consulo.ui.image.Image;

/**
 * Date: 06.05.2009
 * Time: 12:19:43
 *
 * @author Joachim Ansorg
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

    public PsiElement getElement() {
        return this;
    }

    @Override
    public boolean canNavigate() {
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }

    public TextRange getRangeInElement() {
        PsiElement keyword = keywordElement();
        if (keyword != null) return TextRange.from(keyword.getStartOffsetInParent(), keyword.getTextLength());

        return TextRange.from(0, getTextLength());
    }

    public PsiElement resolve() {
        return this;
    }

    @Nonnull
    public String getCanonicalText() {
        return getText();
    }

    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException("bindToElement not implemented");
    }

    public boolean isReferenceTo(PsiElement element) {
        return element == this;
    }

    @Nonnull
    public Object[] getVariants() {
        return EMPTY;
    }

    public boolean isSoft() {
        return false;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                final PsiElement element = keywordElement();
                return element != null ? element.getText() : null;
            }

            public String getLocationString() {
                return null;
            }

            public Image getIcon() {
                return null;
            }
        };
    }
}
