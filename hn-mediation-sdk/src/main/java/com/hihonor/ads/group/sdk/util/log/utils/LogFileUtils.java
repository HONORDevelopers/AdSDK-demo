package com.hihonor.ads.group.sdk.util.log.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;


import com.hihonor.ads.group.sdk.HnGroupAds;
import com.hihonor.framework.common.IoUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 日志文件工具类
 *
 * @since 2022-10-18
 */
public class LogFileUtils {
    public static final String TAG = "LogFileUtils";
    private static final String FILE_START_NAME = "HnAdsLog";
    /**
     * 单个文件不超过4MB
     */
    private static final int MAX_BYTES = 4 * 1024 * 1024;

    /**
     * 最大文件数不超过50
     */
    private static final int MAX_FILE_COUNTS = 50;

    /**
     * 获取日志路径
     *
     * @return 日志路径
     */
    private static String getLogFolder() {
        String diskFolderPath;
        try {
            if (HnGroupAds.get().getContext() == null) {
                Log.i(TAG, "get disk log folder path fail,but context is null");
                return null;
            }
            diskFolderPath = HnGroupAds.get().getContext().getExternalCacheDir().getCanonicalPath();
        } catch (Exception e) {
            Log.e(TAG, "get disk log folder path exception");
            return null;
        }
        // 文件夹位置
        return diskFolderPath + File.separatorChar + "logs";
    }

    /**
     * 将日志写入本地文件
     *
     * @param contents 日志内容
     * @return 返回true表示写入成功
     */
    public static boolean writeLogToFile(String... contents) {
        FileWriter fileWriter = null;
        String folder = getLogFolder();
        if (folder == null || TextUtils.isEmpty(folder)) {
            Log.w(TAG, "Folder path is empty.");
            return false;
        }
        try {
            File logFile = getLogFile(folder);
            if (logFile == null) {
                Log.w(TAG, "Get log file is null, unable write log to disk.");
                return false;
            }
            fileWriter = new FileWriter(logFile, true);
            for (String content : contents) {
                writeLog(fileWriter, content);
            }
            fileWriter.flush();
            return true;
        } catch (Exception e) {
            Log.w(TAG, "writer log to disk fail " + e.getMessage());
            return false;
        } finally {
            IoUtils.closeSecure(fileWriter);
        }
    }

    /**
     * 该方法总是在异步线程中被调用，通过FileWriter将日志写入流中。
     *
     * @param fileWriter 文件输出流
     */
    private static void writeLog(@NonNull FileWriter fileWriter, @NonNull String content) throws IOException {
        fileWriter.append(content);
    }

    /**
     * 获取日志文件
     * 文件名遵循 HnAdsLog_日期_数量编号
     * 例如 HnAdsLog_19000110_0、HnAdsLog_19000110_1.
     *
     * @param folderName 文件夹名称
     * @return 返回配置好的{@link File}对象
     */
    private static File getLogFile(@NonNull String folderName) {
        File folder = new File(folderName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String formatTime = simpleDateFormat.format(new Date());
        int fileCount = 0;
        File newFile = new File(folder, String.format("%s_%s_%s.txt", FILE_START_NAME, formatTime, fileCount));
        // 如果folder不存在则直接说明该folder下没有file，直接新建folder返回一个新的file。
        if (!folder.exists()) {
            Log.w(TAG, "getLogFile#Log folder is not found!");
            return folder.mkdirs() ?
                    newFile :
                    null;
        }
        // 文件夹存在 遍历文件夹
        File[] listFiles = folder.listFiles();

        if (listFiles != null && listFiles.length > 0) {
            // 排序最近编辑的的在最前面，最早编辑的在最后
            Arrays.sort(listFiles, (file1, file2) -> {
                long diff = file1.lastModified() - file2.lastModified();
                if (diff > 0)
                    return -1; // 这里-1 else 为 1 是递减排序， 这里1 else -1 是递增排序
                else if (diff == 0)
                    return 0;
                else
                    return 1;
            });
            removeOldFile(listFiles);
            while (newFile.exists()) {
                if (newFile.length() < MAX_BYTES) {
                    Log.i(TAG, "New file writable.");
                    break;
                }
                fileCount++;
                newFile = new File(folder, String.format("%s_%s_%s.txt", FILE_START_NAME, formatTime, fileCount));
            }
        }
        return newFile;
    }

    private static void removeOldFile(@NonNull File[] listFiles) {
        int folderCounts = listFiles.length;
        File firstModifiedFile = listFiles[folderCounts - 1];
        // 不为空检查文件夹最大数量是否大于等于50个
        if (folderCounts >= MAX_FILE_COUNTS) {
            // 如果大于等于50个，则删除最早的。
            Log.i(TAG, "delete first create file. current logs folder file counts is " + folderCounts);
            if (firstModifiedFile == null || !firstModifiedFile.delete()) {
                Log.w(TAG, "delete first create file fail.");
            }
        }
    }
}
