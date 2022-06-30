package ml.pkom.uncraftingtable.easyapi.config;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config implements IConfig {
    public Map<String, Object> configMap = new LinkedHashMap<>();

    /**
     * 任意ファイルを読み込み、Mapへ変換
     * 
     * @param file
     */
    public Config(File file) {
        load(file);
    }

    /**
     * 任意ファイルを読み込み、Mapへ変換
     * 
     * @param file
     */
    public Config(String file) {
        this(new File(file));
    }

    public Config() {};

    /**
     * Configの変換
     * 
     * @param config
     */
    public Config(Config config) {
        configMap = config.configMap;
    };

    /**
     * マップのキーから値を取得
     * "."でパスを区切る
     * 
     * @param key キー
     * @return 取得した値
     */
    @SuppressWarnings("unchecked")
    public Object get(String key) {
        try {
            key = key.replace("\\.", "$&#92;$");
            String[] keyList = key.split("\\.");
            if (keyList.length == 1) {
                key = key.replace("$&#92;$", ".");
                return configMap.get(key);
            }
            int i = 0;
            Map<String, Object> inMap = new LinkedHashMap<>();
            for (String k : keyList) {
                i++;
                k = k.replace("$&#92;$", ".");
                if (keyList.length == i) {
                    return inMap.get(k);
                }
                if (i == 1) {
                    inMap = (Map<String, Object>) configMap.get(k);
                    continue;
                }
                inMap = (Map<String, Object>) inMap.get(k);
            }
            return null;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * マップのキーから値(文字列)を取得 
     * "."でパスを区切る
     * 
     * @param key キー
     * @return 取得した値
     */
    public String getString(String key) {
        return (String) get(key);
    }

    /**
     * マップのキーから値(数値)を取得
     * "."でパスを区切る
     * 
     * @param key キー
     * @return 取得した値
     */
    public int getInt(String key) {
        Object obj = get(key);
        if (obj instanceof Double) {
            return ((Double)obj).intValue();
        }
        if (obj instanceof Short) {
            return ((Short)obj).intValue();
        }
        if (obj instanceof Long) {
            return ((Long)obj).intValue();
        }
        return (Integer) obj;
    }

    public double getDouble(String key) {
        return (Double) get(key);
    }

    /**
     * マップのキーから値(真偽値)を取得
     * "."でパスを区切る
     * 
     * @param key キー
     * @return 取得した値
     */
    public boolean getBoolean(String key) {
        try {
            return (Boolean) get(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * マップのキーに値をセット
     * "."でパスを区切る
     * 失敗するとfalseを返す
     * 
     * @param key キー
     * @param value 値
     * @return 真偽値
     */
    @SuppressWarnings("unchecked")
    public boolean set(String key, Object value) {
        try {
            key = key.replace("\\.", "$&#92;$");
            String[] keyList = key.split("\\.");
            if (keyList.length == 1) {
                key = key.replace("$&#92;$", ".");
                configMap.put(key, value);
                return true;
            }
            int i = 0;
            Map<String, Object> inMap = new LinkedHashMap<>();
            for (String k : keyList) {
                i++;
                k = k.replace("$&#92;$", ".");
                if (keyList.length == i) {
                    inMap.put(k, value);
                    continue;
                }
                Map<String, Object> beforeInMap = inMap;
                if (configMap.containsKey(k) && i == 1) {
                    inMap = (LinkedHashMap<String, Object>) configMap.get(k);
                }else if (beforeInMap.containsKey(k)){
                    inMap = (LinkedHashMap<String, Object>) beforeInMap.get(k);
                }else {
                    inMap = new LinkedHashMap<String, Object>();
                }
                if (i == 1) {
                    configMap.put(k, inMap);
                    continue;
                }
                beforeInMap.put(k, inMap);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * マップのキーに値(文字列)をセット
     * "."でパスを区切る
     * 失敗するとfalseを返す
     * 
     * @param key キー
     * @param value 値
     * @return 真偽値
     */
    public boolean setString(String key, String value) {
        return set(key, value);
    }

    /**
     * マップのキーに値(数値)をセット
     * "."でパスを区切る
     * 失敗するとfalseを返す
     * 
     * @param key キー
     * @param value 値
     * @return 真偽値
     */
    public boolean setInt(String key, int value) {
        return set(key, value);
    }

    public boolean setDouble(String key, double value) {
        return set(key, value);
    }

    /**
     * マップのキーに値(真偽値)をセット
     * "."でパスを区切る
     * 失敗するとfalseを返す
     * 
     * @param key キー
     * @param value 値
     * @return 真偽値
     */
    public boolean setBoolean(String key, boolean value) {
        return set(key, value);
    }

    /**
     * StringをFileにして読み込み、継承先で処理
     * 
     * @param file ファイル名
     */
    public boolean load(String file) {
        return load(new File(file));
    }

    /**
     * この関数は継承先で定義
     * 
     * @param file ファイル
     */
    public boolean load(File file) {
        return false;
    }

    /**
     * StringをFileにし、継承先で処理して保存
     * 
     * @param file ファイル名
     */
    public boolean save(String file) {
        return save(new File(file));
    }

    /**
     * ファイルを継承先で処理して保存
     * 
     * @param file ファイル
     */
    public boolean save(File file) {
        return save(file, true);
    }

    /**
     * ファイルを継承先で処理して保存
     * 
     * @param file ファイル名
     * @param pretty 整形
     */
    public boolean save(String file, boolean pretty) {
        return save(new File(file), pretty);
    }

    /**
     * この関数は継承先で定義
     * 
     * @param file ファイル
     * @param pretty 整形
     */
    public boolean save(File file, boolean pretty) {
        return false;
    }
}
