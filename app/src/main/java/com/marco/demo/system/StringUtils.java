package com.marco.demo.system;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringUtils {
    public StringUtils() {
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return str != null && str.trim().length() > 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEquals(String actual, String expected) {
        if (actual != expected) {
            if (actual == null) {
                if (expected != null) {
                    return false;
                }
            } else if (!actual.equals(expected)) {
                return false;
            }
        }

        return true;
    }

    public static String nullStrToEmpty(String str) {
        return str == null ? "" : str;
    }

    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        } else {
            char c = str.charAt(0);
            return Character.isLetter(c) && !Character.isUpperCase(c) ? (new StringBuilder(str.length())).append(Character.toUpperCase(c)).append(str.substring(1)).toString() : str;
        }
    }

    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException var2) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", var2);
            }
        } else {
            return str;
        }
    }

    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException var3) {
                return defultReturn;
            }
        } else {
            return str;
        }
    }

    public static boolean isNumber(String num) {
        for (int i = 0; i < num.length(); ++i) {
            if (!Character.isDigit(num.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static int stringToInt(String str) {
        if (isEmpty(str)) {
            return 0;
        } else {
            int ret = 0;

            try {
                ret = Integer.parseInt(str);
            } catch (Exception var3) {
                ;
            }

            return ret;
        }
    }

    public static long stringToLong(String str) {
        if (isEmpty(str)) {
            return 0L;
        } else {
            long dest = 0L;

            try {
                dest = Long.parseLong(str);
            } catch (Exception var4) {
                ;
            }

            return dest;
        }
    }

    public static float stringToFloat(String str) {
        if (isEmpty(str)) {
            return 0.0F;
        } else {
            float dest = 0.0F;

            try {
                dest = Float.parseFloat(str);
            } catch (Exception var3) {
                ;
            }

            return dest;
        }
    }
}
