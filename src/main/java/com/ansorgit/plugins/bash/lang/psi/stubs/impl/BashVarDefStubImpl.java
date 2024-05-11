package com.ansorgit.plugins.bash.lang.psi.stubs.impl;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashVarDefStub;
import consulo.language.psi.stub.StubBase;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubElement;

/**
 * @author jansorg
 */
public class BashVarDefStubImpl extends StubBase<BashVarDef> implements BashVarDefStub {
    private final StringRef name;

    public BashVarDefStubImpl(StubElement parent, StringRef name, final IStubElementType elementType) {
        super(parent, elementType);
        this.name = name;
    }

    public String getName() {
        return StringRef.toString(name);
    }
}