package com.hankz.util.dbutil;

public class LibraryInfo {
    private String lib;
    private String fingerprint;
    private String constants;

    public LibraryInfo(){}

    public LibraryInfo(String lib, String fingerprint, String constants){
        this.lib = lib;
        this.fingerprint = fingerprint;
        this.constants = constants;
    }

    public void setLib(String lib){
        this.lib = lib;
    }

    public void setFingerprint(String fingerprint){
        this.fingerprint = fingerprint;
    }

    public void setConstants(String constantsname){
        this.constants = constants;
    }

    public String getLib() {
        return lib;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getConstants() {
        return constants;
    }
}
