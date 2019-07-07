package com.example.northuniversity.schoolteam.modules.Team.Inside_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.SignUpActivity;
import com.example.northuniversity.schoolteam.utils.HttpUtils;
import com.example.northuniversity.schoolteam.utils.SaveUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.support.constraint.Constraints.TAG;

public class ConcreteActivity extends AppCompatActivity {

    private Toolbar toolbar = null;
    private TextView descriptionTv = null;
    private TextView menberQuantityTv = null;
    private TextView timeTv = null;
    private TextView locationTv = null;
    private TextView fareTv = null;
    private Button joinBtn = null;
    private String team_id;
    private String result;
    private String my_id;
    private String status;
    private String team_picture;
    private ImageView team_pictureImageView = null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete);

        my_id = SaveUtils.getSettingNote(ConcreteActivity.this,"userInfo","id");
        Log.d(TAG, "onCreate: my_id"+my_id);
        descriptionTv = findViewById(R.id.description_team);
        menberQuantityTv = findViewById(R.id.menber_quantity);
        timeTv = findViewById(R.id.time_team);
        locationTv = findViewById(R.id.location_team);
        fareTv = findViewById(R.id.fare_team);
        joinBtn = findViewById(R.id.join_team);
        team_pictureImageView = findViewById(R.id.first_show_team);

        Intent intent = getIntent();
        descriptionTv.setText(intent.getStringExtra("title"));
        menberQuantityTv.setText(intent.getStringExtra("menber_quantity"));
        timeTv.setText(intent.getStringExtra("time"));
        locationTv.setText(intent.getStringExtra("location"));
        fareTv.setText(intent.getStringExtra("fare"));
        team_id = intent.getStringExtra("team_id");
        team_picture = intent.getStringExtra("team_picture");

        team_pictureImageView.setImageBitmap(BitmapFactory.decodeFile(team_picture));
        toolbar = findViewById(R.id.concrete_toolbar);
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
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_team(team_id,my_id);
            }
        });
    }

    public void join_team(final String team_id, final String my_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.137.1:8000/join_team/";
                String params = "team_id=" + team_id+"&"+"my_id="+my_id;
                Log.d(TAG, "run: team_id = " + team_id);
                result = HttpUtils.sendPostRequest(url, params);
                try {
                    JSONObject jsonObject= new JSONObject(result);
                    JSONObject classjson = jsonObject.getJSONObject("stata");//获取JSON对象中的JSON
                    status = classjson.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 13;
                Bundle bundle = new Bundle();
                bundle.putString("status", status);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(final Message message) {
            status = message.getData().getString("status");
            if(message.what ==13){

                if (status.equals("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConcreteActivity.this);
                    builder.setTitle("恭喜");
                    builder.setMessage("加入成功");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConcreteActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("你已经加入改队伍");
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