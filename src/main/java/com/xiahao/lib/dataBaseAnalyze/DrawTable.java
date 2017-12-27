package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.SampleDbService;

public class DrawTable {
    public static void main(String[] args) {
        for (double similarity=0.0; similarity<1.0; similarity=similarity+0.05){
            System.out.println(similarity+" "+ SampleDbService.getInstance().getNumber(similarity));
        }
    }
}
