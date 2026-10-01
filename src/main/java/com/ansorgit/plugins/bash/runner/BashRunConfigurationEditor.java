/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashRunConfigurationEditor.java, Class: BashRunConfigurationEditor
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

import consulo.configurable.ConfigurationException;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.project.Project;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import jakarta.annotation.Nullable;

/**
 * Uses code from the intellij-batch plugin.
 *
 * @author wibotwi, jansorg
 */
public class BashRunConfigurationEditor extends SettingsEditor<BashRunConfiguration> {
    private final Project myProject;

    @Nullable
    private BashRunConfigurationForm myForm;

    public BashRunConfigurationEditor(BashRunConfiguration runConfiguration) {
        myProject = runConfiguration.getProject();
    }

    @Override
    @RequiredUIAccess
    protected void resetEditorFrom(BashRunConfiguration runConfiguration) {
        BashRunConfigurationForm form = myForm;
        if (form == null) {
            return;
        }

        BashRunConfiguration.copyParams(runConfiguration, form);
    }

    @Override
    @RequiredUIAccess
    protected void applyEditorTo(BashRunConfiguration runConfiguration) throws ConfigurationException {
        BashRunConfigurationForm form = myForm;
        if (form == null) {
            return;
        }

        BashRunConfiguration.copyParams(form, runConfiguration);
    }

    @Override
    @RequiredUIAccess
    protected Component createUIComponent() {
        BashRunConfigurationForm form = new BashRunConfigurationForm(myProject, this);
        myForm = form;
        return form.getComponent();
    }

    @Override
    protected void disposeEditor() {
        myForm = null;
    }
}
