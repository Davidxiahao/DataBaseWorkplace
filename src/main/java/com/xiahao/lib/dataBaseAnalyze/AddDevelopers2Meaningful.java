package com.xiahao.lib.dataBaseAnalyze;

import com.hankz.util.dbService.GpDbService;
import com.hankz.util.dbService.SampleDbService;
import com.hankz.util.dbutil.GooglePlayModel;
import com.hankz.util.dbutil.OriginModel;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDevelopers2Meaningful {
    public static void main(String[] args) {
        List<GooglePlayModel> googlePlayList = GpDbService.getInstance().getAllRecords();
        Map<String, String> apk2Developers = new HashMap<>();

        for (GooglePlayModel line : googlePlayList){
            apk2Developers.put(line.pkg_name, line.developers);
        }

        List<OriginModel> meaningfulList = AddPNandAN.getAllData();
        for (OriginModel line : meaningfulList){
            if (apk2Developers.containsKey(line.ApkName)){
                try {
                    line.developers = filterOffUtf8Mb4(apk2Developers.get(line.ApkName));
                }
                catch (Exception e){
                    System.out.println(e.toString());
                }
            }
        }

        SampleDbService.getInstance().updateDevelopers(meaningfulList);
    }

    public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes("utf-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }

            b += 256; // 去掉符号位

            if (((b >> 5) ^ 0x6) == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            } else if (((b >> 4) ^ 0xE) == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            } else if (((b >> 3) ^ 0x1E) == 0) {
                i += 4;
            } else if (((b >> 2) ^ 0x3E) == 0) {
                i += 5;
            } else if (((b >> 1) ^ 0x7E) == 0) {
                i += 6;
            } else {
                buffer.put(bytes[i++]);
            }
        }
        buffer.flip();
        return new String(buffer.array(), "utf-8");
    }
}
