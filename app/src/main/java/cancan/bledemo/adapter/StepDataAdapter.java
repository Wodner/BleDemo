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
import cancan.bledemo.model.StepModel;

/**
 * 描述：
 * 作者：Wu on 2017/4/25 20:33
 * 邮箱：wuwende@live.cn
 */

public class StepDataAdapter extends RecyclerView.Adapter<StepDataAdapter.MyViewHolder> {


    private Context mContext;
    private List<StepModel> stepModelList;

    public StepDataAdapter(Context mContext) {
        this.mContext = mContext;
        stepModelList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return stepModelList.size();
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
        holder.tvMsg.setText(stepModelList.get(position).getYear()+"-" + stepModelList.get(position).getMonth()+"-"+stepModelList.get(position).getDay()+ "  " +
                stepModelList.get(position).getHour()+":" + stepModelList.get(position).getMinute()+":" + stepModelList.get(position).getSecond() + "\n" +
            "步数 ：" + stepModelList.get(position).getStep());
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvMsg;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvMsg = (TextView)itemView.findViewById(R.id.tv_device);
        }
    }



    public void setData( List<StepModel>  dataModelList){
        stepModelList.clear();
        stepModelList.addAll(dataModelList);
        notifyDataSetChanged();
    }


    public void clear(){
        stepModelList.clear();
        notifyDataSetChanged();
    }


}
