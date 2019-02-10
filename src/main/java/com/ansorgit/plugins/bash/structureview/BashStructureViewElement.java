/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashStructureViewElement.java, Class: BashStructureViewElement
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

package com.ansorgit.plugins.bash.structureview;

import java.util.ArrayList;
import java.util.List;

import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNamedElement;
import consulo.ide.IconDescriptorUpdaters;
import consulo.ui.image.Image;

/**
 * Date: 12.04.2009
 * Time: 20:28:14
 *
 * @author Joachim Ansorg
 */
class BashStructureViewElement implements StructureViewTreeElement {
    final PsiElement myElement;
    private static final Logger log = Logger.getInstance("#Bash.StructureViewElement");

    BashStructureViewElement(PsiElement element) {
        this.myElement = element;
    }

    public PsiElement getValue() {
        return myElement;
    }

    public ItemPresentation getPresentation() {
        if (myElement instanceof NavigationItem) {
            return ((NavigationItem) myElement).getPresentation();
        }

        //fallback
        return new ItemPresentation() {
            public String getPresentableText() {
                return ((PsiNamedElement) myElement).getName();
            }

            public String getLocationString() {
                return null;
            }

            public Image getIcon() {
                return IconDescriptorUpdaters.getIcon(myElement, 0);
            }
        };
    }

    public TreeElement[] getChildren() {
        final List<BashPsiElement> childrenElements = new ArrayList<BashPsiElement>();
        myElement.acceptChildren(new PsiElementVisitor() {
            public void visitElement(PsiElement element) {
                if (isBrowsableElement(element)) {
                    childrenElements.add((BashPsiElement) element);
                } else {
                    element.acceptChildren(this);
                }
            }
        });

        StructureViewTreeElement[] children = new StructureViewTreeElement[childrenElements.size()];
        final int length = children.length;
        for (int i = 0; i < length; i++) {
            children[i] = new BashStructureViewElement(childrenElements.get(i));
        }

        return children;
    }

    boolean isBrowsableElement(PsiElement element) {
        //log.info("Checking element " + element);
        return (element instanceof BashFunctionDef)
                && (((BashFunctionDef) element).getNameSymbol() != null);
    }

    public void navigate(boolean requestFocus) {
        ((NavigationItem) myElement).navigate(requestFocus);
    }

    public boolean canNavigate() {
        return ((NavigationItem) myElement).canNavigateToSource();
    }

    public boolean canNavigateToSource() {
        return ((NavigationItem) myElement).canNavigateToSource();
    }
}
