package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.OriginModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class newXSOP2old {
    public static void main(String[] args) {
        List<OriginModel> newXSOPList = OriginDbService.getInstance().getAllDataFromTable("last_origin_gp8w_meaningful_copy");
        List<OriginModel> oldXSOPList = OriginDbService.getInstance().getAllDataFromLastParsedOrigin();

        Map<String, OriginModel>  oldMap = new HashMap<>();
        for (OriginModel line : oldXSOPList){
            oldMap.put(line.webOrigins+line.codeOrigins, line);
        }

        int sum = 0;
        for (OriginModel line : newXSOPList){
            if (line.similarity<0.4 && oldMap.containsKey(line.webOrigins+line.codeOrigins) &&
                    oldMap.get(line.webOrigins+line.codeOrigins).isXSOP==0){
                System.out.println(line.idx+" "+line.declaringClass+" "+line.webOrigins+" "+line.keyWord+" "+line.similarity);
                sum++;
            }
        }

        System.out.println(sum);
    }
}
