package ml.pkom.uncraftingtable;

import net.minecraft.text.Text;

public class Utils {
    public static Text translatableText(String key) {
        return Text.translatable(key);
    }

    public static Text text(String string) {
        return Text.literal(string);
    }
}
