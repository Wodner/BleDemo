package com.inuker.bluetooth.library.myble;

import android.util.Log;

import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.myble.callback.BleBateryListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentStatusListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentStepListener;
import com.inuker.bluetooth.library.myble.callback.BleDisableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleEnableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleMotorFlagListener;
import com.inuker.bluetooth.library.myble.callback.BleSetMotorShockListener;
import com.inuker.bluetooth.library.myble.callback.BleSynDataListener;
import com.inuker.bluetooth.library.myble.myutils.BitOperator;
import com.inuker.bluetooth.library.myble.myutils.MyStringUtils;
import com.inuker.bluetooth.library.utils.ByteUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.UUID;

/**
 * 描述：
 * 作者：Wu on 2017/4/22 19:55
 * 邮箱：wuwende@live.cn
 */

public class BLE {


    private static final String TAG = "BLE_BLE";





    private static BleSynDataListener bleSynListener;
    private static BleBateryListener bleBateryListener;
    private static BleMotorFlagListener bleMotorListener;
    private static BleCurrentStatusListener bleCurrentStatusListener;
    private static BleEnableDeviceListener bleEnableDeviceListener;
    private static BleDisableDeviceListener bleDisableDeviceListener;

    private static BleSetMotorShockListener bleSetMotorShockListener;
    private static BleCurrentStepListener bleCurrentStepListener;



    public static void setOnBleSynListener(BleSynDataListener listener){
        bleSynListener = listener;
    }

    public static void setOnBateryListener(BleBateryListener listener){
        bleBateryListener = listener;
    }
    public static void setOnMotorListener(BleMotorFlagListener listener){
        bleMotorListener = listener;
    }

    public static void setOnCurrentStatusListener(BleCurrentStatusListener listener){
        bleCurrentStatusListener = listener;
    }

    public static void setOnEnableDeviceListener(BleEnableDeviceListener listener){
        bleEnableDeviceListener = listener;
    }
    public static void setOnDisableDeviceListener(BleDisableDeviceListener listener){
        bleDisableDeviceListener = listener;
    }


    public static void setOnMotorShockListener(BleSetMotorShockListener listener){
        bleSetMotorShockListener = listener;
    }

    public static void setOnCurrentStepListener(BleCurrentStepListener listener){
        bleCurrentStepListener = listener;
    }




    private static int sittingMonth;
    private static int sittingDay;
    private static int sittingFrame;
    private static int sittingPoints;
    private static int pointLength;
    private static byte[]  pointsArray;

    /**
     * BLE 打开notify 后回调
     */
    public static final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {

            if (service.equals(MyConstant.SERVICE_UUID) && character.equals(MyConstant.CHARACTERISTIC_NOTIFY_UUID)) {
                String result = ByteUtils.byteToString(value);
                String msgHead = result.substring(0,2).toUpperCase();
                String msgID = result.substring(4,6).toUpperCase();
                Log.w(TAG,  " 收到通知信息头 ------ " + msgHead  + msgID + " ----- "+ "\n");
                Log.d(TAG," 收到通知信息 ------ " + Arrays.toString(value)  + "\n"  +String.format("%s", ByteUtils.byteToString(value))+ "\n" );

                if(msgHead.equals("FE") && msgID.equals("E1")){ //首次同步 电池电量 马达标志位，版本号
                    byte[] battery = new byte[1] ;
                    System.arraycopy(value,3,battery,0,battery.length);
                    byte[] motorFlag = new byte[1] ;
                    System.arraycopy(value,4,motorFlag,0,motorFlag.length);
                    byte[] firmwareVersion = new byte[value.length-5];
                    System.arraycopy(value,5,firmwareVersion,0,firmwareVersion.length);
                    if (bleSynListener!=null){
                            String jsonresult = "";
                            try {
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("code","0");
                                jsonObj.put("batery",  (BitOperator.byteToInteger(battery) + "%"));
                                jsonObj.put("monitorflag", ByteUtils.byteToString(motorFlag));
                                jsonObj.put("firmwareversion", MyStringUtils.ascii2String(firmwareVersion));
                                jsonresult = jsonObj.toString();//生成返回字符串
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        bleSynListener.onFirstResponse(jsonresult);
                        bleSynListener.startSynTime(true);
                    }
                }


                else if(msgHead.equals("FE") && msgID.equals("E3")){//同步历史坐姿数据
                    byte[] currentIndex = new byte[1] ;
                    System.arraycopy(value,3,currentIndex,0,currentIndex.length);
                    int index = BitOperator.byteToInteger(currentIndex);

                    if(index==0){
                        byte[] month = new byte[1] ;
                        System.arraycopy(value,4,month,0,month.length);
                        sittingMonth = BitOperator.byteToInteger(month);

                        byte[] day = new byte[1] ;
                        System.arraycopy(value,5,day,0,day.length);
                        sittingDay = BitOperator.byteToInteger(day);

                        byte[] count = new byte[1] ;
                        System.arraycopy(value,6,count,0,count.length);
                        sittingPoints = BitOperator.byteToInteger(count);

                        byte[] num = new byte[1] ;
                        System.arraycopy(value,7,num,0,num.length);
                        sittingFrame = BitOperator.byteToInteger(num);

                        pointLength=0;
                        pointsArray = new byte[sittingPoints*3];
                    }else {
//                        Log.d(TAG,"总点数：" + sittingPoints + " 当前帧 ： "  + index + " 帧数 ：" + sittingFrame + " --- " + Arrays.toString(currentIndex));
                        byte[] points = new byte[value.length-4];
                        System.arraycopy(value,4,points,0,points.length);
                        if ((index  < sittingFrame)) {
                            System.arraycopy(points,0,pointsArray,pointLength,points.length);
                            pointLength = pointLength + points.length;
                        }else if(index == sittingFrame){
                            System.arraycopy(points,0,pointsArray,pointLength,points.length);
                            pointLength = pointLength + points.length;
                            String jsonresult = "";
                            try {
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("code",0);
                                jsonObj.put("month",  sittingMonth);
                                jsonObj.put("day", sittingDay);
                                JSONArray jsonArr = new JSONArray();//json格式的数组
                                for (int i=0;i<pointLength/3;i++){
                                    byte [] hour = new byte[1];
                                    hour[0] = pointsArray[(i*3)];
                                    byte [] minute = new byte[1];
                                    minute[0] = pointsArray[(i*3+1)];
                                    byte [] status = new byte[1] ;
                                    status[0] = pointsArray[(i*3+2)];
                                    JSONObject jsonObjArr = new JSONObject();
                                    jsonObjArr.put("hour", BitOperator.byteToInteger(hour));
                                    jsonObjArr.put("minute", BitOperator.byteToInteger(minute));
                                    jsonObjArr.put("status", getStatus(BitOperator.byteToInteger(status)));
                                    jsonArr.put(jsonObjArr);//将json格式的数据放到json格式的数组里
                                }
                                jsonObj.put("rows", jsonArr);//再将这个json格式的的数组放到最终的json对象中。
                                jsonresult = jsonObj.toString();//生成返回字符串
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            bleSynListener.onSittingSyn(jsonresult);
                        }
                    }
                }



                else if(msgHead.equals("FE") && msgID.equals("E4")){//数据交换完成
                    bleSynListener.sendFinishSyn();
                }

                else if(msgHead.equals("FE") && msgID.equals("E5")){//读取电量返回
                    byte[] battery = new byte[1] ;
                    System.arraycopy(value,3,battery,0,battery.length);
                    if (bleBateryListener!=null){
                        bleBateryListener.onBattery(BitOperator.byteToInteger(battery));
                    }
                }


                else if (msgHead.equals("FE") && msgID.equals("EB")){//读取马达震动标志位
                    byte[] motorFlag = new byte[1] ;
                    System.arraycopy(value,3,motorFlag,0,motorFlag.length);

                    byte[] second = new byte[1] ;
                    System.arraycopy(value,4,second,0,second.length);
                    bleMotorListener.onMotor(ByteUtils.byteToString(motorFlag),BitOperator.byteToInteger(second));
                }


                else if(msgHead.equals("FE") && msgID.equals("E9")){//同步当前坐姿数据返回信息
                    byte[] currentIndex = new byte[1] ;
                    System.arraycopy(value,3,currentIndex,0,currentIndex.length);
                    int index = BitOperator.byteToInteger(currentIndex);

                    if(index==0){
                        byte[] month = new byte[1] ;
                        System.arraycopy(value,4,month,0,month.length);
                        sittingMonth = BitOperator.byteToInteger(month);

                        byte[] day = new byte[1] ;
                        System.arraycopy(value,5,day,0,day.length);
                        sittingDay = BitOperator.byteToInteger(day);

                        byte[] count = new byte[1] ;
                        System.arraycopy(value,6,count,0,count.length);
                        sittingPoints = BitOperator.byteToInteger(count);

                        byte[] num = new byte[1] ;
                        System.arraycopy(value,7,num,0,num.length);
                        sittingFrame = BitOperator.byteToInteger(num);

                        pointLength=0;
                        pointsArray = new byte[sittingPoints*3];
                    }else {
//                        Log.d(TAG,"总点数：" + sittingPoints + " 当前帧 ： "  + index + " 帧数 ：" + sittingFrame + " --- " + Arrays.toString(currentIndex));
                        byte[] points = new byte[value.length-4];
                        System.arraycopy(value,4,points,0,points.length);
                        if ((index  < sittingFrame)) {
                            System.arraycopy(points,0,pointsArray,pointLength,points.length);
                            pointLength = pointLength + points.length;
                        }else if(index == sittingFrame){
                            System.arraycopy(points,0,pointsArray,pointLength,points.length);
                            pointLength = pointLength + points.length;
                            String jsonresult = "";
                            try {
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("code",0);
                                jsonObj.put("month",  sittingMonth);
                                jsonObj.put("day", sittingDay);
                                JSONArray jsonArr = new JSONArray();//json格式的数组
                                for (int i=0;i<pointLength/3;i++){
                                    byte [] hour = new byte[1];
                                    hour[0] = pointsArray[(i*3)];
                                    byte [] minute = new byte[1];
                                    minute[0] = pointsArray[(i*3+1)];
                                    byte [] status = new byte[1] ;
                                    status[0] = pointsArray[(i*3+2)];
                                    JSONObject jsonObjArr = new JSONObject();
                                    jsonObjArr.put("hour", BitOperator.byteToInteger(hour));
                                    jsonObjArr.put("minute", BitOperator.byteToInteger(minute));
                                    jsonObjArr.put("status", getStatus(BitOperator.byteToInteger(status)));
                                    jsonArr.put(jsonObjArr);//将json格式的数据放到json格式的数组里
                                }
                                jsonObj.put("rows", jsonArr);//再将这个json格式的的数组放到最终的json对象中。
                                jsonresult = jsonObj.toString();//生成返回字符串
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            bleCurrentStatusListener.onCurrentStatus(jsonresult);
                        }
                    }
                }


                else if(msgHead.equals("FE") && msgID.equals("E6")){//设备激活成功返回
                    bleEnableDeviceListener.onEnable(true);
                }

                else if(msgHead.equals("FE") && msgID.equals("E7")){//取消设备激活成功返回
                    bleDisableDeviceListener.onDisable(true);
                }

                else if(msgHead.equals("FE") && msgID.equals("EA")){//设置马达震动应答
                    bleSetMotorShockListener.onSetMotorShock(true);
                }

                else if(msgHead.equals("FE") && msgID.equals("E8")){
                    bleCurrentStepListener.onStep("" + Arrays.toString(value));
                }



            }
        }

        @Override
        public void onResponse(int code) {
            if (code == 0) {
                Log.d(TAG,"打开通知成功 --  " + code);
            } else {
                Log.d(TAG,"打开通知失败 --  " + code);
            }
        }
    };



    /**
     * 关闭 notify 回调
     */
    public static final BleUnnotifyResponse mUnnotifyRsp = new BleUnnotifyResponse() {
        @Override
        public void onResponse(int code) {
            if (code == 0) {
                Log.d(TAG,"关闭通知成功 --  " + code);
            } else {
                Log.d(TAG,"关闭通知失败 --  " + code);
            }
        }
    };





    /**
     * BLE 打开另一个服务的notify 后回调
     */
    public static final BleNotifyResponse mOtherNotifyRsp = new BleNotifyResponse() {

        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(MyConstant.OTHER_SERVICE_UUID) && character.equals(MyConstant.OTHER_CHARACTERISTIC_NOTIFY_UUID)) {
                Log.d(TAG,"另一个服务通知返回信息 --  " + Arrays.toString(value));
            }


        }

        @Override
        public void onResponse(int code) {
            if (code == 0) {
                Log.d(TAG,"打开另一个服务通知成功 --  " + code);
            } else {
                Log.d(TAG,"打开另一个服务通知失败 --  " + code);
            }
        }
    };

    /**
     * 关闭 另一个服务的notify 回调
     */
    public static final BleUnnotifyResponse mOtherUnnotifyRsp = new BleUnnotifyResponse() {
        @Override
        public void onResponse(int code) {
            if (code == 0) {
                Log.d(TAG,"关闭另一个服务通知成功 --  " + code);
            } else {
                Log.d(TAG,"关闭另一个服务通知失败 --  " + code);
            }
        }
    };



    private static String getStatus(int status){
        String s="";
        if (status==0){
            s = "未知";
        }else if(status==1){
            s = "坐/站立";
        }else if(status==2){
            s = "躺";
        }else if(status==3){
            s = "走";
        }else {
            s = "跑";
        }
        return s;
    }



}
