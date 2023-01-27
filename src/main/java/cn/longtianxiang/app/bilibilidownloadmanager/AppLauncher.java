package cn.longtianxiang.app.bilibilidownloadmanager;

import cn.longtianxiang.app.bilibilidownloadmanager.da.AppProperties;
import cn.longtianxiang.app.bilibilidownloadmanager.ui.MainUi;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * Created by 龍天翔 on 2023/1/21 at 5:01 PM.
 */
public class AppLauncher {


    public static void main(String[] args) {}

    static {
        AppProperties.load();
    }

    static {
        FlatLaf.setUseNativeWindowDecorations(false);
        FlatLightLaf.setup();
    }

    static {
        MainUi mainUi = MainUi.getInstance();
        mainUi.show();
        new Thread(mainUi::fetchAndShowLoginInfo).start();
    }

}
