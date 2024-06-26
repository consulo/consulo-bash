/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashBlock.java, Class: BashBlock
 * Last modified: 2011-04-30 16:33
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

package com.ansorgit.plugins.bash.editor.formatting;

import com.ansorgit.plugins.bash.editor.formatting.processor.BashSpacingProcessor;
import com.ansorgit.plugins.bash.editor.formatting.processor.BashSpacingProcessorBasic;
import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.ast.ILazyParseableElementType;
import consulo.language.codeStyle.*;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiErrorElement;
import consulo.language.psi.PsiWhiteSpace;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;

/**
 * Block implementation for the Bash formatter.
 * <p/>
 * This class is based on the block code for the Groovy formatter.
 *
 * @author ilyas, jansorg
 */
public class BashBlock implements Block, BashElementTypes {

    final protected ASTNode myNode;
    final protected Alignment myAlignment;
    final protected Indent myIndent;
    final protected Wrap myWrap;
    final protected CodeStyleSettings mySettings;

    protected List<Block> mySubBlocks = null;

    public BashBlock(@Nonnull final ASTNode node, @Nullable final Alignment alignment, @Nonnull final Indent indent, @Nullable final Wrap wrap, final CodeStyleSettings settings) {
        myNode = node;
        myAlignment = alignment;
        myIndent = indent;
        myWrap = wrap;
        mySettings = settings;
    }

    @Nonnull
    public ASTNode getNode() {
        return myNode;
    }

    @Nonnull
    public CodeStyleSettings getSettings() {
        return mySettings;
    }

    @Nonnull
    public TextRange getTextRange() {
        return myNode.getTextRange();
    }

    @Nonnull
    public List<Block> getSubBlocks() {
        if (mySubBlocks == null) {
            mySubBlocks = BashBlockGenerator.generateSubBlocks(myNode, myAlignment, myWrap, mySettings, this);
        }
        return mySubBlocks;
    }

    @Nullable
    public Wrap getWrap() {
        return myWrap;
    }

    @Nullable
    public Indent getIndent() {
        return myIndent;
    }

    @Nullable
    public Alignment getAlignment() {
        return myAlignment;
    }

    /**
     * Returns spacing between neighrbour elements
     *
     * @param child1 left element
     * @param child2 right element
     * @return
     */
    @Nullable
    public Spacing getSpacing(Block child1, Block child2) {
        if ((child1 instanceof BashBlock) && (child2 instanceof BashBlock)) {
            Spacing spacing = BashSpacingProcessor.getSpacing(((BashBlock) child1), ((BashBlock) child2), mySettings);
            return spacing != null ? spacing : BashSpacingProcessorBasic.getSpacing(((BashBlock) child1), ((BashBlock) child2), mySettings);
        }
        return null;
    }

    @Nonnull
    public ChildAttributes getChildAttributes(final int newChildIndex) {
        return getAttributesByParent();
    }

    private ChildAttributes getAttributesByParent() {
        ASTNode astNode = myNode;
        final PsiElement psiParent = astNode.getPsi();
        if (psiParent instanceof BashFile) {
            return new ChildAttributes(Indent.getNoneIndent(), null);
        }

        if (BLOCK_ELEMENT == astNode.getElementType()) {
            return new ChildAttributes(Indent.getNormalIndent(), null);
        }

        return new ChildAttributes(Indent.getNoneIndent(), null);
    }


    public boolean isIncomplete() {
        return isIncomplete(myNode);
    }

    /**
     * @param node Tree node
     * @return true if node is incomplete
     */
    public boolean isIncomplete(@Nonnull final ASTNode node) {
        if (node.getElementType() instanceof ILazyParseableElementType) {
            return false;
        }
        ASTNode lastChild = node.getLastChildNode();
        while (lastChild != null &&
                !(lastChild.getElementType() instanceof ILazyParseableElementType) &&
                (lastChild.getPsi() instanceof PsiWhiteSpace || lastChild.getPsi() instanceof PsiComment)) {
            lastChild = lastChild.getTreePrev();
        }
        return lastChild != null && (lastChild.getPsi() instanceof PsiErrorElement || isIncomplete(lastChild));
    }

    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
