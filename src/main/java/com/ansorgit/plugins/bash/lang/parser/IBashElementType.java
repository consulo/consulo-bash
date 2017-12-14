package com.ansorgit.plugins.bash.lang.parser;

import org.jetbrains.annotations.NonNls;
import com.ansorgit.plugins.bash.file.BashFileType;
import com.intellij.psi.tree.IElementType;

public class IBashElementType extends IElementType {
    private final boolean myLeftBound;

    public IBashElementType(@NonNls final String debugName) {
        this(debugName, false);
    }

    public IBashElementType(@NonNls final String debugName, final boolean leftBound) {
        super(debugName, BashFileType.BASH_LANGUAGE);
        myLeftBound = leftBound;
    }

    @Override
    public boolean isLeftBound() {
        return myLeftBound;
    }
}