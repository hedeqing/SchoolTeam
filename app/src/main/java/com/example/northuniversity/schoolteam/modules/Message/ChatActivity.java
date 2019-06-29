package com.example.northuniversity.schoolteam.modules.Message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatActivity extends AppCompatActivity {

    private  static Logger logger = (Logger) Logger.getLogger(String.valueOf(WebSocketClient.class));
    private  static  String TAG = "MainActivity";
    private  String status;
    private Button sendBtn = null;
    private EditText textEt = null;
    private  String text ;
    private TextView showTv = null;

    private WebSocketClient client = null;

    private ChatAdapter chatAdapter;
    /**
     * 声明ListView
     */
    private ListView lv_chat_dialog;
    /**
     * 集合
     */
    private List<PersonChat> personChats = new ArrayList<PersonChat>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    /**
                     * ListView条目控制在最后一行
                     */
                    lv_chat_dialog.setSelection(personChats.size());
                    break;

                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent =new Intent();
        sendBtn = findViewById(R.id.btn_chat_message_send);

        for (int i = 0; i <= 3; i++) {
            PersonChat personChat = new PersonChat();
            personChat.setMeSend(false);
            personChats.add(personChat);
        }
        lv_chat_dialog = (ListView) findViewById(R.id.lv_chat_dialog);
        Button btn_chat_message_send = (Button) findViewById(R.id.btn_chat_message_send);
        final EditText et_chat_message = (EditText) findViewById(R.id.et_chat_message);
        /**
         *setAdapter
         */
        chatAdapter = new ChatAdapter(this, personChats);
        lv_chat_dialog.setAdapter(chatAdapter);
        /**
         * 发送按钮的点击事件
         */
//        btn_chat_message_send.setOnClickListener(new View.OnClickListener() {
//
//
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                if (TextUtils.isEmpty(et_chat_message.getText().toString())) {
//                    Toast.makeText(ChatActivity.this, "发送内容不能为空", 0).show();
//                    return;
//                }
//                PersonChat personChat = new PersonChat();
//                //代表自己发送
//                personChat.setMeSend(true);
//                //得到发送内容
//                personChat.setChatMessage(et_chat_message.getText().toString());
//                //加入集合
//                personChats.add(personChat);
//                //清空输入框
//                et_chat_message.setText("");
//                //刷新ListView
//                chatAdapter.notifyDataSetChanged();
//                handler.sendEmptyMessage(1);
//            }
//        });
//    }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = textEt.getText().toString().trim();
                final Gson gson = new Gson();
                com.example.northuniversity.schoolteam.Bean.Message message = new com.example.northuniversity.schoolteam.Bean.Message();
                message.setMessage(text);
                String json = gson.toJson(message);
                try {
                    client = new WebSocketClient(new URI("ws://10.0.2.2:8000/ws/chat/lobby/"), new Draft_6455()) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            logger.info("握手成功");
                        }

                        @Override
                        public void onMessage(String message) {
                            System.out.println("received message: " + message);
                            try {
                                message.getBytes(message);
                                System.out.println(message);
                            } catch (UnsupportedEncodingException e) {
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
                client.send(json);
                textEt.setText("");
            }
        });
    }
}
