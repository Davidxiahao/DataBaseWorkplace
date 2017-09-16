package com.hankz.util.dbutil;

public class OriginModel {
    public final String apk;
    public final String unit;
    public final String lib;
    public final String webOrigins;
    public final String codeOrigins;

    public OriginModel(String apk, String unit, String lib, String webOrigins, String codeOrigins){
        this.apk = apk;
        this.unit = unit;
        this.lib = lib;
        this.webOrigins = webOrigins;
        this.codeOrigins = codeOrigins;
    }

    public String getApk() {
        return apk;
    }

    public String getUnit() {
        return unit;
    }

    public String getLib() {
        return lib;
    }

    public String getWebOrigins() {
        return webOrigins;
    }

    public String getCodeOrigins() {
        return codeOrigins;
    }
}