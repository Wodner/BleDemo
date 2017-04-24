package cancan.bledemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cancan.bledemo.adapter.SittingDataAdapter;
import cancan.bledemo.model.SittingDataModel;

/**
 * 描述：
 * 作者：Wu on 2017/4/24 23:55
 * 邮箱：wuwende@live.cn
 */

public class BleHistorySitStatusActivity extends AppCompatActivity {

    @Bind(R.id.sitrecyclerview)
    RecyclerView sitrecyclerview;

    private SittingDataAdapter sittingHistoryDataAdapter;
    private List<SittingDataModel> historySitList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getIntent().getCharSequenceExtra("title"));

        //获得传过来的List<Object>

        historySitList = (List<SittingDataModel>) getIntent().getSerializableExtra("history_sit");
        initHistorySitView();
    }


    private void initHistorySitView() {
        sittingHistoryDataAdapter = new SittingDataAdapter(this);
        sitrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        sitrecyclerview.setAdapter(sittingHistoryDataAdapter);
        sittingHistoryDataAdapter.setData(historySitList);
    }


}
