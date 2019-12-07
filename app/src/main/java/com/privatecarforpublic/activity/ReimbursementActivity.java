package com.privatecarforpublic.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.ReimItemAdapter;
import com.privatecarforpublic.model.Reimburse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReimbursementActivity extends Activity {
    private List<Reimburse> reimburseList = new ArrayList<>();
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.reim_list)
    ListView listView;
    @BindView(R.id.side)
    TextView side;

    @OnClick(R.id.back)
    void back(){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimbursement);
        ButterKnife.bind(this);
        title.setText("申请报销");
        side.setVisibility(View.INVISIBLE);

        init();
        ReimItemAdapter adapter = new ReimItemAdapter(ReimbursementActivity.this,R.layout.reim_item, reimburseList);
        listView.setAdapter(adapter);


    }

    private void init(){
        for(int i = 0; i < 24; ++i){
            Reimburse reimburse = new Reimburse(R.drawable.head_image, "12-05  15:18", i +"始   浙江大学软件学院西门",
                    "终   宁波站", "未报销",50.0);
            reimburseList.add(reimburse);
        }
    }
}
