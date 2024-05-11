package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.language.psi.stub.StubIndexKey;

/**
 * User: jansorg
 * Date: 11.01.12
 * Time: 22:46
 */
@ExtensionImpl
public class BashScriptNameIndex extends StringStubIndexExtension<BashFile> {
    public static final StubIndexKey<String, BashFile> KEY = StubIndexKey.createIndexKey("bash.script.name");

    @Override
    public StubIndexKey<String, BashFile> getKey() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return BashIndexVersion.VERSION;
    }
}
