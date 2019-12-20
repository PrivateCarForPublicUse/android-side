package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;
import com.privatecarforpublic.util.CommonUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class Register2Activity extends Activity {
    private static final String TAG = "Register2Activity";
    private List<String> cityList;
    private List<String> companyList;
    @BindView(R.id.city)
    Spinner city;
    @BindView(R.id.company)
    Spinner company;
    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user2);
        ButterKnife.bind(this);
        title.setText("用户注册");
        side.setVisibility(View.INVISIBLE);
        //状态栏颜色设置
        StatusBarUtil.setColor(Register2Activity.this, 25);
        getCityList();
    }

    private void getCityList(){
        String [] data = new String[]{"中国","捷克","日本","南京","重庆","成都"};
        cityList= Arrays.asList(data);
        /*String[] data = new String[cityList.size()];
        cityList.toArray(data);*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,data);
        city.setAdapter(adapter);
    }

    @OnItemSelected(R.id.city)
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        String cardNumber = cityList.get(arg2);
        CommonUtil.showMessage(Register2Activity.this,cardNumber);
        getCompanyList();
    }

    private void getCompanyList(){
    }

    @OnClick(R.id.next)
    void next() {
        MyApplication.addDestroyActivity(this,TAG);
        Intent intent = new Intent(Register2Activity.this, IdCardActivity.class);
        intent.putExtra("name", name.getText().toString());
        startActivity(intent);

    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }
}
