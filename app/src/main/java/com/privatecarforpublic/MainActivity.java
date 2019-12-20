package com.privatecarforpublic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.help.Tip;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.entity.LocationMode;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackRequest;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.ParamErrorResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.activity.MyCarsActivity;
import com.privatecarforpublic.activity.MyTravelsActivity;
import com.privatecarforpublic.activity.RegimeActivity;
import com.privatecarforpublic.activity.ReimbursementActivity;
import com.privatecarforpublic.activity.RemainingSegmentActivity;
import com.privatecarforpublic.activity.SearchPlaceActivity;
import com.privatecarforpublic.model.MyTravels;
import com.privatecarforpublic.application.MyApplication;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {
    public final static int TO_REGIME=201;
    public final static int TO_CHOOSE_SEGMENT=202;

    private String terminalName="test";
    private Long terminalId=(long)-1;
    private Long trackId=(long)-1;
    private AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //轨迹跟踪客户端
    private AMapTrackClient aMapTrackClient;
    private List<LatLng> latLngList=new ArrayList<>();
    private double latitude;
    private double longitude;

    @BindView(R.id.function)
    ImageView function;
    @BindView(R.id.destination)
    TextView destination;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.map)
    MapView map;

    @OnClick(R.id.function)
    void function(){
        drawer.openDrawer(Gravity.LEFT);
        //打开手势滑动：DrawerLayout.LOCK_MODE_UNLOCKED（Gravity.LEFT：代表左侧的）
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,Gravity.LEFT);
    }

    @OnClick(R.id.destination)
    void toSelectDestination(){
        Intent intent = new Intent(MainActivity.this, RegimeActivity.class);
        startActivityForResult(intent, TO_REGIME);
    }

    @OnClick(R.id.start)
    void startJourney(){
        start.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.VISIBLE);
        initTrack();
        locate();
    }

    @OnClick(R.id.cancel)
    void cancelJourney(){
        start.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        destination.setVisibility(View.VISIBLE);
        CommonUtil.showMessage(this,"行程已取消");
    }

    @OnClick(R.id.finish)
    void finishJourney(){
        finish.setText("行程已结束");
        TrackParam trackParam = new TrackParam(Long.parseLong(Constants.SERVICE_ID), terminalId);
        trackParam.setTrackId(trackId);
        aMapTrackClient.stopTrack(trackParam, onTrackLifecycleListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(MainActivity.this, 25);

        map.onCreate(savedInstanceState);
        //首次当前位置定位
        firstLocate();
        /*//实现实时定位
        locate();*/
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init(){
        ButterKnife.bind(this);
        start.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_purse) {
            // Handle the camera action
        } else if (id == R.id.remaining_segment) {
            Intent intent = new Intent(MainActivity.this, RemainingSegmentActivity.class);
            startActivityForResult(intent,TO_CHOOSE_SEGMENT);

        } else if (id == R.id.reply_reimbursement) {
            Intent intent = new Intent(MainActivity.this, ReimbursementActivity.class);
            startActivity(intent);

        } else if (id == R.id.my_cars) {
            Intent intent = new Intent(MainActivity.this, MyCarsActivity.class);
            startActivity(intent);

        } else if (id == R.id.my_travels) {
            Intent intent = new Intent(MainActivity.this, MyTravelsActivity.class);
            startActivity(intent);
        } else if (id == R.id.remove) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_REGIME && resultCode == Activity.RESULT_OK) {
            start.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            destination.setVisibility(View.INVISIBLE);
        }else if (requestCode == TO_CHOOSE_SEGMENT && resultCode == Activity.RESULT_OK) {
             CommonUtil.showMessage(this,"开始段行程");
        }

    }

    //初始化并开启猎鹰服务
    private void initTrack(){
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        aMapTrackClient.setCacheSize(20);
        aMapTrackClient.setLocationMode(LocationMode.HIGHT_ACCURACY);
        //这一步将直接创建TRACK并开启跟踪上报
        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Long.parseLong(Constants.SERVICE_ID), terminalName), onTrackListener);
    }

    //实现实时定位
    private void locate(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        /*mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式,AMapLocationMode.Battery_Saving为低耗模式，只是用网络定位
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(3000);
        //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);*/
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            //启动定位
            mLocationClient.startLocation();
        }
    }

    //首次当前位置定位
    private void firstLocate(){
        AMap aMap=null;
        if (aMap == null) {
            aMap = map.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.point);
        myLocationStyle.myLocationIcon(bitmapDescriptor);
        myLocationStyle.strokeWidth((float) 0);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setZoomControlsEnabled(false);//设置地图放大缩小的按钮不显示，可通过手势进行控制
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
        //aMap.setOnMyLocationChangeListener(mAMapLocationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        map.onDestroy();
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

    //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
    // 以下为后者的举例：
    AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    latitude=amapLocation.getLatitude();//获取纬度
                    longitude=amapLocation.getLongitude();//获取经度
                    //Toast.makeText(MainActivity.this, "经度:" + longitude+"纬度:"+latitude, Toast.LENGTH_SHORT).show();

                    //测距
                    LatLng latLng1=new LatLng(latitude,longitude);
                    LatLng latLng2=new LatLng((double)29.888942222222,(double)121.64113277777766);
                    float distance = AMapUtils.calculateLineDistance(latLng1,latLng2);
                    //Toast.makeText(MainActivity.this, "距离为:" + distance, Toast.LENGTH_SHORT).show();


                    //绘制线
                    judge(latLngList,latLng1);
                    Polyline polyline=map.getMap().addPolyline(new PolylineOptions().
                            addAll(latLngList).width(10).color(Color.argb(255, 1, 1, 1)));
                    Log.e("AmapError","location Error, ErrCode:"
                            + polyline.getPoints().size());
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private void judge(List<LatLng> list, LatLng latLng){
        if(0==list.size())
        {
            list.add(latLng);
            return;
        }
        LatLng last=list.get(list.size()-1);
        if(last.latitude==latLng.latitude
                &&last.longitude==latLng.longitude)
            return;
        else
            list.add(latLng);
    }

    final OnTrackLifecycleListener onTrackLifecycleListener = new OnTrackLifecycleListener() {
        @Override
        public void onBindServiceCallback(int var1, String var2) {
        }

        @Override
        public void onStopGatherCallback(int var1, String var2) {

        }

        @Override
        public void onStopTrackCallback(int var1, String var2) {
            Toast.makeText(MainActivity.this, "定位跟踪关闭成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStartGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_GATHER_SUCEE ||
                    status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                Toast.makeText(MainActivity.this, "定位采集开启成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "定位采集启动异常，" + msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStartTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_TRACK_SUCEE ||
                    status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
                    status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 服务启动成功，继续开启收集上报
                aMapTrackClient.startGather(this);
            } else {
                Toast.makeText(MainActivity.this, "轨迹上报服务服务启动异常，" + msg, Toast.LENGTH_SHORT).show();
            }
        }
    };
    final OnTrackListener onTrackListener=new OnTrackListener() {
        @Override
        public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
            if (queryTerminalResponse.isSuccess()) {
                if (queryTerminalResponse.getTid() <= 0)
                    // terminal还不存在，先创建
                    aMapTrackClient.addTerminal(new AddTerminalRequest(terminalName, Long.parseLong(Constants.SERVICE_ID)), this);
                else {
                    // terminal已经存在，直接开启猎鹰服务
                    terminalId = queryTerminalResponse.getTid();
                    aMapTrackClient.addTrack(new AddTrackRequest(Long.parseLong(Constants.SERVICE_ID), terminalId),this);
                }
            }else {
                // 请求失败
                Toast.makeText(MainActivity.this, "请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
            if (addTerminalResponse.isSuccess()) {
                // 创建完成，开启猎鹰服务
                terminalId = addTerminalResponse.getTid();
                aMapTrackClient.addTrack(new AddTrackRequest(Long.parseLong(Constants.SERVICE_ID), terminalId),this);
            } else {
                // 请求失败
                Toast.makeText(MainActivity.this, "请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDistanceCallback(DistanceResponse distanceResponse) {

        }

        @Override
        public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

        }

        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

        }

        @Override
        public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

        }

        @Override
        public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
            if (addTrackResponse.isSuccess()) {
                trackId = addTrackResponse.getTrid();
                Log.e("trackId",trackId+"");
                TrackParam trackParam = new TrackParam(Long.parseLong(Constants.SERVICE_ID), terminalId);
                trackParam.setTrackId(trackId);
                aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
            } else {
                Toast.makeText(MainActivity.this, "网络请求失败，" + addTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

        }
    };
}
