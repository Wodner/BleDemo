package cancan.bledemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cancan.bledemo.adapter.UserStatusDataAdapter;
import cancan.bledemo.model.UserStatusDataModel;

/**
 * 描述：历史坐姿显示页面
 * 作者：Wu on 2017/4/24 23:55
 * 邮箱：wuwende@live.cn
 */

public class BleHistoryUserStatusActivity extends AppCompatActivity {

    @Bind(R.id.sitrecyclerview)
    RecyclerView sitrecyclerview;

    private UserStatusDataAdapter sittingHistoryDataAdapter;
    private List<UserStatusDataModel> historySitList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getIntent().getCharSequenceExtra("title"));

        //获得传过来的List<Object>

        historySitList = (List<UserStatusDataModel>) getIntent().getSerializableExtra("history_user");
        initHistorySitView();
    }


    private void initHistorySitView() {
        sittingHistoryDataAdapter = new UserStatusDataAdapter(this);
        sitrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        sitrecyclerview.setAdapter(sittingHistoryDataAdapter);
        sittingHistoryDataAdapter.setData(historySitList);
    }


}
