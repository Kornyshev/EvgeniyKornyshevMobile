package data;

import java.util.ResourceBundle;

public class CloudCredentials {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("credentials.cloud");

    public static String getUrlWithToken() {
        return bundle.getString("url")
                .replaceAll("token", bundle.getString("token"));
    }

    public static String getDeviceUdid(String platformName) {
        if (platformName.equals("Android")) {
            return bundle.getString("android.udid");
        } else if (platformName.equals("iOS")) {
            return bundle.getString("ios.udid");
        } else {
            throw new RuntimeException("Platform name is incorrect!!!");
        }
    }

    public static String getToken() {
        return bundle.getString("token");
    }
}