/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashFileTypeLoader.java, Class: BashFileTypeLoader
 * Last modified: 2010-03-24 21:22
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

package com.ansorgit.plugins.bash.file;

import consulo.annotation.component.ExtensionImpl;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;

import jakarta.annotation.Nonnull;

/**
 * The file type loader of the BashSupport plugin. Registered in the plugin.xml file.
 *
 * @author Joachim Ansorg
 */
@ExtensionImpl
public class BashFileTypeLoader extends FileTypeFactory {
    public void createFileTypes(@Nonnull FileTypeConsumer consumer) {
        consumer.consume(BashFileType.INSTANCE, BashFileType.DEFAULT_EXTENSION);
        consumer.consume(BashFileType.INSTANCE, BashFileType.BASH_EXTENSION);
    }
}
