package top.wuhaojie.se.utils;

public class Log {

    private static final String DEFAULT_TAG = "StringExtractor";

    private Log() {
    }

    public static void d(Object... args) {
        d(DEFAULT_TAG, args);
    }

    public static void d(String tag, Object... args) {
        StringBuilder builder = new StringBuilder();
        builder.append('[').append(tag).append(']').append('\t');
        for (Object arg : args) {
            builder.append(arg).append(", ");
        }
        System.out.println(builder.toString());
    }

    public static void e(Object... args) {
        e(DEFAULT_TAG, new RuntimeException(), args);
    }

    public static void e(String tag, Throwable throwable, Object... args) {
        d(tag, args);
        throwable.printStackTrace();
    }

    public static void e(String tag, Object... args) {
        e(tag, new RuntimeException(), args);
    }

}
