/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFunctionDefImpl.java, Class: BashFunctionDefImpl
 * Last modified: 2013-05-05
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
package com.ansorgit.plugins.bash.lang.psi.impl.function;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashElementSharedImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashGroupImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFunctionDefStub;
import com.ansorgit.plugins.bash.lang.psi.util.BashChangeUtil;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.language.ast.ASTNode;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.StubBasedPsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.PsiScopesUtilCore;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;
import consulo.util.dataholder.Key;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Joachim Ansorg
 */
public class BashFunctionDefImpl extends BashBaseStubElementImpl<BashFunctionDefStub> implements BashFunctionDef, StubBasedPsiElement<BashFunctionDefStub> {
    private static final Logger log = Logger.getInstance("#Bash.BashFunctionDefImpl");

    public BashFunctionDefImpl(ASTNode astNode) {
        super(astNode, "bash function()");
    }

    public BashFunctionDefImpl(@Nonnull BashFunctionDefStub stub, @Nonnull IStubElementType nodeType) {
        super(stub, nodeType, null);
    }

    @RequiredWriteAction
    public PsiElement setName(@Nonnull String name) throws IncorrectOperationException {
        if (StringUtil.isEmpty(name)) {
            return null;
        }

        //fixme validate name

        PsiElement nameNode = getNameSymbol();
        PsiElement newNameSymbol = BashChangeUtil.createSymbol(getProject(), name);

        if (log.isDebugEnabled()) {
            log.debug("renamed to symbol " + newNameSymbol);
        }

        getNode().replaceChild(nameNode.getNode(), newNameSymbol.getNode());
        return this;
    }

    @Override
    @RequiredReadAction
    public String getName() {
        return getDefinedName();
    }

    @Override
    @RequiredReadAction
    public BashBlock body() {
        BashGroupImpl bashGroup = findChildByClass(BashGroupImpl.class);

        if (log.isDebugEnabled()) {
            log.debug("found commandGroup: " + bashGroup);
        }

        return bashGroup;
    }

    @Override
    @RequiredReadAction
    public boolean hasCommandGroup() {
        log.debug("hasCommandGroup");

        BashBlock body = body();

        return body != null && body.isCommandGroup();
    }

    private static final Key<BashFunctionDefName> NAME_SYMBOL_KEY = Key.create("nameSymbol");

    @Override
    @RequiredReadAction
    public BashFunctionDefName getNameSymbol() {
        BashFunctionDefName nameWord = findChildByClass(BashFunctionDefName.class);

        if (log.isDebugEnabled()) {
            log.debug("getNameSymbol result: " + nameWord);
        }

        putUserData(NAME_SYMBOL_KEY, nameWord);

        return nameWord;
    }

    @Override
    public List<PsiComment> findAttachedComment() {
        return BashPsiUtils.findDocumentationElementComments(this);
    }

    @Nonnull
    @Override
    @RequiredReadAction
    public List<BashVar> findReferencedParameters() {
        //call the visitor to find all uses of the parameter variables
        Collection<BashVar> usedVariables = PsiTreeUtil.collectElementsOfType(this, BashVar.class);

        List<BashVar> parameters = new LinkedList<>();

        for (BashVar var : usedVariables) {
            if (var.isParameterReference()) {
                parameters.add(var);
            }
        }

        return parameters;
    }

    private Key<String> DEFINED_NAME_KEY = Key.create("definedName");

    @RequiredReadAction
    public String getDefinedName() {
        BashFunctionDefName symbol = getNameSymbol();
        String result = symbol == null ? "" : symbol.getNameString();

        putUserData(DEFINED_NAME_KEY, result);

        return result;
    }

    @Override
    @RequiredReadAction
    public int getTextOffset() {
        ASTNode name = getNameSymbol().getNode();
        return name != null ? name.getStartOffset() : super.getTextOffset();
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            @RequiredReadAction
            public LocalizeValue getPresentableText() {
                return LocalizeValue.of(getName() + "()");
            }

            @Override
            public @Nullable String getLocationString() {
                return null;
            }

            @Override
            @RequiredReadAction
            public @Nullable Image getIcon() {
                return IconDescriptorUpdaters.getIcon(BashFunctionDefImpl.this, 0);
            }
        };
    }

    @Override
    @RequiredReadAction
    public PsiElement getNameIdentifier() {
        return getNameSymbol();
    }

    @Override
    public void accept(@Nonnull PsiElementVisitor visitor) {
        if (visitor instanceof BashVisitor bashVisitor) {
            bashVisitor.visitFunctionDef(this);
        }
        else {
            visitor.visitElement(this);
        }
    }

    @Override
    @RequiredReadAction
    public boolean processDeclarations(
        @Nonnull PsiScopeProcessor processor,
        @Nonnull ResolveState state,
        PsiElement lastParent,
        @Nonnull PsiElement place
    ) {
        if (!processor.execute(this, state)) {
            return false;
        }

        //the group does its own processing, do not duplicate it here
        if (!hasCommandGroup()) {
            return BashElementSharedImpl.walkDefinitionScope(this, processor, state, lastParent, place);
        }

        return PsiScopesUtilCore.walkChildrenScopes(this, processor, state, lastParent, place);
    }
}
