package com.ansorgit.plugins.bash.lang.psi.stubs.api;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import consulo.language.psi.stub.StubElement;

/**
 * @author jansorg
 */
public interface BashIncludeCommandStub extends StubElement<BashIncludeCommand> {
    String getIncludedFilename();

    String getIncluderFilename();
}
