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
        String sql = "insert into libs values (?, ?, ?)";
        return dbHelper.doUpdate(sql, ps -> {
            ps.setString(1, libraryInfo.getLib());
            ps.setString(2, libraryInfo.getFingerprint());
            ps.setString(3, libraryInfo.getConstants());
        });
    }

    public  boolean insertLibraryInfoList(List<LibraryInfo> list){
        String sql = "insert into libs values (?, ?, ?)";
        return dbHelper.doBatchUpdate(sql, ps -> {
            for(int i=0; i<list.size(); i++) {
                LibraryInfo libraryInfo = list.get(i);
                ps.setString(1, libraryInfo.getLib());
                ps.setString(2, libraryInfo.getFingerprint());
                ps.setString(3, libraryInfo.getConstants());
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
            ps.setString(1, originInfo.getApk());
            ps.setString(2, originInfo.getUnit());
            ps.setString(3, originInfo.getLib());
            ps.setString(4, originInfo.getWebOrigins());
            ps.setString(5, originInfo.getCodeOrigins());
        });
    }

    public boolean insertOriginInfoList(List<OriginInfo> list){
        String sql = "insert into origins values (?, ?, ?, ?, ?)";
        return dbHelper.doBatchUpdate(sql, ps -> {
            for(int i=0; i<list.size(); i++){
                OriginInfo originInfo = list.get(i);
                ps.setString(1, originInfo.getApk());
                ps.setString(2, originInfo.getUnit());
                ps.setString(3, originInfo.getLib());
                ps.setString(4, originInfo.getWebOrigins());
                ps.setString(5, originInfo.getCodeOrigins());
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
        final String[] result = new String[1];
        dbHelper.doQuery(sql,
                ps -> ps.setString(1, libraryName),
                rs -> result[0] = rs.getString("fingerprint")
                );
        return result[0];
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
