package com.xiahao.lib.machinelearning;

import com.xiahao.lib.FileOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class makeVector {
    public static double getSimilarity(List<String> inputlist1, List<String> inputlist2) {
        Map<String, Integer> vector1;
        Map<String, Integer> vector2;
        vector1 = readVector(inputlist1);
        vector2 = readVector(inputlist2);

        for (Map.Entry<String, Integer> entry : vector2.entrySet()){
            if (!vector1.containsKey(entry.getKey())){
                vector1.put(entry.getKey(), 0);
            }
        }

        for (Map.Entry<String, Integer> entry : vector1.entrySet()){
            if (!vector2.containsKey(entry.getKey())){
                vector2.put(entry.getKey(), 0);
            }
        }

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : vector1.entrySet()){
            list1.add(entry.getValue());
            list2.add(vector2.get(entry.getKey()));
            System.out.println((list1.size()-1)+" "+entry.getKey()+" "+entry.getValue()+" "+vector2.get(entry.getKey()));
        }

        int up = 0;
        int value1 = 0;
        int value2 = 0;
        for (int i = 0; i < list1.size(); i++){
            up = up + list1.get(i) * list2.get(i);
            System.out.println(up + "= " + i + " " + list1.get(i) + " " + list2.get(i));
            value1 = value1 + list1.get(i) * list1.get(i);
            value2 = value2 + list2.get(i) * list2.get(i);
        }

        System.out.println(up);
        System.out.println(value1);
        System.out.println(value2);

        if (value1<20) value1=20;
        if (value2<20) value2=20;

        return (double)up / (Math.sqrt((double)value1) * Math.sqrt((double)value2));
    }

    public static Map<String, Integer> readVector(List<String> list){
        Map<String, Integer> result = new HashMap<>();

        for (String line : list){
//            System.out.println(line.split("=")[0]);
//            System.out.println(line.split("=")[1]);
            result.put(line.split("=")[0], Integer.parseInt(line.split("=")[1]));
        }

        return result;
    }
}
