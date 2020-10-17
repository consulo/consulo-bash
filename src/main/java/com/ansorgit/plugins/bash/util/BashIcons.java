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

import consulo.bash.icon.BashIconGroup;
import consulo.ui.image.Image;

/**
 * Contains the paths for the various Bash icons.
 *
 * @author Joachim Ansorg, mail@ansorg-it.com.
 */
public interface BashIcons
{
	Image BASH_FILE_ICON = BashIconGroup.bash_icon();

	Image FUNCTION_DEF_ICON = BashIconGroup.function_16();

	Image GLOBAL_VAR_ICON = BashIconGroup.global_var_16();

	Image BASH_VAR_ICON = BashIconGroup.bash_var_16();

	Image BOURNE_VAR_ICON = BashIconGroup.bash_var_16();
}
