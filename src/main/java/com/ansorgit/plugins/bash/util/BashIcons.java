/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashIcons.java, Class: BashIcons
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

package com.ansorgit.plugins.bash.util;

import consulo.execution.icon.ExecutionIconGroup;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.image.Image;

/**
 * Contains the paths for the various Bash icons.
 *
 * @author Joachim Ansorg, mail@ansorg-it.com.
 */
public interface BashIcons
{
	Image BASH_FILE_ICON = ExecutionIconGroup.console();

	Image GLOBAL_VAR_ICON = PlatformIconGroup.nodesValue();

	Image BOURNE_VAR_ICON = PlatformIconGroup.nodesVariable();
}
