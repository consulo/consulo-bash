package com.ansorgit.plugins.bash.lang.psi.stubs.impl;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFileStub;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.IStubFileElementType;
import consulo.language.psi.stub.PsiFileStubImpl;

/**
 * @author jansorg
 */
public class BashFileStubImpl extends PsiFileStubImpl<BashFile> implements BashFileStub {
  private final StringRef myName;

  public BashFileStubImpl(BashFile file) {
    super(file);
    myName = StringRef.fromString(file.getName());
  }

  public BashFileStubImpl(StringRef name) {
    super(null);
    myName = name;
  }

  public IStubFileElementType getType() {
      return BashElementTypes.FILE;
  }

  public StringRef getName() {
    return myName;
  }
}