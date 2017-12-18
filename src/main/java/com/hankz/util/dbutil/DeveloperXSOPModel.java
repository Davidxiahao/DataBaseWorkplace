package com.hankz.util.dbutil;

public class DeveloperXSOPModel {
    public int idx;
    public String domain;
    public String developer;
    public int isXSOP;
    public int counts;

    public DeveloperXSOPModel(int idx, String domain, String developer, int isXSOP, int counts){
        this.idx = idx;
        this.domain = domain;
        this.developer = developer;
        this.isXSOP = isXSOP;
        this.counts = counts;
    }
}
