package com.hihonor.adsdk.demo.external.splash;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.hihonor.adsdk.base.HnAds;
import com.hihonor.adsdk.base.log.HiAdsLog;

/**
 * 摇一摇监听管理
 * 触发条件：加速度15m/s  便宜角度 35°  时长3s
 * 逻辑：第一步：加速度达到15m/s时，记录手机的位置，并达成第一个条件（加速度），
 * 第二步：触发加速度15m/s，计算此时手机的位置，并计算和第一次的手机位置的偏移度，如果大于35°，则达成第二个条件，同时记录时间
 * 第三步：触发加速度15m/s，计算和第一次记录的时间的时长，大于3s，达成第三个条件
 * 第四步：触发摇一摇
 */
public class ShakeManager implements SensorEventListener {
    private static final String TAG = "ShakeUtils";

    private double shakeAcc = 15;

    private double shakeAngle = 35;

    private double shakeDuration = 3;

    private final SensorManager mSensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];


    // 加速度大于15m/s²
    private boolean moreThanTheAccelerationValue = false;

    // 偏移角度大于35°
    private boolean moreThanTheOffsetValue = false;

    private OnShakeListener onShakeListener;


    public interface OnShakeListener {
        void onShake();
    }

    public ShakeManager() {
        Context context = HnAds.get().getContext();
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        register();
    }

    public void setOnShakeListener(OnShakeListener onShakeListener) {
        this.onShakeListener = onShakeListener;
    }

    public void setShakeAngle(double shakeAngle) {
        this.shakeAngle = shakeAngle;
    }

    public void setShakeAcc(double shakeAcc) {
        this.shakeAcc = shakeAcc;
    }

    public void setShakeDuration(double shakeDuration) {
        this.shakeDuration = shakeDuration;
    }

    public void register() {
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            mSensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void unregister() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        // 加速度
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.length);
            //这里可以调节摇一摇的灵敏度
            if (Math.abs(values[0]) > shakeAcc || Math.abs(values[1]) > shakeAcc) {
                updateOrientationAngles();
                moreThanTheAccelerationValue = true;
                onSensorListener();
            }
        } else if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.length);
        }
    }

    private final float[] firstOrientationAngles = new float[3];

    private void updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);

        // 获取 手机位置 xyz
        float[] values = SensorManager.getOrientation(rotationMatrix, orientationAngles);
        // 记录第一次手机的位置
        if (firstOrientationAngles[0] == 0 && values[0] != 0) {
            System.arraycopy(values, 0, firstOrientationAngles, 0, firstOrientationAngles.length);
            return;
        }
        HiAdsLog.i(TAG, "OrientationAnglesX: " + values[0] + ", y: " + values[1] + ", z: " + values[2]);
        HiAdsLog.i(TAG, "firstOrientationAngles: " + firstOrientationAngles[0] + ", y: " + firstOrientationAngles[1] + ", z: " + firstOrientationAngles[2]);
        double shakeAngleValue = shakeAngle * Math.PI / 180;
        HiAdsLog.i(TAG, "OrientationAnglesX: " + shakeAngleValue);
        if (Math.abs(firstOrientationAngles[0] - values[0]) > shakeAngleValue
                || Math.abs(firstOrientationAngles[1] - values[1]) > shakeAngleValue
                || Math.abs(firstOrientationAngles[2] - values[2]) > shakeAngleValue) {
            HiAdsLog.i(TAG, "action shake");
            moreThanTheOffsetValue = true;
        }
    }

    private long firstShakeTime = -1;

    private void onSensorListener() {
        if (moreThanTheAccelerationValue && moreThanTheOffsetValue) {
            if (firstShakeTime == -1) {
                firstShakeTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - firstShakeTime >= shakeDuration * 1000) {
                onShakeListener.onShake();
                firstShakeTime = -1;
            }
        }
    }
}