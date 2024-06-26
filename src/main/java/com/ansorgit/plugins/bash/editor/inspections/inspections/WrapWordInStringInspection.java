/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: WrapWordInStringInspection.java, Class: WrapWordInStringInspection
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

package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.WordToDoublequotedStringQuickfix;
import com.ansorgit.plugins.bash.editor.inspections.quickfix.WordToSinglequotedStringQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.ansorgit.plugins.bash.lang.psi.api.word.BashWord;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;

import jakarta.annotation.Nonnull;

/**
 * Inspection which can wrap a word token inside of a string.
 * It offers options to either convert to a double quoted string or into
 * a single quoted string.
 * <p/>
 * User: jansorg
 * Date: 21.05.2009
 * Time: 10:32:31
 */
@ExtensionImpl
public class WrapWordInStringInspection extends AbstractBashInspection {
    @Pattern("[a-zA-Z_0-9.]+")
    @Nonnull
    @Override
    public String getID() {
        return "ConvertToString";
    }

    @Nonnull
    public String getShortName() {
        return "Convert to string";
    }

    @Nls
    @Nonnull
    public String getDisplayName() {
        return "Convert to a quoted or unquoted string";
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public String getStaticDescription() {
        return "This inspection can convert text which is not in a string into a string. For example \"echo a\" can be converted into \"echo 'a'\".";
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder problemsHolder, boolean b) {
        return new BashVisitor() {
            @Override
            public void visitCombinedWord(BashWord word) {
                PsiElement parent = word.getParent();
                if (parent instanceof BashString) {
                    return;
                }

                boolean wrappable = word.isWrappable();
                if (wrappable) {
                    problemsHolder.registerProblem(word, getShortName(), new WordToDoublequotedStringQuickfix(word));
                    problemsHolder.registerProblem(word, getShortName(), new WordToSinglequotedStringQuickfix(word));
                }
            }
        };
    }
}
