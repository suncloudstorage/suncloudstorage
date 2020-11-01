package com.suncloudstorage.util;

import static com.suncloudstorage.constant.StringConstants.DOT;
import static com.suncloudstorage.constant.StringConstants.SLASH_SEPARATOR;

public class FileUtils {

    public static String getExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(DOT));
    }

    public static String getFileNameWithoutExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(DOT) + 1);
    }

    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf(SLASH_SEPARATOR) + 1);
    }

}
