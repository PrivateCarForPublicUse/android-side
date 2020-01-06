package com.privatecarforpublic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.entity.LocationMode;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackRequest;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceRequest;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.ParamErrorResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.activity.MyCarsActivity;
import com.privatecarforpublic.activity.MyTravelsActivity;
import com.privatecarforpublic.activity.RegimeActivity;
import com.privatecarforpublic.activity.ReimbursementActivity;
import com.privatecarforpublic.activity.RemainingSegmentActivity;
import com.privatecarforpublic.activity.SelectCarActivity;
import com.privatecarforpublic.model.PointLatDTO;
import com.privatecarforpublic.model.Route;
import com.privatecarforpublic.model.RouteModel;
import com.privatecarforpublic.model.SecRoute;
import com.privatecarforpublic.model.SecRouteModel;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.response.PollingResult;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.IGetRequest;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_SHORTEST;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {
    public final static String TAG = "MainActivity";
    public final static int TO_REGIME = 201;
    public final static int TO_CHOOSE_SEGMENT = 202;

    private float plannedDistance;
    private double actualDistance;
    private User user;
    private String terminalName;
    private Long terminalId = (long) -1;
    private Long trackId = (long) -1;
    private long startTime;
    private long endTime;
    //轮询结果
    private PollingResult recordResult;
    private int status = -100;

    private AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //轨迹跟踪客户端
    private AMapTrackClient aMapTrackClient;
    private List<LatLng> latLngList = new ArrayList<>();
    private LatLonPoint startPoint;
    private LatLonPoint endPoint;
    private long secRouteId;

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
    void function() {
        drawer.openDrawer(Gravity.LEFT);
        //打开手势滑动：DrawerLayout.LOCK_MODE_UNLOCKED（Gravity.LEFT：代表左侧的）
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
    }

    @OnClick(R.id.destination)
    void toSelectDestination() {
        Intent intent = new Intent(MainActivity.this, RegimeActivity.class);
        intent.putExtra("user", user);
        startActivityForResult(intent, TO_REGIME);
    }

    @OnClick(R.id.start)
    void startJourney() {
        startTime = System.currentTimeMillis();
        initTrack();
    }

    @OnClick(R.id.cancel)
    void cancelJourney() {
        start.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        destination.setVisibility(View.VISIBLE);
        CommonUtil.showMessage(this, "行程已取消");
    }

    @OnClick(R.id.finish)
    void finishJourney() {
        endTime = System.currentTimeMillis();
        DistanceRequest distanceRequest = new DistanceRequest(
                Long.parseLong(Constants.SERVICE_ID),
                terminalId,
                startTime, // 开始时间
                endTime,   // 结束时间
                -1  // 轨迹id，传-1表示包含散点在内的所有轨迹点
        );
        aMapTrackClient.queryDistance(distanceRequest, onTrackListener);
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

    private void init() {
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra("user");
        terminalName = user.getPhoneNumber();
        View headView = navigationView.getHeaderView(0);
        ImageView head_image = headView.findViewById(R.id.head_image);
        TextView username = headView.findViewById(R.id.username);
        Picasso.get().load(user.getHeadPhotoUrl()).into(head_image);
        username.setText(user.getUserName());
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
            if (recordResult != null) {
                intent.putExtra("routeId", recordResult.getData().getRoute().getId());
            }
            startActivityForResult(intent, TO_CHOOSE_SEGMENT);

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
            start.setText("等待审核中...");
            start.setClickable(false);
            cancel.setVisibility(View.VISIBLE);
            destination.setVisibility(View.INVISIBLE);

            PointLatDTO firstPoint = (PointLatDTO) data.getSerializableExtra("firstPoint");
            startPoint = new LatLonPoint(Double.parseDouble(firstPoint.getLatitude()), Double.parseDouble(firstPoint.getLongitude()));
            PointLatDTO secondPoint = (PointLatDTO) data.getSerializableExtra("secondPoint");
            endPoint = new LatLonPoint(Double.parseDouble(secondPoint.getLatitude()), Double.parseDouble(secondPoint.getLongitude()));

            polling(data.getLongExtra("routeId", -1));
        } else if (requestCode == TO_CHOOSE_SEGMENT && resultCode == Activity.RESULT_OK) {
            startTime = System.currentTimeMillis();
            finish.setText("结束行程");
            finish.setClickable(true);
            CommonUtil.showMessage(this, "开始段行程");
            SecRoute secRoute = (SecRoute) data.getSerializableExtra("secRoute");
            startPoint.setLatitude(Double.parseDouble(secRoute.getOriLatitude()));
            startPoint.setLongitude(Double.parseDouble(secRoute.getOriLongitude()));
            endPoint.setLatitude(Double.parseDouble(secRoute.getDesLatitude()));
            endPoint.setLongitude(Double.parseDouble(secRoute.getDesLongitude()));
            secRouteId = secRoute.getId();
            latLngList.clear();
            mLocationClient.startLocation();
            initTrack();
        }

    }

    private void updateStarted() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> param = new HashMap<>();
                    RouteModel routeModel = recordResult.getData();
                    param.put("routeId", routeModel.getRoute().getId());
                    param.put("secRouteId", secRouteId);
                    param.put("tid", terminalId);
                    param.put("trid", trackId);
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, SharePreferenceUtil.getString(MainActivity.this, "token", ""), Constants.SERVICE_ROOT + "Route/start", param);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(MainActivity.this, "开始行程失败");
                        handler.sendEmptyMessage(4);
                        return;
                    }
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    handler.sendEmptyMessage(4);
                    CommonUtil.showMessage(MainActivity.this, "开始行程出错");
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void updateEnded() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> param = new HashMap<>();
                    RouteModel routeModel = recordResult.getData();
                    param.put("routeId", routeModel.getRoute().getId());
                    param.put("secRouteId", secRouteId);
                    param.put("actualDistance", actualDistance);
                    param.put("plannedDistance", plannedDistance);
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, SharePreferenceUtil.getString(MainActivity.this, "token", ""), Constants.SERVICE_ROOT + "Route/stop", param);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(MainActivity.this, "结束行程失败");
                        //handler.sendEmptyMessage(4);
                        return;
                    }
                    handler.sendEmptyMessage(5);
                } catch (Exception e) {
                    //handler.sendEmptyMessage(4);
                    CommonUtil.showMessage(MainActivity.this, "结束行程出错");
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //初始化并开启猎鹰服务
    private void initTrack() {
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        aMapTrackClient.setCacheSize(20);
        aMapTrackClient.setLocationMode(LocationMode.HIGHT_ACCURACY);
        //这一步将直接创建TRACK并开启跟踪上报
        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Long.parseLong(Constants.SERVICE_ID), terminalName), onTrackListener);
        calculate();
    }

    //实现实时定位
    private void locate() {
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
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            //启动定位
            mLocationClient.startLocation();
        }
    }

    //首次当前位置定位
    private void firstLocate() {
        AMap aMap = null;
        if (aMap == null) {
            aMap = map.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.point);
        myLocationStyle.myLocationIcon(bitmapDescriptor);
        myLocationStyle.strokeWidth((float) 0);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        //设置希望展示的地图缩放级别
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(15);
        aMap.animateCamera(mCameraUpdate);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setZoomControlsEnabled(false);//设置地图放大缩小的按钮不显示，可通过手势进行控制
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
        //aMap.setOnMyLocationChangeListener(mAMapLocationListener);
    }

    private void calculate() {
        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(onRouteSearchListener);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, DRIVING_SINGLE_SHORTEST, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
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
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    double latitude = amapLocation.getLatitude();//获取纬度
                    double longitude = amapLocation.getLongitude();//获取经度
                    //Toast.makeText(MainActivity.this, "经度:" + longitude+"纬度:"+latitude, Toast.LENGTH_SHORT).show();

                    //测距
                    LatLng latLng1 = new LatLng(latitude, longitude);
                    LatLng latLng2 = new LatLng((double) 29.888942222222, (double) 121.64113277777766);
                    float distance = AMapUtils.calculateLineDistance(latLng1, latLng2);
                    //Toast.makeText(MainActivity.this, "距离为:" + distance, Toast.LENGTH_SHORT).show();


                    //绘制线
                    judge(latLngList, latLng1);
                    Polyline polyline = map.getMap().addPolyline(new PolylineOptions().
                            addAll(latLngList).width(10).color(Color.argb(255, 1, 1, 1)));
                    Log.e(TAG, "记录点数："
                            + polyline.getPoints().size());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private void judge(List<LatLng> list, LatLng latLng) {
        if (0 == list.size()) {
            list.add(latLng);
            return;
        }
        LatLng last = list.get(list.size() - 1);
        if (last.latitude == latLng.latitude
                && last.longitude == latLng.longitude)
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
    final OnTrackListener onTrackListener = new OnTrackListener() {
        @Override
        public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
            if (queryTerminalResponse.isSuccess()) {
                if (queryTerminalResponse.getTid() <= 0)
                    // terminal还不存在，先创建
                    aMapTrackClient.addTerminal(new AddTerminalRequest(terminalName, Long.parseLong(Constants.SERVICE_ID)), this);
                else {
                    // terminal已经存在，直接开启猎鹰服务
                    terminalId = queryTerminalResponse.getTid();
                    aMapTrackClient.addTrack(new AddTrackRequest(Long.parseLong(Constants.SERVICE_ID), terminalId), this);
                }
            } else {
                // 请求失败
                Toast.makeText(MainActivity.this, "请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
            if (addTerminalResponse.isSuccess()) {
                // 创建完成，开启猎鹰服务
                terminalId = addTerminalResponse.getTid();
                aMapTrackClient.addTrack(new AddTrackRequest(Long.parseLong(Constants.SERVICE_ID), terminalId), this);
            } else {
                // 请求失败
                Toast.makeText(MainActivity.this, "请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDistanceCallback(DistanceResponse distanceResponse) {
            if (distanceResponse.isSuccess()) {
                actualDistance = distanceResponse.getDistance();
                // 行驶里程查询成功，行驶了meters米
                updateEnded();
            } else {
                // 行驶里程查询失败
                CommonUtil.showMessage(MainActivity.this, "实际里程查询失败");
                Log.e(TAG,"实际里程查询失败"+distanceResponse.getErrorMsg());
            }
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
                Log.e("trackId", trackId + "");
                TrackParam trackParam = new TrackParam(Long.parseLong(Constants.SERVICE_ID), terminalId);
                trackParam.setTrackId(trackId);
                aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
                updateStarted();
            } else {
                Toast.makeText(MainActivity.this, "网络请求失败，" + addTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

        }
    };
    RouteSearch.OnRouteSearchListener onRouteSearchListener = new RouteSearch.OnRouteSearchListener() {
        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
            if (i == 1000) {
                plannedDistance = driveRouteResult.getPaths().get(0).getTollDistance();
                Log.e(TAG,"计算规划路程出错");
            } else {
                CommonUtil.showMessage(MainActivity.this, "计算规划路程出错");
                Log.e(TAG,"计算规划路程出错"+i);
            }
        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

        }
    };

    private void polling(long routeId) {
        // 步骤1：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVICE_ROOT) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build();

        // 步骤2：创建 网络请求接口 的实例
        IGetRequest request = retrofit.create(IGetRequest.class);

        // 步骤3：采用Observable<...>形式 对 网络请求 进行封装
        String token = SharePreferenceUtil.getString(MainActivity.this, "token", "");
        Observable<PollingResult> observable = request.getRoute(token, routeId);
        // 步骤4：发送网络请求 & 通过repeatWhen（）进行轮询
        observable.repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            // 在Function函数中，必须对输入的 Observable<Object>进行处理，此处使用flatMap操作符接收上游的数据
            public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
                // 将原始 Observable 停止发送事件的标识（Complete（） /  Error（））转换成1个 Object 类型数据传递给1个新被观察者（Observable）
                // 以此决定是否重新订阅 & 发送原来的 Observable，即轮询
                // 此处有2种情况：
                // 1. 若返回1个Complete（） /  Error（）事件，则不重新订阅 & 发送原来的 Observable，即轮询结束
                // 2. 若返回其余事件，则重新订阅 & 发送原来的 Observable，即继续轮询
                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Object throwable) throws Exception {

                        // 加入判断条件：当申请状态发生改变后结束
                        try {
                            if (status != 0) {
                                // 此处选择发送onError事件以结束轮询，因为可触发下游观察者的onError（）方法回调
                                if (status == 1) {
                                    handler.sendEmptyMessage(1);
                                } else if (status == -100) {
                                    return Observable.just(1).delay(2000, TimeUnit.MILLISECONDS);
                                } else {
                                    handler.sendEmptyMessage(2);
                                }
                                return Observable.error(new Throwable("轮询结束"));
                            }
                            // 还未审核，则继续轮询
                            // 注：此处加入了delay操作符，作用 = 延迟一段时间发送（此处设置 = 2s），以实现轮询间间隔设置
                            return Observable.just(1).delay(2000, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                            return Observable.error(new Throwable("轮询出错"));
                        }
                    }
                });

            }
        }).subscribeOn(Schedulers.io())               // 切换到IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 切换回到主线程 处理请求结果
                .subscribe(new Observer<PollingResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(PollingResult result) {
                        // e.接收服务器返回的数据
                        recordResult = result;
                        Route route = result.getData().getRoute();
                        status = route.getStatus();
                        Log.e(TAG, "请求次数+1");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 获取轮询结束信息
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                changeToStart();
            } else if (msg.what == 2) {
                changeToCancle();
            } else if (msg.what == 3) {
                changeToStarted();
            } else if (msg.what == 4) {
                changeToCanceled();
            } else if (msg.what == 5) {
                changeToEnded();
            }

        }
    };

    private void changeToStart() {
        start.setClickable(true);
        start.setText("开始行程");
        secRouteId = recordResult.getData().getSecRoutesModel().get(0).getSecRoute().getId();
    }

    private void changeToCancle() {
        start.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        destination.setVisibility(View.VISIBLE);
        CommonUtil.showMessage(MainActivity.this, "申请已被拒绝");
    }

    private void changeToStarted() {
        start.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.VISIBLE);
        locate();
    }

    private void changeToCanceled() {
        TrackParam trackParam = new TrackParam(Long.parseLong(Constants.SERVICE_ID), terminalId);
        trackParam.setTrackId(trackId);
        aMapTrackClient.stopTrack(trackParam, onTrackLifecycleListener);
    }

    private void changeToEnded() {
        finish.setText("行程已结束");
        finish.setClickable(false);
        TrackParam trackParam = new TrackParam(Long.parseLong(Constants.SERVICE_ID), terminalId);
        trackParam.setTrackId(trackId);
        aMapTrackClient.stopTrack(trackParam, onTrackLifecycleListener);
        mLocationClient.stopLocation();
    }
}
