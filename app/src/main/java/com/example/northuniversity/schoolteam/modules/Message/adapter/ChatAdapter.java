package com.example.northuniversity.schoolteam.modules.Message.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.northuniversity.schoolteam.Bean.PersonChat;
import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.utils.SaveUtils;

import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class ChatAdapter  extends BaseAdapter {
    private Context context;
    private  List<String> contents;
    private  List<String> fromName;
    private  List<String> rooms;

    public ChatAdapter(Context context, List<String> contents, List<String> fromNames, List<String> rooms) {
        this.context = context;
        this.contents = contents;
        this.fromName = fromNames;
        this.rooms = rooms;
    }

//    public ChatAdapter(Context context, List<PersonChat> lists) {
//        super();
//        this.context = context;
//        this.lists = lists;
//    }

    /**
     * 是否是自己发送的消息
     *
     * @author cyf
     *
     */
    public  interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }
    public void setData(List<String> contents, List<String> fromNames, List<String> rooms) {
        if (null != contents) {
//            this.contents.clear();
            this.contents.addAll(contents);
//            this.rooms.clear();
            this.rooms.addAll(rooms);
//            this.fromName.clear();
            this.fromName.addAll(fromNames);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        return lists.size();
        Log.d(TAG, "getCount: size" +fromName.size());
        return  rooms.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
//        return lists.get(arg0);
        Log.d(TAG, "getItem: "+fromName.get(arg0));
        return  rooms.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    /**
     * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
     */
    public int getItemViewType(int position) {
//        PersonChat entity = lists.get(position);
        String name = fromName.get(position);
        Log.d(TAG, "getItemViewType: name receive"+name);
        if (name=="何德庆") {// 收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        } else {// 自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        HolderView holderView = null;

        String name = SaveUtils.getSettingNote(context,"userInfo","username");
        boolean isMeSend;
        Log.d(TAG, "getViewfromName: "+fromName.get(position));
        isMeSend = (String.valueOf(fromName.get(position)).equals(name));
        Log.d(TAG, "getView: isMesend"+isMeSend);
        if (holderView == null) {
            holderView = new HolderView();
            if (isMeSend) {
                arg1 = View.inflate(context, R.layout.activity_chat_right,
                        null);
                holderView.tv_chat_me_message = (TextView) arg1
                        .findViewById(R.id.tv_chat_me_message_right);
                holderView.tv_vaht_name = arg1.findViewById(R.id.chat_name_right);
                holderView.tv_chat_me_message.setText(contents.get(position));
                holderView.tv_vaht_name.setText(fromName.get(position));
                Log.d(TAG, "getView:  do it right");
            } else {
                arg1 = View.inflate(context, R.layout.activity_chat_left,
                        null);
                holderView.tv_chat_me_message = (TextView) arg1
                        .findViewById(R.id.tv_chat_me_message_left);
                holderView.tv_vaht_name = arg1.findViewById(R.id.chat_name_left);
                holderView.tv_chat_me_message.setText(contents.get(position));
                holderView.tv_vaht_name.setText(fromName.get(position));
                Log.d(TAG, "getView:  do it left");
            }
            arg1.setTag(holderView);
        } else {
            holderView = (HolderView) arg1.getTag();
        }
        return arg1;
    }

    class HolderView {
        TextView tv_chat_me_message;
        TextView tv_vaht_name;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
