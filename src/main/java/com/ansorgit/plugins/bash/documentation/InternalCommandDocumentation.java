/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: InternalCommandDocumentation.java, Class: InternalCommandDocumentation
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

package com.ansorgit.plugins.bash.documentation;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import consulo.language.psi.PsiElement;

/**
 * Provides documentation for internal commands.
 * <p/>
 * Date: 03.05.2009
 * Time: 18:25:29
 *
 * @author Joachim Ansorg
 */
class InternalCommandDocumentation extends ClasspathDocSource {
    InternalCommandDocumentation() {
        super("/documentation/internal");
    }

    boolean isValid(PsiElement element, PsiElement originalElement) {
        return element instanceof BashCommand && ((BashCommand) element).isInternalCommand();
    }

    public String documentationUrl(PsiElement element, PsiElement originalElement) {
        return null;
    }
}
