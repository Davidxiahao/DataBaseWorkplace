package com.hankz.util.dbutil;

public class ResultModel {
    public final String apk;
    public final String sameString;

    public ResultModel(String apk, String sameString){
        this.apk = apk;
        this.sameString = sameString;
    }

    public String getApk() {
        return apk;
    }

    public String getSameString() {
        return sameString;
    }
}
