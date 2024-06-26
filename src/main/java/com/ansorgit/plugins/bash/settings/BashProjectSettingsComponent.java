/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashProjectSettingsComponent.java, Class: BashProjectSettingsComponent
 * Last modified: 2010-02-12 23:10
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

package com.ansorgit.plugins.bash.settings;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import jakarta.inject.Singleton;

/**
 * User: jansorg
 * Date: Oct 30, 2009
 * Time: 9:12:54 PM
 */
@Singleton
@State(name = "BashSupportProjectSettings", storages = @Storage("bash.xml"))
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
public class BashProjectSettingsComponent implements PersistentStateComponent<BashProjectSettings> {
    private BashProjectSettings settings = new BashProjectSettings();

    public BashProjectSettings getState() {
        return settings;
    }

    public void loadState(BashProjectSettings state) {
        this.settings = state;
    }
}
