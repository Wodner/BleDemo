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
import com.inuker.bluetooth.library.myble.callback.BleSetBleNickname;
import com.inuker.bluetooth.library.myble.callback.BleSetMotorShockListener;
import com.inuker.bluetooth.library.myble.callback.BleSynDataListener;
import com.inuker.bluetooth.library.myble.callback.BleTodaytSitPositionListener;
import com.inuker.bluetooth.library.myble.callback.BleUserStatusAndSittingStatusListener;
import com.inuker.bluetooth.library.myble.myutils.BitOperator;
import com.inuker.bluetooth.library.myble.myutils.HexStringUtils;
import com.inuker.bluetooth.library.myble.myutils.MyStringUtils;
import com.inuker.bluetooth.library.utils.ByteUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.UUID;

import static com.inuker.bluetooth.library.myble.myutils.BitOperator.byteToInteger;

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

    private static BleSetBleNickname bleSetBleNickname;
    private static BleTodaytSitPositionListener bleTodaytSitPositionListener;

    public static void setOnBleSetBleNickname(BleSetBleNickname listener){
        bleSetBleNickname = listener;
    }

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

    public static void setOnTodaySitStateListener(BleTodaytSitPositionListener listener){
        bleTodaytSitPositionListener = listener;
    }


    private static int sittingYear;
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
                int btyeLength = Integer.parseInt(result.substring(2,4).toUpperCase(),16);
                Log.w(TAG,  " 收到通知信息头 ------ " + msgHead  + msgID + " ----- " + " 字节长度 ：" + btyeLength + "实际长度：" + value.length +"\n" );
                Log.d(TAG," 收到通知信息 ------ " + Arrays.toString(value)  + "\n"  +String.format("%s", ByteUtils.byteToString(value))+ "\n" );
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
                                jsonObj.put("batery",  (byteToInteger(battery) + "%"));
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
                    if(btyeLength==value.length){
                        byte[] year_l = new byte[1] ;
                        System.arraycopy(value,3,year_l,0,year_l.length);
                        byte[] year_h = new byte[1] ;
                        System.arraycopy(value,4,year_h,0,year_h.length);

                        Log.e(TAG, "同步历史步数年份 ... " + HexStringUtils.toHexString(year_h)  + HexStringUtils.toHexString(year_l));

                        String mYear = String.valueOf((byteToInteger(year_h)<<8|byteToInteger(year_l)));
                        byte[] month = new byte[1] ;
                        System.arraycopy(value,5,month,0,month.length);
                        String mMonth = String.valueOf(byteToInteger(month));
                        byte[] day = new byte[1] ;
                        System.arraycopy(value,6,day,0,day.length);
                        String mDay = String.valueOf(byteToInteger(day));
                        byte[] hour = new byte[1] ;
                        System.arraycopy(value,7,hour,0,hour.length);
                        String mHour = String.valueOf(byteToInteger(hour));
                        byte[] minute = new byte[1] ;
                        System.arraycopy(value,8,minute,0,minute.length);
                        String mMinute = String.valueOf(byteToInteger(minute));
                        byte[] second = new byte[1] ;
                        System.arraycopy(value,9,second,0,second.length);
                        String mSecond = String.valueOf(byteToInteger(second));


                        byte[] step_1 = new byte[1];
                        System.arraycopy(value,value.length-4,step_1,0,step_1.length);
                        byte[] step_2 = new byte[1];
                        System.arraycopy(value,value.length-3,step_2,0,step_2.length);
                        byte[] step_3 = new byte[1];
                        System.arraycopy(value,value.length-2,step_3,0,step_3.length);
                        byte[] step_4 = new byte[1];
                        System.arraycopy(value,value.length-1,step_4,0,step_4.length);

                        int step =  byteToInteger(step_4)<<24 | byteToInteger(step_3)<<16 |
                                byteToInteger(step_2)<<8 | byteToInteger(step_1);

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
                }

                else if(msgHead.equals("FE") && msgID.equals("E3")){//同步历史用户状态数据

                    Log.d(TAG,"历史用户状态数据：" + Arrays.toString(value));

                    byte[] currentIndex = new byte[1] ;
                    System.arraycopy(value,3,currentIndex,0,currentIndex.length);
                    int index = byteToInteger(currentIndex);

                    Log.d(TAG,"总点数：" + sittingPoints + " 当前帧 ： "  + index + " 帧数 ：" + sittingFrame + " --- " + Arrays.toString(currentIndex));

                    if(index==0){

                        byte[] year_l = new byte[1] ;
                        System.arraycopy(value,4,year_l,0,year_l.length);
                        byte[] year_h = new byte[1] ;
                        System.arraycopy(value,5,year_h,0,year_h.length);
                        sittingYear = byteToInteger(year_h)<<8 | byteToInteger(year_l);

                        byte[] month = new byte[1] ;
                        System.arraycopy(value,6,month,0,month.length);
                        sittingMonth = byteToInteger(month);
                        byte[] day = new byte[1] ;
                        System.arraycopy(value,7,day,0,day.length);
                        sittingDay = byteToInteger(day);
                        byte[] count = new byte[1] ;
                        System.arraycopy(value,8,count,0,count.length);
                        sittingPoints = byteToInteger(count);
                        byte[] num = new byte[1] ;
                        System.arraycopy(value,9,num,0,num.length);
                        sittingFrame = byteToInteger(num);
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
                                jsonObj.put("year",sittingYear);
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
                                    jsonObjArr.put("hour", byteToInteger(hour));
                                    jsonObjArr.put("minute", byteToInteger(minute));
                                    jsonObjArr.put("status", getUserStatus(byteToInteger(status)));
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
                    if(btyeLength==value.length) {
                        byte[] year_l = new byte[1];
                        System.arraycopy(value, 3, year_l, 0, year_l.length);
                        byte[] year_h = new byte[1];
                        System.arraycopy(value, 4, year_h, 0, year_h.length);
                        String mYear =String.valueOf((byteToInteger(year_h))<<8 | (byteToInteger(year_l))) ;

                        byte[] month = new byte[1];
                        System.arraycopy(value, 5, month, 0, month.length);
                        String mMonth = String.valueOf(byteToInteger(month));

                        byte[] day = new byte[1];
                        System.arraycopy(value, 6, day, 0, day.length);
                        String mDay = String.valueOf(byteToInteger(day));


                        byte[] sitting_l = new byte[1];
                        System.arraycopy(value, 7, sitting_l, 0, sitting_l.length);
                        byte[] sitting_h = new byte[1];
                        System.arraycopy(value, 8, sitting_h, 0, sitting_h.length);

                        int s_h = byteToInteger(sitting_h) << 8;
                        int s_l = byteToInteger(sitting_l);
                        int sitting = s_h | s_l;


                        byte[] forward_l = new byte[1];
                        System.arraycopy(value, 9, forward_l, 0, forward_l.length);
                        byte[] forward_h = new byte[1];
                        System.arraycopy(value, 10, forward_h, 0, forward_h.length);

                        int f_h = byteToInteger(forward_h) << 8;
                        int f_l = byteToInteger(forward_l);
                        int forward = f_h | f_l;


                        byte[] backward_l = new byte[1];
                        System.arraycopy(value, 11, backward_l, 0, backward_l.length);
                        byte[] backward_h = new byte[1];
                        System.arraycopy(value, 12, backward_h, 0, backward_h.length);

                        int b_h = byteToInteger(backward_h) << 8;
                        int b_l = byteToInteger(backward_l);
                        int backward = b_h | b_l;


                        byte[] left_leaning_l = new byte[1];
                        System.arraycopy(value, 13, left_leaning_l, 0, left_leaning_l.length);
                        byte[] left_leaning_h = new byte[1];
                        System.arraycopy(value, 14, left_leaning_h, 0, left_leaning_h.length);

                        int l_h = byteToInteger(left_leaning_h) << 8;
                        int l_l = byteToInteger(left_leaning_l);
                        int leftLeaning = l_h | l_l;


                        byte[] right_leaning_l = new byte[1];
                        System.arraycopy(value, 15, right_leaning_l, 0, right_leaning_l.length);
                        byte[] rigth_leaning_h = new byte[1];
                        System.arraycopy(value, 16, rigth_leaning_h, 0, rigth_leaning_h.length);

                        int r_h = byteToInteger(rigth_leaning_h) << 8;
                        int r_l = byteToInteger(right_leaning_l);
                        int rightLeaning = r_h | r_l;

                        String jsonresult = "";
                        try {
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("code", "0");
                            jsonObj.put("year", mYear);
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
                        if (bleSynListener != null) {
                            bleSynListener.onHistorySittingStatusSyn(jsonresult);
                            new BleRequest().setHistorySittingSynResponse(mContext, mMac, new BleHistorySitStatusSynListener() {
                                @Override
                                public void onReceive(boolean isFinish) {
                                    Log.d(TAG, "同步历史历史坐姿完成标志... " + isFinish);
                                }
                            });
                        }
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
                    if(btyeLength==value.length) {
                        byte[] battery = new byte[1];
                        System.arraycopy(value, 3, battery, 0, battery.length);
                        if (bleBateryListener != null) {
                            bleBateryListener.onBattery(true, byteToInteger(battery));
                        }
                    }
                }

                else if (msgHead.equals("FE") && msgID.equals("EB")){//读取马达震动标志位
                    if(btyeLength==value.length) {
                        byte[] motorFlag = new byte[1];
                        System.arraycopy(value, 3, motorFlag, 0, motorFlag.length);

                        byte[] second = new byte[1];
                        System.arraycopy(value, 4, second, 0, second.length);
                        if (bleMotorListener != null)
                            bleMotorListener.onMotor(true, ByteUtils.byteToString(motorFlag), byteToInteger(second));
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("E9")){//同步当前用户状态数据返回信息
                    byte[] currentIndex = new byte[1] ;
                    System.arraycopy(value,3,currentIndex,0,currentIndex.length);
                    int index = byteToInteger(currentIndex);
                    if(index==0){
                        byte[] month = new byte[1] ;
                        System.arraycopy(value,4,month,0,month.length);
                        sittingMonth = byteToInteger(month);
                        byte[] day = new byte[1] ;
                        System.arraycopy(value,5,day,0,day.length);
                        sittingDay = byteToInteger(day);
                        byte[] count = new byte[1] ;
                        System.arraycopy(value,6,count,0,count.length);
                        sittingPoints = byteToInteger(count);
                        byte[] num = new byte[1] ;
                        System.arraycopy(value,7,num,0,num.length);
                        sittingFrame = byteToInteger(num);
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
                                jsonObj.put("year",sittingYear);
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
                                    jsonObjArr.put("hour", byteToInteger(hour));
                                    jsonObjArr.put("minute", byteToInteger(minute));
                                    jsonObjArr.put("status", getUserStatus(byteToInteger(status)));
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
                    if(btyeLength==value.length) {
                        if (bleEnableDeviceListener != null)
                            bleEnableDeviceListener.onEnable(true);
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("E7")){//取消设备激活成功返回
                    if(btyeLength==value.length) {
                        if (bleDisableDeviceListener != null)
                            bleDisableDeviceListener.onDisable(true);
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("EA")){//设置马达震动应答
                    if(btyeLength==value.length) {
                        if (bleSetMotorShockListener != null)
                            bleSetMotorShockListener.onSetMotorShock(true);
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("E8")){//连接状态下同步计步数据
                    if(btyeLength==value.length) {
                        byte[] year_l = new byte[1] ;
                        System.arraycopy(value,3,year_l,0,year_l.length);
                        byte[] year_h = new byte[1] ;
                        System.arraycopy(value,4,year_h,0,year_h.length);
                        String mYear = String.valueOf((byteToInteger(year_h)<<8|byteToInteger(year_l)));

                        byte[] month = new byte[1];
                        System.arraycopy(value, 5, month, 0, month.length);
                        String mMonth = String.valueOf(byteToInteger(month));
                        byte[] day = new byte[1];
                        System.arraycopy(value, 6, day, 0, day.length);
                        String mDay = String.valueOf(byteToInteger(day));
                        byte[] hour = new byte[1];
                        System.arraycopy(value, 7, hour, 0, hour.length);
                        String mHour = String.valueOf(byteToInteger(hour));
                        byte[] minute = new byte[1];
                        System.arraycopy(value, 8, minute, 0, minute.length);
                        String mMinute = String.valueOf(byteToInteger(minute));
                        byte[] second = new byte[1];
                        System.arraycopy(value, 9, second, 0, second.length);
                        String mSecond = String.valueOf(byteToInteger(second));


                        byte[] step_1 = new byte[1];
                        System.arraycopy(value, value.length - 4, step_1, 0, step_1.length);
                        byte[] step_2 = new byte[1];
                        System.arraycopy(value, value.length - 3, step_2, 0, step_2.length);
                        byte[] step_3 = new byte[1];
                        System.arraycopy(value, value.length - 2, step_3, 0, step_3.length);
                        byte[] step_4 = new byte[1];
                        System.arraycopy(value, value.length - 1, step_4, 0, step_4.length);

                        int step = byteToInteger(step_4) << 24 | byteToInteger(step_3) << 16 |
                                byteToInteger(step_2) << 8 | byteToInteger(step_1);
                        String jsonresult = "";
                        try {
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("code", "0");
                            jsonObj.put("year", mYear);
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
                        if (bleCurrentStepListener != null)
                            bleCurrentStepListener.onStep(jsonresult);
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("EC")){//坐姿校准
                    if(btyeLength==value.length) {
                        if (bleCalibrateSitPositionListener != null) {
                            bleCalibrateSitPositionListener.onCalibrate(true);
                        }
                    }
                }


                else if(msgHead.equals("FE") && msgID.equals("ED")){//坐姿校准清除
                    if(btyeLength==value.length) {
                        if (bleClarCalibrateSitPositionListener != null) {
                            bleClarCalibrateSitPositionListener.onClearCalibrate(true);
                        }
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("A0")){//设置前倾角度
                    if(btyeLength==value.length) {
                        if (bleForwardAngleListener != null) {
                            bleForwardAngleListener.onForwardAngle(true);
                        }
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("A1")){//设置后倾角度
                    if(btyeLength==value.length) {
                        if (bleBackwardAngleListener != null) {
                            bleBackwardAngleListener.onBackwardAngle(true);
                        }
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("A2")){//设置左倾角度
                    if(btyeLength==value.length) {
                        if (bleLeftAngleListener != null) {
                            bleLeftAngleListener.onLeftAngle(true);
                        }
                    }
                }

                else if(msgHead.equals("FE") && msgID.equals("A3")){//设置右倾角度
                    if(btyeLength==value.length) {
                        if (bleRightAngleListener != null) {
                            bleRightAngleListener.onRightAngle(true);
                        }
                    }
                }



                else if(msgHead.equals("FE") && msgID.equals("A5")){//清除BLE数据设备应答
                    if(btyeLength==value.length) {
                        if (bleClearDataListener != null) {
                            bleClearDataListener.onClearData(true);
                        }
                    }
                }


                else if(msgHead.equals("FE") && msgID.equals("A6")){// 设置昵称设备应答
                    if(btyeLength==value.length){
                        if(bleSetBleNickname!=null){
                            byte[] isSuccess = new byte[1];
                            System.arraycopy(value,3,isSuccess,0,isSuccess.length);
                            int flag = BitOperator.byteToInteger(isSuccess);
                            if(flag==0){
                                bleSetBleNickname.onSetNickname(true);
                            }else {
                                bleSetBleNickname.onSetNickname(false);
                            }
                        }
                    }
                }


                else if(msgHead.equals("FE") && msgID.equals("A7")){ // 获取今天的坐姿数据
                    if(btyeLength==value.length){
                        if(bleTodaytSitPositionListener!=null){

                            byte[] month = new byte[1] ;
                            System.arraycopy(value,3,month,0,month.length);
                            int mMonth = byteToInteger(month);
                            byte[] day = new byte[1] ;
                            System.arraycopy(value,4,day,0,day.length);
                            int mDay = byteToInteger(day);


                            byte[] sitting_l = new byte[1];
                            System.arraycopy(value,5,sitting_l,0,sitting_l.length);
                            byte[] sitting_h = new byte[1];
                            System.arraycopy(value,6,sitting_h,0,sitting_h.length);

                            int sitting = byteToInteger(sitting_h)<<8|byteToInteger(sitting_l);


                            byte[] forward_l = new byte[1];
                            System.arraycopy(value,7,forward_l,0,forward_l.length);
                            byte[] forward_h = new byte[1];
                            System.arraycopy(value,8,forward_h,0,forward_h.length);

                            int forward = byteToInteger(forward_h)<<8|byteToInteger(forward_l);

                            byte[] backward_l = new byte[1];
                            System.arraycopy(value,9,backward_l,0,backward_l.length);
                            byte[] backward_h = new byte[1];
                            System.arraycopy(value,10,backward_h,0,backward_h.length);

                            int backward = byteToInteger(backward_h)<<8|byteToInteger(backward_l);

                            byte[] leftLeaning_l = new byte[1];
                            System.arraycopy(value,11,leftLeaning_l,0,leftLeaning_l.length);
                            byte[] leftLeaning_h = new byte[1];
                            System.arraycopy(value,12,leftLeaning_h,0,leftLeaning_h.length);

                            int leftLeaning = byteToInteger(leftLeaning_h)<<8|byteToInteger(leftLeaning_l);

                            byte[] rightLeaning_l = new byte[1];
                            System.arraycopy(value,13,rightLeaning_l,0,rightLeaning_l.length);
                            byte[] rightLeaning_h = new byte[1];
                            System.arraycopy(value,14,rightLeaning_h,0,rightLeaning_h.length);

                            int rightLeaning = byteToInteger(rightLeaning_h)<<8|byteToInteger(rightLeaning_l);

                            Log.d(TAG, mMonth+"月" + mDay+"日，" + sitting + "s," + forward + "s," + backward + "s," + leftLeaning + "s," + rightLeaning + "s" );

                            String jsonresult = "";
                            try {
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("code", "0");
                                jsonObj.put("month", mMonth);
                                jsonObj.put("day", mDay);
                                jsonObj.put("sitting", sitting);
                                jsonObj.put("forward", forward);
                                jsonObj.put("backward", backward);
                                jsonObj.put("leftLeaning", leftLeaning);
                                jsonObj.put("rightLeaning", rightLeaning);
                                jsonresult = jsonObj.toString();//生成返回字符串
                                bleTodaytSitPositionListener.onSitPositionState(jsonresult);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
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
     * BLE 打开另一个服务的notify 后回调(这个服务的notify实时更新用户状态和坐姿)
     */
    public static final BleNotifyResponse mOtherNotifyRsp = new BleNotifyResponse() {

        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(MyConstant.OTHER_SERVICE_UUID) && character.equals(MyConstant.OTHER_CHARACTERISTIC_NOTIFY_UUID)) {
                byte[] userStatus = new byte[1] ;
                System.arraycopy(value,0,userStatus,0,userStatus.length);
                String uStatus = getUserStatus(byteToInteger(userStatus));
                byte[] sitStatus = new byte[1] ;
                System.arraycopy(value,1,sitStatus,0,sitStatus.length);
                String sStatus = getSitStatus(byteToInteger(sitStatus));
                if(bleUserStatusAndSittingStatusListener!=null)
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
