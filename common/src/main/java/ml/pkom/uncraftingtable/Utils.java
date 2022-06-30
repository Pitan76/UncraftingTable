package ml.pkom.uncraftingtable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class Utils {
    public static Text translatableText(String key) {
        return Text.translatable(key);
    }

    public static Text text(String string) {
        return Text.literal(string);
    }
}
