package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HankZhang on 2017/8/7.
 */
public class SampleDbService {
    private final DbHelper dbHelper;

    public SampleDbService() {
        String dbUrl = "jdbc:sqlite:apk_hierarchy.db";
        dbHelper = new DbHelper(dbUrl);
    }

    public SampleDbService(String dbUrl) {
        dbHelper = new DbHelper(dbUrl);
    }

    /**
     * only an example
     * @return
     */
    public List<String> getApkList(){
        String sql = "select apkname from apkinfo group by apkname;";
        List<String> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs ->{
            while (rs.next()) {
                result.add(rs.getString("apkname"));
            }
        });
        return result;
    }


    public List<String> getAllClassesForApk(String apkname){
        List<String> result = new ArrayList<>();

        String sql = "select classname from apkinfo WHERE apkname=?";
        dbHelper.doQuery(sql,
                ps -> ps.setString(1, apkname),
                rs -> {
                    while (rs.next()) {
                        result.add(rs.getString("classname"));
                    }
                });
        return result;
    }

}
