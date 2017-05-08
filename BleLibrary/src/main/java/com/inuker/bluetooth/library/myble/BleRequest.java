package com.inuker.bluetooth.library.myble;

import android.content.Context;
import android.util.Log;

import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.myble.callback.BleBackwardAngleListener;
import com.inuker.bluetooth.library.myble.callback.BleBateryListener;
import com.inuker.bluetooth.library.myble.callback.BleCalibrateSitPositionListener;
import com.inuker.bluetooth.library.myble.callback.BleClearCalibrateSitPositionListener;
import com.inuker.bluetooth.library.myble.callback.BleClearDataListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentStepListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentUserStatusListener;
import com.inuker.bluetooth.library.myble.callback.BleDefaultNotifyListener;
import com.inuker.bluetooth.library.myble.callback.BleDfuModelListener;
import com.inuker.bluetooth.library.myble.callback.BleDisableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleEnableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleForwardAngleListener;
import com.inuker.bluetooth.library.myble.callback.BleHistorySitStatusSynListener;
import com.inuker.bluetooth.library.myble.callback.BleHistoryStepSynListener;
import com.inuker.bluetooth.library.myble.callback.BleHistoryUserStatusSynListener;
import com.inuker.bluetooth.library.myble.callback.BleLeftAngleListener;
import com.inuker.bluetooth.library.myble.callback.BleMotorFlagListener;
import com.inuker.bluetooth.library.myble.callback.BleOtherNotifyListener;
import com.inuker.bluetooth.library.myble.callback.BleRebootListener;
import com.inuker.bluetooth.library.myble.callback.BleRightAngleListener;
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
     * 清除BLE数据
     * @param context
     * @param mac
     * @param listener
     */
    public void setClearBleData(Context context, String mac,final BleClearDataListener listener){
        BLE.setOnBleClearDataListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F10395");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送清除数据命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送清除数据命令失败 --  " + code);
                    listener.onClearData(false);
                }
            }
        });

    }




    /**
     * 设置后倾角度
     * @param context
     * @param mac
     * @param angle  角度 0 ~ 90
     * @param listener
     */
    public void setRightAngle(Context context, String mac, int angle,final BleRightAngleListener listener){
        BLE.setOnBleRightAngleListener(listener);
        String hexAngle = Integer.toHexString(angle);
        if ((hexAngle.length() & 0x01) != 0) {//奇数
            hexAngle = "0"+ hexAngle;
        }
        byte [] msg = HexStringUtils.hexString2Bytes("F10493"+ hexAngle);
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送设置后倾角度命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送设置后倾角度命令失败 --  " + code);
                    listener.onRightAngle(false);
                }
            }
        });
    }

    /**
     * 设置后倾角度
     * @param context
     * @param mac
     * @param angle  角度 0 ~ 90
     * @param listener
     */
    public void setLeftAngle(Context context, String mac, int angle,final BleLeftAngleListener listener){
        BLE.setOnBleLeftAngleListener(listener);
        String hexAngle = Integer.toHexString(angle);
        if ((hexAngle.length() & 0x01) != 0) {//奇数
            hexAngle = "0"+ hexAngle;
        }
        byte [] msg = HexStringUtils.hexString2Bytes("F10492"+ hexAngle);
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送设置后倾角度命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送设置后倾角度命令失败 --  " + code);
                    listener.onLeftAngle(false);
                }
            }
        });
    }


    /**
     * 设置后倾角度
     * @param context
     * @param mac
     * @param angle  角度 0 ~ 90
     * @param listener
     */
    public void setBackwardAngle(Context context, String mac, int angle,final BleBackwardAngleListener listener){
        BLE.setOnBleBackwardAngleListener(listener);
        String hexAngle = Integer.toHexString(angle);
        if ((hexAngle.length() & 0x01) != 0) {//奇数
            hexAngle = "0"+ hexAngle;
        }
        byte [] msg = HexStringUtils.hexString2Bytes("F10491"+ hexAngle);
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送设置后倾角度命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送设置后倾角度命令失败 --  " + code);
                    listener.onBackwardAngle(false);
                }
            }
        });
    }


    /**
     * 设置前倾角度
     * @param context
     * @param mac
     * @param angle  角度 0 ~ 90
     * @param listener
     */
    public void setForwardAngle(Context context, String mac, int angle,final BleForwardAngleListener listener){
        BLE.setOnBleForwardAngleListener(listener);
        String hexAngle = Integer.toHexString(angle);
        if ((hexAngle.length() & 0x01) != 0) {//奇数
            hexAngle = "0"+ hexAngle;
        }
        byte [] msg = HexStringUtils.hexString2Bytes("F10490"+ hexAngle);
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送设置前倾角度命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送设置前倾角度命令失败 --  " + code);
                    listener.onForwardAngle(false);
                }
            }
        });
    }




    /**
     * 坐姿校准清除
     * @param context
     * @param mac
     * @param listener
     */
    public void setClearCalibrateSitPostion(Context context, String mac, final BleClearCalibrateSitPositionListener listener){
        BLE.setOnClearCalibrateSitPositionListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F103DD");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送坐姿校准清除命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送坐姿校准清除命令失败 --  " + code);
                    listener.onClearCalibrate(false);
                }
            }
        });
    }



    /**
     * 重启ble
     * @param context
     * @param mac
     * @param listener
     */
    public void setBleReboot(Context context, String mac, final BleRebootListener listener){
        byte [] msg = HexStringUtils.hexString2Bytes("F103B2");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送重启ble命令成功 --  " + code);
                    listener.onReboot(true);
                }else {
                    Log.d(TAG,"发送重启ble命令失败 --  " + code);
                    listener.onReboot(false);
                }
            }
        });


    }



    /**
     * 校准坐姿
     * @param context
     * @param mac
     * @param listener
     */
    public void setCalibrateSitPosition(Context context, String mac, final BleCalibrateSitPositionListener listener){
        BLE.setOnCalibrateSitPositionListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F103DC");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送校准坐姿命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送校准坐姿命令失败 --  " + code);
                    listener.onCalibrate(false);
                }
            }
        });
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
                    Log.d(TAG,"发送读取当前步数命令成功 --  " + code);
                }else {
                    Log.d(TAG,"发送读取当前步数命令失败 --  " + code);
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
    public  void setHistorySittingSynResponse(Context context, String mac, final BleHistorySitStatusSynListener listener){
        byte [] msg = HexStringUtils.hexString2Bytes("F103A4");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送完成同步历史坐姿成功 --  " + code);
                    listener.onReceive(true);
                }else {
                    Log.d(TAG,"发送完成同步历史坐姿失败 --  " + code);
                    listener.onReceive(false);
                }
            }
        });
    }




    /**
     * 首次同步接收到每天用户状态 数据 都要应答给设备 表示已经收到数据
     * @param context
     * @param mac
     * @param listener
     */
    public  void setHistoryUserSynResponse(Context context, String mac, final BleHistoryUserStatusSynListener listener){
        byte [] msg = HexStringUtils.hexString2Bytes("F103D3");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送完成同步历史用户状态成功 --  " + code);
                    listener.onFinish(true);
                }else {
                    Log.d(TAG,"发送完成同步历史用户状态失败 --  " + code);
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
    public  void getCurrentUserStatus(Context context, String mac, BleCurrentUserStatusListener listener){
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
    public  void getMotorShockFlag(Context context, String mac, final BleMotorFlagListener listener){
        BLE.setOnMotorListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F103DB");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送成功 --  " + code);
                }else {
                    Log.d(TAG,"发送失败 --  " + code);
                    listener.onMotor(false,"NAN",0);
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
    public  void getBattery(Context context, String mac, final BleBateryListener listener){
        BLE.setOnBateryListener(listener);
        byte [] msg = HexStringUtils.hexString2Bytes("F103D5");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送成功 --  " + code);
                }else {
                    Log.d(TAG,"发送失败 --  " + code);
                    listener.onBattery(false,-1);
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
     * 首次连接同步记忆步数数据应答
     * @param context
     * @param mac
     */
    public  void setHistoryStepSynResponse(Context context, String mac, final BleHistoryStepSynListener listener){
        byte [] msg = HexStringUtils.hexString2Bytes("F103D2");
        ClientManager.getClient(context).write(mac, MyConstant.SERVICE_UUID, MyConstant.CHARACTERISTIC_READ_WRITE_UUID, msg, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code==0){
                    Log.d(TAG,"发送同步记忆步数成功 --  " + code);
                    listener.onFinish(true);
                }else {
                    Log.d(TAG,"发送同步记忆步数失败 --  " + code);
                    listener.onFinish(false);
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
    public void openDeafultNotify(Context context, String mac, BleDefaultNotifyListener listener){
        BLE.setOnDefaultNotifyListener(listener);
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
    public void openOtherNotify(Context context, String mac, BleOtherNotifyListener listener){
        BLE.setOnOtherNotifyListener(listener);
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
