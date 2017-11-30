package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.LibraryHashModel;
import com.hankz.util.dbutil.OriginModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepairLibrary {
    public static void main(String[] args) {
        List<OriginModel> listGp8wCopy3 = OriginDbService.getInstance().getAllDataFromTable("last_origin_gp8w_meaningful_copy3");
        List<LibraryHashModel> listHashCopy3 = OriginDbService.getInstance().getAllDataFromLastLibraryGp8wHashCopy3();

        Map<Integer, String> libraryMap = new HashMap<>();
        for (LibraryHashModel line : listHashCopy3){
            libraryMap.put(line.idx, line.declareClass);
        }

        for (OriginModel line : listGp8wCopy3){
            if (line.libNum!=0){
                System.out.println(line.declaringClass+" "+libraryMap.get(line.libNum));
            }
        }
    }
}
