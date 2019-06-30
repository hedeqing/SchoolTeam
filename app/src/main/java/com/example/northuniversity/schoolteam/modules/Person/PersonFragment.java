package com.example.northuniversity.schoolteam.modules.Person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.northuniversity.schoolteam.LoginActivity;
import com.example.northuniversity.schoolteam.MainActivity;
import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.base.BaseFragment;
import com.example.northuniversity.schoolteam.utils.HttpUtils;
import com.example.northuniversity.schoolteam.utils.SaveUtils;
import com.leon.lib.settingview.LSettingItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class PersonFragment extends BaseFragment {

    private String username;
    private String number;
    private String  avator;
    private String gender;
    private String dynamic;
    private String password;
    private TextView name;

    private String result;
    private String status;
    private CardView cardView = null;


    private  LSettingItem lSettingItem= null;

    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    TextView textView;
    private String old_number;

    private TextView dynamicTv = null;
    private  TextView numberTv = null;
    private  TextView genderTv = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.fragment_person, container, false);
            initView();
            isPrepared = true;
//        实现懒加载
            lazyLoad();
        }
        //缓存的mView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }




        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        number= SaveUtils.getSettingNote(getContext(), "userInfo", "number");
        username= SaveUtils.getSettingNote(getContext(), "userInfo", "username");
        dynamic= SaveUtils.getSettingNote(getContext(), "userInfo", "dynamic");
        gender= SaveUtils.getSettingNote(getContext(), "userInfo", "gender");
        avator= SaveUtils.getSettingNote(getContext(), "userInfo", "avator");
        password= SaveUtils.getSettingNote(getContext(), "userInfo", "password");
        Log.d(TAG, "onActivityCreated: number"+number);
        Log.d(TAG, "onActivityCreated: username"+username);
        Log.d(TAG, "onActivityCreated: dynamic"+dynamic);
        Log.d(TAG, "onActivityCreated: gender"+gender);
        Log.d(TAG, "onActivityCreated: avator"+avator);
        Log.d(TAG, "onActivityCreated: password"+password);

        name =getActivity().findViewById(R.id.person_name);
        dynamicTv = getActivity().findViewById(R.id.dynamic_person);
        genderTv = getActivity().findViewById(R.id.gender_person);

        name.setText(username);
        dynamicTv.setText(dynamic);
        genderTv.setText(gender);
        cardView = getActivity().findViewById(R.id.person_cardview);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                View view = inflater.inflate(R.layout.dialog_view, null);
                builder.setView(view);
                AlertDialog dialog=builder.create();
                final EditText oldPassEt  = view.findViewById(R.id.old_password);
                final EditText newPassEt  = view.findViewById(R.id.new_password);
                final EditText genderEt  = view.findViewById(R.id.change_gender);
                final EditText dynamicEt  = view.findViewById(R.id.change_dynamic);
                final EditText nameEt = view.findViewById(R.id.change_name);
                final  EditText numberEt = view.findViewById(R.id.change_number);
                Button button = view.findViewById(R.id.confirm);
//                oldPassEt.setText(password);
//                newPassEt.setText(password);
                genderEt.setText(gender);
                dynamicEt.setText(dynamic);
                nameEt.setText(username);
                numberEt.setText(number);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldpassword = oldPassEt.getText().toString();
                        String newPassword = newPassEt.getText().toString();
                        String new_gender = genderEt.getText().toString();
                        String new_dynamic =dynamicEt.getText().toString();
                        String new_name = nameEt.getText().toString();
                        String new_number = numberEt.getText().toString();
                        update_message(oldpassword,newPassword,new_gender,new_dynamic,new_name,new_number,number);
                    }
                });
                dialog.show();
            }
        });



    }

    private void update_message(final String oldpassword, final String newPassword, final String new_gender, final String new_dynamic, final String new_name, final String new_number,final String old_number ) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "http://10.0.2.2:8000/update/";
                    String params = "oldPassword="+oldpassword+"&"+"newPassword="+newPassword+"&"+"new_gender="+new_gender+"&"+"new_dynamic="+new_dynamic+"&"+"new_name="+new_name+"&"+"old_number="+old_number+"&"+"new_number="+new_number;
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
                    message.what = 9;
                    Bundle bundle = new Bundle();
                    bundle.putString("status",status);
                    bundle.putString("password",newPassword);
                    bundle.putString("number",new_number);
                    bundle.putString("username",new_name);
                    bundle.putString("dynamic",new_dynamic);
                    bundle.putString("gender",new_gender);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    Log.d(TAG, "run: status "+status);
                }
            }).start();
    }
    Handler handler = new Handler() {
        public void handleMessage(final Message msg) {
            status = msg.getData().getString("status");

            if (msg.what == 9) {
                if (status.equals("1")) {
                    Map<String, String> map = new HashMap<String, String>(); //本地保存数据
                    map.put("number",msg.getData().getString("number"));
                    map.put("username",msg.getData().getString("username"));
                    map.put("dynamic",msg.getData().getString("dynamic"));
                    map.put("gender",msg.getData().getString("gender"));
                    map.put("password",msg.getData().getString("password"));
                    SaveUtils.saveSettingNote(getContext(), "userInfo",map);
                } else {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
                    builder.setTitle("警告" ) ;
                    builder.setMessage("信息错误" ) ;
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


    /**
     * 初始化控件
     */
    private void initView() {

    }
    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        //填充各控件的数据
        mHasLoadedOnce = true;
    }
    public static PersonFragment newInstance(String param1) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
}
