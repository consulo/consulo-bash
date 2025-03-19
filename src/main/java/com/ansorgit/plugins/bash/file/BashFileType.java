/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFileType.java, Class: BashFileType
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

package com.ansorgit.plugins.bash.file;

import com.ansorgit.plugins.bash.lang.BashLanguage;
import com.ansorgit.plugins.bash.settings.facet.ui.FileMode;
import com.ansorgit.plugins.bash.util.BashIcons;
import com.ansorgit.plugins.bash.util.content.BashContentUtil;
import consulo.bash.module.extension.BashModuleExtension;
import consulo.language.Language;
import consulo.language.file.LanguageFileType;
import consulo.language.util.ModuleUtilCore;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.project.DumbService;
import consulo.project.Project;
import consulo.project.ProjectLocator;
import consulo.ui.image.Image;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileTypeIdentifiableByVirtualFile;
import consulo.virtualFileSystem.util.VirtualFileUtil;

import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * The file type implementation for Bash files.
 * <p/>
 * Date: 22.03.2009
 * Time: 11:08:04
 *
 * @author Joachim Ansorg
 */
public class BashFileType extends LanguageFileType implements FileTypeIdentifiableByVirtualFile {
    public static final BashFileType INSTANCE = new BashFileType();
    @Deprecated
    public static final Language BASH_LANGUAGE = INSTANCE.getLanguage();

    /**
     * The default file extension of bash scripts.
     */
    public static final String DEFAULT_EXTENSION = "sh";
    public static final String BASH_EXTENSION = "bash";

    /**
     * All extensions which are associated with this plugin.
     */
    public static final String[] extensions = {DEFAULT_EXTENSION, BASH_EXTENSION};
    public static final List<String> extensionList = Arrays.asList(extensions);

    //needed for the automatic file content type guessing
    private static final double MIN_FILE_PROBABILIY = 0.75d;

    protected BashFileType() {
        super(BashLanguage.INSTANCE);
    }

    @Override
    @Nonnull
    public String getId() {
        return "Bash";
    }

    @Override
    @Nonnull
    public LocalizeValue getDescription() {
        return LocalizeValue.localizeTODO("Bourne Again Shell");
    }

    @Override
    @Nonnull
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Nonnull
    @Override
    public Image getIcon() {
        return BashIcons.BASH_FILE_ICON;
    }

    /**
     * Here we check if a given file belongs to our plugin.
     * We take this road because we need the actual file and not a filename to check files without extension.
     * <p/>
     * A file is checked according to the rules defined in the facet settings.
     * A file can be set to ignored, accepted or auto. Auto means that the content is checked.
     *
     * @param file The file to check
     * @return True if BashSupport wants to take that file
     */
    @Override
    public boolean isMyFileType(VirtualFile file) {
        if (file == null) {
            return false;
        }

        if (file.isDirectory()) {
            return false;
        }

        if (extensionList.contains(file.getExtension())) {
            return true;
        }
        else if (!file.isInLocalFileSystem()) {
            return false;
        }
        else if (StringUtil.isEmpty(file.getExtension())) {
            //no extensions, special checks (looking at the content, etc)

            //guess project
            Project project = ProjectLocator.getInstance().guessProjectForFile(file);
            if (project == null) {
                return false;
            }

            DumbService dumbService = DumbService.getInstance(project);
            if (dumbService == null || dumbService.isDumb()) {
                return false;
            }

            Module module = ModuleUtilCore.findModuleForFile(file, project);
            if (module == null) {
                return false;
            }

            BashModuleExtension facet = ModuleUtilCore.getExtension(module, BashModuleExtension.class);
            if (facet == null) {
                return false;
            }

            FileMode mode = facet.findMode(file);

            if (mode == FileMode.accept()) {
                return true;
            }
            else if (mode == FileMode.ignore()) {
                return false;
            }
            else if (mode == FileMode.auto()) {
                return BashContentUtil.isProbablyBashFile(VirtualFileUtil.virtualToIoFile(file), MIN_FILE_PROBABILIY, project);
            }
        }

        return false;
    }
}
