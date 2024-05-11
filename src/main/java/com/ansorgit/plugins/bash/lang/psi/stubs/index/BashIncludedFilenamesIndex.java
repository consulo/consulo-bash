package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StubIndexKey;
import consulo.language.psi.stub.StringStubIndexExtension;

/**
 * User: jansorg
 * Date: 11.01.12
 * Time: 22:46
 */
@ExtensionImpl
public class BashIncludedFilenamesIndex extends StringStubIndexExtension<BashIncludeCommand> {
    public static final StubIndexKey<String, BashIncludeCommand> KEY = StubIndexKey.createIndexKey("bash.included");

    @Override
    public StubIndexKey<String, BashIncludeCommand> getKey() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return BashIndexVersion.VERSION;
    }
}
