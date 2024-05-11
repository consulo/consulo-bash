package com.ansorgit.plugins.bash.lang.psi.fileInclude;

import org.junit.Assert;
import org.junit.Test;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import consulo.language.psi.PsiElement;

/**
 * User: jansorg
 * Date: 18.02.11
 * Time: 20:25
 */
public abstract class FileReferenceTest extends AbstractFileIncludeTest {
    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "fileReference/";
    }

    @Test
    public void testSimpleFileReference() throws Exception {
        PsiElement element = checkWithIncludeFile("includedFile.bash", true);
        Assert.assertTrue(element instanceof BashFile);
    }
}
