package com.example.northuniversity.schoolteam.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.northuniversity.schoolteam.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestDataActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private Map<String,String> map = null;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data);
        button = (Button)findViewById(R.id.butPost);
        button.setOnClickListener(this);
        textView = (TextView)findViewById(R.id.lblPostResult);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.butPost:
                connectServer();
                break;
            default:
                break;
        }
    }


    private void connectServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int c;
                    String param = "账号=何德庆&密码=240484406";
                    StringBuilder string = new StringBuilder();
                    URL url = new URL("http://10.0.2.2:8000/get_data/");
                    //打开和url的连接
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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
                    while ((line = reader.readLine())!=null){
                        string.append(line);
                    }

                    Log.d("反馈：",string.toString());
                    showResquesPonse(string.toString());

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }).start();

    }
    private void showResquesPonse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(response);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}