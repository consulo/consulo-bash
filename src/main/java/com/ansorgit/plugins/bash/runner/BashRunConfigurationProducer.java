/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashRunConfigurationProducer.java, Class: BashRunConfigurationProducer
 * Last modified: 2011-05-03 20:01
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

package com.ansorgit.plugins.bash.runner;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.util.BashInterpreterDetection;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.RunnerAndConfigurationSettings;
import consulo.execution.action.ConfigurationContext;
import consulo.execution.action.Location;
import consulo.execution.action.RuntimeConfigurationProducer;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;

/**
 * This class is based on code of the intellij-batch plugin.
 *
 * @author wibotwi, jansorg
 */
@ExtensionImpl
public class BashRunConfigurationProducer extends RuntimeConfigurationProducer implements Cloneable {
    private PsiFile sourceFile;

    public BashRunConfigurationProducer() {
        super(BashConfigurationType.getInstance());
    }

    @Override
    public PsiElement getSourceElement() {
        return sourceFile;
    }

    @Override
    protected RunnerAndConfigurationSettings createConfigurationByElement(Location location, ConfigurationContext configurationContext) {
        sourceFile = location.getPsiElement().getContainingFile();

        if (sourceFile != null && sourceFile.getFileType().equals(BashFileType.INSTANCE)) {
            Project project = sourceFile.getProject();
            RunnerAndConfigurationSettings settings = cloneTemplateConfiguration(project, configurationContext);

            VirtualFile file = sourceFile.getVirtualFile();

            BashRunConfiguration runConfiguration = (BashRunConfiguration) settings.getConfiguration();
            runConfiguration.setName(file.getPresentableName());

            runConfiguration.setScriptName(file.getPath());
            if (file.getParent() != null) {
                runConfiguration.setWorkingDirectory(file.getParent().getPath());
            }

            if (StringUtil.isEmptyOrSpaces(runConfiguration.getInterpreterPath())) {
                runConfiguration.setInterpreterPath(new BashInterpreterDetection().findBestLocation());
            }

            Module module = ModuleUtilCore.findModuleForPsiElement(location.getPsiElement());
            if (module != null) {
                runConfiguration.setModule(module);
            }

            return settings;
        }

        return null;
    }

    public int compareTo(Object o) {
        return 0;
    }
}