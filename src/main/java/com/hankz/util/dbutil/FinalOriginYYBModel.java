package com.hankz.util.dbutil;

public class FinalOriginYYBModel {
    public final int idx;
    public final String apk;
    public final String unit;
    public final String declaringClass;
    public final String webOrigins;
    public final String codeOrigins;
    public final String webHelpInfo;
    public final String codeHelpInfo;

    public FinalOriginYYBModel(int idx, String apk, String unit, String declaringClass, String webOrigins,
                               String codeOrigins, String webHelpInfo, String codeHelpInfo){
        this.idx = idx;
        this.apk = apk;
        this.unit = unit;
        this.declaringClass = declaringClass;
        this.webOrigins = webOrigins;
        this.codeOrigins = codeOrigins;
        this.webHelpInfo = webHelpInfo;
        this.codeHelpInfo = codeHelpInfo;
    }
}
