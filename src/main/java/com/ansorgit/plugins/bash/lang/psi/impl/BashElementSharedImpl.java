package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.lang.psi.FileInclusionManager;
import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashPsiElement;
import com.ansorgit.plugins.bash.lang.psi.api.function.BashFunctionDef;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.util.BashFunctions;
import consulo.content.scope.SearchScope;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.scope.LocalSearchScope;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.Set;

public class BashElementSharedImpl {
    public static GlobalSearchScope getElementGlobalSearchScope(BashPsiElement element, Project project) {
        //fixme is this right?
        BashFile psiFile = (BashFile) BashPsiUtils.findFileContext(element);
//        BashFile psiFile = (BashFile) element.getContainingFile();
        GlobalSearchScope currentFileScope = GlobalSearchScope.fileScope(psiFile);

        Set<PsiFile> includedFiles = FileInclusionManager.findIncludedFiles(psiFile, true, true);
        Collection<VirtualFile> files = includedFiles.stream().map(BashFunctions.psiToVirtualFile()).toList();

        return currentFileScope.uniteWith(GlobalSearchScope.filesScope(project, files));
    }

    public static SearchScope getElementUseScope(BashPsiElement element, Project project) {
        //all files which include this element's file belong to the requested scope

        //fixme can this be optimized?

        PsiFile currentFile = BashPsiUtils.findFileContext(element);
        //PsiFile currentFile = element.getContainingFile();
        Set includers = FileInclusionManager.findIncluders(project, currentFile);
        Set<PsiFile> included = FileInclusionManager.findIncludedFiles(currentFile, true, true);

        if (includers.isEmpty() && included.isEmpty()) {
            //we should return a local search scope if we only have local references
            //not return a local scope then inline renaming is not possible
            return new LocalSearchScope(currentFile);
        }

        Collection<VirtualFile> virtualFiles = ContainerUtil.map(ContainerUtil.<PsiFile>union(included, includers), BashFunctions.psiToVirtualFile());
        return GlobalSearchScope.fileScope(currentFile).union(GlobalSearchScope.filesScope(project, virtualFiles));
    }

    public static boolean walkDefinitionScope(PsiElement thisElement, @Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement place) {

        //walk the tree from top to bottom because the first definition has higher priority and should still be processed
        //if a later definition masks it

        PsiElement child = thisElement.getFirstChild();
        if (child == lastParent) {
            return true;
        }

        while (child != null) {
            if (!child.processDeclarations(processor, state, null, place)) {
                return false;
            }

            if (child == lastParent) {
                break;
            }

            child = child.getNextSibling();
        }

        // If the last processed child is the parent of the place element check if we need to process
        // the elements after the element
        // In certain cases the resolving has to continue below the initial place, e.g.
        // function x() {
        //    function y() {
        //        echo $a
        //    }
        //
        //    a=
        // }

        if (child != null) {
            PsiElement functionContainer = BashPsiUtils.findParent(child.getNextSibling(), BashFunctionDef.class);
            if (functionContainer != null && functionContainer != thisElement) {
                //process the siblings after the parent of place
                while (child != null) {
                    if (!child.processDeclarations(processor, state, null, place)) {
                        return false;
                    }

                    child = child.getNextSibling();
                }
            }
        }

        return true;
    }
}
