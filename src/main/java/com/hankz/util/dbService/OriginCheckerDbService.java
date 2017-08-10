package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.LibraryInfo;
import com.hankz.util.dbutil.OriginInfo;

import java.util.ArrayList;
import java.util.List;

public class OriginCheckerDbService {
    private final DbHelper dbHelper;
    static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/originchecker?user=originchecker&password=originchecker";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public OriginCheckerDbService() {
        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
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
     * insert into origins
     * @param originInfo
     * @return
     */

    public boolean insertOriginInfo(OriginInfo originInfo){
        String sql = "insert into origins values (?, ?, ?, ?, ?)";
        return dbHelper.doUpdate(sql, ps -> {
            ps.setString(1, originInfo.apk);
            ps.setString(2, originInfo.unit);
            ps.setString(3, originInfo.lib);
            ps.setString(4, originInfo.webOrigins);
            ps.setString(5, originInfo.codeOrigins);
        });
    }

    public boolean insertOriginInfoList(List<OriginInfo> list){
        String sql = "insert into origins values (?, ?, ?, ?, ?)";
        return dbHelper.doBatchUpdate(sql, ps -> {
            for (OriginInfo originInfo : list) {
                ps.setString(1, originInfo.apk);
                ps.setString(2, originInfo.unit);
                ps.setString(3, originInfo.lib);
                ps.setString(4, originInfo.webOrigins);
                ps.setString(5, originInfo.codeOrigins);
            }
        });
    }

    /**
     * get from libs
     * @return
     */

    public List<String> getLibraryList(){
        String sql = "select lib from libs group by lib";
        List<String> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                result.add(rs.getString("lib"));
            }
        });
        return result;
    }

    public String getLibraryFingerprint(String libraryName){
        String sql = "select fingerprint from libs where lib=?";
        List<String> result = new ArrayList<>();
        dbHelper.doQuery(sql,
                ps -> ps.setString(1, libraryName),
                rs -> {
                    while (rs.next()) {
                        result.add(rs.getString("fingerprint"));
                    }
                    });
        return result.get(0);
    }

    public List<String> searchLibsByFingerprint(String fingerprint){
        String sql = "select lib from libs where fingerprint=?";
        List<String> result = new ArrayList<>();
        dbHelper.doQuery(sql,
                ps -> ps.setString(1, fingerprint),
                rs -> {
                    while (rs.next()){
                        result.add(rs.getString("lib"));
                    }
                });
        return result;
    }

    /**
     * get from origins
     * @return
     */

    public List<String> getApkList(){
        String sql = "select apk from origins group by apk";
        List<String> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                result.add(rs.getString("apk"));
            }
        });
        return result;
    }
}
