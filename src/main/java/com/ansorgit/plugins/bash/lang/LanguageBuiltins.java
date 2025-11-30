/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: LanguageBuiltins.java, Class: LanguageBuiltins
 * Last modified: 2013-05-05
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

package com.ansorgit.plugins.bash.lang;

import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * Contains definitions of built-in resources like variables or functions.
 */
public final class LanguageBuiltins {
    //variables valid even in the very original shell implementation
    public static final Collection<String> bourneShellVars = Set.of(
            "CDPATH", "HOME", "IFS", "MAIL", "MAILPATH", "OPTARG", "OPTIND", "PATH", "PS1", "PS2"
    );

    //variables which are references to parameters. This is just a reasonable default set, Bash doesn't seem to restrict
    //the range of valid parameter
    public static final Collection<String> bashShellParamReferences = Set.of(
            "*", "@", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
    );

    /**
     * A set of built-in variables. The variables are stored without the marker character (i.e. $).
     */
    public static final Collection<String> bashShellVars = Set.of(
            "$", "#", "*", "@", "-", "!", "_", "?",
            "BASH", "BASHOPTS", "BASHPID", "BASH_ALIASES", "BASH_ARGC",
            "BASH_ARGV", "BASH_CMDS", "BASH_COMMAND", "BASH_ENV",
            "BASH_EXECUTION_STRING", "BASH_LINENO", "BASH_REMATCH", "BASH_SOURCE",
            "BASH_SUBSHELL", "BASH_VERSINFO", "BASH_VERSION", "COLUMNS",
            "COMP_CWORD", "COMP_LINE", "COMP_POINT", "COMP_TYPE",
            "COMP_KEY", "COMP_WORDBREAKS", "COMP_WORDS", "COMPREPLY",
            "DIRSTACK", "EMACS", "EUID", "FCEDIT",
            "FIGNORE", "FUNCNAME", "GLOBIGNORE", "GROUPS",
            "histchars", "HISTCMD", "HISTCONTROL", "HISTFILE",
            "HISTFILESIZE", "HISTIGNORE", "HISTSIZE",
            "HISTTIMEFORMAT", "HOSTFILE", "HOSTNAME", "HOSTTYPE",
            "IGNOREEOF", "INPUTRC", "LANG", "LC_ALL",
            "LC_COLLATE", "LC_CTYPE", "LC_MESSAGES", "LC_NUMERIC",
            "LINENO", "LINES", "MACHTYPE", "MAILCHECK",
            "OLDPWD", "OPTERR", "OSTYPE", "PIPESTATUS",
            "POSIXLY_CORRECT", "PPID", "PROMPT_COMMAND", "PROMPT_DIRTRIM",
            "PS3", "PS4", "PWD", "RANDOM",
            "REPLY", "SECONDS", "SHELL", "SHELLOPTS",
            "SHLVL", "TIMEFORMAT", "TMOUT", "TMPDIR", "UID",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19");

    public static final Collection<String> bashShellVars_v4 = Set.of(
            "BASHPID", "PROMPT_DIRTRIM"
    );

    public static final Collection<String> readonlyShellVars = Set.of(
            "BASH", "BASHOPTS", "BASHPID", "BASH_SUBSHELL", "BASH_VERSINFO", "BASH_VERSION", "EUID",
            "HOSTNAME", "HOSTTYPE", "OLDPWD", "PPID", "PWD", "UID",
            "*", "@", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
    );

    public static boolean isInternalCommand(@Nullable String commandName) {
        return commandName != null && commands.contains(commandName);
    }

    public static final Collection<String> commands = Set.of(
            ".", ":", "alias", "bg", "bind", "break", "builtin", "cd", "caller",
            "command", "compgen", "complete", "continue", "declare", "typeset",
            "dirs", "disown", "echo", "enable", "eval", "exec", "exit",
            "export", "fc", "fg", "getopts", "hash", "help", "history", "jobs",
            "kill", "let", "local", "logout", "popd", "printf", "pushd", "pwd",
            "read", "readonly", "return", "set", "shift", "shopt", "unset", "source",
            "suspend", "test", "times", "trap", "type", "ulimit", "umask", "unalias", "wait"
    );

    public static final Collection<String> commands_v4 = Set.of(
            "coproc", "mapfile"
    );

    public static final Collection<String> varDefCommands = Set.of(
            "export", "read", "declare", "readonly", "typeset"
    );

    public static final Collection<String> localVarDefCommands = Set.of(
            "local"

    );
    public static final Collection<String> readonlyVarDefCommands = Set.of(
            "readonly"
    );
    public static final Collection<String> arithmeticCommands = Set.of(
            "let"
    );

    public static final Collection<String> bashInjectionHostCommand = Set.of(
            "eval", "trap"
    );
}
