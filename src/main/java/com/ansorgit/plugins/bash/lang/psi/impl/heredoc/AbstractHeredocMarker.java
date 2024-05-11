/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: AbstractHeredocMarker.java, Class: AbstractHeredocMarker
 * Last modified: 2011-04-30 16:33
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

package com.ansorgit.plugins.bash.lang.psi.impl.heredoc;

import jakarta.annotation.Nonnull;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.ResolveProcessor;
import com.ansorgit.plugins.bash.lang.psi.api.heredoc.BashHereDocMarker;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import com.ansorgit.plugins.bash.lang.psi.util.BashChangeUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashIdentifierUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.language.ast.ASTNode;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.language.psi.PsiReference;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.stub.StubElement;
import org.jetbrains.annotations.NonNls;

/**
 * Abstract base class for heredoc markers.
 * <p/>
 * User: jansorg
 * Date: Jan 30, 2010
 * Time: 12:48:49 PM
 */
abstract class AbstractHeredocMarker extends BashBaseStubElementImpl<StubElement> implements BashHereDocMarker, PsiReference {
    private final Object[] EMPTY = new Object[0];
    private final Class<? extends BashPsiElement> otherEndsType;
    private final boolean expectLater;


    public AbstractHeredocMarker(ASTNode astNode, String name, @Nonnull Class<? extends BashPsiElement> otherEndsType, boolean expectLater) {
        super(astNode, name);
        this.otherEndsType = otherEndsType;
        this.expectLater = expectLater;
    }

    @Override
    public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement place) {
        return processor.execute(this, state);
    }

    @Override
    public String getName() {
        return getText();
    }

    public PsiElement setName(@Nonnull @NonNls String newname) throws IncorrectOperationException {
        if (!BashIdentifierUtil.isValidIdentifier(newname)) {
            throw new IncorrectOperationException("The name is empty");
        }

        return BashPsiUtils.replaceElement(this, BashChangeUtil.createWord(getProject(), newname));
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    public String getReferencedName() {
        return getText();
    }

    public PsiElement getElement() {
        return this;
    }

    public PsiElement getNameIdentifier() {
        return this;
    }

    public TextRange getRangeInElement() {
        return TextRange.from(0, getTextLength());
    }

    public PsiElement resolve() {
        final String varName = getText();
        if (varName == null) {
            return null;
        }

        final ResolveProcessor processor = new BashHereDocMarkerProcessor(getReferencedName(), otherEndsType);
        if (expectLater) {
            PsiTreeUtil.treeWalkUp(processor, this, this.getContainingFile(), ResolveState.initial());
        } else {
            PsiTreeUtil.treeWalkUp(processor, this, this.getContainingFile(), ResolveState.initial());
        }

        return processor.getBestResult(true, this);
    }

    public String getCanonicalText() {
        return getText();
    }

    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return setName(newElementName);
    }

    public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException("Not yet implemented");
    }

    public boolean isReferenceTo(PsiElement element) {
        return otherEndsType.isInstance(element) && element.getText().equals(getText());
    }

    @Nonnull
    public Object[] getVariants() {
        return EMPTY;
    }

    public boolean isSoft() {
        return false;
    }
}
