package cn.longtianxiang.app.bilibilidownloadmanager.da;

import cn.longtianxiang.app.bilibilidownloadmanager.value.Constant;
import cn.longtianxiang.app.bilibilidownloadmanager.value.Dynamic;

import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by 龍天翔 on 2023/1/24 at 6:21 PM.
 */
public class AppProperties {
    private static final Properties PROPERTIES = new Properties();

    public static class Key{
        public static final String COOKIE_SESSDATA = "cookie_sessdata";
        public static final String POSITION_1 = "position_1";
        public static final String POSITION_2 = "position_2";
        public static final String POSITION_3 = "position_3";
        public static final String POSITION_4 = "position_4";
        public static final String POSITION_5 = "position_5";
        public static final String POSITION_6 = "position_6";
        public static final String POSITION_7 = "position_7";
        public static final String POSITION_8 = "position_8";
        public static final String POSITION_9 = "position_9";
        public static final String POSITION_10 = "position_10";
    }

    public static boolean load(){
        try {
            File file = new File(Dynamic.APP_PROPERTIES_FILE_PATH);
            file.createNewFile();
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            PROPERTIES.load(in);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean set(String key, String value){
        try(OutputStream output= new FileOutputStream(Dynamic.APP_PROPERTIES_FILE_PATH)) {
            PROPERTIES.setProperty(key, value);
            PROPERTIES.store(output, new Date().toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object get(String key){
        return PROPERTIES.get(key);
    }

}
