package cancan.bledemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cancan.bledemo.R;
import cancan.bledemo.model.SittingDataModel;

/**
 * 描述：
 * 作者：Wu on 2017/4/24 13:55
 * 邮箱：wuwende@live.cn
 */

public class SittingDataAdapter extends RecyclerView.Adapter<BaseViewHoder>{


    private Context mContext;
    private List<SittingDataModel> sittingDataModelList;


    public SittingDataAdapter(Context mContext) {
        this.mContext = mContext;
        sittingDataModelList = new ArrayList<>();
    }




    @Override
    public int getItemCount() {
        return sittingDataModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public BaseViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sitting_list,parent,false);
        return new SittingDataPointsAdapter(view,mContext);
    }

    @Override
    public void onBindViewHolder(BaseViewHoder holder, int position) {
        holder.refreshData(sittingDataModelList.get(position),position);
    }


    public void setData( List<SittingDataModel>  dataModelList){
        sittingDataModelList.clear();
        sittingDataModelList.addAll(dataModelList);
        notifyDataSetChanged();
    }


    public void clear(){
        sittingDataModelList.clear();
        notifyDataSetChanged();
    }



}
