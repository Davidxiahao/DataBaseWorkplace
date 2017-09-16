package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.OriginModel;

import java.util.List;

public class ResultDbService {
    private final DbHelper dbHelper;
    static final String dburl = "jdbc:sqlite:C:\\Users\\xiahao\\Desktop\\result.db";
    static final String JDBC_DRIVER = "org.sqlite.JDBC";
    public String table = "access_token";

    private static ResultDbService ourInstance = new ResultDbService();

    public static ResultDbService getInstance(){return ourInstance;}

    private ResultDbService(){
        dbHelper = new DbHelper(JDBC_DRIVER, dburl);
    }

    public void createTable(){
        String sql =    "create table " + table +
                        "(apk           text," +
                        "unit           text," +
                        "lib            text," +
                        "webOrigins     text," +
                        "codeOrigins    text)";

        dbHelper.doUpdate(sql);
    }

    public void insertOriginInfoList(List<OriginModel> list){
        String sql = "insert into " + table + " (apk, unit, lib, webOrigins, codeOrigins) values (?, ?, ?, ?, ?)";
        for (OriginModel originModel : list) {
            dbHelper.doUpdate(sql, ps -> {
                ps.setString(1, originModel.apk);
                ps.setString(2, originModel.unit);
                ps.setString(3, originModel.lib);
                ps.setString(4, originModel.webOrigins);
                ps.setString(5, originModel.codeOrigins);
            });
        }
    }
}
