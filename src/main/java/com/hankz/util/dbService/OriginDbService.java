package com.hankz.util.dbService;

import com.hankz.util.dbutil.*;
import com.xiahao.lib.DCInformation;

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
                    OriginModel temp = new OriginModel(rs.getString("apk"),
                            rs.getString("unit"),
                            rs.getString("declaringClass"),
                            rs.getString("webOrigins"),
                            rs.getString("codeOrigins"),
                            rs.getString("webHelpInfo"),
                            rs.getString("codeHelpInfo"));
                    temp.idx = rs.getInt("idx");
                    temp.libNum = rs.getInt("libNum");
                    if (table.equals("last_origin_gp8w_meaningful_copy")) {
                        temp.similarity = rs.getDouble("similarity");
                        temp.keyWord = rs.getString("keyword");
                    }
                    result.add(temp);
                }
            });
            return result;
    }

    public List<LibraryHashModel> getAllDataFromLastLibraryGp8wHashCopy3(){
        String sql = "select * from last_library_gp8w_hash";
        List<LibraryHashModel> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                LibraryHashModel temp = new LibraryHashModel(rs.getInt("idx"),
                        rs.getString("api"),
                        rs.getString("webOrigins"),
                        rs.getString("declareClass"),
                        rs.getInt("count"));
                result.add(temp);
            }
        });

        return result;
    }

    public List<OriginModel> getAllDataFromLastParsedOrigin(){
        String sql = "select * from last_parsed_origin";
        List<OriginModel> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                OriginModel temp = new OriginModel(rs.getString("apk"),
                        rs.getString("unit"),
                        "",
                        rs.getString("webOrigins"),
                        rs.getString("codeOrigins"),
                        rs.getString("webHelpInfo"),
                        rs.getString("codeHelpInfo"));
                temp.idx = rs.getInt("idx");
                temp.isXSOP = rs.getInt("isXSOP");
                result.add(temp);
            }
        });

        return result;
    }

    public List<ggsearchModel> getAllDataFromggsearch(){
        String sql = "select * from ggsearch_apkname_keywords";
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
        String sql = "select * from ggsearch_copy";
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

    public List<ggsearchModel> getAllDataFromggsearch_full(){
        String sql = "select * from ggsearch_full3";
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

    public List<DCInformationModel> getAllDCInformationData(String table){
        String sql = "select * from " + table;
        List<DCInformationModel> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                DCInformationModel temp = new DCInformationModel(rs.getString("DC"),
                        rs.getString("mainwords"),
                        rs.getInt("total_frequence"),
                        rs.getInt("different_APK_frequence"),
                        rs.getString("APKs"),
                        rs.getString("URLs"));
                temp.model_choice = rs.getInt("model_choice");
                temp.mainwordsnippet = rs.getString("mainwordsnippet");
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

    public void insertIntoggsearch_wait(List<ggsearchModel> list){
        String sql = "insert into ggsearch_wait (idx, mainwords, urls, mainwordsnippet, urlssnippet, urlssnippetfull, " +
                "similarity) values (?, ?, ?, ?, ?, ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (ggsearchModel line : list){
                ps.setInt(1, line.idx);
                ps.setString(2, line.mainwords);
                ps.setString(3, line.urls);
                ps.setString(4, line.mainwordsnippet);
                ps.setString(5, line.urlssnippet);
                ps.setString(6, line.urlssnippetfull);
                ps.setDouble(7, line.similarity);
                ps.addBatch();
            }
        });
    }

    public void insertIntoggsearch_full(List<ggsearchModel> list){
        String sql = "insert into ggsearch_full (idx, mainwords, urls, mainwordsnippet, urlssnippet, urlssnippetfull, " +
                "similarity) values (?, ?, ?, ?, ?, ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (ggsearchModel line : list){
                ps.setInt(1, line.idx);
                ps.setString(2, line.mainwords);
                ps.setString(3, line.urls);
                ps.setString(4, line.mainwordsnippet);
                ps.setString(5, line.urlssnippet);
                ps.setString(6, line.urlssnippetfull);
                ps.setDouble(7, line.similarity);
                ps.addBatch();
            }
        });
    }

    public void insertIntoggsearch_full3(List<ggsearchModel> list){
        String sql = "insert into ggsearch_full3 (idx, mainwords, urls, mainwordsnippet, urlssnippet, urlssnippetfull, " +
                "similarity) values (?, ?, ?, ?, ?, ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (ggsearchModel line : list){
                ps.setInt(1, line.idx);
                ps.setString(2, line.mainwords);
                ps.setString(3, line.urls);
                ps.setString(4, line.mainwordsnippet);
                ps.setString(5, line.urlssnippet);
                ps.setString(6, line.urlssnippetfull);
                ps.setDouble(7, line.similarity);
                ps.addBatch();
            }
        });
    }

    public void insertDCInformation(List<DCInformationModel> list){
        String sql = "insert into DCInformation (DC, mainwords, total_frequence, different_APK_frequence, APKs," +
                " URLs) values (?, ?, ?, ?, ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (DCInformationModel line : list){
                ps.setString(1, line.DC);
                ps.setString(2, line.mainwords);
                ps.setInt(3, line.total_frequence);
                ps.setInt(4, line.different_APK_frequence);
                ps.setString(5, line.APKs);
                ps.setString(6, line.URLs);
                ps.addBatch();
            }
        });
    }

    public void updateggsearchfullOnUrl(List<ggsearchModel> list){
        String sql = "update ggsearch_full3 set urls=? where idx=?";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (ggsearchModel line : list){
                ps.setString(1, line.urls);
                ps.setInt(2, line.idx);
                ps.addBatch();
            }
        });
    }

    public void updateDCInformation(List<DCInformationModel> list){
        String sql = "update DCInformation set model_choice=? where DC=?";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (DCInformationModel line : list){
                ps.setInt(1, line.model_choice);
                ps.setString(2, line.DC);
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

    public void insertfinal_origin_gp8w_meaningful_copy(List<OriginModel> list){
        String sql = "insert into last_origin_gp8w_meaningful_copy (idx, libNum, apk, unit, declaringClass, " +
                "webOrigins, keyword, similarity, codeOrigins, webHelpInfo, codeHelpInfo) values " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        dbHelper.doBatchUpdate(sql, ps -> {
            for (OriginModel line : list){
                ps.setInt(1, line.idx);
                ps.setInt(2, line.libNum);
                ps.setString(3, line.apk);
                ps.setString(4, line.unit);
                ps.setString(5, line.declaringClass);
                ps.setString(6, line.webOrigins);
                ps.setString(7, line.keyWord);
                ps.setDouble(8, line.similarity);
                ps.setString(9, line.codeOrigins);
                ps.setString(10, line.webHelpInfo);
                ps.setString(11, line.codeHelpInfo);
                ps.addBatch();
            }
        });
    }
}
