/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: ModuleFileTreeTable.java, Class: ModuleFileTreeTable
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

import consulo.dataContext.DataContext;
import consulo.module.Module;
import consulo.project.Project;
import consulo.ui.ex.action.ActionManager;
import consulo.ui.ex.action.ActionPlaces;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.Presentation;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

/**
 * User: jansorg
 * Date: Feb 12, 2010
 * Time: 10:35:52 PM
 */
class ModuleFileTreeTable extends AbstractFileTreeTable<FileMode> {
    public Map<VirtualFile, FileMode> getMapping() {
        return getValues();
    }

    public ModuleFileTreeTable(Module module, final Map<VirtualFile, FileMode> mapping) {
        super(module, mapping, FileMode.class, "Ignore / Accept / Guess", new ModuleFileFilter());

        getValueColumn().setCellRenderer(new DefaultTableCellRenderer());
        getValueColumn().setCellEditor(new ModuleDefaultCellEditor());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    @Override
    protected boolean isNullObject(final FileMode value) {
        return FileMode.defaultMode().equals(value);
    }

    @Override
    protected boolean isValueEditableForFile(final VirtualFile virtualFile) {
        return true;
    }

    private class ModuleDefaultCellEditor extends DefaultCellEditor {
        private VirtualFile myVirtualFile;

        public ModuleDefaultCellEditor() {
            super(new JComboBox());
        }

        {
            delegate = new EditorDelegate() {
                public void setValue(Object value) {
                    getTableModel().setValueAt(value, new DefaultMutableTreeNode(myVirtualFile), -1);
                }

                public Object getCellEditorValue() {
                    return getTableModel().getValueAt(new DefaultMutableTreeNode(myVirtualFile), 1);
                }
            };
        }

        public Component getTableCellEditorComponent(JTable table, final Object value, boolean isSelected, int row, int column) {
            final Object o = table.getModel().getValueAt(row, 0);
            myVirtualFile = o instanceof Module ? null : (VirtualFile) o;

            final ChooseFileModeAction changeAction = new ChooseFileModeAction(myVirtualFile) {
                protected void chosen(VirtualFile virtualFile, FileMode mode) {
                    getValueColumn().getCellEditor().stopCellEditing();
                    boolean clearSettings = clearSubdirectoriesOnDemandOrCancel(virtualFile, "There are settings specified for the subdirectories. Override them?", "Override Subdirectory Settings");
                    if (clearSettings) {
                        getTableModel().setValueAt(mode, new DefaultMutableTreeNode(virtualFile), 1);
                        getValues().put(virtualFile, mode);
                    }
                }
            };

            Presentation templatePresentation = changeAction.getTemplatePresentation();
            final JComponent comboComponent = changeAction.createCustomComponent(templatePresentation);

            DataContext dataContext = DataContext.builder()
                    .add(VirtualFile.KEY, myVirtualFile)
                    .add(Project.KEY, getModule().getProject())
                    .build();

            AnActionEvent event = new AnActionEvent(null,
                    dataContext,
                    ActionPlaces.UNKNOWN,
                    templatePresentation, ActionManager.getInstance(), 0);
            changeAction.update(event);

            editorComponent = comboComponent;

            comboComponent.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(final ComponentEvent e) {
                    press((Container) e.getComponent());
                }
            });

            FileMode mode = (FileMode) getTableModel().getValueAt(new DefaultMutableTreeNode(myVirtualFile), 1);
            templatePresentation.setText(mode == null ? "" : mode.getDisplayName());
            comboComponent.revalidate();

            return editorComponent;
        }
    }

    private class DefaultTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(final JTable table,
                                                       final Object value,
                                                       final boolean isSelected,
                                                       final boolean hasFocus,
                                                       final int row,
                                                       final int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            final FileMode t = (FileMode) value;
            final Object userObject = table.getModel().getValueAt(row, 0);
            final VirtualFile file = userObject instanceof VirtualFile ? (VirtualFile) userObject : null;
            final Pair<String, Boolean> pair = ChangeFileTypeUpdateGroup.update(file);
            final boolean enabled = file == null || pair.getSecond();

            if (t != null) {
                setText(t.getDisplayName());
            } else if (file != null) {
                FileMode fileMode = getValues().get(file);
                if (fileMode != null) {
                    setText(fileMode.getDisplayName());
                }
            }

            setEnabled(enabled);
            return this;
        }
    }
}
