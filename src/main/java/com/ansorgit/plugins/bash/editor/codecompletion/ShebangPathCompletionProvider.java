/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: ShebangPathCompletionProvider.java, Class: ShebangPathCompletionProvider
 * Last modified: 2013-05-09
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ansorgit.plugins.bash.editor.codecompletion;

import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import consulo.language.editor.completion.CompletionContributor;
import consulo.language.editor.completion.CompletionParameters;
import consulo.language.editor.completion.CompletionType;
import consulo.language.psi.PsiElement;

import java.io.File;
import java.util.function.Predicate;

/**
 * This completion provider provides code completion for file / directory paths in the file.
 */
class ShebangPathCompletionProvider extends AbsolutePathCompletionProvider {
    public ShebangPathCompletionProvider() {
    }

    @Override
    void addTo(CompletionContributor contributor) {
        contributor.extend(CompletionType.BASIC, new BashPsiPattern().inside(BashShebang.class), this);
    }

    @Override
    protected Predicate<File> createFileFilter() {
        return new Predicate<File>() {
            public boolean test(File file) {
                return file.canExecute() && file.canRead();
            }
        };
    }

    @Override
    protected String findOriginalText(PsiElement element) {
        String original = element.getText();

        if (element instanceof BashShebang) {
            int offset = ((BashShebang) element).getShellCommandOffset();
            return original.substring(offset);
        }

        return original;
    }

    @Override
    protected String findCurrentText(CompletionParameters parameters, PsiElement element) {
        PsiElement command = element;
        while (command != null && !(command instanceof BashShebang)) {
            command = command.getParent();
        }

        if (command != null) {
            BashShebang shebang = (BashShebang) command;
            String shellcommand = shebang.shellCommand(false);

            int elementOffset = parameters.getOffset() - shebang.commandRange().getStartOffset();
            return (elementOffset > 0 && elementOffset <= shellcommand.length())
                    ? shellcommand.substring(0, elementOffset)
                    : null;
        }

        return super.findCurrentText(parameters, element);
    }
}