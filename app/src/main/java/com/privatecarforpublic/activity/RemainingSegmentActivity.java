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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.RemainSecAdapter;
import com.privatecarforpublic.adapter.SegmentAdapter;
import com.privatecarforpublic.model.SecRoute;
import com.privatecarforpublic.model.Segment;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;

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

    @BindView(R.id.remain_sec_list)
    ListView remain_sec_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;
    @BindView(R.id.start_time)
    TextView start_time;
    @BindView(R.id.end_time)
    TextView end_time;

    private List<SecRoute> secRouteList;
    private RemainSecAdapter remainSecAdapter;
    private String select_time;
    private long routeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remaining_segment);
        ButterKnife.bind(this);
        title.setText("剩余段程");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(RemainingSegmentActivity.this, 25);
    }

    private void init() {
        routeId=getIntent().getLongExtra("routeId",-1);
        if(routeId==-1){
            CommonUtil.showMessage(RemainingSegmentActivity.this, "已无剩余段程");
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson=new Gson();
                    secRouteList=new ArrayList<>();
                    String url= Constants.SERVICE_ROOT + "SecRoute/findRemainSec?routeId="+routeId;
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, SharePreferenceUtil.getString(RemainingSegmentActivity.this, "token", ""), url, null);
                    if (responseResult.getCode() !=200 ) {
                        CommonUtil.showMessage(RemainingSegmentActivity.this, responseResult.getMessage());
                    } else{
                        secRouteList=gson.fromJson(responseResult.getData(), new TypeToken<List<SecRoute>>() {
                        }.getType());
                    }
                } catch (Exception e) {
                    CommonUtil.showMessage(RemainingSegmentActivity.this, "查询剩余段程出错");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        remainSecAdapter= new RemainSecAdapter(this,secRouteList);
        remain_sec_list.setAdapter(remainSecAdapter);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }


    @OnItemClick(R.id.remain_sec_list)
    void onSegmentItemClick(AdapterView<?> adapterView, View view, int i, long l){
        Intent intent = new Intent();
        intent.putExtra("secRoute",secRouteList.get(i));
        setResult(Activity.RESULT_OK, intent);
        finish();   //finish应该写到这个地方
    }
}
