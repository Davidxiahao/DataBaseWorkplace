package com.hankz.util.dbutil;

public class OriginInfo {
    private String apk;
    private String unit;
    private String lib;
    private String webOrigins;
    private String codeOrigins;

    public OriginInfo(){}

    public OriginInfo(String apk, String unit, String lib, String webOrigins, String codeOrigins){
        this.apk = apk;
        this.unit = unit;
        this.lib = lib;
        this.webOrigins = webOrigins;
        this.codeOrigins = codeOrigins;
    }

    public void setApk(String apk){
        this.apk = apk;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }

    public void setLib(String lib){
        this.lib = lib;
    }

    public void setWebOrigins(String webOrigins){
        this.webOrigins = webOrigins;
    }

    public void setCodeOrigins(String codeOrigins){
        this.codeOrigins = codeOrigins;
    }

    public String getLib() {
        return lib;
    }

    public String getApk() {
        return apk;
    }

    public String getCodeOrigins() {
        return codeOrigins;
    }

    public String getUnit() {
        return unit;
    }

    public String getWebOrigins() {
        return webOrigins;
    }
}
