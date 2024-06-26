package com.github.linyuzai.inherit.plugin.intellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;

public class LibraryUtils {

    private static final String CONCEPT_INHERIT_PACKAGE = "com.github.linyuzai.inherit.core";

    /**
     * 是否存在对应的依赖
     */
    public static boolean hasLibrary(Project project) {
        ApplicationManager.getApplication().assertReadAccessAllowed();
        return CachedValuesManager.getManager(project).getCachedValue(project, () -> {
            PsiPackage aPackage = JavaPsiFacade.getInstance(project).findPackage(CONCEPT_INHERIT_PACKAGE);
            return new CachedValueProvider.Result<>(aPackage, ProjectRootManager.getInstance(project));
        }) != null;
    }
}
