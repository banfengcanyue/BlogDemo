package com.bfcy.blogdemo.encrypt;

import android.text.TextUtils;

/**
 * 通用的工具类型
 */
public class CommonUtil {
    /**
     * 打乱字符串的顺序：奇数和偶数分离，奇数在前偶数在后
     *
     * @param string 待处理的字符串
     * @return 打乱后的字符串
     */
    public static String upsetData(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        } else {
            char[] chars = string.toCharArray();
            StringBuilder sbOdd = new StringBuilder();
            StringBuilder sbEven = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                if ((i + 1) % 2 == 1) {
                    sbOdd.append(chars[i]);
                } else {
                    sbEven.append(chars[i]);
                }
            }
            sbOdd.append(sbEven.toString());
            return sbOdd.toString();
        }
    }

    /**
     * 还原被打乱的字符串顺序
     *
     * @param string 打乱后的字符串
     * @return 原字符串
     */
    public static String recoveryData(String string) {
        if (string == null || string.length() == 0) {
            return "";
        } else {
            if (string.length() == 1) {
                return string;
            }
            int len;
            if (string.length() % 2 == 1) {
                len = (string.length() - 1) / 2 + 1;

            } else {
                len = string.length() / 2;
            }
            String strOdd = string.substring(0, len);
            String strEven = string.substring(len);
            char[] charOdd = strOdd.toCharArray();
            char[] charEven = strEven.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < charOdd.length; i++) {
                sb.append(charOdd[i]);
                if (i == charOdd.length - 1 && charOdd.length != charEven.length) {
                    break;
                }
                sb.append(charEven[i]);
            }
            return sb.toString();
        }
    }
}
