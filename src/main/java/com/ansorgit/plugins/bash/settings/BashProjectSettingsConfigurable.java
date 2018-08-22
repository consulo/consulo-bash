/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashProjectSettingsConfigurable.java, Class: BashProjectSettingsConfigurable
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

package com.ansorgit.plugins.bash.settings;

import javax.swing.JComponent;

import org.jetbrains.annotations.Nls;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import consulo.annotations.RequiredDispatchThread;

/**
 * Date: 12.05.2009
 * Time: 18:51:48
 *
 * @author Joachim Ansorg
 */
public class BashProjectSettingsConfigurable implements Configurable
{
	private BashProjectSettingsPane settingsPanel;
	private final Project project;

	public BashProjectSettingsConfigurable(Project project)
	{
		this.project = project;
	}

	@Nls
	public String getDisplayName()
	{
		return "Bash";
	}

	@RequiredDispatchThread
	public JComponent createComponent()
	{
		if(settingsPanel == null)
		{
			settingsPanel = new BashProjectSettingsPane();
		}

		return settingsPanel.getPanel();
	}

	@RequiredDispatchThread
	public boolean isModified()
	{
		if(settingsPanel == null)
		{
			return false;
		}

		return settingsPanel.isModified(BashProjectSettings.storedSettings(project));
	}

	@RequiredDispatchThread
	public void apply() throws ConfigurationException
	{
		settingsPanel.storeSettings(BashProjectSettings.storedSettings(project));
	}

	@RequiredDispatchThread
	public void reset()
	{
		settingsPanel.setData(BashProjectSettings.storedSettings(project));
	}

	@RequiredDispatchThread
	public void disposeUIResources()
	{
		if(settingsPanel != null)
		{
			this.settingsPanel.dispose();
			this.settingsPanel = null;
		}
	}
}