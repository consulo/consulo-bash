/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashCommonOptionsForm.java, Class: BashCommonOptionsForm
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

package com.ansorgit.plugins.bash.runner;

import consulo.bash.localize.BashLocalize;
import consulo.disposer.Disposable;
import consulo.execution.localize.ExecutionLocalize;
import consulo.execution.ui.awt.EnvironmentVariablesTextFieldWithBrowseButton;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.localize.LocalizeValue;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.process.cmd.ParametersListUtil;
import consulo.project.Project;
import consulo.ui.Component;
import consulo.ui.TextBoxWithExpandAction;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.util.FormBuilder;
import consulo.util.lang.StringUtil;

import java.util.Map;

/**
 * User: jansorg
 * Date: 10.07.2009
 * Time: 21:43:12
 */
public class BashCommonOptionsForm implements CommonBashRunConfigurationParams {
    private final FileChooserTextBoxBuilder.Controller myInterpreterPath;
    private final TextBoxWithExpandAction myInterpreterOptions;
    private final FileChooserTextBoxBuilder.Controller myWorkingDirectory;
    private final EnvironmentVariablesTextFieldWithBrowseButton myEnvironmentVariables;
    private final Component myComponent;

    @RequiredUIAccess
    public BashCommonOptionsForm(Project project, Disposable uiDisposable) {
        myInterpreterPath = FileChooserTextBoxBuilder.create(project)
            .dialogTitle(BashLocalize.runConfigurationSelectInterpreterTitle())
            .fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor())
            .uiDisposable(uiDisposable)
            .build();

        myInterpreterOptions = TextBoxWithExpandAction.create(
            PlatformIconGroup.actionsShow(),
            BashLocalize.runConfigurationInterpreterOptionsDialogTitle().get(),
            ParametersListUtil.DEFAULT_LINE_PARSER,
            ParametersListUtil.DEFAULT_LINE_JOINER
        );

        myWorkingDirectory = FileChooserTextBoxBuilder.create(project)
            .dialogTitle(BashLocalize.runConfigurationSelectWorkingDirectoryTitle())
            .fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFolderDescriptor())
            .uiDisposable(uiDisposable)
            .build();

        myEnvironmentVariables = new EnvironmentVariablesTextFieldWithBrowseButton();

        myComponent = FormBuilder.create()
            .addLabeled(BashLocalize.runConfigurationInterpreterPathLabel(), myInterpreterPath.getComponent())
            .addLabeled(BashLocalize.runConfigurationInterpreterOptionsLabel(), myInterpreterOptions)
            .addLabeled(BashLocalize.runConfigurationWorkingDirectoryLabel(), myWorkingDirectory.getComponent())
            .addLabeled(
                LocalizeValue.join(ExecutionLocalize.environmentVariablesComponentTitle(), LocalizeValue.colon()),
                myEnvironmentVariables.getComponent()
            )
            .build();
    }

    public Component getComponent() {
        return myComponent;
    }

    @Override
    @RequiredUIAccess
    public String getInterpreterOptions() {
        return StringUtil.notNullize(myInterpreterOptions.getValue());
    }

    @Override
    @RequiredUIAccess
    public void setInterpreterOptions(String options) {
        myInterpreterOptions.setValue(StringUtil.notNullize(options));
    }

    @Override
    @RequiredUIAccess
    public String getWorkingDirectory() {
        return myWorkingDirectory.getValue();
    }

    @Override
    @RequiredUIAccess
    public void setWorkingDirectory(String workingDirectory) {
        myWorkingDirectory.setValue(StringUtil.notNullize(workingDirectory));
    }

    @Override
    public boolean isPassParentEnvs() {
        return myEnvironmentVariables.isPassParentEnvs();
    }

    @Override
    @RequiredUIAccess
    public void setPassParentEnvs(boolean passParentEnvs) {
        myEnvironmentVariables.setPassParentEnvs(passParentEnvs);
    }

    @Override
    public Map<String, String> getEnvs() {
        return myEnvironmentVariables.getEnvs();
    }

    @Override
    @RequiredUIAccess
    public void setEnvs(Map<String, String> envs) {
        myEnvironmentVariables.setEnvs(envs);
    }

    @Override
    @RequiredUIAccess
    public String getInterpreterPath() {
        return myInterpreterPath.getValue();
    }

    @Override
    @RequiredUIAccess
    public void setInterpreterPath(String path) {
        myInterpreterPath.setValue(StringUtil.notNullize(path));
    }
}
