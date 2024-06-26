/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: ChangeFileTypeUpdateGroup.java, Class: ChangeFileTypeUpdateGroup
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

import consulo.language.editor.PlatformDataKeys;
import consulo.navigation.Navigatable;
import consulo.navigation.OpenFileDescriptor;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DefaultActionGroup;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;

import java.text.MessageFormat;

/**
 * @author cdr
 */
public class ChangeFileTypeUpdateGroup extends DefaultActionGroup {
    public void update(final AnActionEvent e) {
        VirtualFile virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (files != null && files.length > 1) {
            virtualFile = null;
        }

        if (virtualFile != null) {
            Navigatable navigatable = e.getData(PlatformDataKeys.NAVIGATABLE);
            if (navigatable instanceof OpenFileDescriptor) {
                // prefer source to the class file
                virtualFile = ((OpenFileDescriptor) navigatable).getFile();
            }
        }

        Pair<String, Boolean> result = update(virtualFile);
        e.getPresentation().setText(result.getFirst());
        e.getPresentation().setEnabled(result.getSecond());
    }

    public static Pair<String, Boolean> update(final VirtualFile virtualFile) {
        boolean enabled = virtualFile != null && ChooseFileModeAction.isEnabled(virtualFile);

        String text;
        if (enabled) {
            String pattern;
            /*FileMode mode = ChooseFileModeAction.modeFromFile(virtualFile);
            if (mode != null) {
                pattern = "Mode: {0}";
                enabled = false;
            } else if (FileDocumentManager.getInstance().isFileModified(virtualFile)) {
                pattern = "Save ''{0}''-encoded file in";
            }
            //no sense to reload file with UTF-detected chars using other encoding
            else if (LoadTextUtil.utfCharsetWasDetectedFromBytes(virtualFile)) {
                pattern = "Mode: {0}";
                enabled = false;
            } else {
                pattern = "Change mode from ''{0}'' to";
            } */

            pattern = "Change mode from ''{0}'' to";
            FileMode mode = FileMode.defaultMode();

            text = MessageFormat.format(pattern, mode.getDisplayName());
        } else {
            text = "Mode";
        }

        return Pair.create(text, enabled);
    }
}
