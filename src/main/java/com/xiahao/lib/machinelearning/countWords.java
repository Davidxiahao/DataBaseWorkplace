package com.xiahao.lib.machinelearning;

import com.sun.xml.internal.bind.v2.TODO;
import com.xiahao.lib.CreateWhiteList;
import com.xiahao.lib.FileOperator;

import java.util.*;

public class countWords {

    public static List<String> getWordsVector(String inputStrings) {
        Map<String, Integer> counter = new HashMap<>();
        //for (String line : inputStrings){
            for (String string : inputStrings.split("[^a-zA-Z0-9%]")){
                if (!string.equals("") && string.length()>=2
                        && !CreateWhiteList.networkwords.contains(string.toLowerCase())
                        && !CreateWhiteList.prep.contains(string.toLowerCase())
                        && !CreateWhiteList.public_suffix_list.contains(string.toLowerCase())){
                    if (!counter.containsKey(string.toLowerCase())){
                        counter.put(string.toLowerCase(), 1);
                    }
                    else {
                        counter.put(string.toLowerCase(), counter.get(string.toLowerCase()) + 1);
                    }
                }
            }
        //}

        List<Map.Entry<String, Integer>> wordsSet = new ArrayList<Map.Entry<String, Integer>>(counter.entrySet());

        Collections.sort(wordsSet, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });

        List<String> result = new ArrayList<>();
        for (int i = 0; i < wordsSet.size(); i++){
            String word = wordsSet.get(i).toString();
            result.add(word);
        }
        return result;
    }
}
