package com.hankz.util.dbutil;

public class OriginInfo {
    public final String apk;
    public final String unit;
    public final String lib;
    public final String webOrigins;
    public final String codeOrigins;

    public OriginInfo(String apk, String unit, String lib, String webOrigins, String codeOrigins){
        this.apk = apk;
        this.unit = unit;
        this.lib = lib;
        this.webOrigins = webOrigins;
        this.codeOrigins = codeOrigins;
    }
}
