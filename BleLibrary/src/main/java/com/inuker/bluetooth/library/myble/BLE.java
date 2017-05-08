package com.inuker.bluetooth.library.myble;

import android.content.Context;
import android.util.Log;

import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.myble.callback.BleBackwardAngleListener;
import com.inuker.bluetooth.library.myble.callback.BleBateryListener;
import com.inuker.bluetooth.library.myble.callback.BleCalibrateSitPositionListener;
import com.inuker.bluetooth.library.myble.callback.BleClearCalibrateSitPositionListener;
import com.inuker.bluetooth.library.myble.callback.BleClearDataListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentStepListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentUserStatusListener;
import com.inuker.bluetooth.library.myble.callback.BleDefaultNotifyListener;
import com.inuker.bluetooth.library.myble.callback.BleDisableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleEnableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleForwardAngleListener;
import com.inuker.bluetooth.library.myble.callback.BleHistorySitStatusSynListener;
import com.inuker.bluetooth.library.myble.callback.BleHistoryStepSynListener;
import com.inuker.bluetooth.library.myble.callback.BleHistoryUserStatusSynListener;
import com.inuker.bluetooth.library.myble.callback.BleLeftAngleListener;
import com.inuker.bluetooth.library.myble.callback.BleMotorFlagListener;
import com.inuker.bluetooth.library.myble.callback.BleOtherNotifyListener;
import com.inuker.bluetooth.library.myble.callback.BleRightAngleListener;
import com.inuker.bluetooth.library.myble.callback.BleSetMotorShockListener;
import com.inuker.bluetooth.library.myble.callback.BleSynDataListener;
import com.inuker.bluetooth.library.myble.callback.BleUserStatusAndSittingStatusListener;
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
    private static BleCurrentUserStatusListener bleCurrentStatusListener;
    private static BleEnableDeviceListener bleEnableDeviceListener;
    private static BleDisableDeviceListener bleDisableDeviceListener;
    private static BleSetMotorShockListener bleSetMotorShockListener;
    private static BleCurrentStepListener bleCurrentStepListener;
    private static BleUserStatusAndSittingStatusListener bleUserStatusAndSittingStatusListener;
    private static BleDefaultNotifyListener bleDefaultNotifyListener;
    private static BleOtherNotifyListener bleOtherNotifyListener;
    private static BleCalibrateSitPositionListener bleCalibrateSitPositionListener;
    private static BleClearCalibrateSitPositionListener bleClarCalibrateSitPositionListener;


    private static Context mContext;
    private static String mMac;

    private static BleForwardAngleListener bleForwardAngleListener;
    private static BleBackwardAngleListener bleBackwardAngleListener;
    private static BleLeftAngleListener bleLeftAngleListener;
    private static BleRightAngleListener bleRightAngleListener;

    private static BleClearDataListener bleClearDataListener;


    public static void setOnBleClearDataListener(BleClearDataListener listener){
        bleClearDataListener = listener;
    }

    public static void setOnBleRightAngleListener(BleRightAngleListener listener){
        bleRightAngleListener = listener;
    }

    public static void setOnBleLeftAngleListener(BleLeftAngleListener listener){
        bleLeftAngleListener = listener;
    }

    public static void setOnBleBackwardAngleListener(BleBackwardAngleListener listener){
        bleBackwardAngleListener = listener;
    }

    public static void setOnBleForwardAngleListener(BleForwardAngleListener listener){
        bleForwardAngleListener = listener;
    }

    public static void setOnClearCalibrateSitPositionListener(BleClearCalibrateSitPositionListener listener){
        bleClarCalibrateSitPositionListener = listener;
    }

    public static void setOnCalibrateSitPositionListener(BleCalibrateSitPositionListener listener){
        bleCalibrateSitPositionListener = listener;
    }

    public static void setOnOtherNotifyListener(BleOtherNotifyListener listener){
        bleOtherNotifyListener = listener;
    }

    public static void setOnDefaultNotifyListener(BleDefaultNotifyListener listener){
        bleDefaultNotifyListener = listener;
    }

    public static void setOnBleSynListener(Context context,String mac,BleSynDataListener listener){
        bleSynListener = listener;
        mContext = context;
        mMac = mac;
    }

    public static void setOnBateryListener(BleBateryListener listener){
        bleBateryListener = listener;
    }

    public static void setOnMotorListener(BleMotorFlagListener listener){
        bleMotorListener = listener;
    }

    public static void setOnCurrentStatusListener(BleCurrentUserStatusListener listener){
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

    public static void setOnUserStatusAndSittingStatusListener(BleUserStatusAndSittingStatusListener listener){
        bleUserStatusAndSittingStatusListener = listener;
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
//                Log.d(TAG," 收到通知信息 ------ " + Arrays.toString(value)  + "\n"  +String.format("%s", ByteUtils.byteToString(value))+ "\n" );
                if(msgHead.equals("FE") && msgID.equals("E1")){ //首次同步 电池电量 马达标志位，版本号
                    Log.w(TAG,  " -------------   首次连接  返回电池电量版本号等-0 --    -----");
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
                        new BleRequest().synTime(mContext,mMac);
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("E2")){//同步记忆计步数据
                    Log.e(TAG, "同步历史步数... " + Arrays.toString(value));
                    byte[] year_h = new byte[1] ;
                    System.arraycopy(value,3,year_h,0,year_h.length);
                    byte[] year_l = new byte[1] ;
                    System.arraycopy(value,4,year_l,0,year_l.length);
                    String mYear = String.valueOf(BitOperator.byteToInteger(year_h)) + String.valueOf(BitOperator.byteToInteger(year_l));
                    byte[] month = new byte[1] ;
                    System.arraycopy(value,5,month,0,month.length);
                    String mMonth = String.valueOf(BitOperator.byteToInteger(month));
                    byte[] day = new byte[1] ;
                    System.arraycopy(value,6,day,0,day.length);
                    String mDay = String.valueOf(BitOperator.byteToInteger(day));
                    byte[] hour = new byte[1] ;
                    System.arraycopy(value,7,hour,0,hour.length);
                    String mHour = String.valueOf(BitOperator.byteToInteger(hour));
                    byte[] minute = new byte[1] ;
                    System.arraycopy(value,8,minute,0,minute.length);
                    String mMinute = String.valueOf(BitOperator.byteToInteger(minute));
                    byte[] second = new byte[1] ;
                    System.arraycopy(value,9,second,0,second.length);
                    String mSecond = String.valueOf(BitOperator.byteToInteger(second));


                    byte[] step_1 = new byte[1];
                    System.arraycopy(value,value.length-4,step_1,0,step_1.length);
                    byte[] step_2 = new byte[1];
                    System.arraycopy(value,value.length-3,step_2,0,step_2.length);
                    byte[] step_3 = new byte[1];
                    System.arraycopy(value,value.length-2,step_3,0,step_3.length);
                    byte[] step_4 = new byte[1];
                    System.arraycopy(value,value.length-1,step_4,0,step_4.length);

                    int step =  BitOperator.byteToInteger(step_4)<<24 | BitOperator.byteToInteger(step_3)<<16 |
                            BitOperator.byteToInteger(step_2)<<8 | BitOperator.byteToInteger(step_1);

                    String jsonresult = "";
                    try {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("code","0");
                        jsonObj.put("year",  mYear);
                        jsonObj.put("month", mMonth);
                        jsonObj.put("day", mDay);
                        jsonObj.put("hour", mHour);
                        jsonObj.put("minute", mMinute);
                        jsonObj.put("second", mSecond);
                        jsonObj.put("step", step);
                        jsonresult = jsonObj.toString();//生成返回字符串
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (bleSynListener!=null){
                        bleSynListener.onHistoryStepSyn(jsonresult);
                        new BleRequest().setHistoryStepSynResponse(mContext, mMac, new BleHistoryStepSynListener() {
                            @Override
                            public void onFinish(boolean isFinish) {
                                Log.d(TAG, "同步历史步数完成标志... " + isFinish);
                            }
                        });
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("E3")){//同步史用户状态数据
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
                                    jsonObjArr.put("status", getUserStatus(BitOperator.byteToInteger(status)));
                                    jsonArr.put(jsonObjArr);//将json格式的数据放到json格式的数组里
                                }
                                jsonObj.put("rows", jsonArr);//再将这个json格式的的数组放到最终的json对象中。
                                jsonresult = jsonObj.toString();//生成返回字符串
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            if(bleSynListener!=null){
                                bleSynListener.onHistoryUserStatusSyn(jsonresult);
                                new BleRequest().setHistoryUserSynResponse(mContext, mMac, new BleHistoryUserStatusSynListener() {
                                    @Override
                                    public void onFinish(boolean isFinish) {
                                        Log.d(TAG, "同步历史用户状态完成标志... " + isFinish);
                                    }
                                });
                            }
                        }
                    }
                }


                else if(msgHead.equals("FE") && msgID.equals("94")){//历史坐姿数据
                    byte[] year_h = new byte[1] ;
                    System.arraycopy(value,3,year_h,0,year_h.length);
                    byte[] year_l = new byte[1] ;
                    System.arraycopy(value,4,year_l,0,year_l.length);
                    String mYear = String.valueOf(BitOperator.byteToInteger(year_h)) + String.valueOf(BitOperator.byteToInteger(year_l));

                    byte[] month = new byte[1] ;
                    System.arraycopy(value,5,month,0,month.length);
                    String mMonth = String.valueOf(BitOperator.byteToInteger(month));

                    byte[] day = new byte[1] ;
                    System.arraycopy(value,6,day,0,day.length);
                    String mDay = String.valueOf(BitOperator.byteToInteger(day));



                    byte[] sitting_l = new byte[1];
                    System.arraycopy(value,7,sitting_l,0,sitting_l.length);
                    byte[] sitting_h = new byte[1];
                    System.arraycopy(value,8,sitting_h,0,sitting_h.length);

                    int s_h =  BitOperator.byteToInteger(sitting_h)<<8;
                    int s_l =  BitOperator.byteToInteger(sitting_l);
                    int sitting = s_h|s_l;



                    byte[] forward_l = new byte[1];
                    System.arraycopy(value,9,forward_l,0,forward_l.length);
                    byte[] forward_h = new byte[1];
                    System.arraycopy(value,10,forward_h,0,forward_h.length);

                    int f_h =  BitOperator.byteToInteger(forward_h)<<8;
                    int f_l =  BitOperator.byteToInteger(forward_l);
                    int forward = f_h|f_l;



                    byte[] backward_l = new byte[1];
                    System.arraycopy(value,11,backward_l,0,backward_l.length);
                    byte[] backward_h = new byte[1];
                    System.arraycopy(value,12,backward_h,0,backward_h.length);

                    int b_h =  BitOperator.byteToInteger(backward_h)<<8;
                    int b_l =  BitOperator.byteToInteger(backward_l);
                    int backward = b_h|b_l;



                    byte[] left_leaning_l = new byte[1];
                    System.arraycopy(value,13,left_leaning_l,0,left_leaning_l.length);
                    byte[] left_leaning_h = new byte[1];
                    System.arraycopy(value,14,left_leaning_h,0,left_leaning_h.length);

                    int l_h =  BitOperator.byteToInteger(left_leaning_h)<<8;
                    int l_l =  BitOperator.byteToInteger(left_leaning_l);
                    int leftLeaning = l_h|l_l;



                    byte[] right_leaning_l = new byte[1];
                    System.arraycopy(value,15,right_leaning_l,0,right_leaning_l.length);
                    byte[] rigth_leaning_h = new byte[1];
                    System.arraycopy(value,16,rigth_leaning_h,0,rigth_leaning_h.length);

                    int r_h =  BitOperator.byteToInteger(rigth_leaning_h)<<8;
                    int r_l =  BitOperator.byteToInteger(right_leaning_l);
                    int rightLeaning = r_h|r_l;

                    String jsonresult = "";
                    try {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("code","0");
                        jsonObj.put("year",  mYear);
                        jsonObj.put("month", mMonth);
                        jsonObj.put("day", mDay);
                        jsonObj.put("sitting", sitting);
                        jsonObj.put("forward", forward);
                        jsonObj.put("backward", backward);
                        jsonObj.put("leftLeaning", leftLeaning);
                        jsonObj.put("rightLeaning", rightLeaning);
                        jsonresult = jsonObj.toString();//生成返回字符串
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(bleSynListener!=null){
                        bleSynListener.onHistorySittingStatusSyn(jsonresult);
                        new BleRequest().setHistorySittingSynResponse(mContext, mMac, new BleHistorySitStatusSynListener() {
                            @Override
                            public void onReceive(boolean isFinish) {
                                Log.d(TAG, "同步历史历史坐姿完成标志... " + isFinish);
                            }
                        });
                    }


                }


                else if(msgHead.equals("FE") && msgID.equals("E4")){//数据交换完成
                    if(bleSynListener!=null){
                        new BleRequest().finishSyn(mContext, mMac, new BleRequest.SynFinishListener() {
                            @Override
                            public void finishSyn(boolean isFinish) {
                                Log.d(TAG, "同步完成标志... " + isFinish);
                            }
                        });
                        bleSynListener.sendFinishSyn();
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("E5")){//读取电量返回
                    byte[] battery = new byte[1] ;
                    System.arraycopy(value,3,battery,0,battery.length);
                    if (bleBateryListener!=null){
                        bleBateryListener.onBattery(true,BitOperator.byteToInteger(battery));
                    }
                }

                else if (msgHead.equals("FE") && msgID.equals("EB")){//读取马达震动标志位
                    byte[] motorFlag = new byte[1] ;
                    System.arraycopy(value,3,motorFlag,0,motorFlag.length);

                    byte[] second = new byte[1] ;
                    System.arraycopy(value,4,second,0,second.length);
                    if(bleMotorListener!=null)
                         bleMotorListener.onMotor(true,ByteUtils.byteToString(motorFlag),BitOperator.byteToInteger(second));
                }

                else if(msgHead.equals("FE") && msgID.equals("E9")){//同步当前用户状态数据返回信息
//                    byte[] currentIndex = new byte[1] ;
//                    System.arraycopy(value,3,currentIndex,0,currentIndex.length);
//                    int index = BitOperator.byteToInteger(currentIndex);
//
//                    if(index==0){
//                        byte[] month = new byte[1] ;
//                        System.arraycopy(value,4,month,0,month.length);
//                        sittingMonth = BitOperator.byteToInteger(month);
//                        byte[] day = new byte[1] ;
//                        System.arraycopy(value,5,day,0,day.length);
//                        sittingDay = BitOperator.byteToInteger(day);
//                        byte[] count = new byte[1] ;
//                        System.arraycopy(value,6,count,0,count.length);
//                        sittingPoints = BitOperator.byteToInteger(count);
//                        byte[] num = new byte[1] ;
//                        System.arraycopy(value,7,num,0,num.length);
//                        sittingFrame = BitOperator.byteToInteger(num);
//                        pointLength=0;
//                        pointsArray = new byte[sittingPoints*3];
//                    }else {
//                        byte[] points = new byte[value.length-4];
//                        System.arraycopy(value,4,points,0,points.length);
//                        if ((index  < sittingFrame)) {
//                            System.arraycopy(points,0,pointsArray,pointLength,points.length);
//                            pointLength = pointLength + points.length;
//                        }else if(index == sittingFrame){
//                            System.arraycopy(points,0,pointsArray,pointLength,points.length);
//                            pointLength = pointLength + points.length;
//                            String jsonresult = "";
//                            try {
//                                JSONObject jsonObj = new JSONObject();
//                                jsonObj.put("code",0);
//                                jsonObj.put("month",  sittingMonth);
//                                jsonObj.put("day", sittingDay);
//                                JSONArray jsonArr = new JSONArray();//json格式的数组
//                                for (int i=0;i<pointLength/3;i++){
//                                    byte [] hour = new byte[1];
//                                    hour[0] = pointsArray[(i*3)];
//                                    byte [] minute = new byte[1];
//                                    minute[0] = pointsArray[(i*3+1)];
//                                    byte [] status = new byte[1] ;
//                                    status[0] = pointsArray[(i*3+2)];
//                                    JSONObject jsonObjArr = new JSONObject();
//                                    jsonObjArr.put("hour", BitOperator.byteToInteger(hour));
//                                    jsonObjArr.put("minute", BitOperator.byteToInteger(minute));
//                                    jsonObjArr.put("status", getUserStatus(BitOperator.byteToInteger(status)));
//                                    jsonArr.put(jsonObjArr);//将json格式的数据放到json格式的数组里
//                                }
//                                jsonObj.put("rows", jsonArr);//再将这个json格式的的数组放到最终的json对象中。
//                                jsonresult = jsonObj.toString();//生成返回字符串
//                            } catch (JSONException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                            if(bleCurrentStatusListener!=null)
//                                bleCurrentStatusListener.onCurrentStatus(jsonresult);
//                        }
//                    }


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
                        Log.d(TAG,"总点数：" + sittingPoints + " 当前帧 ： "  + index + " 帧数 ：" + sittingFrame + " --- " + Arrays.toString(currentIndex));
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
                                    jsonObjArr.put("status", getUserStatus(BitOperator.byteToInteger(status)));
                                    jsonArr.put(jsonObjArr);//将json格式的数据放到json格式的数组里
                                }
                                jsonObj.put("rows", jsonArr);//再将这个json格式的的数组放到最终的json对象中。
                                jsonresult = jsonObj.toString();//生成返回字符串
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            if(bleCurrentStatusListener!=null)
                                bleCurrentStatusListener.onCurrentStatus(jsonresult);
                        }
                    }

                }





                else if(msgHead.equals("FE") && msgID.equals("E6")){//设备激活成功返回
                    if(bleEnableDeviceListener!=null)
                        bleEnableDeviceListener.onEnable(true);
                }

                else if(msgHead.equals("FE") && msgID.equals("E7")){//取消设备激活成功返回
                    if(bleDisableDeviceListener!=null)
                        bleDisableDeviceListener.onDisable(true);
                }

                else if(msgHead.equals("FE") && msgID.equals("EA")){//设置马达震动应答
                    if(bleCurrentStepListener!=null)
                        bleSetMotorShockListener.onSetMotorShock(true);
                }

                else if(msgHead.equals("FE") && msgID.equals("E8")){//连接状态下同步计步数据
                    byte[] year_h = new byte[1] ;
                    System.arraycopy(value,3,year_h,0,year_h.length);
                    byte[] year_l = new byte[1] ;
                    System.arraycopy(value,4,year_l,0,year_l.length);
                    String mYear = String.valueOf(BitOperator.byteToInteger(year_h)) + String.valueOf(BitOperator.byteToInteger(year_l));
                    byte[] month = new byte[1] ;
                    System.arraycopy(value,5,month,0,month.length);
                    String mMonth = String.valueOf(BitOperator.byteToInteger(month));
                    byte[] day = new byte[1] ;
                    System.arraycopy(value,6,day,0,day.length);
                    String mDay = String.valueOf(BitOperator.byteToInteger(day));
                    byte[] hour = new byte[1] ;
                    System.arraycopy(value,7,hour,0,hour.length);
                    String mHour = String.valueOf(BitOperator.byteToInteger(hour));
                    byte[] minute = new byte[1] ;
                    System.arraycopy(value,8,minute,0,minute.length);
                    String mMinute = String.valueOf(BitOperator.byteToInteger(minute));
                    byte[] second = new byte[1] ;
                    System.arraycopy(value,9,second,0,second.length);
                    String mSecond = String.valueOf(BitOperator.byteToInteger(second));


                    byte[] step_1 = new byte[1];
                    System.arraycopy(value,value.length-4,step_1,0,step_1.length);
                    byte[] step_2 = new byte[1];
                    System.arraycopy(value,value.length-3,step_2,0,step_2.length);
                    byte[] step_3 = new byte[1];
                    System.arraycopy(value,value.length-2,step_3,0,step_3.length);
                    byte[] step_4 = new byte[1];
                    System.arraycopy(value,value.length-1,step_4,0,step_4.length);

                    int step =  BitOperator.byteToInteger(step_4)<<24 | BitOperator.byteToInteger(step_3)<<16 |
                            BitOperator.byteToInteger(step_2)<<8 | BitOperator.byteToInteger(step_1);
                    String jsonresult = "";
                    try {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("code","0");
                        jsonObj.put("year",  mYear);
                        jsonObj.put("month", mMonth);
                        jsonObj.put("day", mDay);
                        jsonObj.put("hour", mHour);
                        jsonObj.put("minute", mMinute);
                        jsonObj.put("second", mSecond);
                        jsonObj.put("step", step);
                        jsonresult = jsonObj.toString();//生成返回字符串
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (bleCurrentStepListener!=null)
                        bleCurrentStepListener.onStep(jsonresult);
                }

                else if(msgHead.equals("FE") && msgID.equals("EC")){//坐姿校准
                    if (bleCalibrateSitPositionListener!=null){
                        bleCalibrateSitPositionListener.onCalibrate(true);
                    }
                }


                else if(msgHead.equals("FE") && msgID.equals("ED")){//坐姿校准清除
                    if(bleClarCalibrateSitPositionListener!=null){
                        bleClarCalibrateSitPositionListener.onClearCalibrate(true);
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("A0")){//设置前倾角度
                    if(bleForwardAngleListener!=null){
                        bleForwardAngleListener.onForwardAngle(true);
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("A1")){//设置后倾角度
                    if(bleBackwardAngleListener!=null){
                        bleBackwardAngleListener.onBackwardAngle(true);
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("A2")){//设置左倾角度
                    if(bleLeftAngleListener!=null){
                        bleLeftAngleListener.onLeftAngle(true);
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("A3")){//设置右倾角度
                    if (bleRightAngleListener!=null){
                        bleRightAngleListener.onRightAngle(true);
                    }
                }



                else if(msgHead.equals("FE") && msgID.equals("A5")){//清除BLE数据设备应答
                    if(bleClearDataListener!=null){
                        bleClearDataListener.onClearData(true);
                    }
                }


            }
        }

        @Override
        public void onResponse(int code) {
            if (code == 0) {
                Log.d(TAG,"打开通知成功 --  " + code);
                if(bleDefaultNotifyListener!=null)
                    bleDefaultNotifyListener.onOpen(true);
            } else {
                Log.d(TAG,"打开通知失败 --  " + code);
                if(bleDefaultNotifyListener!=null)
                    bleDefaultNotifyListener.onOpen(false);
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
                byte[] userStatus = new byte[1] ;
                System.arraycopy(value,0,userStatus,0,userStatus.length);
                String uStatus = getUserStatus(BitOperator.byteToInteger(userStatus));
                byte[] sitStatus = new byte[1] ;
                System.arraycopy(value,1,sitStatus,0,sitStatus.length);
                String sStatus = getSitStatus(BitOperator.byteToInteger(sitStatus));
                bleUserStatusAndSittingStatusListener.onStatus(uStatus,sStatus);
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
                if (bleOtherNotifyListener!=null)
                    bleOtherNotifyListener.onOpen(true);
            } else {
                Log.d(TAG,"关闭另一个服务通知失败 --  " + code);
                if (bleOtherNotifyListener!=null)
                    bleOtherNotifyListener.onOpen(false);
            }
        }
    };


    /**
     * @param status 0:未知 1:坐/站立 2:躺 3:走 4:跑
     * @return
     */
    private static String getUserStatus(int status){
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

    /**
     * @param status 0:未知 1:正坐 2:偏左 3:偏右 4:偏前 5:偏后
     * @return
     */
    private static String getSitStatus(int status){
        String s="";
        if (status==0){
            s = "未知";
        }else if(status==1){
            s = "正坐";
        }else if(status==2){
            s = "偏左";
        }else if(status==3){
            s = "偏右";
        }else if(status==4){
            s = "偏前";
        }else {
            s = "偏后";
        }
        return s;
    }





}
