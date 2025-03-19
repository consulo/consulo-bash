package consulo.bash.parser;

import com.ansorgit.plugins.bash.lang.psi.BashStubElementType;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFunctionDefStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashIncludeCommandStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashVarDefStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashFunctionDefElementType;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashIncludeCommandElementType;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashVarDefElementType;

/**
 * @author VISTALL
 * @since 2025-03-19
 */
public interface BashStubElementTypes {
    BashStubElementType<BashVarDefStub, BashVarDef> VAR_DEF_ELEMENT = new BashVarDefElementType();

    BashStubElementType<BashFunctionDefStub, BashFunctionDef> FUNCTION_DEF_COMMAND = new BashFunctionDefElementType();

    BashStubElementType<BashIncludeCommandStub, BashIncludeCommand> INCLUDE_COMMAND_ELEMENT = new BashIncludeCommandElementType();
}
