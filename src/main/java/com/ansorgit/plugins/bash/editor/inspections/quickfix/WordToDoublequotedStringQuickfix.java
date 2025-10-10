/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: WordToDoublequotedStringQuickfix.java, Class: WordToDoublequotedStringQuickfix
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
package com.ansorgit.plugins.bash.editor.inspections.quickfix;

import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

/**
 * Wraps a word like a in a quoted string, i.e. "a".
 *
 * @author jansorg
 * @since 2009-05-21
 */
public class WordToDoublequotedStringQuickfix extends AbstractWordWrapQuickfix {
    public WordToDoublequotedStringQuickfix(BashWord word) {
        super(word);
    }

    protected String wrapText(String text) {
        return "\"" + text + "\"";
    }

    @Nonnull
    @Override
    public LocalizeValue getName() {
        return LocalizeValue.localizeTODO("Convert to quoted string \"...\"");
    }
}
