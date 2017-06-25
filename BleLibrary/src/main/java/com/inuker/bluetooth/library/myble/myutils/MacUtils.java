package com.inuker.bluetooth.library.myble.myutils;

/**
 * 描述：
 * 作者：Wu on 2017/6/25 22:20
 * 邮箱：wuwende@live.cn
 */

public class MacUtils {


    /**
     * mac 最后加一
     * @param mac
     * @return
     */
    public static String getMacIncrease(String mac){
        String[] strMac = mac.split(":");
        int lastValue = Integer.parseInt(strMac[strMac.length-1], 16);
        byte  lastByte = (byte)(lastValue & 0xff);
        lastByte += 1;
        String lastHex = Integer.toHexString(lastByte & 0xFF);
        String mMac = "";
        for(int i = 0;i<strMac.length-1;i++){
            if(i == strMac.length-1){
                mMac +=strMac[i];
            }else {
                mMac +=strMac[i] + ":";
            }
        }
        if(lastHex.length()==1){
            lastHex = "0" + lastHex;
        }
        mMac += lastHex;
        return mMac.toUpperCase();
    }

}
