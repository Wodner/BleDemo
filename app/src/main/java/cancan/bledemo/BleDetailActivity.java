package cancan.bledemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.myble.BLE;
import com.inuker.bluetooth.library.myble.BleRequest;
import com.inuker.bluetooth.library.myble.ClientManager;
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
import com.inuker.bluetooth.library.myble.callback.BleLeftAngleListener;
import com.inuker.bluetooth.library.myble.callback.BleMotorFlagListener;
import com.inuker.bluetooth.library.myble.callback.BleOtherNotifyListener;
import com.inuker.bluetooth.library.myble.callback.BleRebootListener;
import com.inuker.bluetooth.library.myble.callback.BleRightAngleListener;
import com.inuker.bluetooth.library.myble.callback.BleSetBleNickname;
import com.inuker.bluetooth.library.myble.callback.BleSetMotorShockListener;
import com.inuker.bluetooth.library.myble.callback.BleSynDataListener;
import com.inuker.bluetooth.library.myble.callback.BleTodaytSitPositionListener;
import com.inuker.bluetooth.library.myble.callback.BleUserStatusAndSittingStatusListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cancan.bledemo.model.FirmwareModel;
import cancan.bledemo.model.SittingStatusDataModel;
import cancan.bledemo.model.StepModel;
import cancan.bledemo.model.TodaySitStateMode;
import cancan.bledemo.model.UserStatusDataModel;
import cancan.bledemo.utils.JsonParser;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

/**
 * 描述：连接BLE后的数据交互页面
 * 作者：Wu on 2017/4/16 16:50
 * 邮箱：wuwende@live.cn
 */

public class BleDetailActivity extends AppCompatActivity {

    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_connect_status)
    TextView tvConnectStatus;
    @Bind(R.id.rl_connect_status)
    RelativeLayout rlConnectStatus;
    @Bind(R.id.btn_get_battery)
    Button btnGetBattery;
    @Bind(R.id.btn_get_motor)
    Button btnGetMonitor;
    @Bind(R.id.btn_get_current_step_status)
    Button btnGetCurrentStep;
    @Bind(R.id.tv_first_response)
    TextView tvFiestResponse;
    @Bind(R.id.btn_get_today_sit)
    Button btnGetTodaySit;

    private final String TAG = "BLE_DETAIL";

    private String bleName;//设备名字
    private String bleMac;//设备地址

    private boolean isConnected;
    private Context mContext;
    private BleRequest mBleRequest;

    private List<UserStatusDataModel> userStatusDataModelList = new ArrayList<>();
    private List<StepModel> stepDataModelList = new ArrayList<StepModel>();

    private List<SittingStatusDataModel> sittingStatusDataModelsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_detail);
        ButterKnife.bind(this);
        init();
    }


    private void init() {
        mContext = this;
        mBleRequest = new BleRequest();
        bleMac = this.getIntent().getStringExtra("mac");
        bleName = this.getIntent().getStringExtra("name");
        initSynListener();
        initUserStatus();
    }



    private void initUserStatus(){
        BLE.setOnUserStatusAndSittingStatusListener(new BleUserStatusAndSittingStatusListener() {
            @Override
            public void onStatus(String userStatus, String sittingStatus) {
                String status = "用户状态:" + userStatus +  " —— 坐姿:" + sittingStatus;
                getSupportActionBar().setTitle(status);
            }
        });
    }

    private void initSynListener() {
        /* 每次连接 都会数据进行同步*/
        BLE.setOnBleSynListener(mContext,bleMac,new BleSynDataListener() {
            @Override
            public void onFirstResponse(String result) {
                Log.d(TAG, "返回信息... " + result);
                FirmwareModel firmwareModel = JsonParser.parseWithGson(FirmwareModel.class,result);
                tvFiestResponse.setText("(电量："+ firmwareModel.getBatery() +") (马达标志：" + firmwareModel.getMonitorflag() + ") (固件版本号："+ firmwareModel.getFirmwareversion()+")");
            }

            @Override
            public void startSynTime(boolean isStart) {
                Log.d(TAG, "开始同步... " + isStart);
            }

            @Override
            public void sendFinishSyn() {
                Log.d(TAG, "同步完成标志... ");
                Toast.makeText(mContext,"数据同步完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHistoryStepSyn(String result) {
                Log.d(TAG, "历史步数返回信息... " + result);
                StepModel stepModel = JsonParser.parseWithGson(StepModel.class,result);
                stepDataModelList.add(stepModel);
            }

            @Override
            public void onHistoryUserStatusSyn(String result) {
                Log.d(TAG, "历史用户状态返回信息... " + result);
                UserStatusDataModel sittingDataModel =  JsonParser.parseWithGson(UserStatusDataModel.class,result);
                userStatusDataModelList.add(sittingDataModel);
            }

            @Override
            public void onHistorySittingStatusSyn(String result) {
                Log.d(TAG, "历史坐姿返回信息... " + result);
                SittingStatusDataModel sittingStatusDataModel = JsonParser.parseWithGson(SittingStatusDataModel.class,result);
                sittingStatusDataModelsList.add(sittingStatusDataModel);
            }
        });
    }


    /**
     * 是否连接
     */
    private void connectDeviceIfNeeded() {
        if (!isConnected) {
            connectDevice();
        }
    }


    /**
     * 连接设备
     */
    private void connectDevice() {
        tvConnectStatus.setText("正在连接 " + bleMac);
        progressbar.setVisibility(View.VISIBLE);
        mBleRequest.connect(mContext, bleMac, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code == REQUEST_SUCCESS) {
                    progressbar.setVisibility(View.GONE);
                    tvConnectStatus.setText("已连接 " + bleMac);
                    mBleRequest.openDeafultNotify(mContext, bleMac, new BleDefaultNotifyListener() {
                        @Override
                        public void onOpen(boolean isOpen) {
                            if (!isOpen){

                            }
                        }
                    });

                    mBleRequest.openOtherNotify(mContext, bleMac, new BleOtherNotifyListener() {
                        @Override
                        public void onOpen(boolean isOpen) {
                            if (!isOpen){

                            }
                        }
                    });
                }else {
                    progressbar.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    /**
     * 连接状态监听
     */
    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            isConnected = (status == STATUS_CONNECTED);
            if(!isConnected){
                userStatusDataModelList.clear();
                stepDataModelList.clear();
                sittingStatusDataModelsList.clear();
            }
            connectDeviceIfNeeded();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        connectDeviceIfNeeded();
        ClientManager.getClient(this).registerConnectStatusListener(bleMac,mConnectStatusListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
        ClientManager.getClient(this).unregisterConnectStatusListener(bleMac, mConnectStatusListener);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBleRequest.closeDeafultNotify(mContext,bleMac);
        mBleRequest.closeOtherNotify(mContext,bleMac);
        mBleRequest.disconnect(mContext,bleMac);
    }



    @OnClick({R.id.btn_get_battery, R.id.btn_get_motor, R.id.btn_disable_monitor, R.id.btn_enable_monitor,R.id.btn_get_current_user_status,R.id.btn_calibrate_sit_position,R.id.btn_reboot_ble,
            R.id.btn_get_current_step_status,R.id.btn_enable, R.id.btn_disenable, R.id.btn_start_dfu,R.id.btn_history_sit_status,R.id.btn_history_step_status,R.id.btn_clear_data,R.id.btn_history_sitting_status,
            R.id.btn_clear_calibrate_sit_position,R.id.btn_set_forward_angle,R.id.btn_set_backward_angle,R.id.btn_set_left_angle,R.id.btn_set_right_angle,R.id.btn_set_nickname,R.id.btn_get_today_sit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_right_angle:
                setAngle(3);
                break;
            case R.id.btn_set_left_angle:
                setAngle(2);
                break;
            case R.id.btn_set_backward_angle:
                setAngle(1);
                break;
            case R.id.btn_set_forward_angle:
                setAngle(0);
                break;
            case R.id.btn_clear_calibrate_sit_position:
                mBleRequest.setClearCalibrateSitPostion(mContext, bleMac, new BleClearCalibrateSitPositionListener() {
                    @Override
                    public void onClearCalibrate(boolean isSuccess) {
                        if (isSuccess){
                            Toast.makeText(mContext,"坐姿校准清除",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext,"发送指令失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btn_calibrate_sit_position:
                mBleRequest.setCalibrateSitPosition(mContext, bleMac, new BleCalibrateSitPositionListener() {
                    @Override
                    public void onCalibrate(boolean isCalibrate) {
                        if (isCalibrate){
                            Toast.makeText(mContext,"校准成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext,"发送指令失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.btn_history_sit_status:
                if(userStatusDataModelList.size()>0){
                    Intent intent = new Intent();
                    intent.setClass(BleDetailActivity.this, BleHistoryUserStatusActivity.class);
                    intent.putExtra("title","历史用户状态数据");
                    intent.putExtra("history_user", (Serializable) userStatusDataModelList);
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext,"没有历史用户状态数据",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_history_step_status:
                if(stepDataModelList.size()>0){
                    Intent intent = new Intent();
                    intent.setClass(BleDetailActivity.this, BleHistoryStepActivity.class);
                    intent.putExtra("title","历史步数数据");
                    intent.putExtra("history_step", (Serializable) stepDataModelList);
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext,"没有历史步数数据",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_history_sitting_status:
                if(sittingStatusDataModelsList.size()>0){
                    Intent intent = new Intent();
                    intent.setClass(BleDetailActivity.this, BleHistorySitStatusActivity.class);
                    intent.putExtra("title","历史坐姿数据");
                    intent.putExtra("history_sit", (Serializable) sittingStatusDataModelsList);
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext,"没有历史坐姿数据",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_get_battery:
                mBleRequest.getBattery(mContext, bleMac, new BleBateryListener() {
                    @Override
                    public void onBattery(boolean isSuccess,int level) {
                        btnGetBattery.setText("获取电池电量：  " + level + "%");
                    }
                });
                break;
            case R.id.btn_get_motor:
                mBleRequest.getMotorShockFlag(mContext, bleMac, new BleMotorFlagListener() {
                    @Override
                    public void onMotor(boolean isSuccess,String motorFlag,int second) {
                        btnGetMonitor.setText("读取马达震动标志位：" + motorFlag + "  时间：" + second + "s");
                    }
                });
                break;
            case R.id.btn_disable_monitor:
                mBleRequest.setMotorShock(mContext, bleMac, false, 0, new BleSetMotorShockListener() {
                    @Override
                    public void onSetMotorShock(boolean isSet) {
                        Toast.makeText(mContext,"设置成功",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_enable_monitor:
                setMotorShock();
                break;

            case R.id.btn_enable:
                mBleRequest.setEnableDevice(mContext, bleMac, new BleEnableDeviceListener() {
                    @Override
                    public void onEnable(boolean isSet) {
                        Toast.makeText(mContext,"激活成功",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_disenable:
                mBleRequest.setDisableDevice(mContext, bleMac, new BleDisableDeviceListener() {
                    @Override
                    public void onDisable(boolean isSet) {
                        Toast.makeText(mContext,"取消激活成功",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_start_dfu:
                mBleRequest.setDfuModel(mContext, bleMac, new BleDfuModelListener() {
                    @Override
                    public void onDfuModel(boolean isDfu) {
                        if (isDfu){
                            finish();
                        }else {
                            Toast.makeText(mContext,"发送升级指令失败，请重新发送。",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btn_get_current_user_status:
                mBleRequest.getCurrentUserStatus(mContext, bleMac, new BleCurrentUserStatusListener() {
                    @Override
                    public void onCurrentStatus(String result) {
                        Log.d(TAG, " 返回信息 -------- >  " + result);
                        List<UserStatusDataModel> sittingDataModelList = new ArrayList<UserStatusDataModel>();
                        UserStatusDataModel sittingDataModel =  JsonParser.parseWithGson(UserStatusDataModel.class,result);
                        sittingDataModelList.add(sittingDataModel);
                        if(sittingDataModelList.size()>0){
                            Intent intent = new Intent();
                            intent.setClass(BleDetailActivity.this, BleHistoryUserStatusActivity.class);
                            intent.putExtra("history_user", (Serializable) sittingDataModelList);
                            intent.putExtra("title","当前用户状态数据");
                            startActivity(intent);
                        }else {
                            Toast.makeText(mContext,"没有用户状态数据",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case R.id.btn_get_current_step_status:
                mBleRequest.getCurrentStep(mContext, bleMac, new BleCurrentStepListener() {
                    @Override
                    public void onStep(String result) {
                        Log.d(TAG, " 返回步数信息 -------- >  " + result);
                        StepModel stepModel = JsonParser.parseWithGson(StepModel.class,result);
                        btnGetCurrentStep.setText(stepModel.getYear()+"-"+ stepModel.getMonth()+"-"+ stepModel.getDay() + " " +
                            stepModel.getHour() + ":" + stepModel.getMinute()+":"+ stepModel.getSecond()+ "   步数："+stepModel.getStep());
                    }
                });
                break;

            case R.id.btn_reboot_ble:
                mBleRequest.setBleReboot(mContext, bleMac, new BleRebootListener() {
                    @Override
                    public void onReboot(boolean isSuccess) {
                        if (isSuccess){
                            finish();//发送重启命令成功之后，需要重新扫描BLE设备连接
                            Toast.makeText(mContext,"发送重启命令成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext,"发送重启命令失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btn_clear_data:
                mBleRequest.setClearBleData(mContext, bleMac, new BleClearDataListener() {
                    @Override
                    public void onClearData(boolean isSuccess) {
                        if (isSuccess){
                            Toast.makeText(mContext,"发送清除数据命令成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext,"发送清除数据命令失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case R.id.btn_set_nickname:
                setBleNickname();
                break;

            case R.id.btn_get_today_sit:
                mBleRequest.getTodaySitPosition(mContext,bleMac,new BleTodaytSitPositionListener(){
                    @Override
                    public void onSitPositionState(String result) {
                        Log.d(TAG,"RESULT : " +  result) ;
                        TodaySitStateMode todaySitStateMode = JsonParser.parseWithGson(TodaySitStateMode.class,result);
                        btnGetTodaySit.setText(todaySitStateMode.getMonth()+"月" + todaySitStateMode.getDay()+"日，\n"
                                + todaySitStateMode.getSitting() + "s," + todaySitStateMode.getForward() + "s," + todaySitStateMode.getBackward() + "s,"
                                + todaySitStateMode.getLeftLeaning() + "s," + todaySitStateMode.getRightLeaning() + "s");
                    }
                });
                break;
        }
    }


    /**
     * 设置昵称
     */
    private void setBleNickname(){
        final EditText editTime = new EditText(mContext);
        editTime.setHint("中文最多5个汉字，英文最多17个字节");
        new  AlertDialog.Builder(mContext)
                .setTitle("设置BLE昵称" )
                .setView(editTime)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!TextUtils.isEmpty(editTime.getText().toString())){
                            String name = editTime.getText().toString().trim();
                            if(name.getBytes().length>17){
                                Toast.makeText(mContext,"昵称太长了",Toast.LENGTH_SHORT).show();
                                return;
                            }else{
                                mBleRequest.setBleNickname(mContext, bleMac, name, new BleSetBleNickname() {
                                    @Override
                                    public void onSetNickname(boolean isSuccess) {
                                        if (isSuccess){
                                            Toast.makeText(mContext,"设置成功",Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(mContext,"设置失败",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            Log.d(TAG,name + " -------> " + name.getBytes().length + " - - "+ Arrays.toString(name.getBytes()));
                        }else {
                            Toast.makeText(mContext,"输入内容不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .setNegativeButton("取消" ,  null )
                .show();
    }

    /**
     * 设置马达震动弹出
     */
    private void setMotorShock(){
        final EditText editTime = new EditText(mContext);
        editTime.setHint("最大 120 S");
        new  AlertDialog.Builder(mContext)
                .setTitle("设置马达震动" )
                .setView(editTime)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!TextUtils.isEmpty(editTime.getText().toString())){
                            int time = Integer.valueOf(editTime.getText().toString());
                            if(time<=120 && time>=0){
                                mBleRequest.setMotorShock(mContext, bleMac, true, time, new BleSetMotorShockListener() {
                                    @Override
                                    public void onSetMotorShock(boolean isSet) {
                                        Toast.makeText(mContext,"设置成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Toast.makeText(mContext,"输入时间不合法",Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(mContext,"输入时间不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .setNegativeButton("取消" ,  null )
                .show();
    }


    /**
     * @param model 0: 前倾  1：后倾 2:左倾 3：右倾
     */
    private void setAngle(final int model){
        String title =null;
        if(model ==0){
            title = "设置前倾角度";
        }else if(model ==1){
            title = "设置后倾角度";
        }else if(model ==2){
            title = "设置左倾角度";
        }else if(model ==3){
            title = "设置右倾角度";
        }

        final EditText editAngle = new EditText(mContext);
        editAngle.setHint("角度范围：0 ~ 90 度");
        new  AlertDialog.Builder(mContext)
                .setTitle(title)
                .setView(editAngle)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!TextUtils.isEmpty(editAngle.getText().toString())){
                            int angle = Integer.valueOf(editAngle.getText().toString());
                            if(angle<=90 && angle>=0){
                                Log.d(TAG, " 设置 -------- >  " + model);
                                switch (model){

                                    case 0:
                                        Log.d(TAG, " 设置前倾角度 -------- >  " );
                                        mBleRequest.setForwardAngle(mContext, bleMac, angle, new BleForwardAngleListener() {
                                            @Override
                                            public void onForwardAngle(boolean isSuccess) {
                                                if (isSuccess){
                                                    Toast.makeText(mContext,"设置前倾角度成功",Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(mContext,"发送指令失败",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        break;
                                    case 1:
                                        Log.d(TAG, " 设置后倾角度 -------- >  " );
                                        mBleRequest.setBackwardAngle(mContext, bleMac, angle, new BleBackwardAngleListener() {
                                            @Override
                                            public void onBackwardAngle(boolean isSuccess) {
                                                if (isSuccess){
                                                    Toast.makeText(mContext,"设置后倾角度成功",Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(mContext,"发送指令失败",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        break;
                                    case 2:
                                        Log.d(TAG, " 设置左倾角度 -------- >  " );
                                        mBleRequest.setLeftAngle(mContext, bleMac, angle, new BleLeftAngleListener() {
                                            @Override
                                            public void onLeftAngle(boolean isSuccess) {
                                                if (isSuccess){
                                                    Toast.makeText(mContext,"设置左倾角度成功",Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(mContext,"发送指令失败",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        break;
                                    case 3:
                                        Log.d(TAG, " 设置右倾角度 -------- >  " );
                                        mBleRequest.setRightAngle(mContext, bleMac, angle, new BleRightAngleListener() {
                                            @Override
                                            public void onRightAngle(boolean isSuccess) {
                                                if (isSuccess){
                                                    Toast.makeText(mContext,"设置右倾角度成功",Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(mContext,"发送指令失败",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        break;
                                }
                            }else {
                                Toast.makeText(mContext,"输入角度不合法",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(mContext,"输入时间不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .setNegativeButton("取消" ,  null )
                .show();
    }



}
