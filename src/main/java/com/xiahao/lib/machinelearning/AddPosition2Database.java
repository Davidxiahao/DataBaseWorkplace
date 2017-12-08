package com.xiahao.lib.machinelearning;

import com.hankz.util.dbService.OriginDbService;
import com.hankz.util.dbutil.DCInformationModel;
import com.xiahao.lib.FileOperator;

import java.util.ArrayList;
import java.util.List;

public class AddPosition2Database {
    public static void main(String[] args) {
        List<DCInformationModel> dataBase = new ArrayList<>(OriginDbService.getInstance().getAllDCInformationData("DCInformation"));
        List<String> modelResultString = new ArrayList<>(FileOperator.readFileByCharacter("test.data"));

        List<Integer> positionResult = new ArrayList<>();
        for (String line : modelResultString){
            for (String string : line.split(" ")){
                if (!string.equals("")) {
                    positionResult.add((int)Double.parseDouble(string));
                }
            }
        }

        for (int i=0; i<positionResult.size(); i++){
            dataBase.get(i).model_choice = positionResult.get(i);
        }

        OriginDbService.getInstance().updateDCInformation(dataBase);
    }
}
