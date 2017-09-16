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
    private CheckXSOP(){
        String propsFile = "file_properties.xml";
        try {
            // initialize JWNL (this must be done before JWNL can be used)
            JWNL.initialize(new FileInputStream(propsFile));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    private static CheckXSOP myInstance = new CheckXSOP();

    public static CheckXSOP getInstance(){return myInstance;}

    private static List<String> substringResult = new ArrayList<>();

    private static List<String> getUrlMainName(String url){

        List<String> result = new ArrayList<>();

        for (String string : url.split(";")){
            Url webUrl = new Url(string);
            webUrl.getHost();
            if (webUrl.isMeaningfulUrl()){
                result.addAll(webUrl.getIdentitiesOfHost());
            }
        }

        return result;
    }

    public static List<String> getPackMainName(String url){
        String[] buff = url.split("\\.");
        List<String> result = new ArrayList<>();

        for (String codeString : buff){
            if (!PredefinedList.ignoredWordSetInWebHost.contains(codeString)){
                result.add(codeString);
            }
        }

        return result;
    }

    public static List<String> sameString = new ArrayList<>();

    public static boolean isXSOP(String webOrigins, String packName){

        List<String> webMainName = new ArrayList<>();
        List<String> packMainName = new ArrayList<>();

        webMainName.addAll(getUrlMainName(webOrigins));
        packMainName.addAll(getPackMainName(packName));



        try {
            List<String> webOriginsSubString = new ArrayList<>();
            List<String> packSubString = new ArrayList<>();

            String reg = "[^a-z]";
            Pattern matchsip = Pattern.compile(reg);

            for (String str : webMainName) {
                substringResult.clear();


                String string = str.toLowerCase();
                Matcher mp = matchsip.matcher(string);
                string = mp.replaceAll("");

                //要运行了haveSubString才会有substringResult
                if (haveSubString(string)) {
                    webOriginsSubString.addAll(substringResult);
                } else {
                    webOriginsSubString.addAll(substringResult);
                    webOriginsSubString.add(string);
                }
            }

            for (String str : packMainName) {
                substringResult.clear();

                String string = str.toLowerCase();
                Matcher mp = matchsip.matcher(string);
                string = mp.replaceAll("");

                if (haveSubString(string)) {
                    packSubString.addAll(substringResult);
                } else {
                    packSubString.addAll(substringResult);
                    packSubString.add(string);
                }
            }

            webOriginsSubString.retainAll(packSubString);
            if (webOriginsSubString.size() == 0) {
                return true;
            } else {
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
