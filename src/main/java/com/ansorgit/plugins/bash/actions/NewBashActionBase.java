/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: NewBashActionBase.java, Class: NewBashActionBase
 * Last modified: 2013-01-31
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

package com.ansorgit.plugins.bash.actions;

import com.ansorgit.plugins.bash.file.BashFileType;
import consulo.ide.action.CreateElementActionBase;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.platform.base.localize.CommonLocalize;
import consulo.project.Project;
import consulo.ui.ex.awt.Messages;
import consulo.ui.image.Image;
import consulo.util.io.FileUtil;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.function.Consumer;

import static com.ansorgit.plugins.bash.file.BashFileType.DEFAULT_EXTENSION;

/**
 * Date: 17.04.2009
 * Time: 20:20:20
 *
 * @author Joachim Ansorg
 */
abstract class NewBashActionBase extends CreateElementActionBase {
    private static final Logger log = Logger.getInstance(NewBashActionBase.class);

    public NewBashActionBase(LocalizeValue text, LocalizeValue description, Image icon) {
        super(text, description, icon);
    }

    @Override
    protected final void invokeDialog(final Project project, final PsiDirectory directory, Consumer<PsiElement[]> consumer) {
        log.debug("invokeDialog");
        final MyInputValidator validator = new MyInputValidator(project, directory);
        Messages.showInputDialog(project, getDialogPrompt().get(), getDialogTitle().get(), Messages.getQuestionIcon(), "", validator);

        final PsiElement[] elements = validator.getCreatedElements();
        log.debug("Result: " + elements);
        consumer.accept(elements);
    }

    protected static PsiFile createFileFromTemplate(final PsiDirectory directory,
                                                    String className,
                                                    @NonNls String templateName,
                                                    @NonNls String... parameters) throws IncorrectOperationException {
        log.debug("createFileFromTemplate");

        String usedExtension = FileUtil.getExtension(className);
        boolean withExtension = BashFileType.extensionList.contains(usedExtension.toLowerCase());

        String filename = withExtension ? className : className + "." + DEFAULT_EXTENSION;
        return BashTemplatesFactory.createFromTemplate(directory, className, filename);
    }

    protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
        log.debug("create " + newName + ", dir: " + directory);
        return doCreate(newName, directory);
    }

    protected abstract PsiElement[] doCreate(String newName, PsiDirectory directory);

    protected abstract LocalizeValue getDialogPrompt();

    protected abstract LocalizeValue getDialogTitle();

    @Override
    protected LocalizeValue getErrorTitle() {
        return CommonLocalize.titleError();
    }

    public static void checkCreateFile(@Nonnull PsiDirectory directory, String name) throws IncorrectOperationException {
        final String fileName = name + "." + DEFAULT_EXTENSION;
        directory.checkCreateFile(fileName);
    }
}
