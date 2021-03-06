package cancan.bledemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cancan.bledemo.adapter.SittingStatusDataAdapter;
import cancan.bledemo.model.SittingStatusDataModel;

/**
 * 描述：
 * 作者：Wu on 2017/5/9 00:14
 * 邮箱：wuwende@live.cn
 */

public class BleHistorySitStatusActivity extends AppCompatActivity {

    @Bind(R.id.sitrecyclerview)
    RecyclerView sitrecyclerview;

    private SittingStatusDataAdapter sittingStatusDataAdapter;
    private List<SittingStatusDataModel> sittingStatusDataModelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getIntent().getCharSequenceExtra("title"));

        //获得传过来的List<Object>
        sittingStatusDataModelList = (List<SittingStatusDataModel>) getIntent().getSerializableExtra("history_sit");
        initHistorySitView();
    }


    private void initHistorySitView() {
        sittingStatusDataAdapter = new SittingStatusDataAdapter(this);
        sitrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        sitrecyclerview.setAdapter(sittingStatusDataAdapter);
        sittingStatusDataAdapter.setData(sittingStatusDataModelList);
    }
}

