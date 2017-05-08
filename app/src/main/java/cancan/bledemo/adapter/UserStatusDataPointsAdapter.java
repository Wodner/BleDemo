package cancan.bledemo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cancan.bledemo.R;
import cancan.bledemo.model.UserStatusDataModel;

/**
 * 描述：
 * 作者：Wu on 2017/4/24 14:18
 * 邮箱：wuwende@live.cn
 */

public class UserStatusDataPointsAdapter extends BaseViewHoder<UserStatusDataModel> {



    private Context mContext;

    private RecyclerView recyclerView;
    private TextView tvDate;


    public UserStatusDataPointsAdapter(View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        tvDate = (TextView)itemView.findViewById(R.id.tv_date);
        recyclerView = (RecyclerView)itemView.findViewById(R.id.recyclerview);
    }


    @Override
    public void refreshData(UserStatusDataModel data, int position) {
        tvDate.setText(data.getMonth()+"月" +data.getDay()+"日");
        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(horizontalAdapter);
        horizontalAdapter.setData(data);
    }


    private class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder>{

        private Context mContext;
        private UserStatusDataModel sittingDataModel;

        public HorizontalAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getItemCount() {
            return sittingDataModel==null?0:sittingDataModel.getRows().size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sitting_points_list, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tvTime.setText(sittingDataModel.getRows().get(position).getHour()+"时" +
                sittingDataModel.getRows().get(position).getMinute()+"分");
            holder.tvStatus.setText(sittingDataModel.getRows().get(position).getStatus());
        }


        class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView tvTime;
            private TextView tvStatus;
            public MyViewHolder(View itemView) {
                super(itemView);
                tvTime = (TextView)itemView.findViewById(R.id.tv_time);
                tvStatus = (TextView)itemView.findViewById(R.id.tv_status);
            }
        }

        public void setData(UserStatusDataModel sittingData){
            this.sittingDataModel = sittingData;
            notifyDataSetChanged();
        }
    }



//    private String getStatus(int status){
//        String s="";
//        if (status==0){
//            s = "未知";
//        }else if(status==1){
//            s = "坐/站立";
//        }else if(status==2){
//            s = "躺";
//        }else if(status==3){
//            s = "走";
//        }else {
//            s = "跑";
//        }
//        return s;
//    }











}
