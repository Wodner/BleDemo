package com.inuker.bluetooth.library.myble;

import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;


public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient(Context context) {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(context);
                }
            }
        }
        return mClient;
    }
}
