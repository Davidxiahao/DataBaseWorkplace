package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.CompareModel;
import com.hankz.util.dbutil.OriginModel;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeANPNDC {
    public static void main(String[] args) {
        List<OriginModel> list = SampleDbService.getInstance().getAllDataFromlast_origin_gp8w_meaningful();

        List<CompareModel> result = new ArrayList<>();
        for (OriginModel line : list){
            if (line.libNum==0){
                String apkname = line.apk.replaceFirst("\\.apk", "");
                String PN = line.codeOrigins.split("\\[PN]")[1].split(";")[0];
                String AN = line.codeOrigins.split("\\[AN]")[1].split(";")[0];
                result.add(new CompareModel(line.idx, line.apk, apkname, line.declaringClass, PN, AN));
            }
        }

        ResultDbService.getInstance().createCompareTable();
        ResultDbService.getInstance().insertCompare(result);
    }
}
