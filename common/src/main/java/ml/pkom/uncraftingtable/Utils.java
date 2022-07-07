package ml.pkom.uncraftingtable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class Utils {
    public static TranslatableText translatableText(String key) {
        return new TranslatableText(key);
    }

    public static LiteralText text(String string) {
        return new LiteralText(string);
    }
}
