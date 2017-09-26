package com.xiahao.lib;

import com.hankz.util.dbService.GpDbService;
import com.hankz.util.dbutil.GooglePlayModel;

import java.util.*;

public class CheckDeveloper {
    private static List<String> sameWord = new ArrayList<>();
    //private static List<String> test = new ArrayList<>();

    public static List<GooglePlayModel> haveDifferentName(List<GooglePlayModel> data){
        List<GooglePlayModel> result = new ArrayList<>();

        data.forEach(line -> {
            Set<String> wordsInDevelopers = new HashSet<>();
            String normalizeDevelopers = line.developers.replaceAll("[^0-9a-zA-Z]", " ").toLowerCase();
            wordsInDevelopers.addAll(Arrays.asList(normalizeDevelopers.split(" ")));
            boolean isContain = false;
            //test.add(line.developers);
            for (String word : wordsInDevelopers){
                //test.add(word);
                if (word.length()>1 && line.pkg_name.toLowerCase().contains(word)){
                    isContain = true;
                    sameWord.add("================================================");
                    sameWord.add(line.pkg_name);
                    sameWord.add(line.developers);
                    sameWord.add("The same word is : " + word);
                    sameWord.add("================================================");
                    break;
                }
            }

            if (!isContain){
                result.add(line);
            }
        });

        return result;
    }

    public static void main(String[] args) {
        List<GooglePlayModel> result = new ArrayList<>();
        List<GooglePlayModel> allRecords = new ArrayList<>();

        allRecords.addAll(GpDbService.getInstance().getAllRecords());
        result.addAll(haveDifferentName(allRecords));

        List<String> output = new ArrayList<>();

        result.forEach(line -> {
            output.add("================================================");
            output.add(line.pkg_name);
            output.add(line.developers);
            output.add("================================================");
        });

        output.add(result.size()+"");
        output.add(allRecords.size()+"");
        output.add((float)result.size()/allRecords.size()+"");

        FileOperator.putLinesToFile("results/pkgname_and_developer", String.join("\n", output));
        FileOperator.putLinesToFile("results/same_word", String.join("\n", sameWord));
        //FileOperator.putLinesToFile("results/test", String.join("\n", test));
    }
}
