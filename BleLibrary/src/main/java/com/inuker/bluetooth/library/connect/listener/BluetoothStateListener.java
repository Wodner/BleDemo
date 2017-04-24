package com.inuker.bluetooth.library.connect.listener;

import com.inuker.bluetooth.library.receiver.listener.BluetoothClientListener;



public abstract class BluetoothStateListener extends BluetoothClientListener {

    public abstract void onBluetoothStateChanged(boolean openOrClosed);

    @Override
    public void onSyncInvoke(Object...args) {
        boolean openOrClosed = (boolean) args[0];
        onBluetoothStateChanged(openOrClosed);
    }
}
