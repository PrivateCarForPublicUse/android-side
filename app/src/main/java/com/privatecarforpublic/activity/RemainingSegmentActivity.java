package com.privatecarforpublic.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.services.help.Tip;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.SegmentAdapter;
import com.privatecarforpublic.model.Segment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class RemainingSegmentActivity extends Activity {
    private static final String TAG = "RemainingSegmentActivity";

    @BindView(R.id.seg_list)
    ListView seg_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;
    @BindView(R.id.time)
    TextView time;

    private List<Segment> segmentList;
    private SegmentAdapter segmentAdapter;
    private String select_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remaining_segment);
        ButterKnife.bind(this);
        title.setText("行程分段");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(RemainingSegmentActivity.this, 25);
    }

    private void init() {
        segmentList = new ArrayList<>();
        segmentList.add(newSegment());
        segmentAdapter = new SegmentAdapter(this, RemainingSegmentActivity.this, segmentList);
        seg_list.setAdapter(segmentAdapter);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    private Segment newSegment() {
        Tip departure = new Tip();
        departure.setName("你想从哪出发？");
        Tip destination = new Tip();
        destination.setName("你想去哪里？");
        return new Segment(departure, destination);
    }

    @OnItemClick(R.id.seg_list)
    void onSegmentItemClick(AdapterView<?> adapterView, View view, int i, long l){
        Intent intent = new Intent();
        intent.putExtra("departure",segmentList.get(i).getDeparture());
        intent.putExtra("destination",segmentList.get(i).getDestination());
        setResult(Activity.RESULT_OK, intent);
        finish();   //finish应该写到这个地方
    }
}
