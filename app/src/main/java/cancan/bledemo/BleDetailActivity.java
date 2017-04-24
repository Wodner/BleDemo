package cancan.bledemo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.inuker.bluetooth.library.myble.callback.BleBateryListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentStatusListener;
import com.inuker.bluetooth.library.myble.callback.BleCurrentStepListener;
import com.inuker.bluetooth.library.myble.callback.BleDfuModelListener;
import com.inuker.bluetooth.library.myble.callback.BleDisableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleEnableDeviceListener;
import com.inuker.bluetooth.library.myble.callback.BleFinishSittingListener;
import com.inuker.bluetooth.library.myble.callback.BleMotorFlagListener;
import com.inuker.bluetooth.library.myble.callback.BleSetMotorShockListener;
import com.inuker.bluetooth.library.myble.callback.BleSynDataListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cancan.bledemo.adapter.SittingDataAdapter;
import cancan.bledemo.model.FirmwareModel;
import cancan.bledemo.model.SittingDataModel;
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


    @Bind(R.id.current_sit_recyclerview)
    RecyclerView correntSitRecyclerView;
    @Bind(R.id.history_sit_recyclerview)
    RecyclerView historySitRecyclerview;


    @Bind(R.id.btn_get_battery)
    Button btnGetBattery;
    @Bind(R.id.btn_get_motor)
    Button btnGetMonitor;

//    @Bind(R.id.btn_enable)
//    Button btnEnable;
//    @Bind(R.id.btn_disenable)
//    Button btnDisenable;
//    @Bind(R.id.btn_start_dfu)
//    Button btnStartDfu;

    @Bind(R.id.tv_first_response)
    TextView tvFiestResponse;

    private final String TAG = "BLE_DETAIL";

    private String bleName;//设备名字
    private String bleMac;//设备地址

    private boolean isConnected;
    private Context mContext;
    private BleRequest mBleRequest;


    private SittingDataAdapter sittingDataAdapter;
    private SittingDataAdapter sittingHistoryDataAdapter;
    private List<SittingDataModel> sittingDataModelList = new ArrayList<SittingDataModel>();


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
        initHistorySitView();
        initCurrentSitView();
        bleMac = this.getIntent().getStringExtra("mac");
        bleName = this.getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(bleName);
        initListener();
    }


    private void initHistorySitView(){
        sittingHistoryDataAdapter = new SittingDataAdapter(mContext);
        historySitRecyclerview .setLayoutManager(new LinearLayoutManager(mContext));
        historySitRecyclerview.setAdapter(sittingHistoryDataAdapter);
    }

     private void initCurrentSitView(){
         sittingDataAdapter = new SittingDataAdapter(mContext);
         correntSitRecyclerView .setLayoutManager(new LinearLayoutManager(mContext));
         correntSitRecyclerView.setAdapter(sittingDataAdapter);
     }


    private void initListener() {
        /* 每次连接 都会数据进行同步*/
        BLE.setOnBleSynListener(new BleSynDataListener() {
            @Override
            public void onFirstResponse(String result) {
                Log.d(TAG, "返回信息... " + result);
                FirmwareModel firmwareModel = JsonParser.parseWithGson(FirmwareModel.class,result);
                tvFiestResponse.setText("(电量："+ firmwareModel.getBatery() +") (马达标志：" + firmwareModel.getMonitorflag() + ") (固件版本号："+ firmwareModel.getFirmwareversion()+")");

            }

            @Override
            public void startSynTime(boolean isStart) {
                Log.d(TAG, "开始同步... " + isStart);
                if (isStart)
                    mBleRequest.synTime(mContext, bleMac);
            }

            @Override
            public void sendFinishSyn() {
                mBleRequest.finishSyn(mContext, bleMac, new BleRequest.SynFinishListener() {
                    @Override
                    public void finishSyn(boolean isFinish) {
                        Log.d(TAG, "同步完成标志... " + isFinish);
                    }
                });
            }

            @Override
            public void onSittingSyn(String result) {
                Log.d(TAG, "坐姿返回信息... " + result);
                SittingDataModel sittingDataModel =  JsonParser.parseWithGson(SittingDataModel.class,result);
                sittingDataModelList.add(sittingDataModel);
                sittingHistoryDataAdapter.setData(sittingDataModelList);
                mBleRequest.setFinishSittingSyn(mContext, bleMac, new BleFinishSittingListener() {
                    @Override
                    public void onFinish(boolean isFinish) {
                        Log.d(TAG, "同步坐姿完成标志... " + isFinish);
                    }
                });
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
                    mBleRequest.openDeafultNotify(mContext,bleMac);

                    mBleRequest.openOtherNotify(mContext,bleMac);
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
                sittingDataModelList.clear();
                sittingHistoryDataAdapter.clear();
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
        mBleRequest.closeDeafultNotify(mContext,bleMac);
        mBleRequest.closeOtherNotify(mContext,bleMac);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBleRequest.disconnect(mContext,bleMac);
    }



    @OnClick({R.id.btn_get_battery, R.id.btn_get_motor, R.id.btn_disable_monitor, R.id.btn_enable_monitor,R.id.btn_get_current_sit_status,
            R.id.btn_get_current_step_status,R.id.btn_enable, R.id.btn_disenable, R.id.btn_start_dfu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_battery:
                mBleRequest.getBattery(mContext, bleMac, new BleBateryListener() {
                    @Override
                    public void onBattery(int level) {
                        btnGetBattery.setText("获取电池电量  " + level + "%");
                    }
                });
                break;
            case R.id.btn_get_motor:
                mBleRequest.getMotorShockFlag(mContext, bleMac, new BleMotorFlagListener() {
                    @Override
                    public void onMotor(String motorFlag,int second) {
                        btnGetMonitor.setText("读取马达震动 " + motorFlag + " " + second + "s");
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
                            Toast.makeText(mContext,"发送升级指令不成功，请重新发送。",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btn_get_current_sit_status:
                mBleRequest.getCurrentSttingStatus(mContext, bleMac, new BleCurrentStatusListener() {
                    @Override
                    public void onCurrentStatus(String result) {
                        Log.d(TAG, " 返回信息 -------- >  " + result);
                        List<SittingDataModel> sittingDataModelList = new ArrayList<SittingDataModel>();
                        SittingDataModel sittingDataModel =  JsonParser.parseWithGson(SittingDataModel.class,result);
                        sittingDataModelList.add(sittingDataModel);
                        sittingDataAdapter.setData(sittingDataModelList);
                    }
                });
                break;

            case R.id.btn_get_current_step_status:
                mBleRequest.getCurrentStep(mContext, bleMac, new BleCurrentStepListener() {
                    @Override
                    public void onStep(String result) {
                        Log.d(TAG, " 返回步数信息 -------- >  " + result);
                    }
                });

                break;
        }
    }


    /**
     * 设置马达震动弹出
     */
    private void setMotorShock(){
        final EditText editTime = new EditText(mContext);
        editTime.setHint("最大 120 S");
        new  AlertDialog.Builder(mContext)
                .setTitle("设置马达震动" )
                .setIcon(android.R.drawable.ic_dialog_info)
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


}
