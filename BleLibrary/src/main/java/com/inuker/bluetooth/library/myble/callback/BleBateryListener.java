package com.inuker.bluetooth.library.myble.callback;

/**
 * 描述：
 * 作者：Wu on 2017/4/22 23:28
 * 邮箱：wuwende@live.cn
 */

public interface BleBateryListener {

    void onBattery(boolean isSuccess,int level);
}
