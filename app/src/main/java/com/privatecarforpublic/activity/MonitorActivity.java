package com.privatecarforpublic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointRequest;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.ParamErrorResponse;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MonitorActivity extends Activity {
    private static final String TAG = "MonitorActivity";

    //轨迹跟踪客户端
    private AMapTrackClient aMapTrackClient;
    //位置标记
    private Marker marker;
    private User user;
    private static boolean flag = true;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;
    @BindView(R.id.map)
    MapView map;

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_time_monitor);
        ButterKnife.bind(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        map.onCreate(savedInstanceState);
        title.setText("实时位置");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(MonitorActivity.this, 25);
    }

    private void init() {
        map.getMap().getUiSettings().setZoomControlsEnabled(false);//设置地图放大缩小的按钮不显示，可通过手势进行控制
        user=(User)getIntent().getSerializableExtra("user");
        long terminalId=user.getTid();
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag){
                    try {
                        Thread.sleep(3000); //休眠一秒
                        aMapTrackClient.queryLatestPoint(new LatestPointRequest(Long.parseLong(Constants.SERVICE_ID), terminalId), onTrackListener);
                        Log.e(TAG,"重新定位");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void stopTimer(){
        flag = false;
    }

    final OnTrackListener onTrackListener=new OnTrackListener() {
        @Override
        public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
        }

        @Override
        public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
        }

        @Override
        public void onDistanceCallback(DistanceResponse distanceResponse) {
        }

        @Override
        public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
            if (latestPointResponse.isSuccess()) {
                Point point = latestPointResponse.getLatestPoint().getPoint();
                LatLng latLng=new LatLng(point.getLat(),point.getLng());
                // 查询实时位置成功，point为实时位置信息
                if(marker!=null)
                    marker.remove();
                marker = map.getMap().addMarker(new MarkerOptions().position(latLng).title(user.getName()).snippet("当前位置"));
                CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,13,0,0));
                map.getMap().animateCamera(mCameraUpdate);
            } else {
                // 查询实时位置失败
                CommonUtil.showMessage(MonitorActivity.this,"查询实时位置失败");
            }
        }

        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
        }

        @Override
        public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
        }

        @Override
        public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
        }

        @Override
        public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        map.onDestroy();
        stopTimer();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        map.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        map.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        map.onSaveInstanceState(outState);
    }
}
