package com.example.northuniversity.schoolteam;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Thread.sleep;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mBtnLogin = null;
    private TextView sign_tb = null;
    private String number = null;
    private String password = null;

    private String status = null;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mNumber, mPsw;

    private EditText numberEt = null;

    private EditText passwordEt = null;

    private String TAG = "LoginACtivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mNumber = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        sign_tb = findViewById(R.id.sign_up);
        numberEt = findViewById(R.id.login_nuumber);
        passwordEt = findViewById(R.id.login_password);
        mBtnLogin.setOnClickListener(this);
        sign_tb.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_btn_login:
                number = numberEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();
                // 计算出控件的高与宽
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();

                // 隐藏输入框
//                mNumber.setVisibility(View.INVISIBLE);
//                mPsw.setVisibility(View.INVISIBLE);
//                inputAnimator(mInputLayout, mWidth, mHeight);
                Judge_login(number,password);
                //发送广播，number唯一判断
//                Intent intent1 = new Intent();
//                intent1.setAction("android.intent.action.ACTION_POWER_CONNECTED");
//                intent1.putExtra("number", number);
//                sendBroadcast(intent1);
                //接收广播
//                Intent intent = new Intent();
//                String action = intent.getAction();
//                String data = intent.getStringExtra("key");
//                System.out.println("接受到了广播,action:"+ action +",data:"+data);
                break;
            case R.id.sign_up:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }


    }

    /**
     * 输入框的动画效果
     *
     * @param view 控件
     * @param w    宽
     * @param h    高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }

    public void Judge_login(final String number, final String password) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int c;
                    String param = "号码=" + number + "&" + "密码=" + password;
                    Log.d(TAG, "run: " + "号码=" + number + "&" + "密码=" + password);
//                        String param = "号码=何德庆&密码=240484406";
                    StringBuilder string = new StringBuilder();
                    URL url = new URL("http://10.0.2.2:8000/get_data/");
                    //打开和url的连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    //POST请求必须设置这两个属性
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    //获取HttpURLConnection对象的输出流
                    PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
                    printWriter.print(param);
                    //flush输出流的缓冲
                    printWriter.flush();
                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        string.append(line);
                    }
                     JSONObject jsonObject2 = new JSONObject(string.toString());
                        JSONObject classjson = jsonObject2.getJSONObject("stata");//获取JSON对象中的JSON
                        status = classjson.getString("status");
                        Log.d("status：", classjson.getString("status"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message =  new Message();
                message.what =1;
                Bundle bundle = new Bundle();
                bundle.putString("status",status);
                message.setData(bundle);
                handler.sendMessage(message);
            }

        }).start();
    }
    Handler handler = new Handler() {
        public void handleMessage(final Message msg) {
            if (msg.what == 1) {
                status = msg.getData().getString("status");
                Log.d(TAG, "handleMessage status: "+status);
                if (status.equals("1")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("警告" ) ;
                    builder.setMessage("信息错误" ) ;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            numberEt.setText("");
                            passwordEt.setText("");
                        }
                    });
                    builder.show();
                }
                }

            }
        };

//        private void showResquesPonse(final String response){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject jsonObject2 = null;//创建JSON对象
//                    try {
//                        jsonObject2 = new JSONObject(response);
//                        JSONObject classjson = jsonObject2.getJSONObject("stata");//获取JSON对象中的JSON
//                        status = classjson.getString("status");
//                        Log.d("status：", classjson.getString("status"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
