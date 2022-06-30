package ml.pkom.uncraftingtable.easyapi.config;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public interface IConfig {
    Map<String, Object> configMap = new LinkedHashMap<>();

    Object get(String key);

    String getString(String key);

    int getInt(String key);

    boolean getBoolean(String key);

    boolean set(String key, Object value);

    boolean setString(String key, String value);

    boolean setInt(String key, int value);

    boolean setBoolean(String key, boolean value);

    boolean load(String file);

    boolean load(File file);

    boolean save(String file);

    boolean save(File file);

    boolean save(String file, boolean pretty);

    boolean save(File file, boolean pretty);
}
