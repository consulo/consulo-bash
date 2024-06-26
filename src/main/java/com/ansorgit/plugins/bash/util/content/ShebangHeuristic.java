/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: ShebangHeuristic.java, Class: ShebangHeuristic
 * Last modified: 2011-09-03 14:30
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

package com.ansorgit.plugins.bash.util.content;

import com.ansorgit.plugins.bash.util.BashInterpreterDetection;
import consulo.project.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jansorg
 * Date: Feb 20, 2010
 * Time: 11:28:48 AM
 */
class ShebangHeuristic implements ContentHeuristic {
    private static final List<String> validStarts = new ArrayList<>();

    static {
        for (String location : BashInterpreterDetection.guessLocations) {
            validStarts.add("#!" + location);
        }

    }

    private final double weight;

    public ShebangHeuristic(double weight) {
        this.weight = weight;
    }

    public double isBashFile(File file, String data, Project project) {
        for (String s : validStarts) {
            if (data.startsWith(s)) {
                return weight;
            }
        }

        return 0;
    }
}
