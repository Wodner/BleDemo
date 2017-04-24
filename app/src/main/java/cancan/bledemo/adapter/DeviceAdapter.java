package cancan.bledemo.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

import cancan.bledemo.R;


/**
 * 功能： descriable
 * 作者： Administrator
 * 日期： 2017/3/15 15:08
 * 邮箱： descriable
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder>{

    private Context mContext;
    private List<SearchResult> deviceList;

    public DeviceAdapter(Context mContext) {
        this.mContext = mContext;
        deviceList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return deviceList==null?0:deviceList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_device_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvDevice.setText(deviceList.get(position).getName()+ "\n" +
            deviceList.get(position).getAddress());
        //判断是否设置了监听器
        if(onRecyclerViewItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    onRecyclerViewItemClickListener.onItemClick(v ,deviceList.get(position),position);
                }
            });
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDevice;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvDevice = (TextView)itemView.findViewById(R.id.tv_device);
        }
    }

    /**
     * @param devices
     */
    public void setData( List<SearchResult> devices){
        deviceList.clear();
        deviceList.addAll(devices);
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }

    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View v, SearchResult bluetoothDevice, int postion);
    }

    public void setOnLongItemClickListener(OnLongRecyclerViewItemClickListener listener) {
        this.onLongRecyclerViewItemClickListener = listener;
    }

    public  interface OnLongRecyclerViewItemClickListener {
        void onItemClick(View v, BluetoothDevice bluetoothDevice, int postion);
    }

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private OnLongRecyclerViewItemClickListener onLongRecyclerViewItemClickListener;
}
