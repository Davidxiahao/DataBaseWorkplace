package com.hankz.util.dbutil;

import java.util.ArrayList;
import java.util.List;

public class ExtendOriginModel {
    public final OriginModel information;
    public final List<String> webMainName;
    public final List<String> codeMainName;
    public int isXSOP = 0;
    public String codeOrigin;

    public ExtendOriginModel(OriginModel information){
        this.information = information;
        this.webMainName = new ArrayList<>();
        this.codeMainName = new ArrayList<>();
    }
}
