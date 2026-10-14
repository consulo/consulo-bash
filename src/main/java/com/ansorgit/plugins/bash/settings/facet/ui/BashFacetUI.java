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

import com.ansorgit.plugins.bash.settings.facet.OperationMode;
import consulo.bash.localize.BashLocalize;
import consulo.bash.module.extension.BashMutableModuleExtension;
import consulo.disposer.Disposable;
import consulo.disposer.Disposer;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.ui.ComboBox;
import consulo.ui.Component;
import consulo.ui.Label;
import consulo.ui.MessageBoxBuilder;
import consulo.ui.MessageButtonRole;
import consulo.ui.RadioGroup;
import consulo.ui.TableItemEditor;
import consulo.ui.TreeTable;
import consulo.ui.UIAccess;
import consulo.ui.ValueComponent;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.DockLayout;
import consulo.ui.layout.ScrollableLayout;
import consulo.ui.layout.VerticalLayout;
import consulo.util.lang.ThreeState;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GUI settings which are displayed for a Bash module facet.
 *
 * @author jansorg
 * @since 2010-02-11
 */
public final class BashFacetUI {
    private BashFacetUI() {
    }

    @RequiredUIAccess
    public static Component create(BashMutableModuleExtension extension, Disposable uiDisposable) {
        Map<VirtualFile, FileMode> mapping = extension.getMapping();

        TreeTable<Object> fileTable = TreeTable.create(new BashModuleFileTreeModel(extension.getModule()));
        Disposer.register(uiDisposable, fileTable.destroyHook());
        fileTable.setTreeColumnHeader(BashLocalize.moduleExtensionFileColumn());
        fileTable.addColumn(BashLocalize.moduleExtensionModeColumn(), value -> getMode(mapping, value))
            .setRenderer((presentation, item) -> {
                FileMode mode = item.getValue();
                if (mode != null) {
                    presentation.append(mode.getDisplayName());
                }
            })
            .setEditor(new TableItemEditor<>() {
                @Override
                @RequiredUIAccess
                public ValueComponent<FileMode> createComponent(Object item) {
                    ComboBox<FileMode> modeBox =
                        ComboBox.create(FileMode.defaultMode(), FileMode.auto(), FileMode.accept(), FileMode.ignore());
                    modeBox.setTextRenderer(mode -> mode == null ? LocalizeValue.empty() : LocalizeValue.of(mode.getDisplayName()));
                    FileMode mode = getMode(mapping, item);
                    modeBox.setValue(mode == null ? FileMode.defaultMode() : mode);
                    return modeBox;
                }

                @Override
                @RequiredUIAccess
                public void commit(Object item, @Nullable FileMode value) {
                    changeMode(fileTable, mapping, item instanceof VirtualFile file ? file : null, value);
                }

                @Override
                public boolean isEditable(Object item) {
                    return item instanceof Module || item instanceof VirtualFile;
                }
            });

        RadioGroup<OperationMode> modeGroup = RadioGroup.create();
        VerticalLayout modeLayout = VerticalLayout.create();
        modeLayout.add(modeGroup.newButton(BashLocalize.moduleExtensionModeIgnoreAll(), OperationMode.IgnoreAll));
        modeLayout.add(modeGroup.newButton(BashLocalize.moduleExtensionModeAcceptAll(), OperationMode.AcceptAll));
        modeLayout.add(modeGroup.newButton(BashLocalize.moduleExtensionModeCustom(), OperationMode.Custom));
        modeGroup.setValue(extension.getOperationMode(), false);
        modeGroup.addValueListener(mode -> {
            if (mode == null) {
                return;
            }

            extension.setOperationMode(mode);
            fileTable.setEnabled(mode == OperationMode.Custom);
        });

        fileTable.setEnabled(extension.getOperationMode() == OperationMode.Custom);

        VerticalLayout topLayout = VerticalLayout.create();
        topLayout.add(Label.create(BashLocalize.moduleExtensionDescription()));
        topLayout.add(modeLayout);

        DockLayout layout = DockLayout.create();
        layout.top(topLayout);
        layout.center(ScrollableLayout.create(fileTable));
        return layout;
    }

    private static @Nullable FileMode getMode(Map<VirtualFile, FileMode> mapping, @Nullable Object value) {
        if (value instanceof Module) {
            return mapping.get(null);
        }
        if (value instanceof VirtualFile file) {
            return mapping.get(file);
        }
        return null;
    }

    @RequiredUIAccess
    private static void changeMode(
        TreeTable<Object> fileTable,
        Map<VirtualFile, FileMode> mapping,
        @Nullable VirtualFile file,
        @Nullable FileMode mode
    ) {
        List<VirtualFile> subdirectoryFiles = new ArrayList<>();
        for (VirtualFile mappedFile : mapping.keySet()) {
            if (mappedFile != null && (file == null || VirtualFileUtil.isAncestor(file, mappedFile, true))) {
                subdirectoryFiles.add(mappedFile);
            }
        }

        if (subdirectoryFiles.isEmpty()) {
            setMode(mapping, file, mode);
            fileTable.refreshAll();
            return;
        }

        UIAccess uiAccess = UIAccess.current();
        MessageBoxBuilder.<ThreeState>create()
            .asWarning()
            .title(BashLocalize.moduleExtensionOverrideTitle())
            .text(BashLocalize.moduleExtensionOverrideMessage())
            .button(MessageButtonRole.YES, BashLocalize.moduleExtensionOverrideButton(), ThreeState.YES)
            .asDefaultButton()
            .button(MessageButtonRole.NO, BashLocalize.moduleExtensionDoNotOverrideButton(), ThreeState.NO)
            .button(MessageButtonRole.CANCEL, ThreeState.UNSURE)
            .asExitButton()
            .showAsync(fileTable)
            .whenCompleteAsync((answer, error) -> {
                if (error != null || answer == null || answer == ThreeState.UNSURE) {
                    return;
                }

                if (answer == ThreeState.YES) {
                    for (VirtualFile subdirectoryFile : subdirectoryFiles) {
                        mapping.remove(subdirectoryFile);
                    }
                }
                setMode(mapping, file, mode);
                fileTable.refreshAll();
            }, uiAccess);
    }

    private static void setMode(Map<VirtualFile, FileMode> mapping, @Nullable VirtualFile file, @Nullable FileMode mode) {
        if (mode == null || FileMode.defaultMode().equals(mode)) {
            mapping.remove(file);
        }
        else {
            mapping.put(file, mode);
        }
    }
}
