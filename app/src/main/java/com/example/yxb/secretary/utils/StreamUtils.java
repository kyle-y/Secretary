package com.example.yxb.secretary.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/6/8.
 */
public class StreamUtils {
    public static String inputStream2String (InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) !=null){
            stringBuilder.append(line).append("\r\n");
        }
        if (bufferedReader != null){
            bufferedReader.close();
        }
        if (inputStream != null){
            inputStream.close();
        }
        return stringBuilder.toString();
    }
}
