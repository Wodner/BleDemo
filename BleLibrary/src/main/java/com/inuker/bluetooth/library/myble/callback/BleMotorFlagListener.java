package com.inuker.bluetooth.library.myble.callback;

/**
 * 描述：
 * 作者：Wu on 2017/4/22 23:37
 * 邮箱：wuwende@live.cn
 */

public interface BleMotorFlagListener {
    void onMotor(boolean isSuccess,String motorFlag,int second);
}
