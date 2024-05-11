package com.ansorgit.plugins.bash.lang.psi.stubs.index;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.language.psi.stub.StubIndexKey;

/**
 * User: jansorg
 * Date: 11.01.12
 * Time: 22:46
 */
@ExtensionImpl
public class BashVarDefIndex extends StringStubIndexExtension<BashVarDef> {
    public static final StubIndexKey<String, BashVarDef> KEY = StubIndexKey.createIndexKey("bash.vardef");

    @Override
    public StubIndexKey<String, BashVarDef> getKey() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return BashIndexVersion.VERSION;
    }
}
