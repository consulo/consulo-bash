package com.ansorgit.plugins.bash.editor.inspections.inspections;

import org.junit.Test;

/**
 * User: jansorg
 * Date: 28.12.10
 * Time: 19:47
 */
public abstract class InternalVariableInspectionTest extends AbstractInspectionTestCase {
    public InternalVariableInspectionTest() {
        super(InternalVariableInspection.class);
    }

    @Test
    public void testOk() throws Exception {
        doTest("internalVariableInspection/ok", new InternalVariableInspection());
    }

    @Test
    public void testWriteAttempt() throws Exception {
        doTest("internalVariableInspection/writeAttempt", new InternalVariableInspection());
    }

    @Test
    public void testRedefinition() throws Exception {
        doTest("internalVariableInspection/redefinition", new InternalVariableInspection());
    }
}
