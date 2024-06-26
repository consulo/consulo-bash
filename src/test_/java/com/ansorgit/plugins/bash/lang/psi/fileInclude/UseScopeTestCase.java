package com.ansorgit.plugins.bash.lang.psi.fileInclude;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.content.scope.SearchScope;
import consulo.language.psi.PsiReference;
import consulo.language.psi.scope.GlobalSearchScope;
import org.junit.Assert;

/**
 * User: jansorg
 * Date: 06.02.11
 * Time: 12:57
 */
public abstract class UseScopeTestCase extends AbstractFileIncludeTest {
    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "useScope/";
    }

    public void testUseScope() throws Exception {
        PsiReference variableReference = configure();
        PsiFile included = addFile("included.bash");
        PsiFile notIncluded = addFile("notIncluded.bash");

        PsiElement varDef = variableReference.resolve();
        Assert.assertNotNull("Var must resolve", varDef);
        Assert.assertTrue("var def must resolve to the definition in included.bash", included.equals(varDef.getContainingFile()));

        SearchScope varDefUseScope = varDef.getUseScope();
        Assert.assertTrue("Invalid type of scope: " + varDefUseScope, varDefUseScope instanceof GlobalSearchScope);

        GlobalSearchScope useScope = (GlobalSearchScope) varDefUseScope;
        Assert.assertTrue("The use scope must contain the original file itself.", useScope.contains(included.getVirtualFile()));

        //must contain the file which contains the include statement
        Assert.assertTrue("The use scope must contain the files which include the source file.", useScope.contains(myFile.getVirtualFile()));

        //must not contain the file which does not include the inspected file
        Assert.assertFalse("The use scope must not contain any other file.", useScope.contains(notIncluded.getVirtualFile()));
    }
}
