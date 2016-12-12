package com.naruto.reserveclassroom.utils;
/*
 * Created by NARUTO on 2016/11/6.
 */

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class UserInfoUtil {

    //保存用户名密码
    public static boolean saveUserInfo(Context context, String userInputStr, String password) {

        try {

            String userinfo = userInputStr + "##" + password;

            //String savepath = "/data/data/com.naruto.day02_login/";
            String savepath =context.getFilesDir().getPath();
            File file  = new File(savepath, "userinfo.txt");
            FileOutputStream os = new FileOutputStream(file);
            os.write(userinfo.getBytes());
            os.close();

            return true;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public static Map<String, String> getUserInfo(Context context) {

        //String savepath = "/data/data/com.naruto.day02_login/";
        String savepath =context.getFilesDir().getPath();
        File file  = new File(savepath, "userinfo.txt");
        try {

            FileInputStream is = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String rl = br.readLine();
            String[] split = rl.split("##");
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("userInputStr", split[0]);
            hashMap.put("password", split[1]);

            is.close();
            br.close();

            return hashMap;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }
}
