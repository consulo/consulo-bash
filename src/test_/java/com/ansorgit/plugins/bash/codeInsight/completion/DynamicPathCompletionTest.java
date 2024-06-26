package com.ansorgit.plugins.bash.codeInsight.completion;

import consulo.language.editor.completion.lookup.LookupElement;
import org.junit.Assert;

/**
 * User: jansorg
 * Date: 09.02.11
 * Time: 20:59
 */
public abstract class DynamicPathCompletionTest extends AbstractCompletionTest {
    @Override
    protected String getTestDir() {
        return "dynamicPathCompletion";
    }

    public void testSimpleCompletion() throws Throwable {
        configure();
        checkItems("./SimpleCompletion.bash");
    }

    public void testNoCompletion() throws Throwable {
        configure();
        checkItems(NO_COMPLETIONS);
    }

    public void testHomeVarCompletion() throws Throwable {
        configure();
        Assert.assertTrue("No completions for $HOME", myItems.length >= 1);

        for (LookupElement item : myItems) {
            String lookup = item.getLookupString();
            Assert.assertTrue("item does not begin with $HOME/ : " + lookup, lookup.startsWith("$HOME/"));
            Assert.assertFalse("item must not begin with $HOME// : " + lookup, lookup.startsWith("$HOME//"));
        }
    }

    public void testHomeVarHiddenCompletion() throws Throwable {
        configure();
        Assert.assertTrue("No completions for $HOME", myItems.length >= 1);

        for (LookupElement item : myItems) {
            String lookup = item.getLookupString();
            Assert.assertFalse("item must not contain ./. : " + lookup, lookup.contains("./."));
        }
    }

    public void testTildeCompletion() throws Throwable {
        configure();
        Assert.assertTrue("No completions for $HOME", myItems.length >= 1);

        for (LookupElement item : myItems) {
            String lookup = item.getLookupString();
            Assert.assertTrue("item does not begin with ~/ : " + lookup, lookup.startsWith("~/"));
            Assert.assertFalse("item must not begin with ~// : " + lookup, lookup.startsWith("~//"));
        }
    }

    public void testNoFunctionCompletion() throws Exception {
        configure();
        Assert.assertTrue("No completions found", myItems.length > 0);

        for (LookupElement item : myItems) {
            String lookupString = item.getLookupString();
            Assert.assertFalse("Completion of path must not offer function name: " + lookupString, lookupString.contains("myFunction"));
        }
    }
}
