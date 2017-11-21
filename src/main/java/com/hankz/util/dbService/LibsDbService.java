package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.FinalOriginYYBModel;
import com.hankz.util.dbutil.LibraryInfo;

import java.util.*;
import java.util.stream.Collectors;

public class LibsDbService {
    private final DbHelper dbHelper;
    private static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/originchecker?" +
            "user=originchecker&password=originchecker";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private boolean CACHE_SWITCH = false;
    private List<LibraryInfo> cacheList;

    private static LibsDbService ourInstance = new LibsDbService();

    public static LibsDbService getInstance(){return ourInstance;}

    private LibsDbService(boolean cacheSwitch) {
        this.CACHE_SWITCH = cacheSwitch;
        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
        if (cacheSwitch) loadAllData(false);
    }

    public LibsDbService() {
        this(false);
    }

    public void setCacheSwitch(boolean cacheSwitch) {
        this.CACHE_SWITCH = cacheSwitch;
        if(cacheSwitch) loadAllData(false);
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
                                              rs.getString("liborigins"),
                                              rs.getInt("manual"))
                );
            }
        });
    }

    public List<FinalOriginYYBModel> cacheListOfyyb0818_13w;

    public void loadAllDataFromyyb0818_13w(){
        cacheListOfyyb0818_13w = new ArrayList<>();
        String sql = "select * from final_origin_yyb0818_13w";
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                cacheListOfyyb0818_13w.add(new FinalOriginYYBModel( rs.getInt("idx"),
                                                                    rs.getString("apk"),
                                                                    rs.getString("unit"),
                                                                    rs.getString("declaringClass"),
                                                                    rs.getString("webOrigins"),
                                                                    rs.getString("codeOrigins"),
                                                                    rs.getString("webHelpInfo"),
                                                                    rs.getString("codeHelpInfo")
                ));
            }
        });
    }

    public boolean insertFinalgpTop540(List<FinalOriginYYBModel> list){
        String sql = "insert into final_origin_gp_top540 (idx, apk, pkgname, unit, declaringClass, webOrigins, " +
                "codeOrigins, webHelpInfo, codeHelpInfo) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return dbHelper.doBatchUpdate(sql, ps -> {
            for (FinalOriginYYBModel line : list){
                ps.setInt(1, line.idx);
                ps.setString(2, line.apk);
                ps.setString(3, line.pkgname);
                ps.setString(4, line.unit);
                ps.setString(5, line.declaringClass);
                ps.setString(6, line.webOrigins);
                ps.setString(7, line.codeOrigins);
                ps.setString(8, line.webHelpInfo);
                ps.setString(9, line.codeHelpInfo);
                ps.addBatch();
            }
        });
    }

    public boolean updateFinalgpTop540(Map<Integer, String> map){
        String sql = "update final_origin_gp_top540 set pkgname=? where idx=?";
        return dbHelper.doBatchUpdate(sql, ps -> {
            for (Map.Entry<Integer, String> entry : map.entrySet()){
                ps.setString(1, entry.getValue());
                ps.setInt(2, entry.getKey());
                ps.addBatch();
            }
        });
    }

    public void deleteFinalgpTop540(List<FinalOriginYYBModel> list){
        String sql = "delete from final_origin_yyb0818_13w where idx=?";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (FinalOriginYYBModel line : list){
                ps.setInt(1, line.idx);
                ps.addBatch();
            }
        });
    }

    public Map<String, FinalOriginYYBModel> apphashTocodeOrigins = new HashMap<>();

    public void buildHashMap(){
        for (FinalOriginYYBModel line : cacheListOfyyb0818_13w){
            apphashTocodeOrigins.put(line.apk.split("\\.")[0], line);
        }
    }

    /**
     * insert into libs
     * @param libraryInfo
     * @return
     */

    public boolean insertLibraryInfo(LibraryInfo libraryInfo){
        String sql = "insert into libs (lib, fingerprint, liborigins, manual) values (?, ?, ?, ?)";
        return dbHelper.doUpdate(sql, ps -> {
            ps.setString(1, libraryInfo.lib);
            ps.setString(2, libraryInfo.fingerprint);
            ps.setString(3, libraryInfo.liborigins);
            ps.setInt(4, libraryInfo.manual);
        });
    }

    public  boolean insertLibraryInfoList(List<LibraryInfo> list){
        String sql = "insert into libs (lib, fingerprint, liborigins, manual) values (?, ?, ?, ?)";
        return dbHelper.doBatchUpdate(sql, ps -> {
            for (LibraryInfo libInfo : list) {
                ps.setString(1, libInfo.lib);
                ps.setString(2, libInfo.fingerprint);
                ps.setString(3, libInfo.liborigins);
                ps.setInt(4, libInfo.manual);
            }
        });
    }

    /**
     * get from libs
     * @return
     */

    public List<String> getLibraryList(){
        if (CACHE_SWITCH){
            return cacheList.stream().map(LibraryInfo::getLib).distinct().
                    collect(Collectors.toList());
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
            List<String> cacheResult = cacheList.stream().filter(c -> c.lib.equals(libraryName)).
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

    public String getLibraryOrigins(String libraryName){
        if (CACHE_SWITCH){
            List<String> cacheResult = cacheList.stream().filter(c -> c.lib.equals(libraryName)).
                    map(LibraryInfo::getLiborigins).collect(Collectors.toList());
            if (cacheResult.isEmpty()) return null;
            return cacheResult.get(0);
        }else {
            String sql = "select liborigins from libs where lib=?";
            List<String> result = new ArrayList<>();
            dbHelper.doQuery(sql,
                    ps -> ps.setString(1, libraryName),
                    rs -> {
                        while (rs.next()) {
                            result.add(rs.getString("liborigins"));
                        }
                    });
            if (result.isEmpty()) return null;
            return result.get(0);
        }
    }

    public List<String> searchLibsByFingerprint(String fingerprint){
        if (CACHE_SWITCH){
            return cacheList.stream().filter(c -> c.fingerprint.equals(fingerprint)).
                    map(LibraryInfo::getLib).collect(Collectors.toList());
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
