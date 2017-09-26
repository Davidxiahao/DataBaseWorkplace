package com.xiahao.lib;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileOperator {

    public FileOperator(){}

    public static List<String> readFileByCharacter(String filePath){

        List<String> readResult = new ArrayList<>();

        try(
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fileInputStream, "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(reader);
            )
        {
            String input;
            while ((input = bufferedReader.readLine()) != null){
                readResult.add(input);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return readResult;
    }

    public static void putLinesToFile(String fileName, String lines) {
        try (FileWriter writer = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(writer))
        {
            bufferedWriter.write(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
