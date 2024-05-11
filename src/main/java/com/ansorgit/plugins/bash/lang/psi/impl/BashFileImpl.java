/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFileImpl.java, Class: BashFileImpl
 * Last modified: 2013-05-12
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import consulo.language.file.FileViewProvider;
import consulo.language.impl.psi.PsiFileBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.util.lang.ObjectUtil;
import consulo.virtualFileSystem.fileType.FileType;

import jakarta.annotation.Nonnull;

/**
 * PSI implementation for a Bash file.
 */
public class BashFileImpl extends PsiFileBase implements BashFile {
    public BashFileImpl(FileViewProvider viewProvider) {
        super(viewProvider, BashFileType.BASH_LANGUAGE);
    }

    @Nonnull
    public FileType getFileType() {
        return BashFileType.INSTANCE;
    }

    public boolean hasShebangLine() {
        return findChildByClass(BashShebang.class) != null;
    }

    public BashFunctionDef[] functionDefinitions() {
        return findChildrenByClass(BashFunctionDef.class);
    }

    @Override
    public boolean processDeclarations(@Nonnull final PsiScopeProcessor processor, @Nonnull final ResolveState state, final PsiElement lastParent, @Nonnull final PsiElement place) {
        if (!processor.execute(this, state)) {
            return false;
        }

        boolean walkDeep = ObjectUtil.notNull(processor.getHint(Keys.FILE_WALK_GO_DEEP), true);
        boolean moreProcessing = true;
        if (walkDeep) {
            PsiElement child = getFirstChild();

            while (child != null) {
                if (child != lastParent && !child.processDeclarations(processor, state, lastParent, place)) {
                    moreProcessing = false;
                }

                child = child.getNextSibling();
            }
        } else {
            //walk the toplevel psi elements without diving into them
            //we can compute the first element to walk a bit smarter than getFirstChild().
            //It's the next toplevel element after place, i.e. starting element

            PsiElement child = getFirstChild();
            while (child != null) {
                if (!processor.execute(child, state)) {
                    moreProcessing = false;
                    break;
                }
                child = child.getNextSibling();
            }
        }

        return moreProcessing;
    }

    @Override
    public void accept(@Nonnull PsiElementVisitor visitor) {
        if (visitor instanceof BashVisitor) {
            ((BashVisitor) visitor).visitFile(this);
        } else {
            visitor.visitFile(this);
        }
    }
}
