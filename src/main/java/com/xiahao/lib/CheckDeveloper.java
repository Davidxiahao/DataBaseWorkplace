package com.xiahao.lib;

import com.hankz.util.dbService.GpDbService;
import com.hankz.util.dbutil.GooglePlayModel;

import java.util.*;

public class CheckDeveloper {
    public static List<GooglePlayModel> haveDifferentName(List<GooglePlayModel> data){
        List<GooglePlayModel> result = new ArrayList<>();

        data.forEach(line -> {
            Set<String> wordsInDevelopers = new HashSet<>();
            wordsInDevelopers.addAll(Arrays.asList(line.developers.split(" ")));
            boolean isContain = false;
            for (String word : wordsInDevelopers){
                if (line.pkg_name.contains(word)){
                    isContain = true;
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

        result.forEach(line -> {
            System.out.println("================================================");
            System.out.println(line.pkg_name);
            System.out.println(line.developers);
            System.out.println("================================================");
        });

        System.out.println(result.size());
        System.out.println(allRecords.size());
    }
}