/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: DeclareCommand.java, Class: DeclareCommand
 * Last modified: 2011-03-27 15:15
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

package com.ansorgit.plugins.bash.lang.parser.builtin;

import com.ansorgit.plugins.bash.lang.parser.ParsingTool;

/**
 * Syntax:typeset [-afFirtx] [-p] name[=value] ...
 * Typeset is an obsolete command
 * <p/>
 * Parses the typeset command. It makes the assignments available to the reference detection.
 * <p/>
 * Date: 15.04.2009
 * Time: 22:12:32
 *
 * @author Joachim Ansorg
 */
class TypesetCommand extends AbstractVariableDefParsing implements ParsingTool {
    TypesetCommand() {
        super(true, GENERIC_COMMAND_ELEMENT, "typeset", true);
    }
}
