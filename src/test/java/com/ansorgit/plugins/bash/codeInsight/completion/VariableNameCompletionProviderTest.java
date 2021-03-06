package com.ansorgit.plugins.bash.codeInsight.completion;

import com.ansorgit.plugins.bash.settings.BashProjectSettings;

/**
 * User: jansorg
 * Date: 09.02.11
 * Time: 20:59
 */
public abstract class VariableNameCompletionProviderTest extends AbstractCompletionTest {
    @Override
    protected String getTestDir() {
        return "variableNameCompletion";
    }

    public void testSimpleCompletion() throws Exception {
        configure();
        checkItems("abIsOk1", "abIsOk2");
    }

    public void testIncludedVariables() throws Exception {
        configure("include.bash");
        checkItems("myVarIsOk", "myVarIsOk2", "myIncludedVarIsOk", "myIncludedVarIsOk2");
    }

    public void testDollarCompletion() throws Exception {
        configure();
        checkItems("abIsOk", "aIsOk2");
    }

    public void testGlobalCompletionInvocationOne() throws Exception {
        boolean old = BashProjectSettings.storedSettings(myProject).isAutocompleteBuiltinVars();
        try {
            BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinVars(true);

            configure();

            checkItems("PWD_MINE");
        } finally {
            BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinVars(old);
        }
    }

    public void testGlobalCompletionInvocationOneNoLocals() throws Exception {
        boolean old = BashProjectSettings.storedSettings(myProject).isAutocompleteBuiltinVars();
        try {
            BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinVars(true);

            configure();

            checkItems("PWD", "OLDPWD", "COMP_WORDBREAKS", "COMP_WORDS");
        } finally {
            BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinVars(old);
        }
    }

    public void testGlobalCompletionInvocationTwo() throws Exception {
        boolean old = BashProjectSettings.storedSettings(myProject).isAutocompleteBuiltinVars();
        try {
            BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinVars(true);

            configure(2);

            checkItems("PWD", "PWD_MINE", "OLDPWD", "COMP_WORDBREAKS", "COMP_WORDS");
        } finally {
            BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinVars(old);
        }
    }


    public void testSimpleParameterExpansion() throws Exception {
        configure();

        checkItems("abIsOk1", "abIsOk2");
    }

    public void testWithinTrapCommand() throws Exception {
        configure();

        //the trap command is a language injection host and contains a bash snippet

        checkItems("inner", "outer");
    }

    public void testEmptyParameterExpansion() throws Exception {
        configure();
        checkItems("abIsOk1", "abIsOk2");
    }

    public void testParameterExpansionNoCommands() throws Exception {
        BashProjectSettings.storedSettings(myProject).setAutocompleteBuiltinCommands(true);

        configure();
        checkItems("echoVar");
    }

    /*@Ignore("Completion inside comments seems to be IntelliJ's word completion")
    public void testWithinComment() throws Exception {
        configure();
        checkItems(NO_COMPLETIONS);
    } */


    public void testSelfReference() throws Exception {
        configure();
        checkItems(NO_COMPLETIONS);
    }
}
