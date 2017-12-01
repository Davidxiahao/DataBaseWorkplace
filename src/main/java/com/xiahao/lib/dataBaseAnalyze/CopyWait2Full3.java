package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.ggsearchModel;

import java.util.List;

public class CopyWait2Full3 {
    public static void main(String[] args) {
        List<ggsearchModel> waitList = OriginDbService.getInstance().getAllDataFromggsearch();

        int idx = 2770;
        for (ggsearchModel line : waitList){
            line.idx = idx;
            idx++;
        }

        OriginDbService.getInstance().insertIntoggsearch_full3(waitList);
    }
}
