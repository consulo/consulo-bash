/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashVarVariantsProcessor.java, Class: BashVarVariantsProcessor
 * Last modified: 2011-02-07 20:10
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

package com.ansorgit.plugins.bash.lang.psi.impl.vars;

import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashAbstractProcessor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.util.collection.Lists;
import consulo.util.collection.Sets;
import consulo.util.dataholder.Key;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 14.04.2009
 * Time: 17:34:42
 *
 * @author Joachim Ansorg
 */
public class BashVarVariantsProcessor extends BashAbstractProcessor implements BashVarCollectorProcessor {
    private final List<BashVarDef> variables = new ArrayList<>();
    private final Set<String> variableNames = new HashSet<>();
    private final PsiElement startElement;

    public BashVarVariantsProcessor(PsiElement startElement) {
        super(false);

        this.startElement = startElement;
    }

    public boolean execute(PsiElement psiElement, ResolveState resolveState) {
        if (psiElement instanceof BashVarDef) {
            final BashVarDef varDef = (BashVarDef) psiElement;
            if (varDef.isStaticAssignmentWord() && !varDef.isCommandLocal() && !variableNames.contains(varDef.getName()) && BashVarUtils.isInDefinedScope(startElement, varDef)) {
                variables.add(varDef);
                variableNames.add(varDef.getName());
            }
        }

        return true;
    }

    public List<BashVarDef> getVariables() {
        return variables;
    }

    public <T> T getHint(Key<T> tKey) {
        return null;
    }
}