/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashIncludeCommandImpl.java, Class: BashIncludeCommandImpl
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

package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashIncludeCommandStub;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiFile;
import consulo.language.psi.StubBasedPsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.PsiScopesUtilCore;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.stub.IStubElementType;
import consulo.util.collection.MultiMap;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * User: jansorg
 * Date: 18.02.11
 * Time: 20:17
 */
public class BashIncludeCommandImpl extends BashCommandImpl<BashIncludeCommandStub> implements BashIncludeCommand, StubBasedPsiElement<BashIncludeCommandStub> {
    public BashIncludeCommandImpl(ASTNode astNode) {
        super(astNode, "Bash include command");
    }

    public BashIncludeCommandImpl(@Nonnull BashIncludeCommandStub stub, @Nonnull IStubElementType nodeType) {
        super(stub, nodeType, null);
    }

    @Nullable
    public BashFileReference getFileReference() {
        return findChildByClass(BashFileReference.class);
    }

    @Override
    public void accept(@Nonnull PsiElementVisitor visitor) {
        if (visitor instanceof BashVisitor) {
            ((BashVisitor) visitor).visitIncludeCommand(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public boolean isIncludeCommand() {
        return true;
    }

    @Override
    public boolean isFunctionCall() {
        return false;
    }

    @Override
    public boolean isInternalCommand() {
        return true;
    }

    @Override
    public boolean isExternalCommand() {
        return false;
    }

    @Override
    public boolean isPureAssignment() {
        return false;
    }

    @Override
    public boolean isVarDefCommand() {
        return false;
    }

    @Override
    public boolean canNavigate() {
        return canNavigateToSource();
    }

    @Override
    public boolean canNavigateToSource() {
        return getFileReference().findReferencedFile() != null;
    }

    @Override
    public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement place) {
        boolean result = PsiScopesUtilCore.walkChildrenScopes(this, processor, state, lastParent, place);
        if (!result) {
            //processing is done here
            return false;
        }

        PsiFile containingFile = getContainingFile();
        PsiFile includedFile = BashPsiUtils.findIncludedFile(this);

        MultiMap<VirtualFile, PsiElement> visitedFiles = state.get(visitedIncludeFiles);
        if (visitedFiles == null) {
            visitedFiles = MultiMap.createLinked();
        }

        visitedFiles.put(containingFile.getVirtualFile(), null);

        if (includedFile != null && !visitedFiles.containsKey(includedFile.getVirtualFile())) {
            //mark the file as visited before the actual visit, otherwise we'll get a stack overflow
            visitedFiles.putValue(includedFile.getVirtualFile(), this);

            state = state.put(visitedIncludeFiles, visitedFiles);

            return includedFile.processDeclarations(processor, state, lastParent, place);
        }


        return result;
    }
}
