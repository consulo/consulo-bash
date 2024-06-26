/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFunctionDef.java, Class: BashFunctionDef
 * Last modified: 2013-01-25
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

package com.ansorgit.plugins.bash.lang.psi.api.function;

import com.ansorgit.plugins.bash.lang.psi.api.BashBlock;
import com.ansorgit.plugins.bash.lang.psi.api.BashFunctionDefName;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.DocumentationAwareElement;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVarDefContainer;
import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.language.psi.PsiNamedElement;
import consulo.navigation.NavigationItem;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * Date: 11.04.2009
 * Time: 23:47:42
 *
 * @author Joachim Ansorg
 */
public interface BashFunctionDef extends BashPsiElement, PsiNamedElement, NavigationItem, PsiNameIdentifierOwner, BashVarDefContainer, DocumentationAwareElement {
    /**
     * Returns the function body. A valid function definition always has a valid body.
     *
     * @return The body of the function.
     */
    BashBlock body();

    /**
     * Returns whether the body of this function is wrapped in curly brackets, i.e. in a command group.
     *
     * @return True if the body is in a commmand group.
     */
    boolean hasCommandGroup();

    @Nullable
    BashFunctionDefName getNameSymbol();

    /**
     * The list of parameters used inside this function definition. Sub-functions are not searched for parameters
     * uses.
     *
     * @return The list of parameter variable uses
     */
    @Nonnull
    List<BashVar> findReferencedParameters();
}
