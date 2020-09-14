package setup;

import data.CloudCredentials;
import io.appium.java_client.AppiumDriver;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import pageObjects.ElementSupplier;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BaseTest implements IDriver {

    public static final int IMPLICITLY_WAIT_TIMEOUT = 10;
    private static AppiumDriver appiumDriver;
    private String deviceUdid = "";
    private String appType = "";

    /*
    That collection used for definition those activities which we want to use
    during our test. And we should give that collection to the method responsible
    for WebElement supplying - ElementSupplier.getElement. In my architecture each
    activity class plays ElementSupplier role, it provides WebElement to test scenario.
     */
    protected List<ElementSupplier> activitiesForTest;

    @Override
    public AppiumDriver getDriver() {
        return appiumDriver;
    }

    public ElementSupplier getPO(String activity) {
        return activitiesForTest.stream()
                .filter(supplier -> supplier.getClass().getSimpleName().equals(activity))
                .findFirst().get();
    }

    /*
    That method responsible for activities initialization in each test scenario.
    I don't know how put that logic into BeforeMethod cause' in each test scenario we have
    different particular set of activities and it is difficult to implement that in common
    BeforeMethod.
     */
    public void setActivitiesForTest(ElementSupplier...activities) {
        activitiesForTest = Arrays.asList(activities.clone());
    }

    @Parameters({"appType","platformName","browserName","bundleId","appPackage","appActivity","app"})
    @BeforeSuite(alwaysRun = true)
    public void setUp(
            String appType,
            String platformName,
            @Optional("") String browserName,
            @Optional("") String bundleId,
            @Optional("") String appPackage,
            @Optional("") String appActivity,
            @Optional("") String app)
            throws Exception {
        setAppiumDriver(platformName, browserName, bundleId, appPackage, appActivity, app);
        this.appType = appType;
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        appiumDriver.closeApp();
    }

    /*
    That method solves my problem with interaction with real device which is trying to
    auto-fill Email test field with my data. I need to focus on main activity XML
    instead of that auto suggestion message.
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        if (isNativeAppAndNotEmulator()) {
            getDriver().navigate().back();
        }
    }

    private boolean isNativeAppAndNotEmulator() {
        return this.appType.equals("native") && this.deviceUdid.contains("J9AXB761S242FKH");
    }

    private void setAppiumDriver(
            String platformName,
            String browserName,
            String bundleId,
            String appPackage,
            String appActivity,
            String app) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        //Platform name capability
        capabilities.setCapability("platformName", platformName);

        //UDID capability from property file
        if (platformName.equals("Android")) {
            this.deviceUdid = CloudCredentials.getAndroidDeviceID();
        } else if (platformName.equals("iOS")) {
            this.deviceUdid = CloudCredentials.getIOsDeviceID();
        } else {
            throw new Exception("Platform name is incorrect!!!");
        }
        capabilities.setCapability("udid", deviceUdid);

        //Capability about browser
        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("chromedriverDisableBuildCheck","true");

        //Capabilities for Android app on Cloud device
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);

        //Capability for iOS app identification
        capabilities.setCapability("bundleId", bundleId);

        //Capability for local app with particular path
        if (app.endsWith(".apk")) {
            capabilities.setCapability("app", new File(app).getAbsolutePath());
        }

        //Creating driver instance with implicitly wait setting
        try {
            appiumDriver = new AppiumDriver(new URL(CloudCredentials.getUrlWithToken()), capabilities);
            appiumDriver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_TIMEOUT, TimeUnit.SECONDS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String captureScreenshot() throws IOException {
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String destFilePath = "src/main/resources/screenshots/screen_"
                + getDriver().getSessionId().toString() + ".png";
        FileUtils.copyFile(srcFile, new File(destFilePath));
        return destFilePath;
    }

    /*
    Tesseract API for Java
     */
    public String screenText(String filePath) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("eng");
        return tesseract.doOCR(new File(filePath));
    }

    public void waitForPageLoad() {
        new WebDriverWait(getDriver(), 10).until(
                wd -> ((JavascriptExecutor) wd)
                        .executeScript("return document.readyState").equals("complete")
        );
    }
}