package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.ResultDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.OriginModel;

import java.util.List;

public class CopyGroundTruth {
    public static void main(String[] args) {
        List<OriginModel> input = SampleDbService.getInstance().getGroundTruthFromlast_origin_gp8w_meaningful();
        ResultDbService.getInstance().updateGroundTruth(input);
    }
}
