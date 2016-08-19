package com.example.yxb.secretary.utils;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.utils
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/18
 * MODIFY_BY:
 */
public class StringWriteToFile {

    public static void writeIntoFile(String s){
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,"test.txt");
        BufferedInputStream inputStream = null;
        FileOutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        int length = 0;
        inputStream = new BufferedInputStream(new ByteArrayInputStream(s.getBytes()));
        try {
            outputStream = new FileOutputStream(file,false);
            while ((length = inputStream.read(buffer)) != -1){
                outputStream.write(buffer);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
