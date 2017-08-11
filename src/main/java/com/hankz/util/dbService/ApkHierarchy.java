package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class ApkHierarchy {
    private final DbHelper dbHelper;
    static final String dbUrl = "jdbc:sqlite:C:\\Users\\xiahao\\Desktop\\apk_hierarchy.db";
    static final String JDBC_DRIVER = "org.sqlite.JDBC";

    private List<String> cacheList;

    public ApkHierarchy(){
        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
    }

    public void loadAllData(){
        cacheList = new ArrayList<>();
        String sql = "select * from result";
        dbHelper.doQuery(sql,
                rs -> {
                    while (rs.next()){
                        cacheList.add(rs.getString("libname"));
                    }
                });
    }

    public List<String> getResultList(){
        return cacheList;
    }
}
