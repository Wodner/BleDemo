package cancan.bledemo.dfu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cancan.bledemo.R;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

/**
 * 描述：
 * 作者：Wu on 2017/4/23 13:33
 * 邮箱：wuwende@live.cn
 */

public class DfuActivity extends AppCompatActivity {

    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_connect_status)
    TextView tvConnectStatus;
    @Bind(R.id.rl_connect_status)
    RelativeLayout rlConnectStatus;


    @Bind(R.id.btn_get_zip)
    Button btnGetZip;
    @Bind(R.id.btn_start_dfu)
    Button btnStartDfu;
    @Bind(R.id.progressbar_dfu)
    ProgressBar progressbarDfu;

    private final String TAG = "DFU_DFU";
    private Context mContext;
    private BluetoothClient mClient;

    private String bleName;//设备名字
    private String bleMac;//设备地址
    private boolean isConnected;





    private String firmwareName ="dfuwzV1.1.6.zip";

    private String firmwarePath = Environment.getExternalStorageDirectory().getPath() + "/" + firmwareName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfu);
        ButterKnife.bind(this);
        mContext = this;
        mClient = new BluetoothClient(this);
        getSupportActionBar().setTitle("固件升级");

        bleMac = this.getIntent().getStringExtra("mac");
        bleName = this.getIntent().getStringExtra("name");


        Log.d(TAG,firmwarePath + " ------------------- ");

        //注册空中升级的监听
        DfuServiceListenerHelper.registerProgressListener(this, dfuProgressListener);

        try {
            copyBigDataToSD(firmwarePath);//把assets文件夹的固件复制到手机内存
        } catch (IOException e) {
            e.printStackTrace();
        }

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
     * 连接状态监听
     */
    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            isConnected = (status == STATUS_CONNECTED);
            connectDeviceIfNeeded();
        }
    };

    /**
     * 连接设备
     */
    private void connectDevice() {
        Log.d(TAG, "-------- 开始连接！");
        btnGetZip.setEnabled(false);
        btnStartDfu.setEnabled(false);
        tvConnectStatus.setText("正在连接 " + bleMac);
        progressbar.setVisibility(View.VISIBLE);
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();
        mClient.connect(bleMac, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    Log.d(TAG, "-------- 连接成功！");
                    tvConnectStatus.setText("已连接 " + bleMac);
                    btnGetZip.setEnabled(true);
                    btnStartDfu.setEnabled(true);
                    progressbar.setVisibility(View.GONE);
                }else {
                    btnGetZip.setEnabled(false);
                    btnStartDfu.setEnabled(false);
                    progressbar.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        connectDeviceIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DfuServiceListenerHelper.unregisterProgressListener(this, dfuProgressListener);//取消监听升级回调
        mClient.unregisterConnectStatusListener(bleMac, mConnectStatusListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.disconnect(bleMac);
    }







    //空中升级时的监听
    private final DfuProgressListener dfuProgressListener = new DfuProgressListener() {
        @Override
        public void onDeviceConnecting(String deviceAddress) {
            progressbarDfu.setIndeterminate(true);
        }

        //设备开始连接
        @Override
        public void onDeviceConnected(String deviceAddress) {
            progressbarDfu.setIndeterminate(true);
            Log.w(TAG, "----- : onDeviceConnected");
        }

        //升级准备开始的时候调用
        @Override
        public void onDfuProcessStarting(String deviceAddress) {
            progressbarDfu.setIndeterminate(true);
            Log.w(TAG, "----- : onDfuProcessStarting");
        }

        //设备开始升级
        @Override
        public void onDfuProcessStarted(String deviceAddress) {
            progressbarDfu.setIndeterminate(true);
            Log.w(TAG, "----- : onDfuProcessStarted");
        }

        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            progressbarDfu.setIndeterminate(true);
        }

        //升级过程中的回调
        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            progressbarDfu.setIndeterminate(false);
            progressbarDfu.setProgress(percent);

            Log.w(TAG, "----- : -----  " + percent);
        }

        //固件验证
        @Override
        public void onFirmwareValidating(String deviceAddress) {
            Log.d(TAG, "----- : onFirmwareValidating  —— " + deviceAddress);
        }

        //设备正在断开
        @Override
        public void onDeviceDisconnecting(String deviceAddress) {
        }

        //设备已经断开
        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            Log.e(TAG, "ERROR: onDeviceDisconnected");
        }

        //升级完成
        @Override
        public void onDfuCompleted(String deviceAddress) {
            Toast.makeText(mContext, "升级完成", Toast.LENGTH_SHORT).show();
            progressbarDfu.setIndeterminate(false);
            finish();
        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            progressbarDfu.setIndeterminate(false);
            Toast.makeText(mContext, "升级完成", Toast.LENGTH_SHORT).show();
        }

        //升级失败
        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            Log.e(TAG, "ERROR:" + error + " message:  " + message);
        }
    };

    @OnClick({R.id.btn_get_zip, R.id.btn_start_dfu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_zip:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
                break;
            case R.id.btn_start_dfu:
                if (firmwarePath!=null && !firmwarePath.equals("")){
                    Toast.makeText(mContext, "开始升级", Toast.LENGTH_SHORT).show();
                    final DfuServiceInitiator starter = new DfuServiceInitiator(bleMac)
                            .setDeviceName(bleName)
                            .setKeepBond(true);
                    starter.setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
                    starter.setZip(firmwarePath);
                    starter.start(this, DfuService.class);
                }else {
                    Toast.makeText(mContext, "请先选择固件", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
//            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
//            String path = getPath(mContext, uri);
//            btnGetZip.setText(path);
//            firmwarePath = path;
//            Log.w(TAG," ---- " + path);
//        }
//    }



    /**
     * 获取文件选择器选中的文件路径
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    //把zip固件复制到手机存储，之后再升级
    private void copyBigDataToSD(String strOutFileName) throws IOException {
        Log.e(TAG,"copyBigDataToSD");

        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = this.getAssets().open(firmwareName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }







}
