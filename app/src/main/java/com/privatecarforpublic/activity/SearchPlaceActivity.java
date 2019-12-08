package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.TipAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class SearchPlaceActivity extends Activity implements SearchView.OnQueryTextListener,
        Inputtips.InputtipsListener {
    public static final String DEFAULT_CITY = "宁波";
    public static final int REQUEST_SUC = 1000;

    private List<Tip> myTipList;
    private TipAdapter tipAdapter;

    @BindView(R.id.search_address)
    SearchView searchAddress;
    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.search_place);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        title.setText("搜索地点");
        side.setVisibility(View.INVISIBLE);
        //状态栏颜色设置
        StatusBarUtil.setColor(SearchPlaceActivity.this, 25);
        //初始化SearchView
        initSearchView();
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    private void initSearchView() {
        searchAddress.setOnQueryTextListener(this);
        //设置SearchView默认为展开显示
        searchAddress.setIconified(false);
        searchAddress.onActionViewExpanded();
        searchAddress.setIconifiedByDefault(false);
        searchAddress.setSubmitButtonEnabled(true);
    }

    @OnItemClick(R.id.listView)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (myTipList != null) {
            Tip tip = (Tip) adapterView.getItemAtPosition(i);
            Intent intent = new Intent();
            intent.putExtra("tip", tip);
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        }
    }
    /**
     * 输入提示回调
     */
    @Override
    public void onGetInputtips(List tipList, int rCode) {
        // 正确返回
        if (rCode == REQUEST_SUC) {
            myTipList = tipList;
            tipAdapter = new TipAdapter(getApplicationContext(), myTipList);
            listView.setAdapter(tipAdapter);
            tipAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "错误码 :" + rCode, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 按下确认键触发，本例为键盘回车或搜索键
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    /**
     * 输入字符变化时触发
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, DEFAULT_CITY);
            Inputtips inputTips = new Inputtips(SearchPlaceActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            // 如果输入为空  则清除 listView 数据
            if (tipAdapter != null && myTipList != null) {
                myTipList.clear();
                tipAdapter.notifyDataSetChanged();
            }
        }
        return true;
    }


}
