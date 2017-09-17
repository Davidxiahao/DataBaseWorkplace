package com.xiahao.lib;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HankZhang on 2017/9/16.
 */
public class WordSegmentationUtil {

    static {
        String propsFile = "file_properties.xml";
        try {
            // initialize JWNL (this must be done before JWNL can be used)
            JWNL.initialize(new FileInputStream(propsFile));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    static String reg = "[^a-z]";
    static Pattern matchsip = Pattern.compile(reg);

    /***
     *
     * @param targetStr webOrigins to be segmented.
     * @param helperStr package name from codeOrigins to help the segmentation.
     * @return
     */
    public static Set<String> segmentWord(Set<String> targetStr, String helperStr) {
        Set<String> result = new HashSet<>();
        result.addAll(targetStr);

        try {
            Set<String> webOriginsSubString = new HashSet<>();

            for (String str : targetStr) {

                String string = str.toLowerCase();
                Matcher mp = matchsip.matcher(string);
                string = mp.replaceAll(".");

                for (String str2 : string.split("\\.")) {
                    if (str2.length() >= 3) {
                        webOriginsSubString.add(str2);
                        haveSubString(str2, webOriginsSubString);
                    }
                }
            }

            boolean xsop = true;
            for (String str : webOriginsSubString) {
                if (helperStr.contains(str)){
                    //System.out.println(str + "#####");
                    result.add(str);
                    xsop = false;
                }
            }

            if (xsop){
                result.addAll(webOriginsSubString);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    private static void haveSubString(String str, Set<String> substringResult) throws JWNLException {

        for (int i=3; i<=str.length(); i++){
            String temp = str.substring(0, i);
            //System.out.println(temp);
            String rest = str.substring(i, str.length());
            if (    Dictionary.getInstance().lookupIndexWord(POS.NOUN, temp) != null ||
                    Dictionary.getInstance().lookupIndexWord(POS.VERB, temp) != null ||
                    Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, temp) != null ||
                    Dictionary.getInstance().lookupIndexWord(POS.ADVERB, temp) != null) {
                //System.out.println("isWord");
                if (temp.length() >= 3){substringResult.add(temp);}
                if (rest.length() >= 3){substringResult.add(rest);}
                haveSubString(str.substring(i, str.length()), substringResult);
            }

            if (i < str.length()-2) {
                temp = str.substring(i, str.length());
                rest = str.substring(0, i);
                //System.out.println(temp);
                if (    Dictionary.getInstance().lookupIndexWord(POS.NOUN, temp) != null ||
                        Dictionary.getInstance().lookupIndexWord(POS.VERB, temp) != null ||
                        Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, temp) != null ||
                        Dictionary.getInstance().lookupIndexWord(POS.ADVERB, temp) != null) {
                    //System.out.println("isWord");
                    if (temp.length() >= 3){substringResult.add(temp);}
                    if (rest.length() >= 3){substringResult.add(rest);}
                    haveSubString(str.substring(0, i), substringResult);
                }
            }
        }
    }
}