/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
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

package com.ansorgit.plugins.bash.settings.facet.ui;

import consulo.bash.localize.BashLocalize;
import consulo.module.Module;
import consulo.module.content.ModuleFileIndex;
import consulo.module.content.ModuleRootManager;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.TreeModel;
import consulo.ui.TreeNode;
import consulo.ui.image.Image;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import jakarta.annotation.Nullable;

import java.util.Comparator;
import java.util.function.Function;

class BashModuleFileTreeModel implements TreeModel<Object> {
    private static final Comparator<TreeNode<Object>> NODE_COMPARATOR = (node1, node2) -> {
        Object o1 = node1.getValue();
        Object o2 = node2.getValue();
        if (o1 == o2) {
            return 0;
        }
        if (o1 instanceof Module) {
            return -1;
        }
        if (o2 instanceof Module) {
            return 1;
        }
        if (o1 instanceof VirtualFile file1 && o2 instanceof VirtualFile file2) {
            if (file1.isDirectory() != file2.isDirectory()) {
                return file1.isDirectory() ? -1 : 1;
            }
            return file1.getName().compareTo(file2.getName());
        }
        return 0;
    };

    private final Module myModule;
    private final ModuleFileFilter myFilter = new ModuleFileFilter();

    BashModuleFileTreeModel(Module module) {
        myModule = module;
    }

    @Override
    public void buildChildren(Function<Object, TreeNode<Object>> nodeFactory, @Nullable Object parentValue) {
        if (parentValue == null) {
            TreeNode<Object> moduleNode = nodeFactory.apply(myModule);
            moduleNode.setLeaf(false);
            moduleNode.setRenderer((value, presentation) -> {
                presentation.withIcon(PlatformIconGroup.nodesModule());
                presentation.append(BashLocalize.moduleExtensionModuleNode(myModule.getName()));
            });
            return;
        }

        if (parentValue instanceof Module module) {
            VirtualFile[] roots = ModuleRootManager.getInstance(module).getContentRoots();

            NextRoot:
            for (VirtualFile root : roots) {
                for (VirtualFile candidate : roots) {
                    if (VirtualFileUtil.isAncestor(candidate, root, true)) {
                        continue NextRoot;
                    }
                }

                if (myFilter.accept(root)) {
                    addFileNode(nodeFactory, root, root.getPresentableUrl());
                }
            }
            return;
        }

        if (parentValue instanceof VirtualFile parentFile) {
            ModuleFileIndex fileIndex = ModuleRootManager.getInstance(myModule).getFileIndex();
            for (VirtualFile child : parentFile.getChildren()) {
                if (myFilter.accept(child) && fileIndex.isInContent(child)) {
                    addFileNode(nodeFactory, child, child.getName());
                }
            }
        }
    }

    @Override
    public @Nullable Comparator<TreeNode<Object>> getNodeComparator() {
        return NODE_COMPARATOR;
    }

    private void addFileNode(Function<Object, TreeNode<Object>> nodeFactory, VirtualFile file, String text) {
        Image icon = VirtualFileManager.getInstance().getFileIcon(file, myModule.getProject(), 0);

        TreeNode<Object> node = nodeFactory.apply(file);
        node.setLeaf(!file.isDirectory());
        node.setRenderer((value, presentation) -> {
            presentation.withIcon(icon);
            presentation.append(text);
        });
    }
}
