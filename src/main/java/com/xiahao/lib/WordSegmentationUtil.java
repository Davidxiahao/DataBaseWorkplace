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
    private static List<String> substringResult = new ArrayList<>();

    private WordSegmentationUtil(){
        String propsFile = "file_properties.xml";
        try {
            // initialize JWNL (this must be done before JWNL can be used)
            JWNL.initialize(new FileInputStream(propsFile));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    private static WordSegmentationUtil myInstance = new WordSegmentationUtil();

    public static WordSegmentationUtil getInstance(){return myInstance;}
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
            List<String> webOriginsSubString = new ArrayList<>();

            String reg = "[^a-z]";
            Pattern matchsip = Pattern.compile(reg);

            for (String str : targetStr) {
                substringResult.clear();

                String string = str.toLowerCase();
                Matcher mp = matchsip.matcher(string);
                string = mp.replaceAll("");

                haveSubString(string);
                webOriginsSubString.addAll(substringResult);
                webOriginsSubString.add(string);

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
            System.exit(-1);
        }

        return result;
    }

    private static void haveSubString(String str) throws JWNLException {

        for (int i=3; i<=str.length(); i++){
            String temp = str.substring(0, i);
            //System.out.println(temp);
            String rest = str.substring(i, str.length());
            if (    Dictionary.getInstance().lookupIndexWord(POS.NOUN, temp) != null ||
                    Dictionary.getInstance().lookupIndexWord(POS.VERB, temp) != null ||
                    Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, temp) != null ||
                    Dictionary.getInstance().lookupIndexWord(POS.ADVERB, temp) != null) {
                //System.out.println("isWord");
                if (temp.length() >= 3 && !substringResult.contains(temp)){substringResult.add(temp);}
                if (rest.length() >= 3 && !substringResult.contains(rest)){substringResult.add(rest);}
                haveSubString(str.substring(i, str.length()));
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
                    if (temp.length() >= 3 && !substringResult.contains(temp)){substringResult.add(temp);}
                    if (rest.length() >= 3 && !substringResult.contains(rest)){substringResult.add(rest);}
                    haveSubString(str.substring(0, i));
                }
            }
        }
    }
}