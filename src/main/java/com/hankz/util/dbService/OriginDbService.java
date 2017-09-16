package com.hankz.util.dbService;





import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.ExtendOriginModel;
import com.hankz.util.dbutil.OriginModel;

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

    public String table = "xsop_gp4w";

    private boolean CACHE_SWITCH = false;
    private List<OriginModel> cacheList;
    private List<OriginModel> tempCacheList;

    private static OriginDbService ourInstance = new OriginDbService();

    public static OriginDbService getInstance(){return ourInstance;}

    private OriginDbService(boolean cacheSwitch) {
        this.CACHE_SWITCH = cacheSwitch;
        dbHelper = new DbHelper(JDBC_DRIVER, dbUrl);
        if (cacheSwitch) loadAllData(false);
        loadTempData();
    }

    private OriginDbService() {
        this(false);
    }

    public void setCacheSwitch(boolean cacheSwitch) {
        this.CACHE_SWITCH = cacheSwitch;
        if(cacheSwitch) loadAllData(false);
    }

    public void loadAllData(boolean forceReload){
        if(cacheList != null && cacheList.size() > 0){
            if(!forceReload)
                return;
        }
        cacheList = new ArrayList<>();
        String sql = "SELECT * FROM " + table;
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()) {
                cacheList.add(new OriginModel(rs.getString("apk"),
                        rs.getString("unit"),
                        rs.getString("lib"),
                        rs.getString("webOrigins"),
                        rs.getString("codeOrigins")));
            }
        });
    }

    public void loadTempData(){
        tempCacheList = new ArrayList<>();
        String sql = "SELECT * FROM origins_gp4w_2";
        dbHelper.doQuery(sql, rs -> {
            while (rs.next()) {
                tempCacheList.add(new OriginModel(rs.getString("apk"),
                        rs.getString("unit"),
                        rs.getString("lib"),
                        rs.getString("webOrigins"),
                        rs.getString("codeOrigins")));
            }
        });
    }

    /**
     * insert into origins
     * @param originModel
     * @return
     */

    public boolean insertOriginInfo(OriginModel originModel){
        String sql = "insert into " + table + " (apk, unit, lib, webOrigins, codeOrigins) values (?, ?, ?, ?, ?)";
        return dbHelper.doUpdate(sql, ps -> {
            ps.setString(1, originModel.apk);
            ps.setString(2, originModel.unit);
            ps.setString(3, originModel.lib);
            ps.setString(4, originModel.webOrigins);
            ps.setString(5, originModel.codeOrigins);
        });
    }

    public boolean insertOriginInfoList(List<OriginModel> list){
        String sql = "insert into " + table + " (apk, unit, lib, webOrigins, codeOrigins) values (?, ?, ?, ?, ?)";
        return dbHelper.doBatchUpdate(sql, ps -> {
            for (OriginModel originModel : list) {
                ps.setString(1, originModel.apk);
                ps.setString(2, originModel.unit);
                ps.setString(3, originModel.lib);
                ps.setString(4, originModel.webOrigins);
                ps.setString(5, originModel.codeOrigins);
                ps.addBatch();
            }
        });
    }

    /**
     * get from origins
     * @return
     */

    public List<String> getApkList(){
        if (CACHE_SWITCH){
            return cacheList.stream().map(OriginModel::getApk).distinct().
                    collect(Collectors.toList());
        }else {
            String sql = "select apk from " + table + " group by apk";
            List<String> result = new ArrayList<>();
            dbHelper.doQuery(sql, rs -> {
                while (rs.next()) {
                    result.add(rs.getString("apk"));
                }
            });
            return result;
        }
    }

    public List<ExtendOriginModel> getExtendData(){
        String sql = "select * from xsop_bak where isXSOP=0";
        List<ExtendOriginModel> result = new ArrayList<>();
        dbHelper.doQuery(sql,
                rs -> {
                    while (rs.next()){
                        ExtendOriginModel extendOriginModel = new ExtendOriginModel(new OriginModel(
                                                                            rs.getString("apk"),
                                                                            rs.getString("unit"),
                                                                            rs.getString("lib"),
                                                                            rs.getString("webOrigins"),
                                                                            rs.getString("codeOrigins"))
                                                                                    );

                        extendOriginModel.isXSOP = rs.getInt("isXSOP");

                        result.add(extendOriginModel);
                    }
                });

        return result;
    }

    public List<OriginModel> getAllData(){
        if (CACHE_SWITCH){
            return cacheList;
        }else {
            String sql = "select * from " + table;
            List<OriginModel> result = new ArrayList<>();
            dbHelper.doQuery(sql,
                    rs -> {
                        while (rs.next()){
                            result.add(new OriginModel(rs.getString("apk"),
                                    rs.getString("unit"),
                                    rs.getString("lib"),
                                    rs.getString("webOrigins"),
                                    rs.getString("codeOrigins")));
                        }
                    });
            return result;
        }
    }

    public List<String> getApkLibs(String apkname) {
        if (CACHE_SWITCH) {
            return cacheList.stream().filter(c -> c.apk.equals(apkname)).map(OriginModel::getLib).
                    collect(Collectors.toList());
        } else {
            String sql = "select lib from " + table + " where apk=?";
            List<String> result = new ArrayList<>();
            dbHelper.doQuery(sql,
                    ps -> ps.setString(1, apkname),
                    rs -> {
                        while (rs.next()) {
                            result.add(rs.getString("lib"));
                        }
                    });
            return result;
        }
    }

    public List<String> getApkCodeOrigins(String apkname, String webOrigins) {
        return tempCacheList.stream().filter(c -> c.apk.equals(apkname) && c.webOrigins.equals(webOrigins)).
                map(OriginModel::getCodeOrigins).collect(Collectors.toList());

//        String sql = "select codeOrigins from origins_gp4w_2 where apk=? and webOrigins=?";
//        List<String> result = new ArrayList<>();
//        dbHelper.doQuery(sql,
//                ps -> {ps.setString(1, apkname);
//                    ps.setString(2, webOrigins);},
//                rs -> {
//                    while (rs.next()) {
//                        result.add(rs.getString("codeOrigins"));
//                    }
//                });
//        return result;
    }

    public List<OriginModel> getMeaningfulResult(){
        if(!CACHE_SWITCH){
            setCacheSwitch(true);
        }
        List<OriginModel> originData = new ArrayList<>();
        originData.addAll(cacheList);
        Iterator<OriginModel> oriIter = originData.iterator();

        while(oriIter.hasNext()){
            OriginModel originModel = oriIter.next();
            if (originModel.webOrigins.equals("")){
                oriIter.remove();
            } else {
                String[] buff = originModel.webOrigins.split(";");

                boolean remove = true;
                for(String retval: buff){
                    if (retval.indexOf(":")==-1 || retval.indexOf("http")!=-1 || retval.indexOf("https")!=-1){
                        remove = false;
                        break;
                    }
                }

                if (remove)
                    oriIter.remove();
            }
        }

        return originData;
    }

    public List<ExtendOriginModel> getMainName(){
        if(!CACHE_SWITCH){
            setCacheSwitch(true);
        }

        List<ExtendOriginModel> result = new ArrayList<>();


        for (OriginModel temp : cacheList){
            String webOrigin = temp.webOrigins;

            ExtendOriginModel extendOriginModel = new ExtendOriginModel(temp);
            //储存weborigin和codeorigin的mainname组成的list

            for (String weburl : webOrigin.split(";")){
                //判断有没有类似于http的前缀
                if (weburl.indexOf("://") != -1){
                    //System.out.println(weburl.split("://")[1].split("/")[0]);
                    extendOriginModel.webMainName.addAll(findIdentifiedUrl(weburl.split("://")[1].
                            split("/")[0]));
                }
                else {
                    extendOriginModel.webMainName.addAll(findIdentifiedUrl(weburl.split("/")[0]));
                }
            }

            List<String> codeOrigins = new ArrayList<>();
            codeOrigins.addAll(this.getApkCodeOrigins(temp.apk, temp.webOrigins));
            if (codeOrigins.isEmpty()) continue;

            String codeOrigin = codeOrigins.get(0).split("\\[PN]")[1].split(";")[0];
            extendOriginModel.codeOrigin = codeOrigin;
            //System.out.println(codeOrigin);

            for (String codeString : codeOrigin.split("\\.")){
                if (!INTERNATIONAL.contains(codeString) && !NATIONAL.contains(codeString) &&
                        !SUFFIX.contains(codeString) && !PREFIX.contains(codeString)){
                    //System.out.println(codeString);
                    extendOriginModel.codeMainName.add(codeString);
                }
            }

            result.add(extendOriginModel);
        }

        return result;
    }

    public final static List<String> INTERNATIONAL = Arrays.asList("com", "edu", "gov", "int", "mil", "net", "org",
            "biz", "info", "pro", "name", "museum", "coop", "aero", "xxx", "idv");

    public final static List<String> NATIONAL = Arrays.asList("au", "mo", "br", "de", "ru", "fr", "kr", "ca", "ky",
            "us", "za", "eu", "jp", "tw", "hk", "sg", "in", "uk", "cn", "co", "no", "io", "me", "at", "gl" ,"hr", "pl",
            "vn", "tv", "gr", "to", "tr", "be", "th", "es", "se", "it", "bg", "eg", "is", "su", "ph", "nl", "al", "pe",
            "il", "ua", "ae", "fi", "pk", "dk", "hu", "ch", "do", "cc", "ro", "ly", "ir", "re", "ar", "am", "sk", "mx",
            "my", "id", "im", "mn", "rs", "nz", "pa", "az", "st");

    public final static List<String> SUFFIX = Arrays.asList("htm", "html", "asp", "php", "jsp", "shtml", "nsp", "cgi",
            "aspx", "xml");

    public final static List<String> PREFIX = Arrays.asList("www", "bbs", "play", "sdk", "ads", "ws", "payment",
            "maps", "market", "docs", "web", "mobile", "accounts", "app", "ad", "api", "wap", "oauth", "android",
            "secure", "dl", "video", "store", "cloud", "feedback", "book", "user", "checkin", "dev", "news", "login",
            "app3", "profil", "img", "partner", "service", "box", "build", "search", "nhis", "nip", "auth", "blog",
            "connect", "mw", "www1", "test", "help", "cdn", "sites", "support", "mall", "member");

    public static List<String> findIdentifiedUrl(String url){
        String[] buff = url.split("\\.");
        List<String> result = new ArrayList<>();

        if(INTERNATIONAL.contains(buff[buff.length-1]) || SUFFIX.contains(buff[buff.length-1]) ||
                NATIONAL.contains(buff[buff.length-1]))
        {
            for(String string : buff){
                if(!string.equals("") && string.length()>=2 && !PREFIX.contains(string) && !SUFFIX.contains(string) &&
                        !NATIONAL.contains(string) && !INTERNATIONAL.contains(string)){
                    result.add(string);
                }
            }
        }

        return result;
    }
}
