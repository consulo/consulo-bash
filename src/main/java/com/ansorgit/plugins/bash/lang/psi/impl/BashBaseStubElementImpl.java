/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashBaseElementImpl.java, Class: BashBaseElementImpl
 * Last modified: 2013-04-20
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
import javax.annotation.Nullable;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.scope.util.PsiScopesUtilCore;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;

/**
 * @author Joachim Ansorg
 */
public abstract class BashBaseStubElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> implements BashPsiElement {
    private final String name;

    public BashBaseStubElementImpl(final ASTNode astNode) {
        this(astNode, null);
    }

    public BashBaseStubElementImpl(final ASTNode astNode, @Nullable final String name) {
        super(astNode);
        this.name = name;
    }

    public BashBaseStubElementImpl(@Nonnull T stub, @Nonnull IStubElementType nodeType, @Nullable String name) {
        super(stub, nodeType);
        this.name = name;
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return BashFileType.BASH_LANGUAGE;
    }

    @Override
    public String toString() {
        return "[PSI] " + (name == null ? super.toString() : name);
    }

    @Nonnull
    @Override
    public SearchScope getUseScope() {
        return BashElementSharedImpl.getElementUseScope(this, getProject());
    }

    @Nonnull
    @Override
    public GlobalSearchScope getResolveScope() {
        return BashElementSharedImpl.getElementGlobalSearchScope(this, getProject());
    }

    @Override
    public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement place) {
        if (!processor.execute(this, state)) {
            return false;
        }

        return PsiScopesUtilCore.walkChildrenScopes(this, processor, state, lastParent, place);
    }
}
