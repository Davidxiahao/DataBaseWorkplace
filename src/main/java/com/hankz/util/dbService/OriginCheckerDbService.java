//package com.hankz.util.dbService;
//
//import com.hankz.util.dbutil.DbHelper;
//import com.hankz.util.dbutil.LibraryInfo;
//import com.hankz.util.dbutil.OriginModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class OriginCheckerDbService {
//    private final DbHelper dbHelper;
//    static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/originchecker?user=originchecker&password=originchecker";
//    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//
//    private boolean CACHE_SWITCH = false;
//    private List<OriginModel> cacheList;
//
//    public OriginCheckerDbService(boolean cacheSwitch) {
//        this.CACHE_SWITCH = cacheSwitch;
//        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
//    }
//
//    public OriginCheckerDbService() {
//        this(false);
//    }
//
//    public void setCacheSwitch(boolean cacheSwitch) {
//        this.CACHE_SWITCH = cacheSwitch;
//    }
//
////    public void loadAllData(boolean forceReload){
////        if(cacheList != null && cacheList.size() > 0){
////            if(!forceReload)
////                return;
////        }
////        cacheList = new ArrayList<>();
////        String sql = "SELECT * FROM libs";
////        cacheList = dbHelper.doQuery(sql, rs -> {
////            while (rs.next()) {
////
////                cacheList.add(new OriginModel())
////            }
////        });
////
////    }
//
//    /**
//     * insert into libs
//     * @param libraryInfo
//     * @return
//     */
//
//    public boolean insertLibraryInfo(LibraryInfo libraryInfo){
//        String sql = "insert into libs (lib, fingerprint, constants) values (?, ?, ?)";
//        return dbHelper.doUpdate(sql, ps -> {
//            ps.setString(1, libraryInfo.lib);
//            ps.setString(2, libraryInfo.fingerprint);
//            ps.setString(3, libraryInfo.constants);
//        });
//    }
//
//    public  boolean insertLibraryInfoList(List<LibraryInfo> list){
//        String sql = "insert into libs values (?, ?, ?)";
//        return dbHelper.doBatchUpdate(sql, ps -> {
//            for(int i=0; i<list.size(); i++) {
//                LibraryInfo libraryInfo = list.get(i);
//                ps.setString(1, libraryInfo.lib);
//                ps.setString(2, libraryInfo.fingerprint);
//                ps.setString(3, libraryInfo.constants);
//            }
//        });
//    }
//
//    /**
//     * insert into origins
//     * @param originInfo
//     * @return
//     */
//
//    public boolean insertOriginInfo(OriginModel originInfo){
//        String sql = "insert into origins values (?, ?, ?, ?, ?)";
//        return dbHelper.doUpdate(sql, ps -> {
//            ps.setString(1, originInfo.apk);
//            ps.setString(2, originInfo.unit);
//            ps.setString(3, originInfo.lib);
//            ps.setString(4, originInfo.webOrigins);
//            ps.setString(5, originInfo.codeOrigins);
//        });
//    }
//
//    public boolean insertOriginInfoList(List<OriginModel> list){
//        String sql = "insert into origins values (?, ?, ?, ?, ?)";
//        return dbHelper.doBatchUpdate(sql, ps -> {
//            for (OriginModel originInfo : list) {
//                ps.setString(1, originInfo.apk);
//                ps.setString(2, originInfo.unit);
//                ps.setString(3, originInfo.lib);
//                ps.setString(4, originInfo.webOrigins);
//                ps.setString(5, originInfo.codeOrigins);
//            }
//        });
//    }
//
//    /**
//     * get from libs
//     * @return
//     */
//
//    public List<String> getLibraryList(){
//        String sql = "select lib from libs group by lib";
//        List<String> result = new ArrayList<>();
//        dbHelper.doQuery(sql, rs -> {
//            while (rs.next()) {
//                result.add(rs.getString("lib"));
//            }
//        });
//        return result;
//    }
//
//    public String getLibraryFingerprint(String libraryName){
//        String sql = "select fingerprint from libs where lib=?";
//        List<String> result = new ArrayList<>();
//        dbHelper.doQuery(sql,
//                ps -> ps.setString(1, libraryName),
//                rs -> {
//                    while (rs.next()) {
//                        result.add(rs.getString("fingerprint"));
//                    }
//                    });
//        return result.get(0);
//    }
//
//    public String getLibraryConstants(String libraryName){
//        String sql = "select constants from libs where lib=?";
//        List<String> result = new ArrayList<>();
//        dbHelper.doQuery(sql,
//                ps -> ps.setString(1, libraryName),
//                rs -> {
//                    while (rs.next()){
//                        result.add(rs.getString("constants"));
//                    }
//                });
//        return result.get(0);
//    }
//
//    public List<String> searchLibsByFingerprint(String fingerprint){
//        String sql = "select lib from libs where fingerprint=?";
//        List<String> result = new ArrayList<>();
//        dbHelper.doQuery(sql,
//                ps -> ps.setString(1, fingerprint),
//                rs -> {
//                    while (rs.next()){
//                        result.add(rs.getString("lib"));
//                    }
//                });
//        return result;
//    }
//
//    /**
//     * get from origins
//     * @return
//     */
//
//    public List<String> getApkList(){
//        String sql = "select apk from origins group by apk";
//        List<String> result = new ArrayList<>();
//        dbHelper.doQuery(sql, rs -> {
//            while (rs.next()){
//                result.add(rs.getString("apk"));
//            }
//        });
//        return result;
//    }
//
//    public List<String> getApkLibs(String apkname){
//        String sql = "select lib from origins where apk=?";
//        List<String> result = new ArrayList<>();
//        dbHelper.doQuery(sql,
//                ps -> ps.setString(1,apkname),
//                rs -> {
//                    while (rs.next()){
//                        result.add(rs.getString("lib"));
//                    }
//                });
//        return result;
//    }
//}
