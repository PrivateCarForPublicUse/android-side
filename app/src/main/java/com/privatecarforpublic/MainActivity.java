package com.privatecarforpublic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.amap.api.services.help.Tip;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.activity.ReimbursementActivity;
import com.privatecarforpublic.activity.SearchPlaceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int TO_SEARCH_DESTINATION= 100;

    @BindView(R.id.function)
    ImageView function;
    @BindView(R.id.destination)
    TextView destination;
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
        Intent intent = new Intent(MainActivity.this, SearchPlaceActivity.class);
        startActivityForResult(intent, TO_SEARCH_DESTINATION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //状态栏颜色设置
        StatusBarUtil.setColor(MainActivity.this, 25);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        map.onCreate(savedInstanceState);
        navigationView.setNavigationItemSelectedListener(this);
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
        } else if (id == R.id.reply_reimbursement) {
            Intent intent = new Intent(MainActivity.this, ReimbursementActivity.class);
            startActivity(intent);
            //finish();

        } else if (id == R.id.my_cars) {

        } else if (id == R.id.my_travels) {

        } else if (id == R.id.remove) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_SEARCH_DESTINATION && resultCode == Activity.RESULT_OK) {
            Tip tip=(Tip)data.getParcelableExtra("tip");
            destination.setText(tip.getName());
        }

    }
}
