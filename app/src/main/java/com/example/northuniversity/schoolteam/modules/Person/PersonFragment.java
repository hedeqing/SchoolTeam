package com.example.northuniversity.schoolteam.modules.Person;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.northuniversity.schoolteam.LoginActivity;
import com.example.northuniversity.schoolteam.MainActivity;
import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.SignUpActivity;
import com.example.northuniversity.schoolteam.base.BaseFragment;
import com.example.northuniversity.schoolteam.modules.Team.Inside_activity.ReleaseActivity;
import com.example.northuniversity.schoolteam.utils.HttpUtils;
import com.example.northuniversity.schoolteam.utils.SaveUtils;
import com.leon.lib.settingview.LSettingItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;
import static com.example.northuniversity.schoolteam.modules.Team.Inside_activity.ReleaseActivity.CHOOSE_PHOTO;

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
    private ImageView avatorPicture  = null;
    private String myimagePath ;
    public  final static  int CHOOSE_PHOTO = 2;

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
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new
                    String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
        }

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
        numberTv = getActivity().findViewById(R.id.number_person);
        avatorPicture = getActivity().findViewById(R.id.profile_image);

        avatorPicture.setImageBitmap(BitmapFactory.decodeFile(avator));
        name.setText(username);
        dynamicTv.setText(dynamic);
        genderTv.setText(gender);
        numberTv.setText(number);
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
        avatorPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new
                            String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();

                }
            }
        });
    }

    private void update_message(final String oldpassword, final String newPassword, final String new_gender, final String new_dynamic, final String new_name, final String new_number,final String old_number ) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "http://192.168.137.1:8000/update/";
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


            if (msg.what == 9) {
                status = msg.getData().getString("status");
                if (status.equals("1")) {
                    Map<String, String> map = new HashMap<String, String>(); //本地保存数据
                    map.put("number",msg.getData().getString("number"));
                    map.put("username",msg.getData().getString("username"));
                    map.put("dynamic",msg.getData().getString("dynamic"));
                    map.put("gender",msg.getData().getString("gender"));
                    map.put("password",msg.getData().getString("password"));
                    SaveUtils.saveSettingNote(getContext(), "userInfo",map);
                    AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
                    builder.setTitle("恭喜" ) ;
                    builder.setMessage("修改成功" ) ;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
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
            if (msg.what == 14){
                status = msg.getData().getString("statu");
                Log.d(TAG, "handleMessage: 11111111"+status);
                if (status.equals("2")) {
                    Map<String, String> map = new HashMap<String, String>(); //本地保存数据
                    map.put("avator",myimagePath);
                    SaveUtils.saveSettingNote(getContext(),"userInfo",map);
                    avatorPicture.setImageBitmap(BitmapFactory.decodeFile(myimagePath));
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("恭喜");
                    builder.setMessage("修改头像成功");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("警告");
                    builder.setMessage("修改头像失败");
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
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
//            case TAKE_PHOTO:
//                if( resultCode == RESULT_OK ){
//                    try{
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        picture.setImageBitmap(bitmap);
//                    } catch (FileNotFoundException e){
//                        e.printStackTrace();
//                    }
//                }
//                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    //判断版本型号
                    if(Build.VERSION.SDK_INT >= 19) {
                        //4.4以上版本
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "You denide the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                Toast.makeText(getActivity(), "success01", Toast.LENGTH_SHORT).show();
                String id = docId.split(":")[1];
                String secletion = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, secletion);
            } else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Toast.makeText(getActivity(), "success02", Toast.LENGTH_SHORT).show();
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if("content".equalsIgnoreCase(uri.getScheme())) {
            Toast.makeText(getActivity(), "success03", Toast.LENGTH_SHORT).show();
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，则直接获取文件路径
            Toast.makeText(getActivity(), "success04", Toast.LENGTH_SHORT).show();
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri, String seclection) {
        String path = null;
        //通过Uri和secletion来获取真实的图片路径
        Cursor cursor = getContext().getContentResolver().query(uri, null, seclection, null, null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath) {
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            myimagePath = imagePath;
            avatorPicture.setImageBitmap(bitmap);
            change_avator(myimagePath,number);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    public  void change_avator(final String avator, final String number){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.137.1:8000/update_avator/";
                String params = "avator="+avator+"&"+"number="+number;
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
                message.what = 14;
                Bundle bundle = new Bundle();
                bundle.putString("statu",status);
                message.setData(bundle);
                handler.sendMessage(message);
                Log.d(TAG, "run: statu "+status);
            }
        }).start();
    }
}

