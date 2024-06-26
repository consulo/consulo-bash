/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: AbstractFileTreeTable.java, Class: AbstractFileTreeTable
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

package com.ansorgit.plugins.bash.settings.facet.ui;

import consulo.module.Module;
import consulo.module.content.ModuleFileIndex;
import consulo.module.content.ModuleRootManager;
import consulo.ui.ex.TreeExpander;
import consulo.ui.ex.action.CommonActionsManager;
import consulo.ui.ex.awt.Messages;
import consulo.ui.ex.awt.UIUtil;
import consulo.ui.ex.awt.speedSearch.TreeTableSpeedSearch;
import consulo.ui.ex.awt.tree.DefaultTreeExpander;
import consulo.ui.ex.awt.tree.TreeUtil;
import consulo.ui.ex.awt.tree.table.TreeTable;
import consulo.ui.ex.awt.tree.table.TreeTableCellRenderer;
import consulo.ui.ex.awt.tree.table.TreeTableModel;
import consulo.ui.ex.awt.util.TableUtil;
import consulo.ui.ex.awtUnsafe.TargetAWT;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileFilter;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;

import jakarta.annotation.Nonnull;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Function;

abstract class AbstractFileTreeTable<T> extends TreeTable {
    private final MyModel<T> myModel;
    private final Module myModule;

    public AbstractFileTreeTable(final Module module, Map<VirtualFile, T> map, final Class<T> valueClass, final String valueTitle) {
        this(module, map, valueClass, valueTitle, VirtualFileFilter.ALL);
    }

    public AbstractFileTreeTable(final Module module, Map<VirtualFile, T> map, final Class<T> valueClass, final String valueTitle,
			@Nonnull VirtualFileFilter filter) {
        super(new MyModel<T>(module, map, valueClass, valueTitle, filter));
        myModule = module;

        myModel = (MyModel) getTableModel();
        myModel.setTreeTable(this);

        new TreeTableSpeedSearch(this, new Function<TreePath, String>() {
            public String apply(final TreePath o) {
                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) o.getLastPathComponent();
                final Object userObject = node.getUserObject();
                if (userObject == null) {
                    return getModuleNodeText();
                } else if (userObject instanceof VirtualFile) {
                    return ((VirtualFile) userObject).getName();
                }
                return node.toString();
            }
        });

        final TreeExpander treeExpander = new DefaultTreeExpander(getTree());
        CommonActionsManager.getInstance().createCollapseAllAction(treeExpander, this);
        CommonActionsManager.getInstance().createExpandAllAction(treeExpander, this);

        getTree().setShowsRootHandles(true);
        getTree().setLineStyleAngled();
        getTree().setRootVisible(true);
        getTree().setCellRenderer(new DefaultTreeCellRenderer());
        getTableHeader().setReorderingAllowed(false);


        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setPreferredScrollableViewportSize(new Dimension(300, getRowHeight() * 10));

        getColumnModel().getColumn(0).setPreferredWidth(280);
        getColumnModel().getColumn(1).setPreferredWidth(60);
    }

    protected boolean isNullObject(final T value) {
        return false;
    }

    private String getModuleNodeText() {
        return "Module " + myModule.getName();
    }

    public Module getModule() {
        return myModule;
    }

    public TableColumn getValueColumn() {
        return getColumnModel().getColumn(1);
    }

    protected boolean isValueEditableForFile(final VirtualFile virtualFile) {
        return true;
    }

    public static void press(final Container comboComponent) {
        if (comboComponent instanceof JButton) {
            final AbstractButton button = (JButton) comboComponent;
            button.doClick();
        } else {
            for (int i = 0; i < comboComponent.getComponentCount(); i++) {
                Component child = comboComponent.getComponent(i);
                if (child instanceof Container) {
                    press((Container) child);
                }
            }
        }
    }

    public boolean clearSubdirectoriesOnDemandOrCancel(final VirtualFile parent, final String message, final String title) {
        Map<VirtualFile, T> mappings = myModel.myCurrentMapping;
        Map<VirtualFile, T> subdirectoryMappings = new HashMap<VirtualFile, T>();
        for (VirtualFile file : mappings.keySet()) {
            if (file != null && (parent == null || VirtualFileUtil.isAncestor(parent, file, true))) {
                subdirectoryMappings.put(file, mappings.get(file));
            }
        }
        if (subdirectoryMappings.isEmpty()) {
            return true;
        } else {
            int ret = Messages.showDialog(myModule.getProject(), message, title, new String[]{"Override", "Do Not Override", "Cancel"}, 0, Messages.getWarningIcon());
            if (ret == 0) {
                for (VirtualFile file : subdirectoryMappings.keySet()) {
                    myModel.setValueAt(null, new DefaultMutableTreeNode(file), 1);
                }
            }
            return ret != 2;
        }
    }

    public Map<VirtualFile, T> getValues() {
        return myModel.getValues();
    }

    public TreeTableCellRenderer createTableRenderer(TreeTableModel treeTableModel) {
        TreeTableCellRenderer tableRenderer = super.createTableRenderer(treeTableModel);
        UIUtil.setLineStyleAngled(tableRenderer);
        tableRenderer.setRootVisible(false);
        tableRenderer.setShowsRootHandles(true);

        return tableRenderer;
    }

    public void reset(final Map<VirtualFile, T> mappings) {
        myModel.reset(mappings);

        final TreeNode root = (TreeNode) myModel.getRoot();
        myModel.nodeChanged(root);
        getTree().setModel(null);
        getTree().setModel(myModel);
    }

    public void select(final VirtualFile toSelect) {
        if (toSelect != null) {
            select(toSelect, (TreeNode) myModel.getRoot());
        }
    }

    private void select(@Nonnull VirtualFile toSelect, final TreeNode root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            TreeNode child = root.getChildAt(i);
            VirtualFile file = ((FileNode) child).getObject();
            if (VirtualFileUtil.isAncestor(file, toSelect, false)) {
                if (file == toSelect) {
                    TreeUtil.selectNode(getTree(), child);
                    getSelectionModel().clearSelection();
                    addSelectedPath(TreeUtil.getPathFromRoot(child));
                    TableUtil.scrollSelectionToVisible(this);
                } else {
                    select(toSelect, child);
                }
                return;
            }
        }
    }


    private static class MyModel<T> extends DefaultTreeModel implements TreeTableModel {
        private final Map<VirtualFile, T> myCurrentMapping;
        private final Class<T> myValueClass;
        private final String myValueTitle;
        private AbstractFileTreeTable<T> myTreeTable;

        private MyModel(final Module module, Map<VirtualFile, T> map, final Class<T> valueClass, final String valueTitle, VirtualFileFilter filter) {
            super(new ModuleRootNode(module, filter));
			myCurrentMapping = map;
            myValueClass = valueClass;
            myValueTitle = valueTitle;
        }

        private Map<VirtualFile, T> getValues() {
            return new HashMap<VirtualFile, T>(myCurrentMapping);
        }

        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(final int column) {
            switch (column) {
                case 0:
                    return "File/Directory";
                case 1:
                    return myValueTitle;
                default:
                    throw new RuntimeException("invalid column " + column);
            }
        }

        public Class getColumnClass(final int column) {
            switch (column) {
                case 0:
                    return TreeTableModel.class;
                case 1:
                    return myValueClass;
                default:
                    throw new RuntimeException("invalid column " + column);
            }
        }

        public Object getValueAt(final Object node, final int column) {
            Object userObject = ((DefaultMutableTreeNode) node).getUserObject();
            if (userObject instanceof Module) {
                switch (column) {
                    case 0:
                        return userObject;
                    case 1:
                        return myCurrentMapping.get(null);
                }
            }
            VirtualFile file = (VirtualFile) userObject;
            switch (column) {
                case 0:
                    return file;
                case 1:
                    return myCurrentMapping.get(file);
                default:
                    throw new RuntimeException("invalid column " + column);
            }
        }

        public boolean isCellEditable(final Object node, final int column) {
            switch (column) {
                case 0:
                    return false;
                case 1:
                    final Object userObject = ((DefaultMutableTreeNode) node).getUserObject();
                    return !(userObject instanceof VirtualFile || userObject == null) || myTreeTable.isValueEditableForFile((VirtualFile) userObject);
                default:
                    throw new RuntimeException("invalid column " + column);
            }
        }

        public void setValueAt(final Object aValue, final Object node, final int column) {
            final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
            final Object userObject = treeNode.getUserObject();
            if (userObject instanceof Module) {
                return;
            }
            final VirtualFile file = (VirtualFile) userObject;
            final T t = (T) aValue;
            if (t == null || myTreeTable.isNullObject(t)) {
                myCurrentMapping.remove(file);
            } else {
                myCurrentMapping.put(file, t);
            }
            fireTreeNodesChanged(this, new Object[]{getRoot()}, null, null);
        }

        public void setTree(JTree jTree) {
            //fixme for IDEA X
        }

        public void reset(final Map<VirtualFile, T> mappings) {
            myCurrentMapping.clear();
            myCurrentMapping.putAll(mappings);
            ((ModuleRootNode) getRoot()).clearCachedChildren();
        }

        void setTreeTable(final AbstractFileTreeTable<T> treeTable) {
            myTreeTable = treeTable;
        }
    }

    public static class ModuleRootNode extends ConvenientNode<Module> {
        private VirtualFileFilter myFilter;

        public ModuleRootNode(@Nonnull Module module) {
            this(module, VirtualFileFilter.ALL);
        }

        public ModuleRootNode(@Nonnull Module module, @Nonnull VirtualFileFilter filter) {
            super(module);
            myFilter = filter;
        }

        protected void appendChildrenTo(final Collection<ConvenientNode> children) {
            Module module = getObject();
            VirtualFile[] roots = ModuleRootManager.getInstance(module).getContentRoots();

            NextRoot:
            for (VirtualFile root : roots) {
                for (VirtualFile candidate : roots) {
                    if (VirtualFileUtil.isAncestor(candidate, root, true)) {
                        continue NextRoot;
                    }
                }
                if (myFilter.accept(root)) {
                    children.add(new FileNode(root, module, myFilter));
                }
            }
        }
    }

    public abstract static class ConvenientNode<T> extends DefaultMutableTreeNode {
        private final T myObject;

        private ConvenientNode(T object) {
            myObject = object;
        }

        public T getObject() {
            return myObject;
        }

        protected abstract void appendChildrenTo(final Collection<ConvenientNode> children);

        public int getChildCount() {
            init();
            return super.getChildCount();
        }

        public TreeNode getChildAt(final int childIndex) {
            init();
            return super.getChildAt(childIndex);
        }

        public Enumeration children() {
            init();
            return super.children();
        }

        private void init() {
            if (getUserObject() == null) {
                setUserObject(myObject);
                final List<ConvenientNode> children = new ArrayList<ConvenientNode>();
                appendChildrenTo(children);
                Collections.sort(children, new Comparator<ConvenientNode>() {
                    public int compare(final ConvenientNode node1, final ConvenientNode node2) {
                        Object o1 = node1.getObject();
                        Object o2 = node2.getObject();
                        if (o1 == o2) {
                            return 0;
                        }
                        if (o1 instanceof Module) {
                            return -1;
                        }
                        if (o2 instanceof Module) {
                            return 1;
                        }
                        VirtualFile file1 = (VirtualFile) o1;
                        VirtualFile file2 = (VirtualFile) o2;
                        if (file1.isDirectory() != file2.isDirectory()) {
                            return file1.isDirectory() ? -1 : 1;
                        }
                        return file1.getName().compareTo(file2.getName());
                    }
                });
                int i = 0;
                for (ConvenientNode child : children) {
                    insert(child, i++);
                }
            }
        }

        public void clearCachedChildren() {
            if (children != null) {
                for (Object child : children) {
                    ConvenientNode<T> node = (ConvenientNode<T>) child;
                    node.clearCachedChildren();
                }
            }
            removeAllChildren();
            setUserObject(null);
        }
    }

    public static class FileNode extends ConvenientNode<VirtualFile> {
        private final Module myModule;
        private VirtualFileFilter myFilter;

        public FileNode(@Nonnull VirtualFile file, final @Nonnull Module module) {
            this(file, module, VirtualFileFilter.ALL);
        }

        public FileNode(@Nonnull VirtualFile file, final @Nonnull Module module, @Nonnull VirtualFileFilter filter) {
            super(file);
            myModule = module;
            myFilter = filter;
        }

        protected void appendChildrenTo(final Collection<ConvenientNode> children) {
            VirtualFile[] childrenf = getObject().getChildren();
            ModuleFileIndex fileIndex = ModuleRootManager.getInstance(myModule).getFileIndex();
            for (VirtualFile child : childrenf) {
                if (myFilter.accept(child) && fileIndex.isInContent(child)) {
                    children.add(new FileNode(child, myModule, myFilter));
                }
            }
        }
    }

    private class DefaultTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {
        public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded,
                                                      final boolean leaf, final int row, final boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof ModuleRootNode) {
                setText(getModuleNodeText());
                return this;
            }

            FileNode fileNode = (FileNode) value;
            VirtualFile file = fileNode.getObject();
            if (fileNode.getParent() instanceof FileNode) {
                setText(file.getName());
            } else {
                setText(file.getPresentableUrl());
            }

            setIcon(TargetAWT.to(VirtualFileManager.getInstance().getFileIcon(file, null, 0)));
            return this;
        }
    }
}
