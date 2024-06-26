/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: ForwardingMarker.java, Class: ForwardingMarker
 * Last modified: 2010-11-23 21:32
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

package com.ansorgit.plugins.bash.lang.parser.util;

import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.WhitespacesAndCommentsBinder;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nullable;

/**
 * A forwarding marker implementation which is useful for enhancements which
 * are based on aggregation.
 * <p/>
 * Date: 17.04.2009
 * Time: 16:56:16
 *
 * @author Joachim Ansorg
 */
public abstract class ForwardingMarker implements PsiBuilder.Marker {
    protected final PsiBuilder.Marker original;

    protected ForwardingMarker(PsiBuilder.Marker original) {
        this.original = original;
    }

    public PsiBuilder.Marker getOriginal() {
        return original;
    }

    public void done(IElementType type) {
        original.done(type);
    }

    public void collapse(IElementType iElementType) {
        original.collapse(iElementType);
    }

    public void doneBefore(IElementType type, PsiBuilder.Marker before) {
        original.doneBefore(type, before);
    }

    public void doneBefore(IElementType type, PsiBuilder.Marker before, LocalizeValue errorMessage) {
        original.doneBefore(type, before, errorMessage);
    }

    public void drop() {
        original.drop();
    }

    public void error(LocalizeValue message) {
        original.error(message);
    }

    public void errorBefore(LocalizeValue message, PsiBuilder.Marker marker) {
        original.errorBefore(message, marker);
    }

    public PsiBuilder.Marker precede() {
        return original.precede();
    }

    public void rollbackTo() {
        original.rollbackTo();
    }

    public void setCustomEdgeTokenBinders(@Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder, @Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder1) {
        original.setCustomEdgeTokenBinders(whitespacesAndCommentsBinder, whitespacesAndCommentsBinder1);
    }
}
