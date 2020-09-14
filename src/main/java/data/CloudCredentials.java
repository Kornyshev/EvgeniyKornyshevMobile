package data;

import java.util.ResourceBundle;

public class CloudCredentials {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("credentials.cloud");

    public static String getUrlWithToken() {
        return bundle.getString("url")
                .replaceAll("token", bundle.getString("token"));
    }

    public static String getAndroidDeviceID() {
        return bundle.getString("android.udid");
    }

    public static String getIOsDeviceID() {
        return bundle.getString("ios.udid");
    }
}