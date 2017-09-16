package fdu.lab310.lib;

import com.xiahao.lib.CheckXSOP;
import fdu.lab310.lib.analysis.ApkAnalysis;

/**
 * Created by HankZhang on 2017/8/7.
 */
public class Main {
    public static void main(String[] args) {
//        String webOrigins = "www.realmoo.com";
//        String packName = "com.atukakati.apps.photospace;";
        String webOrigins = "https://getpocket.com/ff_mobile_signin";
        String packName = "com.pocket.app.App;";

        if (CheckXSOP.isXSOP(webOrigins, packName)){
            System.out.println("\"" + webOrigins + "\" and \"" + packName + "\" is XSOP");
        }
        else {
            System.out.println("Same Strings are:");
            for (String string : CheckXSOP.sameString){
                System.out.println(string);
            }
        }
    }
}
