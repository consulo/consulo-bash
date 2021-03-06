package com.ansorgit.plugins.bash.lang;

import javax.annotation.Nonnull;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;

public class BashInjector implements LanguageInjector{
    @Override
    public void getLanguagesToInject(@Nonnull PsiLanguageInjectionHost host, @Nonnull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host instanceof BashString && host.isValidHost()) {
            BashString string = (BashString) host;
            injectionPlacesRegistrar.addPlace(BashFileType.BASH_LANGUAGE, string.getTextContentRange(), null, null);
        }
    }
}
