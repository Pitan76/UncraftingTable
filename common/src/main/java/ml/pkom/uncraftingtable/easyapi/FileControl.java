package ml.pkom.uncraftingtable.easyapi;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileControl {
    
    public static boolean fileWriteContents(String pathName, String contents){ return fileWriteContents(new File(pathName), contents); }

    public static String fileReadContents(String pathName) { return fileReadContents(new File(pathName)); }

    public static boolean fileCopy(String inPathName, String outPathName) { return fileCopy(new File(inPathName), new File(outPathName)); }

    public static boolean fileExists(String pathName) { return fileExists(new File(pathName)); }

    public static long getFileTime(String pathName) { return getFileTime(new File(pathName)); }

    public static boolean setFileTime(String pathName, long time) { return setFileTime(new File(pathName), time); }

    public static boolean fileRename(String pathName, String renamedPathName) { return fileRename(new File(pathName), new File(renamedPathName)); }

    public static String basename(String pathName) { return basename(new File(pathName)); }

    public static String dirname(String pathName) { return dirname(new File(pathName)); }

    public static String dirname(String pathName, int levels) { return dirname(new File(pathName), levels); }

    /**
     * ファイルにデータを書き込みます。
     * 失敗した場合falseを返します。
     * 
     * @param file   ファイル
     * @param contents データ
     */
    public static boolean fileWriteContents(File file, String contents) {
        try {
            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")));
            writer.println(contents);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ファイルからデータを読み込みます。
     * 失敗した場合nullを返します。
     * 
     * @param file   ファイル
     * @return ファイルのデータ or null
     */
    public static String fileReadContents(File file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = "";
            String contents = "";
            while ((line = reader.readLine()) != null) {
                contents += line + "\n";
            }
            reader.close();
            return contents;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ファイルからデータを読み込みます。 
     * 失敗した場合falseを返します。
     * 
     * @param inFile  コピー元ファイル
     * @param outFile コピー先ファイル
     */
    public static boolean fileCopy(File inFile, File outFile) {
        try {
            FileChannel inCh, outCh = null;
            FileInputStream inStream = new FileInputStream(inFile);
            FileOutputStream outStream = new FileOutputStream(outFile);
            inCh = inStream.getChannel();
            outCh = outStream.getChannel();
            outCh.transferFrom(inCh, 0, inCh.size());
            inCh.close();
            outCh.close();
            inStream.close();
            outStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * File.exists()のエイリアス関数です。
     * 
     * @param file 確認するファイル
     */
    public static boolean fileExists(File file) {
        return file.exists();
    }

    /**
     * File.lastModified()のエイリアス関数です。
     * 
     * @param file 更新日時を取得するファイル
     * @return ファイルの更新日時
     */
    public static long getFileTime(File file) {
        return file.lastModified();
    }


    /**
     * File.setLastModified()のエイリアス関数です。
     * 
     * @param file 日時変更されるファイル
     * @param time 変更後の更新日時
     */
    public static boolean setFileTime(File file, long time) {
        return file.setLastModified(time);
    }

    /**
     * File.renameTo()のエイリアス関数です。
     * 
     * @param file        名前変更されるファイル
     * @param renamedFile 名前変更後のファイル
     */
    public static boolean fileRename(File file, File renamedFile) {
        return file.renameTo(renamedFile);
    }

    /**
     * StringからFileへ変換します。
     * 
     * @param pathName ファイルのパス
     * @return File
     */
    public static File stringToFile(String pathName) {
        return new File(pathName);
    }

    public static String basename(File file) {
        return file.getName();
    }

    public static String dirname(File file) {
        return file.getParent();
    }

    public static String dirname(File file, int levels) {
        for (int i = 0; i < levels; i++) {
            file = dirfile(file);
        }
        return file.toString();
    }

    public static File dirfile(File file) {
        return file.getParentFile();
    }

    public static File dirfile(File file, int count) {
        for (int i = 0; i > count; i++) {
            file = dirfile(file);
        }
        return file;
    }

}
