package org.chenzc.communi.utils;

import cn.hutool.core.io.IoUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * 传附件时 需要使用的方法 目前仅有邮件相关
 *
 * @author chenz
 * @date 2024/06/01
 */
@Slf4j
public class FileUtils {
    private FileUtils() {

    }

    /**
     * 根据url返回file对象
     *
     * @param path      文件路径
     * @param remoteUrl 文件链接
     * @return {@link File }
     */
    public static File getRemoteUrl2File(String path, String remoteUrl) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(remoteUrl);
            File file = new File(path, url.getPath());
            inputStream = url.openStream();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                fileOutputStream = new FileOutputStream(file);
                IoUtil.copy(inputStream, fileOutputStream);
            }
            return file;
        } catch (Exception e) {
            log.error("FileUtils#getRemoteUrl2File fail:{},remoteUrl:{}", Throwables.getStackTraceAsString(e), remoteUrl);
        } finally {
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("close#inputStream fail:{}", Throwables.getStackTraceAsString(e));
                }
            }
            if (Objects.nonNull(fileOutputStream)) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("close#fileOutputStream fail:{}", Throwables.getStackTraceAsString(e));
                }
            }
        }
        return null;
    }


    /**
     * 根据链接集合 获取文件对象集合
     * @param path
     * @param remoteUrls
     * @return {@link List }<{@link File }>
     */
    public static List<File> getRemoteUrl2File(String path, Set<String> remoteUrls) {
        List<File> files = new ArrayList<>();
        remoteUrls.forEach(remoteUrl -> {
            File file = getRemoteUrl2File(path, remoteUrl);
            if (file != null) {
                files.add(file);
            }
        });
        return files;
    }
}
