/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: BashElementTypes.java, Class: BashElementTypes
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

package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.lexer.BashElementType;
import com.ansorgit.plugins.bash.lang.psi.BashStubElementType;
import com.ansorgit.plugins.bash.lang.psi.api.command.BashIncludeCommand;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBackquoteImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBlockImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.BashGroupImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.expression.BashSubshellCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.loops.BashForImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.loops.BashSelectImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.loops.BashUntilImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.loops.BashWhileImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashCaseImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashConditionalCommandImpl;
import com.ansorgit.plugins.bash.lang.psi.impl.shell.BashIfImpl;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashFunctionDefStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashIncludeCommandStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.api.BashVarDefStub;
import com.ansorgit.plugins.bash.lang.psi.stubs.elements.BashStubFileElementType;
import consulo.bash.parser.BashStubElementTypes;
import consulo.language.ast.ASTNode;
import consulo.language.ast.ICompositeElementType;
import consulo.language.ast.IElementType;
import consulo.language.psi.stub.IStubFileElementType;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.function.Supplier;

/**
 * The available Bash parser element types.
 *
 * @author Joachim Ansorg
 */
public interface BashElementTypes {
    class BashCompositeElementType extends IBashElementType implements ICompositeElementType {
        private final Supplier<ASTNode> myNewInstanceFunc;

        private BashCompositeElementType(@NonNls final String debugName, final Supplier<ASTNode> newInstanceFunc) {
            this(debugName, newInstanceFunc, false);
        }

        private BashCompositeElementType(@NonNls final String debugName, final Supplier<ASTNode> newInstanceFunc, final boolean leftBound) {
            super(debugName, leftBound);
            myNewInstanceFunc = newInstanceFunc;
        }

        @Nonnull
        @Override
        public ASTNode createCompositeNode() {
            return myNewInstanceFunc.get();
        }
    }

    IStubFileElementType FILE = new BashStubFileElementType();

    IElementType FILE_REFERENCE = new BashElementType("File reference");

    IElementType SHEBANG_ELEMENT = new BashElementType("shebang element");

    IElementType BLOCK_ELEMENT = new BashCompositeElementType("block element", BashBlockImpl::new);
    IElementType GROUP_ELEMENT = new BashCompositeElementType("group element", BashGroupImpl::new);

    //Var usage
    //fixme probably unnecessary
    IElementType VAR_ELEMENT = new BashElementType("variable");
    IElementType VAR_COMPOSED_VAR_ELEMENT = new BashElementType("composed variable, like subshell");
    IElementType PARSED_WORD_ELEMENT = new BashElementType("combined word");
    IElementType PARAM_EXPANSION_ELEMENT = new BashElementType("var substitution");

    IElementType FUNCTION_DEF_NAME_ELEMENT = new BashElementType("named symbol");

    //redirect elements
    IElementType REDIRECT_LIST_ELEMENT = new BashElementType("redirect list");
    IElementType REDIRECT_ELEMENT = new BashElementType("redirect element");

    IElementType PROCESS_SUBSTITUTION_ELEMENT = new BashElementType("process substitution element");

    //command elements
    IElementType SIMPLE_COMMAND_ELEMENT = new BashElementType("simple command element");
    BashStubElementType<BashVarDefStub, BashVarDef> VAR_DEF_ELEMENT = BashStubElementTypes.VAR_DEF_ELEMENT;

    IElementType GENERIC_COMMAND_ELEMENT = new BashElementType("generic bash command");
    BashStubElementType<BashIncludeCommandStub, BashIncludeCommand> INCLUDE_COMMAND_ELEMENT = BashStubElementTypes.INCLUDE_COMMAND_ELEMENT;

    //pipeline commands
    IElementType PIPELINE_COMMAND = new BashElementType("pipeline command");

    //composed command, i.e. a && b
    IElementType COMPOSED_COMMAND = new BashElementType("composed command");

    //shell commands
    IElementType WHILE_COMMAND = new BashCompositeElementType("while loop", BashWhileImpl::new);
    IElementType UNTIL_COMMAND = new BashCompositeElementType("until loop", BashUntilImpl::new);
    IElementType FOR_COMMAND = new BashCompositeElementType("for shellcommand", BashForImpl::new);
    IElementType SELECT_COMMAND = new BashCompositeElementType("select command", BashSelectImpl::new);
    IElementType IF_COMMAND = new BashCompositeElementType("if shellcommand", BashIfImpl::new);
    IElementType CONDITIONAL_COMMAND = new BashCompositeElementType("conditional shellcommand", BashConditionalCommandImpl::new);
    IElementType SUBSHELL_COMMAND = new BashCompositeElementType("subshell shellcommand", BashSubshellCommandImpl::new);
    IElementType BACKQUOTE_COMMAND = new BashCompositeElementType("backquote shellcommand", BashBackquoteImpl::new);

    BashStubElementType<BashFunctionDefStub, BashFunctionDef> FUNCTION_DEF_COMMAND = BashStubElementTypes.FUNCTION_DEF_COMMAND;

    IElementType GROUP_COMMAND = new BashCompositeElementType("group command", BashGroupImpl::new);

    //arithmetic commands
    IElementType ARITHMETIC_COMMAND = new BashElementType("arithmetic command");

    IElementType ARITH_ASSIGNMENT_CHAIN_ELEMENT = new BashElementType("arithmetic assignment chain");
    IElementType ARITH_ASSIGNMENT_ELEMENT = new BashElementType("arithmetic assignment");
    IElementType ARITH_SUM_ELEMENT = new BashElementType("arithmetic sum");
    IElementType ARITH_BIT_OR_ELEMENT = new BashElementType("arithmetic bitwise or");
    IElementType ARITH_BIT_XOR_ELEMENT = new BashElementType("arithmetic bitwise xor");
    IElementType ARITH_BIT_AND_ELEMENT = new BashElementType("arithmetic bitwise and");
    IElementType ARITH_COMPUND_COMPARISION_ELEMENT = new BashElementType("arith compund comparision");
    IElementType ARITH_EQUALITY_ELEMENT = new BashElementType("arithmetic equality");
    IElementType ARITH_EXPONENT_ELEMENT = new BashElementType("arithmetic exponent");
    IElementType ARITH_LOGIC_AND_ELEMENT = new BashElementType("arithmetic logic and");
    IElementType ARITH_LOGIC_OR_ELEMENT = new BashElementType("arithmetic logic or");
    IElementType ARITH_MULTIPLICACTION_ELEMENT = new BashElementType("arithmetic multiplication");
    IElementType ARITH_NEGATION_ELEMENT = new BashElementType("arithmetic negation");
    IElementType ARITH_POST_INCR_ELEMENT = new BashElementType("arithmetic post incr");
    IElementType ARITH_PRE_INC_ELEMENT = new BashElementType("arithmetic pre incr");
    IElementType ARITH_SHIFT_ELEMENT = new BashElementType("arithmetic shift");
    IElementType ARITH_SIMPLE_ELEMENT = new BashElementType("arithmetic simple");
    IElementType ARITH_TERNERAY_ELEMENT = new BashElementType("arithmetic ternary operator");

    IElementType ARITH_PARENS_ELEMENT = new BashElementType("arithmetic parenthesis expr");

    IElementType CASE_COMMAND = new BashCompositeElementType("case pattern", BashCaseImpl::new);
    IElementType CASE_PATTERN_LIST_ELEMENT = new BashElementType("case pattern list");
    IElementType CASE_PATTERN_ELEMENT = new BashElementType("case pattern");

    IElementType TIME_COMMAND = new BashElementType("time with optional -p");

    //misc
    IElementType EXPANSION_ELEMENT = new BashElementType("single bash expansion");

    IElementType VAR_ASSIGNMENT_LIST = new BashElementType("array assignment list");

    //heredoc
    IElementType HEREDOC_ELEMENT = new BashElementType("here doc element");
    IElementType HEREDOC_START_MARKER_ELEMENT = new BashElementType("here doc start marker element");
    IElementType HEREDOC_END_MARKER_ELEMENT = new BashElementType("here doc end marker element");

    IElementType STRING_ELEMENT = new BashElementType("string");
    IElementType LET_EXPRESSION = new BashElementType("lazy LET expression");
}
