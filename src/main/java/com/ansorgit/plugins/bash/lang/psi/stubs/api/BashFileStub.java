package com.ansorgit.plugins.bash.lang.psi.stubs.api;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.PsiFileStub;

/**
 * @author jansorg
 */
public interface BashFileStub extends PsiFileStub<BashFile> {
    StringRef getName();
}

