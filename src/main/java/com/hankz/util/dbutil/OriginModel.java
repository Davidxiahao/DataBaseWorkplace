package com.hankz.util.dbutil;

import java.util.ArrayList;
import java.util.List;

public class OriginModel {
    public int idx;
    public int libNum;
    public final String apk;
    public final String unit;
    public  String declaringClass;
    public final String webOrigins;
    public final String codeOrigins;
    public final String webHelpInfo;
    public final String codeHelpInfo;
    public String keyWord;
    public List<String> keywords;
    public double similarity;
    public int isXSOP;
    public String PackageName;
    public String ApkName;
    public int groundtruth;

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
        this.keyWord = "";
        this.keywords = new ArrayList<>();
        this.similarity = 0.0;
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
