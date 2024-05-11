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

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import consulo.bash.module.extension.BashMutableModuleExtension;
import com.ansorgit.plugins.bash.settings.facet.OperationMode;
import consulo.ui.ex.awt.tree.TreeUtil;

/**
 * GUI settings which are displayed for a Bash module facet.
 * <p/>
 * User: jansorg
 * Date: Feb 11, 2010
 * Time: 10:21:10 PM
 */
public class BashFacetUI extends JPanel
{
	private JRadioButton ignoreFilesWithoutExtensionRadioButton;
	private JRadioButton acceptAllFilesWithoutRadioButton;
	private JRadioButton customSettingsRadioButton;
	private JPanel basePanel;
	private JScrollPane treeScollArea;

	private ModuleFileTreeTable fileTreeTable;

	public BashFacetUI(final BashMutableModuleExtension bashMutableModuleExtension)
	{
		customSettingsRadioButton.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				JRadioButton button = (JRadioButton) e.getSource();
				treeScollArea.setEnabled(button.isSelected());

				basePanel.setEnabled(customSettingsRadioButton.isSelected());
				fileTreeTable.setEnabled(customSettingsRadioButton.isSelected());
			}
		});

		ChangeListener changeListener = new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				bashMutableModuleExtension.setOperationMode(findMode());
			}
		};

		ignoreFilesWithoutExtensionRadioButton.addChangeListener(changeListener);
		acceptAllFilesWithoutRadioButton.addChangeListener(changeListener);
		customSettingsRadioButton.addChangeListener(changeListener);

		fileTreeTable = new ModuleFileTreeTable(bashMutableModuleExtension.getModule(), bashMutableModuleExtension.getMapping());
		treeScollArea.setViewportView(fileTreeTable);
		TreeUtil.expandAll(fileTreeTable.getTree());
	}

	private void createUIComponents()
	{
		basePanel = this;
	}

	private OperationMode findMode()
	{
		if(ignoreFilesWithoutExtensionRadioButton.isSelected())
		{
			return OperationMode.IgnoreAll;
		}

		if(acceptAllFilesWithoutRadioButton.isSelected())
		{
			return OperationMode.AcceptAll;
		}

		return OperationMode.Custom;
	}
}
