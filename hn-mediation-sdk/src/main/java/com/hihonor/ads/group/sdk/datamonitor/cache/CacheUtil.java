package com.hihonor.ads.group.sdk.datamonitor.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.hihonor.ads.group.sdk.util.log.HiAdsLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;


public class CacheUtil {
    private static final String TAG = CacheUtil.class.getSimpleName();
    private SharedPreferences sPreference;
    private static final String FILE_NAME = "loadinstall";

    public CacheUtil(Context context) {
        this(context, FILE_NAME);
    }

    public CacheUtil(Context context, String fileName) {
        sPreference = context.getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public boolean containsKey(String key) {
        if (sPreference == null) {
            return false;
        }
        return sPreference.contains(key);
    }

    public String getString(String key, String defVal) {
        if (sPreference == null) {
            return defVal;
        }
        return sPreference.getString(key, defVal);
    }

    public void putString(String key, String val) {
        if (sPreference == null) {
            return;
        }
        SharedPreferences.Editor editor = sPreference.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public void remove(String key) {
        if (sPreference == null) {
            return;
        }
        SharedPreferences.Editor editor = sPreference.edit();
        editor.remove(key);
        editor.commit();
    }

    public int getInt(String key, int defVal) {
        if (sPreference == null) {
            return defVal;
        }
        return sPreference.getInt(key, defVal);
    }

    public void putInt(String key, int val) {
        if (sPreference == null) {
            return;
        }
        SharedPreferences.Editor editor = sPreference.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defVal) {
        if (sPreference == null) {
            return defVal;
        }
        return sPreference.getBoolean(key, defVal);
    }

    public void putBoolean(String key, boolean val) {
        if (sPreference == null) {
            return;
        }
        SharedPreferences.Editor editor = sPreference.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public long getLong(String key, long defVal) {
        if (sPreference == null) {
            return defVal;
        }
        return sPreference.getLong(key, defVal);
    }

    public void putLong(String key, long val) {
        if (sPreference == null) {
            return;
        }
        SharedPreferences.Editor editor = sPreference.edit();
        editor.putLong(key, val);
        editor.commit();
    }

    public <T> boolean putObject(String key, T obj) {
        if (sPreference == null) {
            return false;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {   //Device为自定义类
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(obj);
            // 将字节流编码成base64的字符串
            String oAuth_Base64 = new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
            sPreference.edit().putString(key, oAuth_Base64).commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将对象从shareprerence中取出来
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getObject(String key) {
        if (sPreference == null) {
            return null;
        }
        T device = null;
        String productBase64 = sPreference.getString(key, null);

        if (productBase64 == null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);

            // 读取对象
            device = (T) bis.readObject();

        } catch (Exception e) {
            HiAdsLog.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return device;
    }
}
