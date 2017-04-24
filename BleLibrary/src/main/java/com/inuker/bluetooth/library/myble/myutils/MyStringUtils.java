package com.inuker.bluetooth.library.myble.myutils;

/**
 * 描述：
 * 作者：Wu on 2017/4/16 18:38
 * 邮箱：wuwende@live.cn
 */

public class MyStringUtils {


    /**
     * @param ascii
     * @return
     */
    public static String ascii2String(byte[] ascii){
        String result="";
        for (int i=0;i<ascii.length;i++){
            char c = (char)ascii[i];
            result = result+ c;
        }
        return result;
    }


    public static String string2HexString(String str){


        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<str.length();i++){
            int ch = (int)str.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();

    }




}
