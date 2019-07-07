package com.example.northuniversity.schoolteam.modules.Message;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.northuniversity.schoolteam.Bean.PersonChat;
import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.modules.Message.adapter.ChatAdapter;
import com.example.northuniversity.schoolteam.utils.HttpUtils;
import com.example.northuniversity.schoolteam.utils.SaveUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.Inflater;

public class ChatActivity extends AppCompatActivity {

    private  static Logger logger = (Logger) Logger.getLogger(String.valueOf(WebSocketClient.class));
    private  static  String TAG = "ChatActivity";
    private Button sendBtn = null;
    private  String text ;
    private String room ;



    private String result;

    private WebSocketClient client = null;

    private  List<String>  contents = new ArrayList<>();
    private  List<String> rooms = new ArrayList<>();
    private  List<String> fromNames = new ArrayList<>();

    private ChatAdapter chatAdapter;
    private  EditText et_chat_message;

    private ListView lv_chat_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        sendBtn = findViewById(R.id.btn_chat_message_send);
        lv_chat_dialog = (ListView) findViewById(R.id.lv_chat_dialog);
        et_chat_message = (EditText) findViewById(R.id.et_chat_message);

        room = getIntent().getStringExtra("room");

        Log.d(TAG, "onCreate: room"+room);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text = et_chat_message.getText().toString().trim();
                final Gson gson = new Gson();
                com.example.northuniversity.schoolteam.Bean.Message message = new com.example.northuniversity.schoolteam.Bean.Message();
                message.setMessage(text);
                message.setFromName(SaveUtils.getSettingNote(ChatActivity.this,"userInfo","username"));
                message.setRoom(room);
                final String json = gson.toJson(message);
//                try {
//                    client = new WebSocketClient(new URI("ws://192.168.137.1:8000/ws/chat/lobby/"), new Draft_6455()) {
//                        @Override
//                        public void onOpen(ServerHandshake handshakedata) {
//                            logger.info("握手成功");
//                        }
//                        @Override
//                        public void onMessage(final String message) {
//
//                            final List<String>  contents = new ArrayList<>();
//                            final List<String> rooms = new ArrayList<>();
//                            final List<String> fromNames = new ArrayList<>();
//                            System.out.println(message);
//                            try {
//                                final JSONObject jsonObject = new JSONObject(message);
//                                Log.d(TAG, "onMessage: 信息:"+jsonObject.getString("message"));
//                                Log.d(TAG, "onMessage: 来自于"+jsonObject.getString("fromName"));
//                                Log.d(TAG, "onMessage: 房间号"+jsonObject.getString("room"));
//
//                                contents.add(jsonObject.getString("message"));
//                                fromNames.add(jsonObject.getString("fromName"));
//                                rooms.add(jsonObject.getString("room"));
//                                Log.d(TAG, "onMessage: data"+contents.size());
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        chatAdapter.setData(contents,fromNames,rooms);
//                                        lv_chat_dialog.setAdapter(chatAdapter);
//                                        chatAdapter.notifyDataSetInvalidated();
//                                        lv_chat_dialog.smoothScrollToPosition(contents.size()-1);
//                                    }
//                                });
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            if (message.equals("over")) {
//                                client.close();
//                            }
//                        }
//                        @Override
//                        public void onClose(int code, String reason, boolean remote) {
//                            logger.info("链接已关闭");
//                        }
//
//                        @Override
//                        public void onError(Exception ex) {
//                            logger.info("发送错误已关闭");
//                        }
//                    };
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }
//                client.connect();
//                logger.info(String.valueOf(client.getDraft()));
//                while (!client.getReadyState().equals(WebSocketClient.READYSTATE.OPEN)) {
//                    logger.info("正在连接");
//                }
                client.send(json);
//                getMessage();
                et_chat_message.setText("");
            }
        });
    }
    public void getMessage(final String room){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.137.1:8000/get_message/";
                String params = "room="+room;
                result = HttpUtils.sendPostRequest(url, params);
                Message message = new Message();
                message.what = 10;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();
    }
    Handler handler = new Handler() {
        public void handleMessage(final Message message) {
            List<String> messages = new ArrayList<>();
            List<String> room = new ArrayList<>();
            List<String> fromName = new ArrayList<>();
            if (message.what == 10) {
                result = message.getData().getString("result");
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("fields");
                        messages.add(jsonObject1.getString("content"));
                        room.add(jsonObject1.getString("room"));
                        fromName.add(jsonObject1.getString("fromName"));
                    }
                    contents = messages;
                    fromNames = fromName;
                    rooms = room;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };
    @Override
    protected void onStart() {
        super.onStart();
        room = getIntent().getStringExtra("room");
        getMessage(room);
        chatAdapter = new ChatAdapter(getApplicationContext(),contents,fromNames,rooms);
        chatAdapter.setData(contents,fromNames,rooms);
        lv_chat_dialog.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        try {
            client = new WebSocketClient(new URI("ws://192.168.137.1:8000/ws/chat/"+room+"/"), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    logger.info("握手成功");
                }
                @Override
                public void onMessage(final String message) {

                    final List<String>  contents = new ArrayList<>();
                    final List<String> rooms = new ArrayList<>();
                    final List<String> fromNames = new ArrayList<>();
                    System.out.println(message);
                    try {
                        final JSONObject jsonObject = new JSONObject(message);
                        Log.d(TAG, "onMessage: 信息:"+jsonObject.getString("message"));
                        Log.d(TAG, "onMessage: 来自于"+jsonObject.getString("fromName"));
                        Log.d(TAG, "onMessage: 房间号"+jsonObject.getString("room"));

                        contents.add(jsonObject.getString("message"));
                        fromNames.add(jsonObject.getString("fromName"));
                        rooms.add(jsonObject.getString("room"));
                        Log.d(TAG, "onMessage: data"+contents.size());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatAdapter.setData(contents,fromNames,rooms);
                                lv_chat_dialog.setAdapter(chatAdapter);
                                chatAdapter.notifyDataSetInvalidated();
                                lv_chat_dialog.smoothScrollToPosition(chatAdapter.getCount() - 1);//移动到尾部
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (message.equals("over")) {
                        client.close();
                    }
                }
                @Override
                public void onClose(int code, String reason, boolean remote) {
                    logger.info("链接已关闭");
                }

                @Override
                public void onError(Exception ex) {
                    logger.info("发送错误已关闭");
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.connect();
        logger.info(String.valueOf(client.getDraft()));
        while (!client.getReadyState().equals(WebSocketClient.READYSTATE.OPEN)) {
            logger.info("正在连接");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
