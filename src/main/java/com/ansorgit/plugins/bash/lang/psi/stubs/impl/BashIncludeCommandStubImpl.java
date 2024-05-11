package com.ansorgit.plugins.bash.lang.psi.stubs.impl;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashIncludeCommandStub;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubBase;
import consulo.language.psi.stub.StubElement;

/**
 * @author ilyas
 */
public class BashIncludeCommandStubImpl extends StubBase<BashIncludeCommand> implements BashIncludeCommandStub {
    private final StringRef includedFilename;
    private final StringRef includerFilename;

    public BashIncludeCommandStubImpl(StubElement parent, StringRef includedFilename, StringRef included, final IStubElementType elementType) {
        super(parent, elementType);
        this.includedFilename = includedFilename;
        this.includerFilename = included;
    }

    @Override
    public String getIncludedFilename() {
        return StringRef.toString(includedFilename);
    }

    public String getIncluderFilename() {
        return StringRef.toString(includerFilename);
    }
}