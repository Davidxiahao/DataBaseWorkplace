package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.LibraryInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibsDbService {
    private final DbHelper dbHelper;
    static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/originchecker?" +
            "user=originchecker&password=originchecker";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private boolean CACHE_SWITCH = false;
    private List<LibraryInfo> cacheList;

    public LibsDbService(boolean cacheSwitch) {
        this.CACHE_SWITCH = cacheSwitch;
        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
        if (cacheSwitch == true) loadAllData(false);
    }

    public LibsDbService() {
        this(false);
    }

    public void setCacheSwitch(boolean cacheSwitch) {
        this.CACHE_SWITCH = cacheSwitch;
        if(cacheSwitch == true) loadAllData(false);
    }

    public void loadAllData(boolean forceReload){
        if(cacheList != null && cacheList.size() > 0){
            if(!forceReload)
                return;
        }
        cacheList = new ArrayList<>();
        String sql = "SELECT * FROM libs";
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()) {
                cacheList.add(new LibraryInfo(rs.getString("lib"),
                                              rs.getString("fingerprint"),
                                              rs.getString("constants"))
                );
            }
        });
    }

    /**
     * insert into libs
     * @param libraryInfo
     * @return
     */

    public boolean insertLibraryInfo(LibraryInfo libraryInfo){
        String sql = "insert into libs (lib, fingerprint, constants) values (?, ?, ?)";
        return dbHelper.doUpdate(sql, ps -> {
            ps.setString(1, libraryInfo.lib);
            ps.setString(2, libraryInfo.fingerprint);
            ps.setString(3, libraryInfo.constants);
        });
    }

    public  boolean insertLibraryInfoList(List<LibraryInfo> list){
        String sql = "insert into libs values (?, ?, ?)";
        return dbHelper.doBatchUpdate(sql, ps -> {
            for(int i=0; i<list.size(); i++) {
                LibraryInfo libraryInfo = list.get(i);
                ps.setString(1, libraryInfo.lib);
                ps.setString(2, libraryInfo.fingerprint);
                ps.setString(3, libraryInfo.constants);
            }
        });
    }

    /**
     * get from libs
     * @return
     */

    public List<String> getLibraryList(){
        if (CACHE_SWITCH){
            List<String> cacheResult = cacheList.stream().map(LibraryInfo::getLib).distinct().
                    collect(Collectors.toList());
            return cacheResult;
        }else {
            String sql = "select lib from libs group by lib";
            List<String> result = new ArrayList<>();
            dbHelper.doQuery(sql, rs -> {
                while (rs.next()) {
                    result.add(rs.getString("lib"));
                }
            });
            return result;
        }
    }

    public String getLibraryFingerprint(String libraryName){
        if (CACHE_SWITCH){
            List<String> cacheResult = cacheList.stream().filter(c -> c.lib == libraryName).
                    map(LibraryInfo::getFingerprint).collect(Collectors.toList());
            if (cacheResult.isEmpty()) return null;
            return cacheResult.get(0);
        }else {
            String sql = "select fingerprint from libs where lib=?";
            List<String> result = new ArrayList<>();
            dbHelper.doQuery(sql,
                    ps -> ps.setString(1, libraryName),
                    rs -> {
                        while (rs.next()) {
                            result.add(rs.getString("fingerprint"));
                        }
                    });
            if (result.isEmpty()) return null;
            return result.get(0);
        }
    }

    public String getLibraryConstants(String libraryName){
        if (CACHE_SWITCH){
            List<String> cacheResult = cacheList.stream().filter(c -> c.lib == libraryName).
                    map(LibraryInfo::getConstants).collect(Collectors.toList());
            if (cacheResult.isEmpty()) return null;
            return cacheResult.get(0);
        }else {
            String sql = "select constants from libs where lib=?";
            List<String> result = new ArrayList<>();
            dbHelper.doQuery(sql,
                    ps -> ps.setString(1, libraryName),
                    rs -> {
                        while (rs.next()) {
                            result.add(rs.getString("constants"));
                        }
                    });
            if (result.isEmpty()) return null;
            return result.get(0);
        }
    }

    public List<String> searchLibsByFingerprint(String fingerprint){
        if (CACHE_SWITCH){
            List<String> cacheResult = cacheList.stream().filter(c -> c.fingerprint == fingerprint).
                    map(LibraryInfo::getLib).collect(Collectors.toList());
            return cacheResult;
        }else {
            String sql = "select lib from libs where fingerprint=?";
            List<String> result = new ArrayList<>();
            dbHelper.doQuery(sql,
                    ps -> ps.setString(1, fingerprint),
                    rs -> {
                        while (rs.next()) {
                            result.add(rs.getString("lib"));
                        }
                    });
            return result;
        }
    }
}
