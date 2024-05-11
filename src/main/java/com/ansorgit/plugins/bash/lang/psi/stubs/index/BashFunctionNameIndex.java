package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.language.psi.stub.StubIndexKey;

/**
 * User: jansorg
 * Date: 11.01.12
 * Time: 22:46
 */
@ExtensionImpl
public class BashFunctionNameIndex extends StringStubIndexExtension<BashFunctionDef> {
    public static final StubIndexKey<String, BashFunctionDef> KEY = StubIndexKey.createIndexKey("bash.function.name");

    @Override
    public StubIndexKey<String, BashFunctionDef> getKey() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return BashIndexVersion.VERSION;
    }
}
