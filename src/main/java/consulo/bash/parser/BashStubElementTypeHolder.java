package consulo.bash.parser;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.ObjectStubSerializerProvider;
import consulo.language.psi.stub.StubElementTypeHolder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author VISTALL
 * @since 11.05.2024
 */
@ExtensionImpl
public class BashStubElementTypeHolder extends StubElementTypeHolder<BashStubElementTypes> {
    @Nullable
    @Override
    public String getExternalIdPrefix() {
        return null;
    }

    @Nonnull
    @Override
    public List<ObjectStubSerializerProvider> loadSerializers() {
        return allFromStaticFields(BashStubElementTypes.class, Field::get);
    }
}
