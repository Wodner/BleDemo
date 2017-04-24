package cancan.bledemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 描述：
 * 作者：Wu on 2017/4/24 13:58
 * 邮箱：wuwende@live.cn
 */

public class BaseViewHoder<T> extends RecyclerView.ViewHolder {

    public BaseViewHoder(View itemView) {
        super(itemView);
    }

    public void refreshData(T data, int position) {

    }
}
