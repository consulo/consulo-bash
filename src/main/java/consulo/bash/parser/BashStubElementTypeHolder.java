package consulo.bash.parser;

import com.ansorgit.plugins.bash.lang.parser.BashElementTypes;
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
public class BashStubElementTypeHolder extends StubElementTypeHolder<BashElementTypes> {
    @Nullable
    @Override
    public String getExternalIdPrefix() {
        return null;
    }

    @Nonnull
    @Override
    public List<ObjectStubSerializerProvider> loadSerializers() {
        return allFromStaticFields(BashElementTypes.class, Field::get);
    }
}
