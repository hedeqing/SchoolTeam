package com.example.northuniversity.schoolteam;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.northuniversity.schoolteam.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar  = null;

    private TextView sign_upBtn = null;

    private EditText nameEt = null;
    private RadioButton genderRb = null;
    private  EditText passwordEt = null;
    private RadioGroup genderRg  = null;
    private  EditText numberEt = null;

    private  String name ;
    private  String  password;
    private  String  gender ;
    private  String number;
    private  String result;


    private String status ;

    private  String  TAG = "Sign_upActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = findViewById(R.id.toolbar_sign_up);
        sign_upBtn = findViewById(R.id.main_btn_sign_up);
        nameEt = findViewById(R.id.sign_up_name_et);
        numberEt = findViewById(R.id.sign_up_number_et);
        passwordEt = findViewById(R.id.sign_up_password_et);
        genderRg = findViewById(R.id.sign_up_radiogroup);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用mToolbar.setNavigationIcion(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

            genderRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int count = genderRg.getChildCount();
                for(int i = 0 ;i < count;i++){
                    RadioButton rb = (RadioButton)genderRg.getChildAt(i);
                    gender = rb.getText().toString();
                    }
                }
        });

        sign_upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name =   nameEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();
                number = numberEt.getText().toString().trim();
                Log.d(TAG, "onClick: gender" +gender);
                Log.d(TAG, "onClick: number" +number);
                Log.d(TAG, "onClick: password" +password);
                Log.d(TAG, "onClick: name" +name);

                sign_up(name,password,number,gender);
            }
        });
    }

    private void sign_up(final String name, final String password, final String number, final String gender) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.137.1:8000/sign_up/";
                String params = "name="+name+"&"+"password="+password+"&"+"number="+number+"&"+"gender="+gender;
                Log.d(TAG, "run: gender = "+gender);
                result  = HttpUtils.sendPostRequest(url,params);

                JSONObject jsonObject2 = null;
                try {
                    jsonObject2 = new JSONObject(result);
                    JSONObject classjson = jsonObject2.getJSONObject("stata");//获取JSON对象中的JSON
                    status = classjson.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("status",status);
                message.setData(bundle);
                handler.sendMessage(message);
                Log.d(TAG, "run: status "+status);
            }
        }).start();


    }
    Handler handler = new Handler(){
        public  void handleMessage(final Message message) {
            if (message.what == 2) {
                status = message.getData().getString("status");
                if (status.equals("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("恭喜");
                    builder.setMessage("注册成功");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("用户已注册");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }


            }
        }
    };
}

