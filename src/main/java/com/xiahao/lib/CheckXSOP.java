package com.xiahao.lib;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.ExtendOriginModel;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckXSOP {
    private CheckXSOP(){}

    private static CheckXSOP myInstance = new CheckXSOP();

    public static CheckXSOP getInstance(){return myInstance;}


    private static List<String> substringResult = new ArrayList<>();

    public static List<String> getMainName(String url){

        List<String> result = new ArrayList<>();

        for (String weburl : url.split(";")){
            if (weburl.indexOf("://") != -1){
                result.addAll(findIdentifiedUrl(weburl.split("://")[1].
                        split("/")[0]));
            }
            else {
                result.addAll(findIdentifiedUrl(weburl.split("/")[0]));
            }
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

        for (String codeString : buff){
            if (!INTERNATIONAL.contains(codeString) && !NATIONAL.contains(codeString) &&
                    !SUFFIX.contains(codeString) && !PREFIX.contains(codeString)){
                result.add(codeString);
            }
        }

        return result;
    }

    public static List<String> sameString = new ArrayList<>();

    public static boolean isXSOP(String webOrigins, String packName){

        List<String> webMainName = new ArrayList<>();
        List<String> packMainName = new ArrayList<>();

        webMainName.addAll(getMainName(webOrigins));
        packMainName.addAll(getMainName(packName));

        String propsFile = "file_properties.xml";
        try {
            // initialize JWNL (this must be done before JWNL can be used)
            JWNL.initialize(new FileInputStream(propsFile));

            System.out.println("==========================================================");
            List<String> webOriginsSubString = new ArrayList<>();
            List<String> packSubString = new ArrayList<>();

            String reg = "[^a-z]";
            Pattern matchsip = Pattern.compile(reg);

            for (String str : webMainName){
                substringResult.clear();


                String string = str.toLowerCase();
                Matcher mp = matchsip.matcher(string);
                string = mp.replaceAll("");

                //要运行了haveSubString才会有substringResult
                if (haveSubString(string)){
                    webOriginsSubString.addAll(substringResult);
                }
                else {
                    webOriginsSubString.addAll(substringResult);
                    webOriginsSubString.add(string);
                }
            }

            for (String str : packMainName){
                substringResult.clear();

                String string = str.toLowerCase();
                Matcher mp = matchsip.matcher(string);
                string = mp.replaceAll("");

                if (haveSubString(string)){
                    packSubString.addAll(substringResult);
                }
                else {
                    packSubString.addAll(substringResult);
                    packSubString.add(string);
                }
            }

            webOriginsSubString.retainAll(packSubString);
            if (webOriginsSubString.size() == 0){
                return true;
            }
            else {
                sameString.clear();
                sameString.addAll(webOriginsSubString);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return false;
    }

    public static boolean haveSubString(String str) throws JWNLException {
        if (str.equals("")){
            return true;
        }

        if (str.substring(0, 1).equals("a") || str.substring(0, 1).equals("i") || str.substring(0, 1).equals("x")){
            if (haveSubString(str.substring(1, str.length()))){
                return true;
            }
            substringResult.remove(str.substring(0, 1));
        }

        for (int i=3; i<=str.length(); i++){
            String temp = str.substring(0, i);
            String rest = str.substring(i, str.length());
            if (    Dictionary.getInstance().lookupIndexWord(POS.NOUN, temp) != null ||
                    Dictionary.getInstance().lookupIndexWord(POS.VERB, temp) != null ||
                    Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, temp) != null ||
                    Dictionary.getInstance().lookupIndexWord(POS.ADVERB, temp) != null) {
                if (temp.length() >= 3 && !substringResult.contains(temp)){substringResult.add(temp);}
                if (rest.length() >= 3 && !substringResult.contains(rest)){substringResult.add(rest);}
                if (haveSubString(str.substring(i, str.length()))) {
                    return true;
                }
                //substringResult.remove(temp);
            }

            if (i < str.length()-2) {
                temp = str.substring(i, str.length());
                rest = str.substring(0, i);
                if (    Dictionary.getInstance().lookupIndexWord(POS.NOUN, temp) != null ||
                        Dictionary.getInstance().lookupIndexWord(POS.VERB, temp) != null ||
                        Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, temp) != null ||
                        Dictionary.getInstance().lookupIndexWord(POS.ADVERB, temp) != null) {
                    if (temp.length() >= 3 && !substringResult.contains(temp)){substringResult.add(temp);}
                    if (rest.length() >= 3 && !substringResult.contains(rest)){substringResult.add(rest);}
                }
            }
        }
        return false;
    }
}
