/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashCasePatternListElementImpl.java, Class: BashCasePatternListElementImpl
 * Last modified: 2010-07-04 21:24
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

package com.ansorgit.plugins.bash.lang.psi.impl.shell;

import com.ansorgit.plugins.bash.lang.psi.api.shell.BashCasePatternListElement;
import com.ansorgit.plugins.bash.lang.psi.impl.BashBaseStubElementImpl;
import consulo.language.ast.ASTNode;
import consulo.language.psi.stub.StubElement;

/**
 * User: jansorg
 * Date: 01.08.2009
 * Time: 20:05:01
 */
public class BashCasePatternListElementImpl extends BashBaseStubElementImpl<StubElement> implements BashCasePatternListElement {
    public BashCasePatternListElementImpl(final ASTNode astNode) {
        super(astNode, "case pattern list element");
    }
}
