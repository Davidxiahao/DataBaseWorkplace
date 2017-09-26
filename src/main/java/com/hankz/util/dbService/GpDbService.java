package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.GooglePlayModel;
import com.hankz.util.dbutil.OriginModel;

import java.util.ArrayList;
import java.util.List;

public class GpDbService {
    private final DbHelper dbHelper;
    static final String dburl = "jdbc:sqlite:C:\\Users\\xiahao\\Desktop\\gp.db";
    static final String JDBC_DRIVER = "org.sqlite.JDBC";
    public String table = "gp";

    private static GpDbService ourInstance = new GpDbService();

    public static GpDbService getInstance(){return ourInstance;}

    private GpDbService(){
        dbHelper = new DbHelper(JDBC_DRIVER, dburl);
    }

    public List<GooglePlayModel> getAllRecords(){
        List<GooglePlayModel> result = new ArrayList<>();
        String sql = "SELECT * FROM " + table;

        dbHelper.doQuery(sql, rs -> {
            while (rs.next()) {
                result.add(new GooglePlayModel(rs.getString("category"),
                        rs.getString("update_time"),
                        rs.getString("name"),
                        rs.getString("rate"),
                        rs.getString("score"),
                        rs.getString("pkg_name"),
                        rs.getString("requires_android"),
                        rs.getString("current_version"),
                        rs.getString("developers"),
                        rs.getString("numDownloads")));
            }
        });

        return result;
    }
}
