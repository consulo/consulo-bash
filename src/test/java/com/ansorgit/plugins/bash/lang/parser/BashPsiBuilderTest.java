package com.ansorgit.plugins.bash.lang.parser;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import com.ansorgit.plugins.bash.lang.BashVersion;

/**
 * User: jansorg
 * Date: 09.11.11
 * Time: 22:21
 */
public class BashPsiBuilderTest extends MockPsiTest {
    @Test
    public void testWhitespaceDisabled() throws Exception {
        BashPsiBuilder builder = new BashPsiBuilder(builderFor(Collections.<String>emptyList(), NUMBER, WHITESPACE, WORD), BashVersion.Bash_v4);

        Assert.assertEquals(NUMBER, builder.getTokenType());

        builder.advanceLexer();
        Assert.assertEquals(WORD, builder.getTokenType());
    }

    @Test
    public void testWhitespaceEnabled() throws Exception {
        BashPsiBuilder builder = new BashPsiBuilder(builderFor(Collections.<String>emptyList(), NUMBER, WHITESPACE, WORD, WHITESPACE, NUMBER), BashVersion.Bash_v4);
        Assert.assertEquals(NUMBER, builder.getTokenType());

        builder.advanceLexer();
        Assert.assertEquals(WHITESPACE, builder.getTokenType(true));

        builder.advanceLexer();
        Assert.assertEquals(WORD, builder.getTokenType(true));

        builder.advanceLexer();
        Assert.assertEquals(NUMBER, builder.getTokenType(false));
    }
}
