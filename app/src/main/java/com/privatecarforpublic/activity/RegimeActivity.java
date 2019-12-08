package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.SegmentAdapter;
import com.privatecarforpublic.model.Segment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class RegimeActivity extends Activity {
    public final static int TO_SEARCH_DESTINATION=101;
    public final static int TO_SEARCH_DEPARTURE=102;

    @BindView(R.id.seg_list)
    ListView seg_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private List<Segment> segmentList;
    private SegmentAdapter segmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regime_route);
        ButterKnife.bind(this);
        title.setText("制定行程");
        side.setText("添加");
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(RegimeActivity.this, 25);
    }

    private void init(){
        segmentList=new ArrayList<>();
        segmentList.add(newSegment());
        segmentAdapter=new SegmentAdapter(this,RegimeActivity.this,segmentList);
        seg_list.setAdapter(segmentAdapter);

        seg_list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                //menu.setHeaderTitle("选择操作");
                menu.add(0, 1, 0, "删除段行程");
            }
        });
    }


    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @OnClick(R.id.side)
    void addSegment() {
        segmentList.add(newSegment());
        segmentAdapter.notifyDataSetChanged();
    }

    private Segment newSegment(){
        Tip departure=new Tip();
        departure.setName("你想从哪出发？");
        Tip destination=new Tip();
        destination.setName("你想去哪里？");
        return new Segment(departure,destination);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_SEARCH_DESTINATION && resultCode == Activity.RESULT_OK) {
            Tip tip=(Tip)data.getParcelableExtra("tip");
            int position=data.getIntExtra("position",0);
            segmentList.get(position).setDestination(tip);
        }else if (requestCode == TO_SEARCH_DEPARTURE && resultCode == Activity.RESULT_OK) {
            Tip tip=(Tip)data.getParcelableExtra("tip");
            int position=data.getIntExtra("position",0);
            segmentList.get(position).setDeparture(tip);
        }
        segmentAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //info.id得到listview中选择的条目绑定的id
        int id = (int) info.id;
        switch (item.getItemId()) {
            case 1:
                segmentList.remove(id);
                segmentAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
