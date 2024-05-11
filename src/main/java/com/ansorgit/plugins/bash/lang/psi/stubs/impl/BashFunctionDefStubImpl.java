package com.ansorgit.plugins.bash.lang.psi.stubs.impl;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFunctionDefStub;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubBase;
import consulo.language.psi.stub.StubElement;

/**
 * @author ilyas
 */
public class BashFunctionDefStubImpl extends StubBase<BashFunctionDef> implements BashFunctionDefStub {
    private final StringRef myName;

    public BashFunctionDefStubImpl(StubElement parent, StringRef name, final IStubElementType elementType) {
        super(parent, elementType);
        myName = name;
    }

    public String getName() {
        return StringRef.toString(myName);
    }
}