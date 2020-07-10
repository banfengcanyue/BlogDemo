package com.bfcy.blogdemo.encrypt;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Base64Util {

    public static String encodeString(String data, int flags) {
        return Base64.encodeToString(data.getBytes(), flags);
    }

    public static String decodeString(String data, int flags) {
        return new String(Base64.decode(data, flags));
    }

    public static String encodeFile(File file, int flags) {
        // "/storage/emulated/0/test_file.txt"
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                line = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(stringBuilder.toString().getBytes(), flags);
    }

    public static boolean decodeFile(String data, String path, int flags) {
        // "/storage/emulated/0/test_file_new.txt"
        File file = new File(path);
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            byte[] decodeBytes = Base64.decode(data.getBytes(), flags);
            bufferedWriter.write(new String(decodeBytes));
            bufferedWriter.flush();
            bufferedWriter.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
