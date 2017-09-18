package com.hankz.util.dbutil;

public class ResultModel {
    public final String apk;
    public final String webOrigins;
    public final String packName;
    public final String sameString;

    public ResultModel(String apk, String webOrigins, String packName, String sameString){
        this.apk = apk;
        this.webOrigins = webOrigins;
        this.packName = packName;
        this.sameString = sameString;
    }

    public String getApk() {
        return apk;
    }

    public String getSameString() {
        return sameString;
    }
}
