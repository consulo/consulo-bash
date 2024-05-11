package com.ansorgit.plugins.bash.lang.psi.util;

import com.ansorgit.plugins.bash.lang.psi.api.ResolveProcessor;
import com.ansorgit.plugins.bash.lang.psi.api.vars.BashVar;
import com.ansorgit.plugins.bash.lang.psi.impl.vars.BashVarProcessor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.resolve.ResolveState;

public final class BashResolveUtil {
    public static PsiElement resolve(BashVar bashVar, boolean leaveInjectionHosts) {
        final String varName = bashVar.getReference().getReferencedName();
        if (varName == null) {
            return null;
        }

        ResolveProcessor processor = new BashVarProcessor(bashVar, true, leaveInjectionHosts);
        PsiFile containingFile = BashPsiUtils.findFileContext(bashVar);
        if (!BashPsiUtils.varResolveTreeWalkUp(processor, bashVar, containingFile, ResolveState.initial())) {
            return processor.getBestResult(false, bashVar);
        }

        return null;
    }
}
