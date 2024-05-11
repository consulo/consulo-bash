package com.ansorgit.plugins.bash.lang;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.ansorgit.plugins.bash.lang.psi.api.BashString;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.inject.LanguageInjector;
import consulo.language.psi.PsiLanguageInjectionHost;
import consulo.language.inject.InjectedLanguagePlaces;
import jakarta.annotation.Nonnull;

@ExtensionImpl
public class BashInjector implements LanguageInjector {
    @Override
    public void injectLanguages(@Nonnull PsiLanguageInjectionHost host, @Nonnull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host instanceof BashString && host.isValidHost()) {
            BashString string = (BashString) host;
            injectionPlacesRegistrar.addPlace(BashFileType.BASH_LANGUAGE, string.getTextContentRange(), null, null);
        }
    }
}
