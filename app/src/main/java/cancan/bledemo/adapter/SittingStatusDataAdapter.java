package cancan.bledemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cancan.bledemo.R;
import cancan.bledemo.model.SittingStatusDataModel;

/**
 * 描述：历史坐姿数据适配器
 *
 * 作者：Wu on 2017/5/9 00:06
 * 邮箱：wuwende@live.cn
 */

public class SittingStatusDataAdapter extends  RecyclerView.Adapter<SittingStatusDataAdapter.MyViewHolder> {


    private Context mContext;
    private List<SittingStatusDataModel> sittingStatusDataList;

    public SittingStatusDataAdapter(Context mContext) {
            this.mContext = mContext;
            this.sittingStatusDataList = new ArrayList<>();
            }

    @Override
    public int getItemCount() {
        return sittingStatusDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_device_list,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvMsg.setText(sittingStatusDataList.get(position).getYear() + "年" + sittingStatusDataList.get(position).getMonth()+"月"+sittingStatusDataList.get(position).getDay()+ "日\n" +
                "正坐："  + sittingStatusDataList.get(position).getSitting() + "s  " +
                "前倾：" + sittingStatusDataList.get(position).getForward() + "s  " +
                "后倾：" +  sittingStatusDataList.get(position).getBackward() + "s  " +
                "左倾：" +  sittingStatusDataList.get(position).getLeftLeaning() + "s  " +
                "右倾：" +  sittingStatusDataList.get(position).getRightLeaning() + "s  " );
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvMsg;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvMsg = (TextView)itemView.findViewById(R.id.tv_device);
        }
    }



    public void setData( List<SittingStatusDataModel> dataModelList){
        sittingStatusDataList.clear();
        sittingStatusDataList.addAll(dataModelList);
        notifyDataSetChanged();
    }


    public void clear(){
        sittingStatusDataList.clear();
        notifyDataSetChanged();
    }


}
