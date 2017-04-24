package com.inuker.bluetooth.library.myble;

import java.util.UUID;

/**
 * 描述：
 * 作者：Wu on 2017/4/16 16:43
 * 邮箱：wuwende@live.cn
 */

public class MyConstant {


    public static final UUID SERVICE_UUID = UUID.fromString("00000001-0000-1000-8000-00805f9b34fb");
    public static final UUID CHARACTERISTIC_NOTIFY_UUID = UUID.fromString("0000eea2-0000-1000-8000-00805f9b34fb");
    public static final UUID CHARACTERISTIC_READ_WRITE_UUID = UUID.fromString("0000eea1-0000-1000-8000-00805f9b34fb");




    public static final UUID OTHER_SERVICE_UUID = UUID.fromString("0000ffa0-0000-1000-8000-00805f9b34fb");
    public static final UUID OTHER_CHARACTERISTIC_NOTIFY_UUID = UUID.fromString("0000ffa2-0000-1000-8000-00805f9b34fb");
//    public static final UUID OTHER_CHARACTERISTIC_READ_WRITE_UUID = UUID.fromString("0000eea1-0000-1000-8000-00805f9b34fb");


}
