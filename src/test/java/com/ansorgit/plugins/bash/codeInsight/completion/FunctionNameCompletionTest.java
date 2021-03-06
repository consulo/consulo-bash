/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: FunctionNameCompletionTest.java, Class: FunctionNameCompletionTest
 * Last modified: 2013-02-03
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

package com.ansorgit.plugins.bash.codeInsight.completion;

import com.ansorgit.plugins.bash.settings.BashProjectSettings;

/**
 * User: jansorg
 * Date: 09.02.11
 * Time: 22:07
 */
public abstract class FunctionNameCompletionTest extends AbstractCompletionTest {
    @Override
    protected String getTestDir() {
        return "functionNameCompletion";
    }

    public void testSimpleCompletion() throws Exception {
        configure();

        checkItems("myFunctionOneIsOk", "myFunctionTwoIsOk", "myFunctionTwoOneIsOk");
    }

    public void testDollarCompletion() throws Exception {
        configure();

        checkItems(NO_COMPLETIONS);
    }

    public void testAutocompleteBuiltInDisabled() throws Exception {
        configure();
        checkItems("echo123");
    }

    public void testAutocompleteBuiltInEnabledCountOne() throws Exception {
        BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinCommands(true);

        configure();
        checkItems("echo123");
    }

    public void testAutocompleteBuiltInEnabledCountOneNoLocals() throws Exception {
        BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinCommands(true);

        configure();
        checkItems("echo");
    }

    public void testAutocompleteBuiltInEnabledCountTwo() throws Exception {
        BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinCommands(true);

        //completes bash built-in command and the local function definition
        configure(2);
        checkItems("disown", "disown123");
    }

    public void testEmptyCompletion() throws Exception {
        configure();
        checkItems("myFunction");
    }

    public void testVarCompletion() throws Exception {
        configure();
        checkItems(NO_COMPLETIONS);
    }

    public void testParameterExpansion() throws Exception {
        configure();
        checkItems(NO_COMPLETIONS);
    }

    public void testVarDef() throws Exception {
        configure();
        checkItems(NO_COMPLETIONS);
    }

    public void testInnerFunctionCompletion() throws Exception {
        configure();
        checkItems(NO_COMPLETIONS);
    }

    public void testGlobalFunctionCompletion() throws Exception {
        configure();
        checkItems(NO_COMPLETIONS);
    }

    public void testNoNameCompletionInParam() throws Exception {
        configure();
        checkItems(NO_COMPLETIONS);
    }
}
