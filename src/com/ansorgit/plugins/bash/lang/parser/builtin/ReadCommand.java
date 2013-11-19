/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: ReadCommand.java, Class: ReadCommand
 * Last modified: 2010-05-19 21:39
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

/**
 * Implements the read command.
 * <p/>
 * User: jansorg
 * Date: 23.05.2009
 * Time: 23:45:48
 */
class ReadCommand extends AbstractReadCommand {
    public ReadCommand() {
        super("read");
    }
}
