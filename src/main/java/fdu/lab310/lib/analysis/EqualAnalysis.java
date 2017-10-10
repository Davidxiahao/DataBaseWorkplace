package fdu.lab310.lib.analysis;

import com.xiahao.lib.FileOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EqualAnalysis {
    public static void main(String[] args) {
        List<String> input = new ArrayList<>();
        input.addAll(FileOperator.readFileByCharacter("C:\\Users\\xiahao\\Downloads\\xsop_gp.csv"));

        List<String> output = new ArrayList<>();

        input.forEach(line -> {
            String webIdentities = line.split("\\\"", -1)[5];
            String codeIdentities = line.split("\\\"", -1)[9];

            List<String> wordsOfWeb = new ArrayList<>();
            wordsOfWeb.addAll(Arrays.asList(webIdentities.split(";")));
//            List<String> wordsOfCode = new ArrayList<>();
//            wordsOfCode.addAll(Arrays.asList(codeIdentities.split(";")));

            if (contain(wordsOfWeb, codeIdentities)){
                output.add("\"" + webIdentities + "\"" + "," + "\"" + codeIdentities + "\"");
            }
        });

        FileOperator.putLinesToFile("result.csv", String.join("\n", output));
    }

    private static boolean contain(List<String> list, String string){
        boolean result = false;
        for (String word : list){
            if (string.contains(word)){
                result = true;
                return result;
            }
        }
        return result;
    }
}
