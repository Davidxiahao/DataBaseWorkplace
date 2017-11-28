package com.xiahao.lib.machinelearning;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbutil.OriginModel;

import java.util.ArrayList;
import java.util.List;

public class gp8wDistribution {
    public static void main(String[] args) {
        List<OriginModel> dataBase = OriginDbService.getInstance().getAllDataFromTable("last_origin_gp8w_meaningful");
        List<OriginModel> result = new ArrayList<>();

        for (OriginModel line : dataBase){
            for (String string : line.webOrigins.split(";")){
                OriginModel temp = new OriginModel(line.apk, line.unit, line.declaringClass, string, line.codeOrigins,
                        line.webHelpInfo, line.codeHelpInfo);
                temp.libNum = line.libNum;
                temp.idx = line.idx;
                result.add(temp);
            }
        }

        ResultDbService.getInstance().createTablefinal_origin_gp8w_meaningful_copy();
        ResultDbService.getInstance().insertfinal_origin_gp8w_meaningful_copy(result);
    }
}
