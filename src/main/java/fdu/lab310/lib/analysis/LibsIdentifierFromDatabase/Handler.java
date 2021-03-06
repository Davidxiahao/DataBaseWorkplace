package fdu.lab310.lib.analysis.LibsIdentifierFromDatabase;

import com.hankz.util.dbutil.DbHelper;
import com.hankz.util.dbutil.OriginModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Handler {
    static private int THRESHOLD = 5;
    static private DbHelper dbHelper;
    static final String dbUrl = "jdbc:mysql://10.141.209.138:6603/originchecker?user=originchecker&password=originchecker";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static private DbHelper mydb;
    static final String mydbUrl = "jdbc:mysql://localhost:3306/stringdatabase?user=root&password=123456789";


    public static String getApi(String unit){
        if(unit.contains("evaluateJavascript")){
            return "evaluateJavascript";
        }
        else if(unit.contains("getCookie")){
            return "getCookie";
        }
        else if(unit.contains("loadUrl")){
            return "loadUrl";
        }
        else if(unit.contains("loadDataWithBaseURL")){
            return "loadDataWithBaseURL";
        }
        else if(unit.contains("onLoadResource")){
            return "onLoadResource";
        }
        else if(unit.contains("onPageStarted")){
            return "onPageStarted";
        }
        else if(unit.contains("onPageFinished")){
            return "onPageFinished";
        }
        else if(unit.contains("shouldOverrideUrlLoading")){
            return "shouldOverrideUrlLoading";
        }
        else if(unit.contains("shouldInterceptRequest")){
            return "shouldInterceptRequest";
        }
        else if(unit.contains("postUrl")){
            return "postUrl";
        }
        else{
            System.out.println("Missing:" + unit);
            return "else";
        }

    }


    public static Map<Record, Integer> recordInit(List<OriginModel> dataList){
        Map<Record, Integer> map = new HashMap<>();

        for(OriginModel om: dataList){
            String[] webOrigins = om.getWebOrigins().split(";");
            String[] codeOrigins = om.getCodeOrigins().split(";");
            String declareClass = "";
            for(String co: codeOrigins){
                if(co.startsWith("[DC]")){
                    declareClass = co;
                    break;
                }
            }

            for(String path: webOrigins){
                if(isWebUrl(path)){
                    String api = getApi(om.getUnit());
                    Record record = new Record(om.getApk(), api, path, declareClass);
                    if(map.containsKey(record)){
                        map.put(record, map.get(record) + 1);
                    }
                    else {
                        map.put(record, 1);
                    }

                }
            }

        }
        return map;
    }


    public static boolean isWebUrl(String url){
        try {
            URI uri = new URI(url);
            if(uri.getScheme()!= null) {
                if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
                    return true;
                }
            }
        } catch (URISyntaxException e) {
            //e.printStackTrace();
            return false;
        }
        return false;
    }




}
