package com.xiahao.lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringIDF {
    public static Map<String, Integer> calculateStringFrequency(List<String> list){
        Map<String, Integer> counter = new HashMap<>();

        for (String string : list){
            if (counter.containsKey(string)){
                counter.put(string, counter.get(string)+1);
            }
            else {
                counter.put(string, 1);
            }
        }

        return counter;
    }

    public static Map<String, Double> calculateStringTFIDF(Map<String, Integer> map,
                                                         int total){
        Map<String, Double> result = new HashMap<>();

        for (Map.Entry<String, Integer> entry : map.entrySet()){
            double sum = entry.getValue();

        }

        return result;
    }
}
