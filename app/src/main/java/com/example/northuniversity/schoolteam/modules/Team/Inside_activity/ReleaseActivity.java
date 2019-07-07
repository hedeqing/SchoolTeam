package com.example.northuniversity.schoolteam.modules.Team.Inside_activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.northuniversity.schoolteam.MainActivity;
import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.utils.HttpUtils;
import com.example.northuniversity.schoolteam.utils.SaveUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import static org.litepal.LitePalApplication.getContext;

public class ReleaseActivity extends AppCompatActivity  {
    private Toolbar toolbar = null;
    private Button sendBtn = null;
    private EditText descriptEt = null;
    private Button addImage = null;
    private EditText startTimeEt = null;
    private EditText endTimeEt = null;
    private EditText locationEt = null;
    private EditText fareDescriptionEt = null;
    private RadioGroup sendRg = null;
    private ImageView show_image = null;

    private String description;
    private String startTime;
    private String endTime;
    private String location;
    private String fareDescription;
    private String result;
    private String status;
    private String category;
    private String team_id_id;
    private String TAG = "ReleaseActivity";
    public static final int CHOOSE_PHOTO = 2;
    private String imgaPath;



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        team_id_id = SaveUtils.getSettingNote(getContext(), "userInfo", "number");
        Log.d(TAG, "onCreate: releaseActivity"+team_id_id);

        toolbar = findViewById(R.id.release_toolbar);
        sendBtn = findViewById(R.id.send_team);
        descriptEt = findViewById(R.id.descriptEt);
        addImage = findViewById(R.id.add_image);
        startTimeEt = findViewById(R.id.start_time_send);
        endTimeEt = findViewById(R.id.end_time_Et);
        locationEt = findViewById(R.id.location_send);
        fareDescriptionEt = findViewById(R.id.fare_description_Et);
        sendRg = findViewById(R.id.send_rg);
        show_image= findViewById(R.id.show_image);
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
                releaseTeam(team_id_id,description, startTime, endTime, location, fareDescription,category,imgaPath);
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
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ReleaseActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ReleaseActivity.this, new
                            String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

    }

    private void releaseTeam(final String team_id_id, final String description, final String startTime, final String endTime, final String location, final String fareDescription, final String category,final String imagePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.137.1:8000/release_team/";
                String params = "team_id="+team_id_id+"&"+"description=" + description + "&" + "startTime=" + startTime + "&" + "endTime=" + endTime + "&" + "location=" + location +"&"+ "fareDescription=" + fareDescription +"&"+"category="+category+"&"+"image_path="+imagePath;
                result = HttpUtils.sendPostRequest(url, params);

                JSONObject jsonObject2 = null;
                try {
                    jsonObject2 = new JSONObject(result);
                    Log.d(TAG, "run1111: result "+result);
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
            if (message.what == 8) {
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
                    builder.setMessage("发布失败");
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

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    Toast.makeText(this, "You denide the permission", Toast.LENGTH_SHORT).show();
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
        if(DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                Toast.makeText(this, "success01", Toast.LENGTH_SHORT).show();
                String id = docId.split(":")[1];
                String secletion = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, secletion);
            } else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Toast.makeText(this, "success02", Toast.LENGTH_SHORT).show();
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if("content".equalsIgnoreCase(uri.getScheme())) {
            Toast.makeText(this, "success03", Toast.LENGTH_SHORT).show();
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，则直接获取文件路径
            Toast.makeText(this, "success04", Toast.LENGTH_SHORT).show();
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
        Cursor cursor = getContentResolver().query(uri, null, seclection, null, null);
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
            imgaPath = imagePath;
            Log.d(TAG, "displayImage: imagepth"+imagePath);
            show_image.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}
