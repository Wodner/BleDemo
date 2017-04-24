package com.inuker.bluetooth.library.myble;

import android.content.Context;
import android.util.Log;

import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.myble.callback.BleBateryListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentStatusListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentStepListener;
import com.inuker.bluetooth.library.myble.callback.BleDfuModelListener;
import com.inuker.bluetooth.library.myble.callback.BleDisableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleEnableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleFinishSittingListener;
import com.inuker.bluetooth.library.myble.callback.BleMotorFlagListener;
import com.inuker.bluetooth.library.myble.callback.BleSetMotorShockListener;
import com.inuker.bluetooth.library.myble.myutils.BitOperator;
import com.inuker.bluetooth.library.myble.myutils.HexStringUtils;

import java.text.SimpleDateFormat;

/**
 * 描述：
 * 作者：Wu on 2017/4/22 20:52
 * 邮箱：wuwende@live.cn
 */

public class BleRequest {


    private  final String TAG = "BleRequest";
    public BleRequest() {
    }


    public interface SynFinishListener{
        void finishSyn(boolean isFinish);
    }


    /**
     * 读取当前步数
     * @param context
     * @param mac
     * @param listener
     */
    public void getCurrentStep(Context context, String mac, BleCurrentStepListener listener){
        BLE.setOnCurrentStepListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F103D8");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送读取不是命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送读取不是命令失败 --  " + code);
                }
            }
        });


    }



    /**
     * 打开或者关闭震动  F5 是打开 F0是关闭  F5 加时间才有效 F0 没必要加时间 （最多120s）
     * @param context
     * @param mac
     * @param isEnable
     * @param time
     * @param listener
     */
    public void setMotorShock(Context context, String mac, boolean isEnable,int time ,BleSetMotorShockListener listener){
        BLE.setOnMotorShockListener(listener);
        if(isEnable){
            String hexTime = Integer.toHexString(time);
            if ((hexTime.length() & 0x01) != 0) {//奇数
                hexTime = "0"+ hexTime;

            }
            Log.d(TAG,time + "  发送取消激活命令成功 --  " + hexTime);
            byte [] msg = HexStringUtils.hexString2Bytes("F105DAF5"+ hexTime);
            ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (code==0){
                        Log.d(TAG,"发送设置马达震动成功 --  " + code);
                    }else {
                        Log.d(TAG,"发送设置马达震动失败 --  " + code);
                    }
                }
            });
        }else {
            byte [] msg = HexStringUtils.hexString2Bytes("F104DAF0");
            ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (code==0){
                        Log.d(TAG,"发送关闭马达成功 --  " + code);
                    }else {
                        Log.d(TAG,"发送关闭马达失败 --  " + code);
                    }
                }
            });
        }
    }



    /**
     * 取消激活
     * @param context
     * @param mac
     * @param listener
     */
    public void setDisableDevice(Context context, String mac, BleDisableDeviceListener listener){
        BLE.setOnDisableDeviceListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F104D7B3");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送取消激活命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送取消激活命令失败 --  " + code);
                }
            }
        });
    }


    /**
     * 激活
     * @param context
     * @param mac
     * @param listener
     */
    public void setEnableDevice(Context context, String mac, BleEnableDeviceListener listener){
        BLE.setOnEnableDeviceListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F104D6B2");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送激活命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送激活命令失败 --  " + code);
                }
            }
        });
    }




    /**
     * 首次同步接收到每天历史坐姿 数据 都要应答给设备 表示已经收到数据
     * @param context
     * @param mac
     * @param listener
     */
    public  void setFinishSittingSyn(Context context, String mac, final BleFinishSittingListener listener){
        byte [] msg = HexStringUtils.hexString2Bytes("F103D3");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送完成同步历史坐姿成功 --  " + code);
                    listener.onFinish(true);
                }else {
                    Log.d(TAG,"发送完成同步历史坐姿失败 --  " + code);
                    listener.onFinish(false);
                }
            }
        });
    }



    /**
     * 获取当前坐姿数据
     * @param context
     * @param mac
     * @param listener
     */
    public  void getCurrentSttingStatus(Context context, String mac, BleCurrentStatusListener listener){
        BLE.setOnCurrentStatusListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F103D9");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送同步当前坐姿成功 --  " + code);
                }else {
                    Log.d(TAG,"发送同步当前坐姿失败 --  " + code);
                }
            }
        });
    }


    /**
     * 发送空中升级指令
     * @param context
     * @param mac
     * @param listener
     */
    public  void setDfuModel(Context context, String mac, final BleDfuModelListener listener){
        byte [] msg = HexStringUtils.hexString2Bytes("F103B1");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送设置空中升级成功 --  " + code);
                    listener.onDfuModel(true);
                }else {
                    Log.d(TAG,"发送设置空中升级失败 --  " + code);
                    listener.onDfuModel(false);
                }
            }
        });
    }




    /**
     * 读取马达震动标志
     *
     * @param context
     * @param mac
     * @param listener
     */
    public  void getMotorShockFlag(Context context, String mac, BleMotorFlagListener listener){
        BLE.setOnMotorListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F103DB");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送成功 --  " + code);
                }else {
                    Log.d(TAG,"发送失败 --  " + code);
                }
            }
        });
    }




    /**
     * 读取电量
     * @param context
     * @param mac
     * @param listener
     */
    public  void getBattery(Context context, String mac, BleBateryListener listener){
        BLE.setOnBateryListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F103D5");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送成功 --  " + code);
                }else {
                    Log.d(TAG,"发送失败 --  " + code);
                }
            }
        });

    }





    /**
     * 首次连接同步数据完成
     * @param context
     * @param mac
     * @param listener
     */
    public  void finishSyn(Context context, String mac, final SynFinishListener listener){
        byte [] msg = HexStringUtils.hexString2Bytes("F103D4");

        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送数据交换成功 --  " + code);
                    listener.finishSyn(true);
                }else {
                    Log.d(TAG,"发送数据交换失败 --  " + code);
                    listener.finishSyn(false);
                }
            }
        });
    }



    /**
     * 首次同步要发送时间下去同步
     * @param context
     * @param mac
     */
    public  void synTime(Context context, String mac){
        byte []  head = new byte[3];
        head[0] = BitOperator.integerTo1Byte(0xF1);
        head[1] = BitOperator.integerTo1Byte(0x0A);
        head[2] = BitOperator.integerTo1Byte(0xD1);
        byte [] body = getCurrentTime();
        byte[] msg = new byte[head.length + body.length];
        System.arraycopy(head,0,msg,0,head.length);
        System.arraycopy(body,0,msg,head.length,body.length);
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送同步时间成功 --  " + code);
                }else {
                    Log.d(TAG,"发送同步时间失败 --  " + code);
                }
            }
        });
    }

    /**
     * @return 返回时间
     */
    private  byte[] getCurrentTime(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = sDateFormat.format(new java.util.Date()).replace("-","");
        byte ret[] = new byte[time.length() / 2];
        String [] buffer = new String[time.length()];
        for(int n=0;n<buffer.length;n++){
            buffer[n] = time.substring(n,n+1);
        }
        for (int i = 0; i < ret.length; i++) {
            int high = Integer.valueOf(buffer[2 * i])*10;
            int low = Integer.valueOf(buffer[2 * i + 1]);
            ret[i] = BitOperator.integerTo1Byte(high+low);
        }
        return ret;
    }


    /**
     * 打开默认的通知
     * @param context
     * @param mac
     */
    public void openDeafultNotify(Context context,String mac){
        ClientManager.getClient(context).notify(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_NOTIFY_UUID, BLE.mNotifyRsp);
    }

    /**
     * 关闭默认的通知
     * @param context
     * @param mac
     */
    public void closeDeafultNotify(Context context,String mac){
        ClientManager.getClient(context).unnotify(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_NOTIFY_UUID, BLE.mUnnotifyRsp);
    }







    /**
     * 打开另一个服务的通知
     * @param context
     * @param mac
     */
    public void openOtherNotify(Context context,String mac){
        ClientManager.getClient(context).notify(mac, MyConstant.OTHER_SERVICE_UUID, MyConstant.OTHER_CHARACTERISTIC_NOTIFY_UUID, BLE.mOtherNotifyRsp);
    }

    /**
     * 关闭另一个服务的通知
     * @param context
     * @param mac
     */
    public void closeOtherNotify(Context context,String mac){
        ClientManager.getClient(context).unnotify(mac, MyConstant.OTHER_SERVICE_UUID, MyConstant.OTHER_CHARACTERISTIC_NOTIFY_UUID, BLE.mOtherUnnotifyRsp);
    }




    /**
     * 连接蓝牙
     * @param context
     * @param mac
     * @param response
     */
    public void connect(Context context,String mac,BleConnectResponse response){
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();
        ClientManager.getClient(context).connect(mac, options,response);
    }


    /**
     * 断开连接
     * @param context
     * @param mac
     */
    public void disconnect(Context context,String mac){
        ClientManager.getClient(context).disconnect(mac);
    }


}
