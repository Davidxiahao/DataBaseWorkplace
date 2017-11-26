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

    public double getSimilarityFromggsearch_copy(String mainwords, String urls){
        String sql = "select similarity from ggsearch_copy where mainwords=\""+mainwords+"\" and urls=\""+urls+"\"";
        List<Double> result = new ArrayList<>();

        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                result.add(new Double(rs.getDouble("similarity")));
            }
        });

        if (!result.isEmpty()) return result.get(0);
        else return 0.0;
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
        String sql = "select * from ggsearch_full3";
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

    public List<ggsearchModel> getAllDataFromggsearch_copy(){
        String sql = "select * from ggsearch_copy2";
        List<ggsearchModel> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                ggsearchModel temp = new ggsearchModel(rs.getInt("idx"),
                        rs.getString("mainwords"),
                        rs.getString("urls"),
                        rs.getString("mainwordsnippet"),
                        rs.getString("urlssnippet"),
                        rs.getString("urlssnippetfull"));
                temp.similarity = rs.getDouble("similarity");
                result.add(temp);
            }
        });
        return result;
    }

    public void insertIntoggsearch_copy(List<ggsearchModel> list){
        String sql = "insert into ggsearch_copy2 (idx, mainwords, urls, mainwordsnippet, urlssnippet, urlssnippetfull, " +
                "allmainwords, allurls) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (ggsearchModel line : list){
                ps.setInt(1, line.idx);
                ps.setString(2, line.mainwords);
                ps.setString(3, line.urls);
                ps.setString(4, line.mainwordsnippet);
                ps.setString(5, line.urlssnippet);
                ps.setString(6, line.urlssnippetfull);
                ps.setString(7, line.allmainwords);
                ps.setString(8, line.allurls);
                ps.addBatch();
            }
        });
    }

    public void updateggsearchOnSimilarity(List<ggsearchModel> list){
        String sql = "update ggsearch set similarity=? where idx=?";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (ggsearchModel line : list){
                ps.setDouble(1, line.similarity);
                ps.setInt(2, line.idx);
                ps.addBatch();
            }
        });
    }

    public void updateggsearch_copyOnSimilarity(List<ggsearchModel> list){
        String sql = "update ggsearch_copy2 set similarity=? where idx=?";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (ggsearchModel line : list){
                ps.setDouble(1, line.similarity);
                ps.setInt(2, line.idx);
                ps.addBatch();
            }
        });
    }
}
