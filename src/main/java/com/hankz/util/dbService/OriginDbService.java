package com.hankz.util.dbService;





import com.hankz.util.dbutil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class OriginDbService {

    private final DbHelper dbHelper;
    private static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/originchecker?" +
            "user=originchecker&password=originchecker";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static OriginDbService ourInstance = new OriginDbService();

    public static OriginDbService getInstance(){return ourInstance;}

    private OriginDbService() {
        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
    }

    public List<OriginModel> getAllDataFromTable(String table){
            String sql = "select * from " + table;
            List<OriginModel> result = new ArrayList<>();
            dbHelper.doQuery(sql, rs -> {
                while (rs.next()) {
                    result.add(new OriginModel(rs.getString("apk"),
                            rs.getString("unit"),
                            rs.getString("declaringClass"),
                            rs.getString("webOrigins"),
                            rs.getString("codeOrigins"),
                            rs.getString("webHelpInfo"),
                            rs.getString("codeHelpInfo")));
                }
            });
            return result;
    }

    public List<ggsearchModel> getAllDataFromggsearch(){
        String sql = "select * from ggsearch";
        List<ggsearchModel> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                result.add(new ggsearchModel(rs.getInt("idx"),
                        rs.getString("mainwords"),
                        rs.getString("urls"),
                        rs.getString("mainwordsnippet"),
                        rs.getString("urlssnippet"),
                        rs.getString("urlssnippetfull")));
            }
        });
        return result;
    }
}
