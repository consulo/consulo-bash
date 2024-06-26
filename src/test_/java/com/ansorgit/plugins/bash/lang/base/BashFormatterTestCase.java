/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFormatterTestCase.java, Class: BashFormatterTestCase
 * Last modified: 2010-04-24
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

package com.ansorgit.plugins.bash.lang.base;

import com.ansorgit.plugins.bash.file.BashFileType;
import consulo.logging.Logger;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiFile;
import consulo.language.codeStyle.CodeStyleManager;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CodeStyleSettingsManager;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import consulo.util.lang.IncorrectOperationException;
import consulo.application.ApplicationManager;
import consulo.project.Project;
import consulo.undoRedo.CommandProcessor;
import org.junit.Assert;

import java.io.IOException;

//this test is currently broken
public abstract class BashFormatterTestCase extends CodeInsightFixtureTestCase {
    private static final Logger LOG = Logger.getInstance("#BashFormatterTestCase");
    protected CodeStyleSettings myTempSettings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setSettings(myFixture.getProject());
    }

    @Override
    protected void tearDown() throws Exception {
        setSettingsBack();
        super.tearDown();
    }

    protected void setSettings(Project project) {
        Assert.assertNull(myTempSettings);
        CodeStyleSettings settings = CodeStyleSettingsManager.getSettings(project);
        myTempSettings = settings.clone();

        CodeStyleSettings.IndentOptions gr = myTempSettings.getIndentOptions(BashFileType.INSTANCE);
        Assert.assertNotSame(gr, settings.OTHER_INDENT_OPTIONS);
        gr.INDENT_SIZE = 2;
        gr.CONTINUATION_INDENT_SIZE = 4;
        gr.TAB_SIZE = 2;
        myTempSettings.CLASS_COUNT_TO_USE_IMPORT_ON_DEMAND = 3;

        CodeStyleSettingsManager.getInstance(project).setTemporarySettings(myTempSettings);
    }

    protected void setSettingsBack() {
        final CodeStyleSettingsManager manager = CodeStyleSettingsManager.getInstance(myFixture.getProject());
        myTempSettings.getIndentOptions(BashFileType.INSTANCE).INDENT_SIZE = 200;
        myTempSettings.getIndentOptions(BashFileType.INSTANCE).CONTINUATION_INDENT_SIZE = 200;
        myTempSettings.getIndentOptions(BashFileType.INSTANCE).TAB_SIZE = 200;

        myTempSettings.CLASS_COUNT_TO_USE_IMPORT_ON_DEMAND = 5;
        manager.dropTemporarySettings();
        myTempSettings = null;
    }

    protected void checkFormatting(String fileText, String expected) throws Throwable {
        myFixture.configureByText(BashFileType.INSTANCE, fileText);
        checkFormatting(expected);
    }

    protected void checkFormatting(String expected) throws IOException {
        CommandProcessor.getInstance().executeCommand(myFixture.getProject(), new Runnable() {
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                        try {
                            final PsiFile file = myFixture.getFile();
                            TextRange myTextRange = file.getTextRange();
                            CodeStyleManager.getInstance(file.getProject()).reformatText(file, myTextRange.getStartOffset(), myTextRange.getEndOffset());
                        } catch (IncorrectOperationException e) {
                            LOG.error(e);
                        }
                    }
                });
            }
        }, null, null);
        myFixture.checkResult(expected);
    }
}
