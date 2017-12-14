package com.hankz.util.dbService;

import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.OriginModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HankZhang on 2017/8/7.
 */
public class SampleDbService {
    private final DbHelper dbHelper;
    static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/xsop2?user=originchecker&password=originchecker";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static SampleDbService ourInstance = new SampleDbService();

    public static SampleDbService getInstance(){return ourInstance;}

    private SampleDbService() {
        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
    }

    public void updatelast_origin_gp8w_meaningful(List<OriginModel> list){
        String sql = "update last_origin_gp8w_meaningful set keyword=?, similarity=?, isXSOP=? where idx=?";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (OriginModel line : list){
                ps.setString(1, line.keyWord);
                ps.setDouble(2, line.similarity);
                ps.setInt(3, line.isXSOP);
                ps.setInt(4, line.idx);
                ps.addBatch();
            }
        });
    }

    public void updateDevelopers(List<OriginModel> list){
        String sql = "update last_origin_gp8w_meaningful set developers=? where idx=?";
        dbHelper.doBatchUpdate(sql, ps -> {
            for (OriginModel line : list){
                //System.out.println(line.developers+" "+line.idx);
                ps.setString(1, line.developers);
                ps.setInt(2, line.idx);
                ps.addBatch();
            }
        });
    }

    public List<OriginModel> getGroundTruthFromlast_origin_gp8w_meaningful(){
        String sql = "select * from last_origin_gp8w_meaningful where groundtruth is not null";
        List<OriginModel> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()){
                OriginModel temp = new OriginModel("", "", "", "", "","", "");
                temp.idx = rs.getInt("idx");
                temp.groundtruth = rs.getInt("groundtruth");
                result.add(temp);
            }
        });

        return result;
    }

    public List<OriginModel> getAllDataFromlast_origin_gp8w_meaningful(){
        String sql = "select * from last_origin_gp8w_meaningful";
        List<OriginModel> result = new ArrayList<>();
        dbHelper.doQuery(sql, rs -> {
            while ((rs.next())){
                OriginModel temp = new OriginModel(rs.getString("apk"),
                        rs.getString("unit"),
                        rs.getString("declaringClass"),
                        rs.getString("webOrigins"),
                        rs.getString("codeOrigins"),
                        rs.getString("webHelpInfo"),
                        rs.getString("codeHelpInfo"));
                temp.idx = rs.getInt("idx");
                temp.libNum = rs.getInt("libNum");
                temp.similarity = rs.getDouble("similarity");
                temp.keyWord = rs.getString("keyword");
                temp.isXSOP = rs.getInt("isXSOP");
                result.add(temp);
            }
        });

        return result;
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
