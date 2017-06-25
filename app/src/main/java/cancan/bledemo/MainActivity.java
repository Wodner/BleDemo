package cancan.bledemo;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.myble.ClientManager;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cancan.bledemo.adapter.DeviceAdapter;
import cancan.bledemo.dfu.DfuActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        DeviceAdapter.OnRecyclerViewItemClickListener,SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private final String TAG = MainActivity.class.getName();
    private final int RC_LOACTION = 123;
    private List<SearchResult> deviceList;
    private DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkBLE();
        initView();
    }


    /**
     * 初始化
     */
    private void initView() {
        deviceAdapter = new DeviceAdapter(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(deviceAdapter);
        deviceAdapter.setOnItemClickListener(this);
        deviceList = new ArrayList<>();
        swipeRefresh.setColorSchemeColors(Color.RED, Color.RED);//改变加载显示的颜色
        swipeRefresh.setBackgroundColor(Color.WHITE);//设置背景颜色
        swipeRefresh.setSize(SwipeRefreshLayout.LARGE);//设置初始时的大小
        swipeRefresh.setOnRefreshListener(this);//设置监听
        swipeRefresh.setDistanceToTriggerSync(100);//设置向下拉多少出现刷新
        swipeRefresh.setProgressViewEndTarget(false, 200);//设置刷新出现的位置
    }


    @Override
    public void onRefresh() {
        startRefresh();/*下拉刷新*/
    }

    private void startRefresh() {
        deviceList.clear();
        deviceAdapter.setData(deviceList);
        startSearch();
    }


    @Override
    public void onItemClick(View v, SearchResult bluetoothDevice, int postion) {
        if(bluetoothDevice.getName().contains("Dfu")){
            Intent intent = new Intent(MainActivity.this, DfuActivity.class);
            intent.putExtra("mac",bluetoothDevice.getAddress());
            intent.putExtra("name",bluetoothDevice.getName());
            startActivity(intent);
        }else {
            Intent intent = new Intent(MainActivity.this, BleDetailActivity.class);
            intent.putExtra("mac",bluetoothDevice.getAddress());
            intent.putExtra("name",bluetoothDevice.getName());
            startActivity(intent);
        }
    }

    /**
     * 检查设备
     */
    private void checkBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "手机不支持低功耗蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager.getAdapter() == null) {//取得蓝牙适配器
            Toast.makeText(this, "你的手机不支持蓝牙4.0", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    /**
     * 开始搜索
     */
    private void startSearch() {
            ClientManager.getClient(this).registerBluetoothStateListener(mBluetoothStateListener);
            if (ClientManager.getClient(this).isBluetoothOpened()) {
                requestLocation();
            } else {
                ClientManager.getClient(this).openBluetooth();

            }
    }


    /**
     * 打开和关闭蓝牙回调
     */
    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            if (openOrClosed) {
                requestLocation();
            } else {
                deviceList.clear();
                deviceAdapter.setData(deviceList);
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        startRefresh();
    }


    @Override
    protected void onPause() {
        super.onPause();
        ClientManager.getClient(this).stopSearch();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
            ClientManager.getClient(this).unregisterBluetoothStateListener(mBluetoothStateListener);
    }


    /**
     * 扫描设备
     */
    private void scanBle() {
        final SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 1)   // 先扫BLE设备1次，每次3s

                .build();

        ClientManager.getClient(this).search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult result) {

                if (!deviceList.contains(result)) {
                    deviceList.add(result);
                    deviceAdapter.setData(deviceList);
                }
            }

            @Override
            public void onSearchStopped() {
                Log.w(TAG, "----------  onSearchStopped");
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onSearchCanceled() {
                swipeRefresh.setRefreshing(false);
                Log.w(TAG, "----------  onSearchCanceled");
            }
        });
    }




/*--------------android 6.0 起使用蓝牙搜索是需要申请定位功能------以下是权限申请----------------------------------------------------*/

    @AfterPermissionGranted(RC_LOACTION)
    public void requestLocation() {
        String perm[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perm)) {
            scanBle();
        } else {
            EasyPermissions.requestPermissions(this, "应用需要使用位置权限", RC_LOACTION, perm);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "权限不允许");
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "权限允许");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


}
