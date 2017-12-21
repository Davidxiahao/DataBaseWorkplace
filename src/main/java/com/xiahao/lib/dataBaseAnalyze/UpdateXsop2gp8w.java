package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.OriginModel;

import java.util.List;

public class UpdateXsop2gp8w {
    public static void main(String[] args) {
        List<OriginModel> oldList = SampleDbService.getInstance().getAllDataFromlast_origin_gp8w_meaningful();

        for (OriginModel line : oldList){
            if (line.similarity>=0.3){
                line.isXSOP=0;
            }
            else if (line.similarity>=0.0){
                line.isXSOP=1;
            }
            else line.isXSOP=-1;
        }

        SampleDbService.getInstance().updatelast_origin_gp8w_meaningful(oldList);
    }
}
