package com.ansorgit.plugins.bash.editor.inspections.inspections;

import org.junit.Test;

/**
 * User: jansorg
 * Date: 28.12.10
 * Time: 19:15
 */
public abstract class UnusedFunctionDefInspectionTest extends AbstractInspectionTestCase {
    public UnusedFunctionDefInspectionTest() {
        super(UnusedFunctionDefInspection.class);
    }

    @Test
    public void testOk() throws Exception {
        doTest("unusedFunctionDefInspection/ok", new UnusedFunctionDefInspection());
    }

    @Test
    public void testOk2() throws Exception {
        doTest("unusedFunctionDefInspection/ok2", new UnusedFunctionDefInspection());
    }

    @Test
    public void testOk3() throws Exception {
        doTest("unusedFunctionDefInspection/ok3", new UnusedFunctionDefInspection());
    }

    @Test
    public void testOk4() throws Exception {
        doTest("unusedFunctionDefInspection/ok4", new UnusedFunctionDefInspection());
    }

    @Test
    public void testUnusedDef() throws Exception {
        doTest("unusedFunctionDefInspection/unusedDef", new UnusedFunctionDefInspection());
    }
}
