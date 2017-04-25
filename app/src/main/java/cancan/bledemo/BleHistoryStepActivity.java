package cancan.bledemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cancan.bledemo.adapter.StepDataAdapter;
import cancan.bledemo.model.StepModel;

/**
 * 描述：历史步数显示页面
 * 作者：Wu on 2017/4/25 20:31
 * 邮箱：wuwende@live.cn
 */

public class BleHistoryStepActivity extends AppCompatActivity {

        @Bind(R.id.sitrecyclerview)
        RecyclerView sitrecyclerview;

        private StepDataAdapter stepDataAdapter;
        private List<StepModel> historyStepList;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recyclerview);
            ButterKnife.bind(this);
            getSupportActionBar().setTitle(getIntent().getCharSequenceExtra("title"));

            //获得传过来的List<Object>
            historyStepList = (List<StepModel>) getIntent().getSerializableExtra("history_step");
            initHistorySitView();
        }


    private void initHistorySitView() {
        stepDataAdapter = new StepDataAdapter(this);
        sitrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        sitrecyclerview.setAdapter(stepDataAdapter);
        stepDataAdapter.setData(historyStepList);
    }


}
