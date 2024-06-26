/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: HereDocData.java, Class: HereDocData
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

import java.util.LinkedList;
import java.util.Queue;

import consulo.util.lang.Trinity;

/**
 * Contains the status of here-doc parsing.
 * It contains a stack of expected heredoc end markers.
 *
 * @author Joachim Ansorg
 */
public final class HereDocData {
    //Values are triples: marker text, type, strip/don't-strip
    private final Queue<consulo.util.lang.Trinity<String, MarkerType, Boolean>> expectedEnds = new LinkedList<Trinity<String, MarkerType, Boolean>>();

    public enum MarkerType {
        Eval, NoEval
    }

    public void addExpectedDoc(String endName, MarkerType markerType, Boolean stripWhitespace) {
        expectedEnds.add(consulo.util.lang.Trinity.create(endName, markerType, stripWhitespace));
    }

    public void removeExpectedEnd() {
        expectedEnds.poll();
    }

    public String expectedDocEnd() {
        return expectedEnds.peek().first;
    }

    public boolean isCurrentlyEvaluating() {
        return expectedEnds.peek().second == MarkerType.Eval;
    }

    public boolean isStrippingWhitespace() {
        return isCurrentlyEvaluating();//expectedEnds.peek().second == MarkerType.Eval;
    }

    public boolean expectsHereDoc() {
        return expectedEnds.size() > 0;
    }

    public void reset() {
        expectedEnds.clear();
    }
}
