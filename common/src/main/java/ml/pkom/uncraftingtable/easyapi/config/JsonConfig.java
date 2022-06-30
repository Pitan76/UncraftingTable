package ml.pkom.uncraftingtable.easyapi.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ml.pkom.uncraftingtable.easyapi.FileControl;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public class JsonConfig extends Config {
    /**
     * 
     * @param file ファイル
     */
    public JsonConfig(File file) {
        super(file);
    }

    /**
     * 
     * @param file ファイル名
     */
    public JsonConfig(String file) {
        super(file);
    }

    public JsonConfig() {
        super();
    }

    /**
     * Config→JsonConfigへ変換
     * 
     * @param config Config
     */
    public JsonConfig(Config config) {
        super(config);
    }

    /**
     * Jsonファイルを読み込んでMapへ変換
     * 
     * @param file ファイル
     * @return 成功→true / 失敗→false
     */
    public boolean load(File file) {
        try {
            String configData = FileControl.fileReadContents(file);
            Gson gson = new Gson();
            Type jsonMap = new TypeToken<LinkedHashMap<String, Object>>() {
            }.getType();
            configMap = gson.fromJson(configData, jsonMap);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * MapをJsonへ変換し、Jsonファイルとして保存
     * 
     * @param file   ファイル
     * @param pretty 整形
     * @return 成功→true / 失敗→false
     */
    public boolean save(File file, boolean pretty) {
        try {
            String configData = this.toJson(pretty);
            FileControl.fileWriteContents(file, configData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * MapをJsonへ変換 失敗した場合はnullを返す
     * 
     * @param pretty 整形
     * @return Jsonの文字列
     */
    public String toJson(boolean pretty) {
        try {
            Gson gson = new Gson();
            if (pretty) {
                gson = new GsonBuilder().setPrettyPrinting().create();
            }
            String configData = gson.toJson(configMap);
            return configData;
        } catch (Exception e) {
            return null;
        }
    }
}
