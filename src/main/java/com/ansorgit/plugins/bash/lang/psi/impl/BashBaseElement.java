package com.ansorgit.plugins.bash.lang.psi.impl;

import jakarta.annotation.Nonnull;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import consulo.content.scope.SearchScope;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.scope.GlobalSearchScope;

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
