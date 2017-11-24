package com.xiahao.lib.machinelearning;

import com.xiahao.lib.DCInformationStructure;

import java.util.Map;

import static com.xiahao.lib.DCInformation.getAllData;

public class DC2UrlSimilarity {
    public static void main(String[] args) {
        Map<String, DCInformationStructure> result = getAllData();

        for (Map.Entry<String, DCInformationStructure> entry : result.entrySet()){
            for (String url : entry.getValue().URLs){
                // TODO: 2017/11/24 search the similarity to every pair of dc and url in the database
            }
        }
    }
}
