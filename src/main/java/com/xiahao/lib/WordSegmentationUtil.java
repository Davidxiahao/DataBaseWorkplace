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

                //要运行了haveSubString才会有substringResult
                if (haveSubString(string)) {
                    webOriginsSubString.addAll(substringResult);
                } else {
                    webOriginsSubString.addAll(substringResult);
                    webOriginsSubString.add(string);
                }
            }

            boolean xsop = true;
            for (String str : substringResult) {
                if (helperStr.contains(str)){
                    result.add(str);
                    xsop = false;
                }
            }

            if (xsop){
                result.addAll(substringResult);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return result;
    }

    private static boolean haveSubString(String str) throws JWNLException {
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
            if (    net.didion.jwnl.dictionary.Dictionary.getInstance().lookupIndexWord(POS.NOUN, temp) != null ||
                    net.didion.jwnl.dictionary.Dictionary.getInstance().lookupIndexWord(POS.VERB, temp) != null ||
                    net.didion.jwnl.dictionary.Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, temp) != null ||
                    net.didion.jwnl.dictionary.Dictionary.getInstance().lookupIndexWord(POS.ADVERB, temp) != null) {

                if (haveSubString(str.substring(i, str.length()))) {
                    if (temp.length() >= 3 && !substringResult.contains(temp)){substringResult.add(temp);}
                    if (rest.length() >= 3 && !substringResult.contains(rest)){substringResult.add(rest);}
                    return true;
                }
                //substringResult.remove(temp);
            }

            if (i < str.length()-2) {
                temp = str.substring(i, str.length());
                rest = str.substring(0, i);
                if (    net.didion.jwnl.dictionary.Dictionary.getInstance().lookupIndexWord(POS.NOUN, temp) != null ||
                        net.didion.jwnl.dictionary.Dictionary.getInstance().lookupIndexWord(POS.VERB, temp) != null ||
                        net.didion.jwnl.dictionary.Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, temp) != null ||
                        Dictionary.getInstance().lookupIndexWord(POS.ADVERB, temp) != null) {
                    if (temp.length() >= 3 && !substringResult.contains(temp)){substringResult.add(temp);}
                    if (rest.length() >= 3 && !substringResult.contains(rest)){substringResult.add(rest);}
                }
            }
        }
        return false;
    }
}