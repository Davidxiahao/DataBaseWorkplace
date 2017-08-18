package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.OriginModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OriginDbService {

    private final DbHelper dbHelper;
    private static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/originchecker?" +
            "user=originchecker&password=originchecker";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private boolean CACHE_SWITCH = false;
    private List<OriginModel> cacheList;

    private static OriginDbService ourInstance = new OriginDbService();

    public static OriginDbService getInstance(){return ourInstance;}

    private OriginDbService(boolean cacheSwitch) {
        this.CACHE_SWITCH = cacheSwitch;
        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
        if (cacheSwitch) loadAllData(false);
    }

    private OriginDbService() {
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
        String sql = "SELECT * FROM origins";
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()) {
                cacheList.add(new OriginModel(rs.getString("apk"),
                                             rs.getString("unit"),
                                             rs.getString("lib"),
                                             rs.getString("webOrigins"),
                                             rs.getString("codeOrigins")));
            }
        });
    }

    /**
     * insert into origins
     * @param originModel
     * @return
     */

    public boolean insertOriginInfo(OriginModel originModel){
        String sql = "insert into origins (apk, unit, lib, webOrigins, codeOrigins) values (?, ?, ?, ?, ?)";
        return dbHelper.doUpdate(sql, ps -> {
            ps.setString(1, originModel.apk);
            ps.setString(2, originModel.unit);
            ps.setString(3, originModel.lib);
            ps.setString(4, originModel.webOrigins);
            ps.setString(5, originModel.codeOrigins);
        });
    }

    public boolean insertOriginInfoList(List<OriginModel> list){
        String sql = "insert into origins (apk, unit, lib, webOrigins, codeOrigins) values (?, ?, ?, ?, ?)";
        return dbHelper.doBatchUpdate(sql, ps -> {
            for (OriginModel originModel : list) {
                ps.setString(1, originModel.apk);
                ps.setString(2, originModel.unit);
                ps.setString(3, originModel.lib);
                ps.setString(4, originModel.webOrigins);
                ps.setString(5, originModel.codeOrigins);
            }
        });
    }

    /**
     * get from origins
     * @return
     */

    public List<String> getApkList(){
        if (CACHE_SWITCH){
            return cacheList.stream().map(OriginModel::getApk).distinct().
                    collect(Collectors.toList());
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

    public List<OriginModel> getAllData(){
        if (CACHE_SWITCH){
            return cacheList;
        }else {
            String sql = "select * from origins";
            List<OriginModel> result = new ArrayList<>();
            dbHelper.doQuery(sql,
                    rs -> {
                        while (rs.next()){
                            result.add(new OriginModel(rs.getString("apk"),
                                    rs.getString("unit"),
                                    rs.getString("lib"),
                                    rs.getString("webOrigins"),
                                    rs.getString("codeOrigins")));
                        }
                    });
            return result;
        }
    }

    public List<String> getApkLibs(String apkname) {
        if (CACHE_SWITCH) {
            return cacheList.stream().filter(c -> c.apk.equals(apkname)).map(OriginModel::getApk).
                    collect(Collectors.toList());
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
