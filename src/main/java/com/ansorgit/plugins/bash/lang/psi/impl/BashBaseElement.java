package com.ansorgit.plugins.bash.lang.psi.impl;

import javax.annotation.Nonnull;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;

public class BashBaseElement extends ASTWrapperPsiElement implements BashPsiElement {
    private String name;

    public BashBaseElement(@Nonnull ASTNode node, String name) {
        super(node);
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
}
