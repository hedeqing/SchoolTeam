package com.example.northuniversity.schoolteam;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.alibaba.fastjson.JSON;
import com.example.northuniversity.schoolteam.modules.Team.Inside_activity.ReleaseActivity;
import com.example.northuniversity.schoolteam.utils.HttpUtils;
import com.example.northuniversity.schoolteam.utils.SaveUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;
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
    private String result;
    private String my_username;
    private  String  my_dynamic;
    private  String my_avator;
    private String my_gender;
    private  String my_number;
    private  String my_password;
    private String my_id;


    private String TAG = "LoginACtivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
                    String url = "http://192.168.137.1:8000/get_data/";

                    result = HttpUtils.sendPostRequest(url, param);
//                    String param = "号码=何德庆&密码=240484406";
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    my_id = jsonObject.getString("pk");
                    Log.d(TAG, "run: my_id"+my_id);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("fields");
                    my_username = jsonObject1.getString("username");
                    my_avator = jsonObject1.getString("picture");
                    my_dynamic = jsonObject1.getString("dynamic");


                    my_gender = jsonObject1.getString("gender");
                    my_number = jsonObject1.getString("number");
                    my_password = jsonObject1.getString("password");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message =  new Message();
                message.what =1;
                Bundle bundle = new Bundle();
                bundle.putString("my_username",my_username);
                bundle.putString("my_avator",my_avator);
                bundle.putString("my_dynamic",my_dynamic);
                bundle.putString("my_gender",my_gender);
                bundle.putString("my_number",my_number);
                bundle.putString("my_password",my_number);
                bundle.putString("my_id",my_id);
                message.setData(bundle);
                handler.sendMessage(message);
            }

        }).start();
    }
    Handler handler = new Handler() {
        public void handleMessage(final Message msg) {
            if (msg.what == 1) {
                my_number= msg.getData().getString("my_number");
                my_username= msg.getData().getString("my_username");
                my_dynamic= msg.getData().getString("my_dynamic");
                my_gender= msg.getData().getString("my_gender");
                my_avator= msg.getData().getString("my_avator");
                my_password= msg.getData().getString("my_password");
                my_id = msg.getData().getString("my_id");
                if (my_number!=null) {
                    Map<String, String> map = new HashMap<String, String>(); //本地保存数据
                    map.put("id",my_id);
                    map.put("number",number);
                    map.put("username",my_username);
                    map.put("dynamic",my_dynamic);
                    map.put("gender",my_gender);
                    map.put("avator",my_avator);
                    map.put("password",my_password);
                    SaveUtils.saveSettingNote(LoginActivity.this, "userInfo",map);
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

    /**
     * 将字符串数据保存到本地
     * @param context 上下文
     * @param filename 生成XML的文件名
     * @param  map <生成XML中每条数据名,需要保存的数据>
     */
    public static void saveSettingNote(Context context,String filename ,Map<String, String> map) {
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            note.putString(entry.getKey(), entry.getValue());
        }
        note.commit();
    }
}
