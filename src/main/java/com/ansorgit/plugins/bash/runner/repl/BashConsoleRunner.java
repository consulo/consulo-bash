/*******************************************************************************
 * Copyright 2011 Joachim Ansorg, mail@ansorg-it.com
 * File: BashConsoleRunner.java, Class: BashConsoleRunner
 * Last modified: 2011-05-17 22:20
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

package com.ansorgit.plugins.bash.runner.repl;

import com.ansorgit.plugins.bash.lang.BashLanguage;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.util.BashInterpreterDetection;
import consulo.execution.ui.console.language.*;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.cmd.GeneralCommandLine;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

/**
 * ConsoleRunner implementation to run a bash shell in a window.
 * <p>
 * User: jansorg
 * Date: 13.05.2010
 * Time: 00:45:23
 */
public class BashConsoleRunner extends AbstractConsoleRunnerWithHistory<LanguageConsoleView>
{
	public BashConsoleRunner(Project myProject, String workingDir)
	{
		super(myProject, "Bash", workingDir);
	}

	@Override
	protected LanguageConsoleView createConsoleView()
	{
		LanguageConsoleBuilderFactory builderFactory = getProject().getInstance(LanguageConsoleBuilderFactory.class);

		LanguageConsoleBuilder builder = builderFactory.newBuilder(BashLanguage.INSTANCE);
		LanguageConsoleView view = builder.build();
		view.getFile().putUserData(BashFile.LANGUAGE_CONSOLE_MARKER, true);
		return view;
	}

	@Nonnull
	@Override
	protected ProcessHandler createProcessHandler() throws ExecutionException
	{
		GeneralCommandLine commandLine = new GeneralCommandLine();

		BashInterpreterDetection detect = new BashInterpreterDetection();
		//fixme make this configurable
		commandLine.setExePath(detect.findBestLocation());

		if (getWorkingDir() != null) {
			commandLine.setWorkDirectory(getWorkingDir());
		}

		//fixme
		//commandLine.addParameters(provider.getArguments());
		return ProcessHandlerBuilder.create(commandLine).colored().killable().build();
	}

	@Nonnull
	@Override
	protected ProcessBackedConsoleExecuteActionHandler createExecuteActionHandler()
	{
		return new ProcessBackedConsoleExecuteActionHandler(getProcessHandler(), true);
	}
}
