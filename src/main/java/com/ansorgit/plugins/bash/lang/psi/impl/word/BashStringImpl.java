/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashStringImpl.java, Class: BashStringImpl
 * Last modified: 2011-02-18 20:22
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.ansorgit.plugins.bash.lang.psi.impl.word;

import com.ansorgit.plugins.bash.lang.LanguageBuiltins;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashCharSequence;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.document.util.ProperTextRange;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.impl.ast.LeafElement;
import consulo.language.inject.InjectedLanguageManager;
import consulo.language.psi.LiteralTextEscaper;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.stub.StubElement;
import consulo.util.lang.Pair;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * A string spanning start and end markers and content elements.
 * <p/>
 * Date: 12.04.2009
 * Time: 13:13:15
 *
 * @author Joachim Ansorg
 */
public class BashStringImpl extends BashBaseStubElementImpl<StubElement> implements BashString, BashCharSequence {
    public BashStringImpl(ASTNode node) {
        super(node, "Bash string");
    }

    public String getUnwrappedCharSequence() {
        String text = getText();
        if (text.length() <= 2) {
            return "";
        }

        return text.substring(1, text.length() - 1);
    }

    public boolean isStatic() {
        return BashPsiUtils.isStaticWordExpr(getFirstChild());
    }

    @Nonnull
    public TextRange getTextContentRange() {
        return TextRange.create(1, getTextLength() - 1);
    }

    @Override
    public void accept(@Nonnull PsiElementVisitor visitor) {
        if (visitor instanceof BashVisitor) {
            ((BashVisitor) visitor).visitString(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public boolean isValidHost() {
        BashCommand command = BashPsiUtils.findParent(this, BashCommand.class);
        return command != null && LanguageBuiltins.bashInjectionHostCommand.contains(command.getReferencedCommandName());
    }

    @Override
    public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement place) {
        boolean walkOn = super.processDeclarations(processor, state, lastParent, place);

        if (walkOn && isValidHost()) {
            InjectedLanguageManager injectedLanguageManager = InjectedLanguageManager.getInstance(getProject());
            List<Pair<PsiElement, TextRange>> injectedPsiFiles = injectedLanguageManager.getInjectedPsiFiles(this);
            if (injectedPsiFiles != null) {
                for (Pair<PsiElement, TextRange> psi_range : injectedPsiFiles) {
                    //fixme check lastParent ?
                    walkOn &= psi_range.first.processDeclarations(processor, state, lastParent, place);
                }
            }
        }

        return walkOn;
    }

    @Override
    public PsiLanguageInjectionHost updateText(@Nonnull String text) {
        ASTNode valueNode = getNode().getFirstChildNode();
        assert valueNode instanceof LeafElement;
        ((LeafElement)valueNode).replaceWithText(text);
        return this;
    }

    @Nonnull
    @Override
    public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
        //TODO [VISALL]
        return new LiteralTextEscaper<BashStringImpl>(this)
		{
			@Override
			public boolean decode(@Nonnull TextRange rangeInsideHost, @Nonnull StringBuilder builder)
			{
				ProperTextRange.assertProperRange(rangeInsideHost);
				builder.append(myHost.getText(), rangeInsideHost.getStartOffset(), rangeInsideHost.getEndOffset());
				return true;
			}

			@Override
			public int getOffsetInHost(int offsetInDecoded, @Nonnull TextRange rangeInsideHost)
			{
				int offset = offsetInDecoded + rangeInsideHost.getStartOffset();
				if (offset < rangeInsideHost.getStartOffset()) offset = rangeInsideHost.getStartOffset();
				if (offset > rangeInsideHost.getEndOffset()) offset = rangeInsideHost.getEndOffset();
				return offset;
			}

			@Override
			public boolean isOneLine()
			{
				return true;
			}
		};
    }
}
