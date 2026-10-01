/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashRunConfigurationForm.java, Class: BashRunConfigurationForm
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
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.process.cmd.ParametersListUtil;
import consulo.project.Project;
import consulo.ui.Component;
import consulo.ui.TextBoxWithExpandAction;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.DockLayout;
import consulo.ui.layout.LabeledLayout;
import consulo.ui.layout.VerticalLayout;
import consulo.ui.util.FormBuilder;
import consulo.util.lang.StringUtil;

/**
 * The configuration user interface to configure a new Bash run configuration.
 * <p/>
 * User: jansorg
 * Date: 10.07.2009
 * Time: 21:30:48
 */
public class BashRunConfigurationForm implements BashRunConfigurationParams {
    private final FileChooserTextBoxBuilder.Controller myScriptName;
    private final TextBoxWithExpandAction myScriptParameters;
    private final BashCommonOptionsForm myCommonOptionsForm;
    private final Component myComponent;

    @RequiredUIAccess
    public BashRunConfigurationForm(Project project, Disposable uiDisposable) {
        myScriptName = FileChooserTextBoxBuilder.create(project)
            .dialogTitle(BashLocalize.runConfigurationSelectScriptTitle())
            .fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor())
            .uiDisposable(uiDisposable)
            .build();

        myScriptParameters = TextBoxWithExpandAction.create(
            PlatformIconGroup.actionsShow(),
            BashLocalize.runConfigurationScriptParametersDialogTitle().get(),
            ParametersListUtil.DEFAULT_LINE_PARSER,
            ParametersListUtil.DEFAULT_LINE_JOINER
        );

        myCommonOptionsForm = new BashCommonOptionsForm(project, uiDisposable);

        Component scriptForm = FormBuilder.create()
            .addLabeled(BashLocalize.runConfigurationScriptNameLabel(), myScriptName.getComponent())
            .addLabeled(BashLocalize.runConfigurationScriptParametersLabel(), myScriptParameters)
            .build();

        VerticalLayout layout = VerticalLayout.create();
        layout.add(scriptForm);
        layout.add(LabeledLayout.create(
            BashLocalize.runConfigurationCommonOptionsTitle(),
            DockLayout.create().center(myCommonOptionsForm.getComponent())
        ));
        myComponent = layout;
    }

    public Component getComponent() {
        return myComponent;
    }

    @Override
    public CommonBashRunConfigurationParams getCommonParams() {
        return myCommonOptionsForm;
    }

    @Override
    @RequiredUIAccess
    public String getScriptName() {
        return myScriptName.getValue();
    }

    @Override
    @RequiredUIAccess
    public void setScriptName(String scriptName) {
        myScriptName.setValue(StringUtil.notNullize(scriptName));
    }

    @Override
    @RequiredUIAccess
    public String getScriptParameters() {
        return StringUtil.notNullize(myScriptParameters.getValue());
    }

    @Override
    @RequiredUIAccess
    public void setScriptParameters(String scriptParameters) {
        myScriptParameters.setValue(StringUtil.notNullize(scriptParameters));
    }
}
