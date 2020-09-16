package apiInteraction;

import data.CloudCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class AppInstallation {

    private static final String URL = "https://mobilecloud.epam.com/automation/api/storage/install/";

    public static String install(String platformName, String udid) throws IOException {
        String result;
        HttpPost post = new HttpPost(URL + udid);
        post.addHeader("Authorization", "Bearer " + CloudCredentials.getToken());

        String pathToApp;
        if (platformName.equals("Android")) {
            pathToApp = "src\\main\\resources\\EPAMTestApp.apk";
        } else {
            pathToApp = "src\\main\\resources\\EPAMTestApp.ipa";
        }
        File file = new File(pathToApp);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName());
        post.setEntity(builder.build());

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)){
            result = EntityUtils.toString(response.getEntity());
        }
        return result;
    }
}