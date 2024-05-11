package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.language.psi.stub.StubIndexKey;

/**
 * User: jansorg
 * Date: 11.01.12
 * Time: 22:46
 */
@ExtensionImpl
public class BashIncludeCommandIndex extends StringStubIndexExtension<BashIncludeCommand> {
    public static final StubIndexKey<String, BashIncludeCommand> KEY = StubIndexKey.createIndexKey("bash.includers");

    @Override
    public StubIndexKey<String, BashIncludeCommand> getKey() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return BashIndexVersion.VERSION;
    }
}
