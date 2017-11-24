package com.xiahao.lib.machinelearning;

import com.xiahao.lib.FileOperator;
import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

import java.util.Map;

import static com.xiahao.lib.machinelearning.makeVector.readVector;

public class wordDistance {
    public static void main(String[] args) {
        NormalizedLevenshtein l = new NormalizedLevenshtein();

        Map<String, Integer> vector1;
        Map<String, Integer> vector2;
        vector1 = readVector(FileOperator.readFileByCharacter("C:\\Users\\xiahao\\Desktop\\anatomyMainWords"));
        vector2 = readVector(FileOperator.readFileByCharacter("C:\\Users\\xiahao\\Desktop\\anatomyURL"));

        for (Map.Entry<String, Integer> entry1 : vector1.entrySet()){
            for (Map.Entry<String, Integer> entry2 : vector2.entrySet()){
                if ((1-l.distance(entry1.getKey(), entry2.getKey()))>=0.5) {
                    System.out.println(entry1.getKey() + " " + entry2.getKey() + " " + (1 - l.distance(entry1.getKey(),
                            entry2.getKey())));
                }
            }
        }
    }
}