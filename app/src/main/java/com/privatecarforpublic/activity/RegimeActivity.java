package com.privatecarforpublic.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.services.help.Tip;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.SegmentAdapter;
import com.privatecarforpublic.application.MyApplication;
import com.privatecarforpublic.model.Segment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class RegimeActivity extends Activity {
    private static final String TAG = "RegimeActivity";

    private Calendar calendar= Calendar.getInstance(Locale.CHINA);
    public final static int TO_SEARCH_DESTINATION = 101;
    public final static int TO_SEARCH_DEPARTURE = 102;
    public final static int TO_SHOW_CARS = 103;

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
        setContentView(R.layout.regime_route);
        ButterKnife.bind(this);
        title.setText("制定行程");
        side.setText("添加");
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(RegimeActivity.this, 25);
    }

    private void init() {
        segmentList = new ArrayList<>();
        segmentList.add(newSegment());
        segmentAdapter = new SegmentAdapter(this, RegimeActivity.this, segmentList);
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

    @OnClick(R.id.time)
    void setTime() {
        showTimePickerDialog(this,2,time,calendar);
    }

    @OnClick(R.id.regime)
    void toSelectCar() {
        Intent intent = new Intent(RegimeActivity.this, SelectCarActivity.class);
        startActivityForResult(intent,TO_SHOW_CARS);
    }

    private Segment newSegment() {
        Tip departure = new Tip();
        departure.setName("你想从哪出发？");
        Tip destination = new Tip();
        destination.setName("你想去哪里？");
        return new Segment(departure, destination);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_SEARCH_DESTINATION && resultCode == Activity.RESULT_OK) {
            Tip tip = (Tip) data.getParcelableExtra("tip");
            int position = data.getIntExtra("position", 0);
            segmentList.get(position).setDestination(tip);
            segmentAdapter.notifyDataSetChanged();
        } else if (requestCode == TO_SEARCH_DEPARTURE && resultCode == Activity.RESULT_OK) {
            Tip tip = (Tip) data.getParcelableExtra("tip");
            int position = data.getIntExtra("position", 0);
            segmentList.get(position).setDeparture(tip);
            segmentAdapter.notifyDataSetChanged();
        } else if (requestCode == TO_SHOW_CARS && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();   //finish应该写到这个地方
        }
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

    private void showTimePickerDialog(Activity activity,int themeResId, final TextView tv, Calendar calendar) {
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog( activity,themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.setText("出行时间：" + hourOfDay + "时" + minute  + "分");
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true).show();
    }

}
