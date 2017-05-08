package com.inuker.bluetooth.library.myble.callback;

/**
 * 描述：
 * 作者：Wu on 2017/4/22 20:07
 * 邮箱：wuwende@live.cn
 */

public interface BleSynDataListener {

    void onFirstResponse(String result);
    void startSynTime(boolean isStart);
    void sendFinishSyn();
    void onHistorySittingStatusSyn(String result);
    void onHistoryUserStatusSyn(String result);
    void onHistoryStepSyn(String result);

}
