package com.xiahao.lib.machinelearning;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.ggsearchModel;
import com.xiahao.lib.CalculateFeatures;
import com.xiahao.lib.FileOperator;
import com.xiahao.lib.StringIDF;

import java.util.*;

public class prepareFeatures {
    public static void main(String[] args) {
        List<ggsearchModel> ggsearchResult = new ArrayList<>();
        ggsearchResult.addAll(OriginDbService.getInstance().getAllDataFromggsearch());

        String totalString = "";
        for (ggsearchModel line : ggsearchResult){
            int documents = 0;
            Map<String, Integer> termsCounter = new HashMap<>();
            for (String words :line.mainwordsnippet.split("}@\\{")){
                if (!words.equals("")){
                    for (String subwords : words.split("]@\\[")){
                        documents++;
                        if (!subwords.equals("")){
                            Set<String> wordlist = new HashSet<>();
                            for (String string : subwords.split("[^a-zA-Z]")){
                                if (!string.equals("")){
                                    wordlist.add(string.toLowerCase());
                                }
                            }

                            for (String string : wordlist){
                                if (!termsCounter.containsKey(string)){
                                    termsCounter.put(string, 1);
                                }
                                else {
                                    termsCounter.put(string, termsCounter.get(string) + 1);
                                }
                            }
                        }
                    }
                }
            }

            for (String words : line.mainwordsnippet.split("}@\\{")){
                if (!words.equals("")){
                    for (String subwords : words.split("]@\\[")){
                        if (!subwords.equals("")){
                            Map<String, Integer> insideTermsCounter = new HashMap<>();
                            int size = 0;
                            for (String string : subwords.split("[^a-zA-Z]")){
                                if (!string.equals("")){
                                    size++;
                                    if (!insideTermsCounter.containsKey(string.toLowerCase())){
                                        insideTermsCounter.put(string.toLowerCase(), 1);
                                    }
                                    else {
                                        insideTermsCounter.put(string.toLowerCase(), insideTermsCounter.get(string.toLowerCase()) + 1);
                                    }
                                }
                            }

                            String string = "(";
                            for (Map.Entry<String, Integer> entry : insideTermsCounter.entrySet()){
                                string = string + entry.getKey() + ": " +
                                        CalculateFeatures.calculateTF(entry.getValue(), size)*
                                                CalculateFeatures.calculateIDF(documents, termsCounter.get(entry.getKey())) + ", ";
                            }
                            string = string.substring(0, string.length()-1);
                            string = string + ");";
                            totalString = totalString + string;
                        }
                    }
                }
            }

            totalString = totalString + "\n";
        }

        FileOperator.putLinesToFile("mainwordsResult", totalString);

//        List<ggsearchModel> ggsearchResult = new ArrayList<>();
//        ggsearchResult.addAll(OriginDbService.getInstance().getAllDataFromggsearch());
//
//        String totalString = "";
//        for (ggsearchModel line : ggsearchResult){
//            for (String words : line.urlssnippetfull.split("}@\\{")) {
//                int documents = 0;
//                Map<String, Integer> termsCounter = new HashMap<>();
//
//                    if (!words.equals("")) {
//                        totalString = totalString + "{";
//                        for (String subwords : words.split("]@\\[")) {
//                            documents++;
//                            if (!subwords.equals("")) {
//                                Set<String> wordlist = new HashSet<>();
//                                for (String string : subwords.split("[^a-zA-Z]")) {
//                                    if (!string.equals("")) {
//                                        wordlist.add(string);
//                                    }
//                                }
//
//                                for (String string : wordlist) {
//                                    if (!termsCounter.containsKey(string)) {
//                                        termsCounter.put(string, 1);
//                                    } else {
//                                        termsCounter.put(string, termsCounter.get(string) + 1);
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//
//
//                    if (!words.equals("")) {
//                        for (String subwords : words.split("]@\\[")) {
//                            if (!subwords.equals("")) {
//                                Map<String, Integer> insideTermsCounter = new HashMap<>();
//                                int size = 0;
//                                for (String string : subwords.split("[^a-zA-Z]")) {
//                                    if (!string.equals("")) {
//                                        size++;
//                                        if (!insideTermsCounter.containsKey(string)) {
//                                            insideTermsCounter.put(string, 1);
//                                        } else {
//                                            insideTermsCounter.put(string, insideTermsCounter.get(string) + 1);
//                                        }
//                                    }
//                                }
//
//                                String string = "(";
//                                for (Map.Entry<String, Integer> entry : insideTermsCounter.entrySet()) {
//                                    string = string + entry.getKey() + ": " +
//                                            CalculateFeatures.calculateTF(entry.getValue(), size) *
//                                                    CalculateFeatures.calculateIDF(documents, termsCounter.get(entry.getKey())) + ", ";
//                                }
//                                string = string.substring(0, string.length() - 1);
//                                string = string + ");";
//                                totalString = totalString + string;
//                            }
//                        }
//                    }
//                totalString = totalString + "};";
//            }
//
//            totalString = totalString + "\n";
//        }
//        FileOperator.putLinesToFile("urlResult", totalString);
    }

}
