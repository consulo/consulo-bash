package com.ansorgit.plugins.bash.lang.psi;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

/**
 * @author jansorg
 */
public abstract class BashStubElementType<S extends StubElement, T extends BashPsiElement> extends IStubElementType<S, T> {
    public BashStubElementType(@NonNls @Nonnull String debugName) {
        super(debugName, BashFileType.BASH_LANGUAGE);
    }

    public abstract PsiElement createElement(final ASTNode node);

    public void indexStub(final S stub, final IndexSink sink) {
    }

    public String getExternalId() {
        return "bash." + super.toString();
    }
}
