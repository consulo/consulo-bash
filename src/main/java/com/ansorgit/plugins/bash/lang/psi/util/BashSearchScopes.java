package com.ansorgit.plugins.bash.lang.psi.util;

import com.ansorgit.plugins.bash.file.BashFileType;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiFile;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.module.Module;
import consulo.virtualFileSystem.VirtualFile;

/**
 * User: jansorg
 * Date: 12.01.12
 * Time: 01:54
 */
public class BashSearchScopes {
    private BashSearchScopes() {
    }

    @RequiredReadAction
    public static GlobalSearchScope moduleScope(PsiFile file) {
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) {
            return GlobalSearchScope.EMPTY_SCOPE;
        }

        Module module = file.getModule();
        if (module == null) {
            return GlobalSearchScope.fileScope(file);
        }

        return GlobalSearchScope.moduleScope(module);
    }

    public static GlobalSearchScope bashOnly(GlobalSearchScope scope) {
        return GlobalSearchScope.getScopeRestrictedByFileTypes(scope, BashFileType.INSTANCE);
    }
}
