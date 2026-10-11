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
import consulo.annotation.access.RequiredReadAction;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.language.psi.PsiElement;
import consulo.localize.LocalizeValue;
import consulo.navigation.NavigationItem;
import consulo.logging.Logger;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiNamedElement;
import consulo.fileEditor.structureView.tree.TreeElement;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;
import org.jspecify.annotations.Nullable;

/**
 * @author Joachim Ansorg
 * @since 2009-04-12
 */
class BashStructureViewElement implements StructureViewTreeElement {
    final PsiElement myElement;
    private static final Logger log = Logger.getInstance("#Bash.StructureViewElement");

    BashStructureViewElement(PsiElement element) {
        this.myElement = element;
    }

    @Override
    public PsiElement getValue() {
        return myElement;
    }

    @Override
    public ItemPresentation getPresentation() {
        if (myElement instanceof NavigationItem navItem) {
            return navItem.getPresentation();
        }

        //fallback
        return new ItemPresentation() {
            @Override
            @RequiredReadAction
            public LocalizeValue getPresentableText() {
                return LocalizeValue.ofNullable(((PsiNamedElement) myElement).getName());
            }

            @Override
            public @Nullable String getLocationString() {
                return null;
            }

            @Override
            @RequiredReadAction
            public @Nullable Image getIcon() {
                return IconDescriptorUpdaters.getIcon(myElement, 0);
            }
        };
    }

    @Override
    public TreeElement[] getChildren() {
        final List<BashPsiElement> childrenElements = new ArrayList<>();
        myElement.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (isBrowsableElement(element)) {
                    childrenElements.add((BashPsiElement) element);
                }
                else {
                    element.acceptChildren(this);
                }
            }
        });

        StructureViewTreeElement[] children = new StructureViewTreeElement[childrenElements.size()];
        int length = children.length;
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

    @Override
    @RequiredReadAction
    public void navigate(boolean requestFocus) {
        ((NavigationItem) myElement).navigate(requestFocus);
    }

    @Override
    @RequiredReadAction
    public boolean canNavigate() {
        return ((NavigationItem) myElement).canNavigateToSource();
    }

    @Override
    @RequiredReadAction
    public boolean canNavigateToSource() {
        return ((NavigationItem) myElement).canNavigateToSource();
    }
}
