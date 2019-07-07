package com.example.northuniversity.schoolteam.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Iterator;
import java.util.Map;

public class SaveUtils {
    /**
     * 将字符串数据保存到本地
     * @param context 上下文
     * @param filename 生成XML的文件名
     * @param map map<生成XML中每条数据名,需要保存的数据>
     */
    public static void saveSettingNote(Context context, String filename , Map<String, String> map) {
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            note.putString(entry.getKey(), entry.getValue());
        }
        note.commit();
    }

    /**
     * 从本地取出要保存的数据
     * @param context 上下文
     * @param filename 文件名
     * @param dataname 生成XML中每条数据名
     * @return 对应的数据(找不到为NUll)
     */
    public static String getSettingNote(Context context,String filename ,String dataname) {
        SharedPreferences read = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return read.getString(dataname, null);
    }
}
