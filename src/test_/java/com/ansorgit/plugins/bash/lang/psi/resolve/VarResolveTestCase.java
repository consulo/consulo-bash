/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: VarResolveTestCase.java, Class: VarResolveTestCase
 * Last modified: 2013-04-30
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

package com.ansorgit.plugins.bash.lang.psi.resolve;

import java.util.concurrent.atomic.AtomicInteger;

import com.ansorgit.plugins.bash.BashTestUtils;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.scope.LocalSearchScope;
import consulo.language.psi.PsiRecursiveElementVisitor;
import junit.framework.Assert;

/**
 * User: jansorg
 * Date: 15.06.2010
 * Time: 19:14:20
 */
public abstract class VarResolveTestCase extends AbstractResolveTest {
    private BashVarDef assertIsWellDefinedVariable() throws Exception {
        PsiReference start = configure();
        PsiElement varDef = start.resolve();
        Assert.assertNotNull("Could not find a definition for the reference", varDef);
        Assert.assertTrue("The definition is NOT a variable definition", varDef instanceof BashVarDef);
        Assert.assertTrue("The reference is NOT a reference to the definition", start.isReferenceTo(varDef));

        return (BashVarDef) varDef;
    }

    public void testBasicResolve() throws Exception {
        BashVarDef varDef = assertIsWellDefinedVariable();

        Assert.assertTrue("Not a local search scope, this will prevent inline renaming", varDef.getUseScope() instanceof LocalSearchScope);
    }

    public void testBasicResolveInsideString() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testBasicResolveCurly() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testBasicResolveCurlyWithDefault() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testBasicResolveCurlyWithDefaultString() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testFunctionAfterDefResolve() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testFunctionBeforeDefResolve() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testBasicResolveArithmetic() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testBasicResolveArithmeticImplicit() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testBasicResolveLocalVar() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testBasicResolveLocalVarToLocalDef() throws Exception {
        BashVarDef def = assertIsWellDefinedVariable();
        Assert.assertNotNull(BashPsiUtils.findNextVarDefFunctionDefScope(def));
        Assert.assertTrue(def.isFunctionScopeLocal());
    }

    public void testBasicResolveLocalVarNested() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testBasicResolveForLoopVar() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testBasicResolveForLoopArithVar() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testResolveFunctionVarOnGlobal() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testOverrideFunctionVarOnGlobal() throws Exception {
        PsiElement varDef = assertIsWellDefinedVariable();
        //the found var def has to be on global level
        Assert.assertTrue(BashPsiUtils.findNextVarDefFunctionDefScope(varDef) == null);
    }

    public void testResolveFunctionVarToGlobalDef() throws Exception {
        PsiElement varDef = assertIsWellDefinedVariable();
        //the found var def has to be on global level
        Assert.assertTrue(BashPsiUtils.findNextVarDefFunctionDefScope(varDef) == null);
    }

    public void testResolveFunctionVarToFirstOnSameLevel() throws Exception {
        BashVar varDef = (BashVar) assertIsWellDefinedVariable();
        Assert.assertTrue(varDef.getReference().resolve() == null);
    }

    public void testResolveFunctionVarToFirstOnSameLevelNonLocal() throws Exception {
        BashVar varDef = (BashVar) assertIsWellDefinedVariable();
        Assert.assertTrue(varDef.getReference().resolve() == null);
    }

    public void testResolveFunctionVarToLocalDef() throws Exception {
        BashVar varDef = (BashVar) assertIsWellDefinedVariable();
        Assert.assertTrue(BashPsiUtils.findBroadestFunctionScope(varDef) != null);
        Assert.assertTrue(varDef.getReference().resolve() == null);
    }

    public void testBasicResolveCurlyWithAssignment() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testArrayResolve1() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testArrayResolve2() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testArrayResolve3() throws Exception {
        assertIsWellDefinedVariable();
    }

    public void testResolveParamLength() throws Exception {
        assertIsWellDefinedVariable();
    }

    //invalid resolves

    public void testBasicResolveLocalVarGlobal() throws Exception {
        PsiReference psiReference = configure();
        Assert.assertNull("The local variable must not be resolved on global level.", psiReference.resolve());
    }

    public void testBasicResolveUnknownVariable() throws Exception {
        PsiReference psiReference = configure();
        Assert.assertNull("The local variable must not be resolved on global level.", psiReference.resolve());
    }

    public void testNoResolveVarWithTwoLocalDefs() throws Exception {
        PsiReference psiReference = configure();

        //must not resolve because the definition is local due to the previous definition
        PsiElement varDef = psiReference.resolve();
        Assert.assertNull("The vardef should not be found, because it is local", varDef);
    }

    public void testBasicResolveUnknownGlobalVariable() throws Exception {
        final PsiReference psiReference = configure();

        //must not resolve because the definition is local due to the previous definition
        PsiElement varDef = psiReference.resolve();
        Assert.assertNull("The vardef should not be found, because it is undefined", varDef);

        //the variable must not be a valid reference to the following var def

        final AtomicInteger visited = new AtomicInteger(0);
        psiReference.getElement().getContainingFile().acceptChildren(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element instanceof BashVarDef) {
                    visited.incrementAndGet();
                    Assert.assertFalse("A var def must not be a valid definition for the variable used.",
                            psiReference.isReferenceTo(element));
                }

                super.visitElement(element);
            }
        });
        Assert.assertEquals(1, visited.get());
    }

    protected String getTestDataPath() {
        return BashTestUtils.getBasePath() + "/psi/resolve/var/";
    }
}
