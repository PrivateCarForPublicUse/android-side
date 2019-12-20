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
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.jaeger.library.StatusBarUtil;
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

public class IdCardActivity extends Activity {
    private static final String TAG = "IdCardActivity";
    private static final int REQUEST_CODE_CAMERA = 102;

    @BindView(R.id.id_card_front_button_auto)
    ImageView id_card_front_button_auto;
    @BindView(R.id.id_card_back_button_auto)
    ImageView id_card_back_button_auto;
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
        setContentView(R.layout.register_id_card);

        ButterKnife.bind(this);
        title.setText("身份认证");
        side.setText("下一步");
        // 初始化验证
        initAccessTokenWithAkSk();
        //状态栏颜色设置
        StatusBarUtil.setColor(IdCardActivity.this, 25);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @OnClick(R.id.side)
    void next() {
        MyApplication.addDestroyActivity(this,TAG);
        Intent intent = new Intent(IdCardActivity.this, DriverLicenseActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("idNumber", idNumber);
        intent.putExtra("expiryDate", expiryDate);
        startActivity(intent);
    }

    //身份证正面（自动）
    @OnClick(R.id.id_card_front_button_auto)
    void takeFrontIDCard() {
        Intent intent = new Intent(IdCardActivity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    //身份证反面（自动）
    @OnClick(R.id.id_card_back_button_auto)
    void takeBackIDCard() {
        Intent intent = new Intent(IdCardActivity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance(IdCardActivity.this).initAccessTokenWithAkSk(
                new OnResultListener<AccessToken>() {
                    @Override
                    public void onResult(AccessToken result) {
                        // 本地自动识别需要初始化
                        initLicense();

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
        CameraNativeHelper.init(this, OCR.getInstance(IdCardActivity.this).getLicense(),
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

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                        try {
                            File file = new File(filePath);
                            FileInputStream stream = new FileInputStream(file);
                            Bitmap bm = BitmapFactory.decodeStream(stream);
                            id_card_front_button_auto.setImageBitmap(bm);
                        } catch (FileNotFoundException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "识别失败");
                                    Toast.makeText(IdCardActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                        try {
                            File file = new File(filePath);
                            FileInputStream stream = new FileInputStream(file);
                            Bitmap bm = BitmapFactory.decodeStream(stream);
                            id_card_back_button_auto.setImageBitmap(bm);
                        } catch (FileNotFoundException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "识别失败");
                                    Toast.makeText(IdCardActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        }

    }

    /**
     * 解析身份证图片
     *
     * @param idCardSide 身份证正反面
     * @param filePath   图片路径
     */
    private void recIDCard(final String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(40);
        OCR.getInstance(IdCardActivity.this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    if (result.getName() != null) {
                        name = result.getName().toString();
                    }
                    if (result.getIdNumber() != null) {
                        idNumber = result.getIdNumber().toString();
                    }
                    if (result.getExpiryDate() != null) {
                        expiryDate = result.getExpiryDate().toString();
                    }
                    if (idCardSide.equals("front")) {
                        Toast.makeText(IdCardActivity.this, "姓名: " + name + "," +
                                "身份证号码: " + idNumber
                                 , Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "姓名: " + name + "," + "身份证号码: " + idNumber);
                    } else if (idCardSide.equals("back")) {
                        Log.d(TAG, "到期时间: " + expiryDate);
                        Toast.makeText(IdCardActivity.this, "到期时间: " + expiryDate, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(OCRError error) {
                Toast.makeText(IdCardActivity.this, "识别出错,请查看log错误代码", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: " + error.getMessage());
            }
        });
    }
}