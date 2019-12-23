package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.EmployeeAdapter;
import com.privatecarforpublic.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class ReviewEmployeeActivity extends Activity {
    private static final String TAG = "ReviewEmployeeActivity";
    public final static int TO_REVIEW_EMPLOYEE = 104;

    @BindView(R.id.employee_list)
    ListView employee_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private List<User> employeeList;
    private EmployeeAdapter employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_review);
        ButterKnife.bind(this);
        title.setText("员工审核");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(ReviewEmployeeActivity.this, 25);
    }

    private void init() {
        employeeList = new ArrayList<>();
        employeeList.add(new User());
        employeeList.add(new User());
        employeeList.add(new User());
        employeeAdapter=new EmployeeAdapter(this,employeeList);
        employee_list.setAdapter(employeeAdapter);
    }

    @OnItemClick(R.id.employee_list)
    public void onPrivateItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(ReviewEmployeeActivity.this,EmployeeDetailActivity.class);
        intent.putExtra("car",employeeList.get(i));
        startActivityForResult(intent,TO_REVIEW_EMPLOYEE);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_REVIEW_EMPLOYEE && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();   //finish应该写到这个地方
        }
    }
}
