package com.privatecarforpublic.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.services.help.Tip;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.SegmentAdapter;
import com.privatecarforpublic.model.PointLatDTO;
import com.privatecarforpublic.model.Segment;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.util.CommonUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegimeActivity extends Activity {
    private static final String TAG = "RegimeActivity";

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    public final static int TO_SEARCH_DESTINATION = 101;
    public final static int TO_SEARCH_DEPARTURE = 102;
    public final static int TO_SHOW_CARS = 103;

    @BindView(R.id.seg_list)
    ListView seg_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;
    @BindView(R.id.start_time)
    TextView start_time;
    @BindView(R.id.end_time)
    TextView end_time;
    @BindView(R.id.reason)
    EditText reason;

    private User user;
    private List<Segment> segmentList;
    private List<PointLatDTO> pointList;
    private List<String> nameList;
    private SegmentAdapter segmentAdapter;
    private Date start;
    private Date end;

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
        user=(User)getIntent().getSerializableExtra("user");
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

    @OnClick(R.id.start_time)
    void setStartTime() {
        start=new Date();
        showTimePickerDialog(this, 2, start_time, calendar,start);
        showDatePickerDialog(this, 2, calendar,start);
    }

    @OnClick(R.id.end_time)
    void setEndTime() {
        end=new Date();
        showTimePickerDialog(this, 2, end_time, calendar,end);
        showDatePickerDialog(this, 2, calendar,end);
    }

    @OnClick(R.id.regime)
    void toSelectCar() {
        if(start==null||end==null||start.after(end)){
            CommonUtil.showMessage(this,"时间填写有误");
            return;
        }else if(reason.getText().toString()==null||reason.getText().toString().length()==0){
            CommonUtil.showMessage(this,"申请理由不能为空");
            return;
        }
        pointList=new ArrayList<>();
        nameList=new ArrayList<>();
        for(int i=0;i<segmentList.size();i++){
            String longitude=segmentList.get(i).getDeparture().getPoint().getLongitude()+"";
            String latitude=segmentList.get(i).getDeparture().getPoint().getLatitude()+"";
            pointList.add(new PointLatDTO(longitude,latitude));
            nameList.add(segmentList.get(i).getDeparture().getName());
            if(i==segmentList.size()-1){
                longitude=segmentList.get(i).getDestination().getPoint().getLongitude()+"";
                latitude=segmentList.get(i).getDestination().getPoint().getLatitude()+"";
                pointList.add(new PointLatDTO(longitude,latitude));
                nameList.add(segmentList.get(i).getDestination().getName());
            }
        }
        Intent intent = new Intent(RegimeActivity.this, SelectCarActivity.class);
        intent.putExtra("startTime",start);
        intent.putExtra("endTime",end);
        intent.putExtra("user",user);
        intent.putExtra("reason",reason.getText().toString());
        intent.putExtra("pointList",(Serializable) pointList);
        intent.putExtra("nameList",(Serializable) nameList);
        startActivityForResult(intent, TO_SHOW_CARS);
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
            if(segmentList.size()>position+1)
                segmentList.get(position+1).setDeparture(tip);
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

    private void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar,Date date) {
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm" );
                        date.setHours(hourOfDay);
                        date.setMinutes(minute);
                        String str = sdf.format(date);
                        if(tv==start_time)
                            tv.setText("开始时间："+str);
                        else
                            tv.setText("结束时间："+str);
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }

    private void showDatePickerDialog(Activity activity, int themeResId, Calendar calendar,Date date) {
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new DatePickerDialog(activity,themeResId,
                new DatePickerDialog.OnDateSetListener() {
                    //实现监听方法
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //设置文本显示内容，i为年，i1为月，i2为日
                        date.setYear(i-1900);
                        date.setMonth(i1);
                        date.setDate(i2);
                    }
                }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DATE)).show();//记得使用show才能显示悬浮窗
    }

}
