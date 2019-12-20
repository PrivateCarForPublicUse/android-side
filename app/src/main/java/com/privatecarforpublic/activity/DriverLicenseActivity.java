package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.MainActivity;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverLicenseActivity extends Activity {
    private static final String TAG = "DriverLicenseActivity";
    private static final int REQUEST_CODE_DRIVING_LICENSE = 103;

    @BindView(R.id.driver_license_button)
    ImageView driver_license_button;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private String name = "";
    private String idNumber = "";
    private String expiryDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_driver_license);

        ButterKnife.bind(this);
        title.setText("驾驶认证");
        side.setText("完成");
        // 初始化验证
        initAccessTokenWithAkSk();
        //状态栏颜色设置
        StatusBarUtil.setColor(DriverLicenseActivity.this, 25);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @OnClick(R.id.side)
    void next() {
        MyApplication.addDestroyActivity(this,TAG);
        Intent intent = new Intent(DriverLicenseActivity.this, MainActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("idNumber", idNumber);
        intent.putExtra("expiryDate", expiryDate);
        startActivity(intent);
        MyApplication.destroyAll();
    }

    //驾驶证（手动）
    @OnClick(R.id.driver_license_button)
    void takeDriverLicense() {
        Intent intent = new Intent(DriverLicenseActivity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_DRIVING_LICENSE);
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance(DriverLicenseActivity.this).initAccessTokenWithAkSk(
                new OnResultListener<AccessToken>() {
                    @Override
                    public void onResult(AccessToken result) {
                        // 本地自动识别需要初始化
                        //initLicense();

                        Log.d(TAG, "onResult: " + result.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "初始化认证成功");
                            }
                        });
                    }

                    @Override
                    public void onError(OCRError error) {
                        error.printStackTrace();
                        Log.e(TAG, "onError: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "初始化认证失败,请检查 key");
                            }
                        });
                    }
                }, getApplicationContext(),
                // 需要自己配置 https://console.bce.baidu.com
                Constants.OCR_API_KEY,
                // 需要自己配置 https://console.bce.baidu.com
                Constants.OCR_SECRET_KEY);
    }

    private void initLicense() {
        CameraNativeHelper.init(this, OCR.getInstance(DriverLicenseActivity.this).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        final String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "本地质量控制初始化错误，错误原因：" + msg);
                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DRIVING_LICENSE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    recDrivingCard(filePath);
                    try {
                        File file = new File(filePath);
                        FileInputStream stream = new FileInputStream(file);
                        Bitmap bm = BitmapFactory.decodeStream(stream);
                        driver_license_button.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "识别失败");
                                Toast.makeText(DriverLicenseActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }

    }

    /**
     * 解析驾驶证
     *
     * @param filePath 图片路径
     */
    private void recDrivingCard(String filePath) {
        // 驾驶证识别参数设置
        OcrRequestParams param = new OcrRequestParams();
        // 设置image参数
        param.setImageFile(new File(filePath));
        // 设置其他参数
        param.putParam("detect_direction", true);
        // 调用驾驶证识别服务
        OCR.getInstance(DriverLicenseActivity.this).recognizeDrivingLicense(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                Log.d(TAG, result.getJsonRes());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                Toast.makeText(DriverLicenseActivity.this, "识别出错,请查看log错误代码", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: " + error.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        CameraNativeHelper.release();
        // 释放内存资源
        OCR.getInstance(DriverLicenseActivity.this).release();
        super.onDestroy();
    }
}