/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashInternalCommandImpl.java, Class: BashInternalCommandImpl
 * Last modified: 2011-02-18 20:22
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.ansorgit.plugins.bash.lang.psi.impl.command;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashInternalCommand;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import consulo.language.ast.ASTNode;
import consulo.language.psi.stub.StubElement;

/**
 * User: jansorg
 * Date: Oct 29, 2009
 * Time: 8:19:49 PM
 */
public class BashInternalCommandImpl extends BashBaseStubElementImpl<StubElement> implements BashInternalCommand {
    public BashInternalCommandImpl(ASTNode astNode) {
        super(astNode, "BashInternalCommand");
    }

    public BashInternalCommandImpl(ASTNode astNode, String description) {
        super(astNode, description);
    }
}
