package com.hankz.util.dbService;

import com.hankz.util.dbutil.*;
import com.xiahao.lib.DCInformationStructure;

import java.util.ArrayList;
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

    public void creatTableTest(){
        String sql =    "create table   test"  +
                        "(apk           text," +
                        "weborigins     text," +
                        "packagename    text," +
                        "samestring     text)";

        dbHelper.doUpdate(sql);
    }

    public void createTableURLInformation(){
        String sql =    "create table               URLInformation" +
                        "(url                       text," +
                        "mainwords                  text," +
                        "total_frequence            integer," +
                        "different_DC_frequence     integer," +
                        "different_APK_frequence    integer," +
                        "DCs                        text," +
                        "APKs                       text)";

        dbHelper.doUpdate(sql);
    }

    public void createTableDCInformation(){
        String sql =    "create table               DCInformation" +
                        "(DC                        text," +
                        "mainwords                  text," +
                        "total_frequence            integer," +
                        "different_APK_frequence    integer," +
                        "APKs                       text," +
                        "URLs                       text)";

        dbHelper.doUpdate(sql);
    }

    public void createTableDCInformationFeature(){
        String sql =    "create table               DCInformationFeature" +
                        "(DC                        text," +
                        "mainwords                  text," +
                        "numberOfWords              integer," +
                        "wordsSequence              text," +
                        "wordsValueSequence         text," +
                        "wordsLenSequence           text," +
                        "total_frequence            integer," +
                        "different_APK_frequence    integer," +
                        "APKs                       text," +
                        "URLs                       text)";

        dbHelper.doUpdate(sql);
    }

    public void insertDCInformationFeature(List<DCInformationFeatureModel> list){
        String sql = "insert into DCInformationFeature (DC, mainwords, numberOfWords, wordsSequence, wordsValueSequence, " +
                "wordsLenSequence, total_frequence, different_APK_frequence, APKs, URLs) values (?, ?, ?, ?, ?, ?, ?, ?" +
                ", ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (DCInformationFeatureModel line : list){
                ps.setString(1, line.DC);
                ps.setString(2, line.mainwords);
                ps.setInt(3, line.numberOfWords);
                ps.setString(4, line.wordsSequence);
                ps.setString(5, line.wordsValueSequence);
                ps.setString(6, line.wordsLenSequence);
                ps.setInt(7, line.total_frequence);
                ps.setInt(8, line.different_APK_frequence);
                ps.setString(9, line.APKs);
                ps.setString(10, line.URLs);
                ps.addBatch();
            }
        });
    }

    public void updateDCInformationFeatureOnChoice(List<DCInformationFeatureModel> list){
        String sql = "update DCInformationFeature set model_choice=? where DC=?";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (DCInformationFeatureModel line : list){
                ps.setInt(1, line.model_choice);
                ps.setString(2, line.DC);
                ps.addBatch();
            }
        });
    }

    public void insertURLInformation(List<URLInformationModel> list){
        String sql = "insert into URLInformation (url, mainwords, total_frequence, different_DC_frequence," +
                "different_APK_frequence, DCs, APKs) values (?, ?, ?, ?, ?, ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (URLInformationModel line : list){
                ps.setString(1, line.url);
                ps.setString(2, line.mainwords);
                ps.setInt(3, line.total_frequence);
                ps.setInt(4, line.different_DC_frequence);
                ps.setInt(5, line.different_APK_frequence);
                ps.setString(6, line.DCs);
                ps.setString(7, line.APKs);
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

    public List<DCInformationModel> getAllDCInformationData(String table){
        String sql = "select * from " + table;
        List<DCInformationModel> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                result.add(new DCInformationModel(rs.getString("DC"),
                        rs.getString("mainwords"),
                        rs.getInt("total_frequence"),
                        rs.getInt("different_APK_frequence"),
                        rs.getString("APKs"),
                        rs.getString("URLs")));
            }
        });
        return result;
    }

    public void insertTest(List<ResultModel> list){
        String sql = "insert into test (apk, weborigins, packagename, samestring) values (?, ?, ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (ResultModel resultModel : list) {
                ps.setString(1, resultModel.apk);
                ps.setString(2, resultModel.webOrigins);
                ps.setString(3, resultModel.packName);
                ps.setString(4, resultModel.sameString);
                ps.addBatch();
            }
        });
    }

    public void insertIntoWordsIDF(List<WordsIDFModel> list){
        String sql = "insert into words_IDF (word, IDF, sum) values (?, ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (WordsIDFModel line : list){
                ps.setString(1, line.word);
                ps.setDouble(2, line.IDF);
                ps.setInt(3, line.sum);
                ps.addBatch();
            }
        });
    }

    public void insertIntoAfterRemove(List<WordsIDFModel> list){
        String sql = "insert into after_remove (word, IDF, sum) values (?, ?, ?)";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (WordsIDFModel line : list){
                ps.setString(1, line.word);
                ps.setDouble(2, line.IDF);
                ps.setInt(3, line.sum);
                ps.addBatch();
            }
        });
    }
}
