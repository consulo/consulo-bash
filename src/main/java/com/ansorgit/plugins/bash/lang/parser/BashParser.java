/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashParser.java, Class: BashParser
 * Last modified: 2011-04-30 16:33
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

package com.ansorgit.plugins.bash.lang.parser;

import javax.annotation.Nonnull;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;
import consulo.lang.LanguageVersion;

/**
 * Consumes a stream of Bash tokens and generates a PSI tree for a Bash file.
 * <p/>
 * The parsing code is split up in logical files. Each file has a descriptive name
 * which says what it does. The instances of those parsing helper classes are
 * available at Parsing.
 * <p/>
 * The package builtin contains the parsing tools to parse the syntax of internal commands.
 *
 * @author Joachim Ansorg, mail@ansorg-it.com
 */
public class BashParser implements PsiParser {
    private static final Logger log = Logger.getInstance("BashParser");
    private static final String debugKey = "bashsupport.debug";
    private static final boolean debugMode = "true".equals(System.getProperty(debugKey)) || "true".equals(System.getenv(debugKey));

    @Nonnull
    public ASTNode parse(final IElementType root, final PsiBuilder psiBuilder, LanguageVersion languageVersion) {
        final BashPsiBuilder builder = new BashPsiBuilder(psiBuilder, null);

        if (debugMode) {
            log.info("Enabling parser's debug mode...");
        }

        builder.setDebugMode(debugMode);

        final PsiBuilder.Marker rootMarker = builder.mark();
        Parsing.file.parseFile(builder);
        rootMarker.done(root);

        return builder.getTreeBuilt();
    }
}
