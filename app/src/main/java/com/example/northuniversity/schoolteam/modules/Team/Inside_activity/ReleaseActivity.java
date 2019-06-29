package com.example.northuniversity.schoolteam.modules.Team.Inside_activity;

import android.content.DialogInterface;
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

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.SignUpActivity;
import com.example.northuniversity.schoolteam.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ReleaseActivity extends AppCompatActivity {
    private Toolbar toolbar = null;
    private Button sendBtn = null;
    private EditText descriptEt = null;
    private Button addImage = null;
    private EditText startTimeEt = null;
    private EditText endTimeEt = null;
    private EditText locationEt = null;
    private EditText fareDescriptionEt = null;
    private RadioGroup sendRg = null;

    private String description;
    private String startTime;
    private String endTime;
    private String location;
    private String fareDescription;
    private String result;
    private String status;
    private String category;
    private String TAG = "ReleaseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        toolbar = findViewById(R.id.release_toolbar);
        sendBtn = findViewById(R.id.send_team);
        descriptEt = findViewById(R.id.descriptEt);
        addImage = findViewById(R.id.add_image);
        startTimeEt = findViewById(R.id.start_time_send);
        endTimeEt = findViewById(R.id.end_time_Et);
        locationEt = findViewById(R.id.location_send);
        fareDescriptionEt = findViewById(R.id.fare_description_Et);
        sendRg = findViewById(R.id.send_rg);

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
                finish();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = descriptEt.getText().toString();
                startTime = startTimeEt.getText().toString();
                endTime = startTimeEt.getText().toString();
                location = locationEt.getText().toString();
                fareDescription = fareDescriptionEt.getText().toString();
                releaseTeam(description, startTime, endTime, location, fareDescription,category);
            }
        });
        sendRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int count = sendRg.getChildCount();
                for(int i = 0 ;i < count;i++){
                    RadioButton rb = (RadioButton)sendRg.getChildAt(i);
                    category = rb.getText().toString();
                }
            }
        });

    }

    private void releaseTeam(final String description, final String startTime, final String endTime, final String location, final String fareDescription, final String category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://10.0.2.2:8000/release_team/";
                String params = "description=" + description + "&" + "startTime=" + startTime + "&" + "endTime=" + endTime + "&" + "location=" + location + "fareDescription=" + fareDescription+"category="+category;
                result = HttpUtils.sendPostRequest(url, params);

                JSONObject jsonObject2 = null;
                try {
                    jsonObject2 = new JSONObject(result);
                    JSONObject classjson = jsonObject2.getJSONObject("stata");//获取JSON对象中的JSON
                    status = classjson.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                message.what = 8;
                Bundle bundle = new Bundle();
                bundle.putString("status", status);
                message.setData(bundle);
                handler.sendMessage(message);
                Log.d(TAG, "run: status " + status);
            }
        }).start();


    }

    Handler handler = new Handler() {
        public void handleMessage(final Message message) {
            if (message.what == 2) {
                status = message.getData().getString("status");
                if (status.equals("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseActivity.this);
                    builder.setTitle("恭喜");
                    builder.setMessage("发布成功");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("注册失败");
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
