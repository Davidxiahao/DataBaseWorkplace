package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.OriginInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OriginDbService {
    private final DbHelper dbHelper;
    static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/originchecker?user=originchecker&password=originchecker";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private boolean CACHE_SWITCH = false;
    private List<OriginInfo> cacheList;

    public OriginDbService(boolean cacheSwitch) {
        this.CACHE_SWITCH = cacheSwitch;
        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
        if (cacheSwitch == true) loadAllData(false);
    }

    public OriginDbService() {
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
                cacheList.add(new OriginInfo(rs.getString("apk"),
                                             rs.getString("unit"),
                                             rs.getString("lib"),
                                             rs.getString("webOrigins"),
                                             rs.getString("codeOrigins")));
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
     * get from origins
     * @return
     */

    public List<String> getApkList(){
        if (CACHE_SWITCH){
            List<String> cacheResult = cacheList.stream().map(OriginInfo::getApk).distinct().
                    collect(Collectors.toList());
            return cacheResult;
        }else {
            String sql = "select apk from origins group by apk";
            List<String> result = new ArrayList<>();
            dbHelper.doQuery(sql, rs -> {
                while (rs.next()) {
                    result.add(rs.getString("apk"));
                }
            });
            return result;
        }
    }

    public List<String> getApkLibs(String apkname) {
        if (CACHE_SWITCH) {
            List<String> cacheResult = cacheList.stream().filter(c -> c.apk == apkname).map(OriginInfo::getApk).
                    collect(Collectors.toList());
            return cacheResult;
        } else {
            String sql = "select lib from origins where apk=?";
            List<String> result = new ArrayList<>();
            dbHelper.doQuery(sql,
                    ps -> ps.setString(1, apkname),
                    rs -> {
                        while (rs.next()) {
                            result.add(rs.getString("lib"));
                        }
                    });
            return result;
        }
    }
}
