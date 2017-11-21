package com.hankz.util.dbutil;

public class OriginModel {
    public final String apk;
    public final String unit;
    public final String declaringClass;
    public final String webOrigins;
    public final String codeOrigins;
    public final String webHelpInfo;
    public final String codeHelpInfo;

    public OriginModel(String apk, String unit, String declaringClass,
                       String webOrigins, String codeOrigins, String webHelpInfo,
                       String codeHelpInfo){
        this.apk = apk;
        this.unit = unit;
        this.declaringClass = declaringClass;
        this.webOrigins = webOrigins;
        this.codeOrigins = codeOrigins;
        this.webHelpInfo = webHelpInfo;
        this.codeHelpInfo = codeHelpInfo;
    }

    public String getApk() {
        return apk;
    }

    public String getUnit() {
        return unit;
    }

    public String getWebOrigins() {
        return webOrigins;
    }

    public String getCodeOrigins() {
        return codeOrigins;
    }
}
