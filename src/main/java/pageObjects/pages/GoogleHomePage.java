package pageObjects.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GoogleHomePage {

    private final AppiumDriver driver;

    @FindBy(xpath = "//input[@name='q']")
    WebElement searchField;

    public GoogleHomePage(AppiumDriver appiumDriver) {
        PageFactory.initElements(appiumDriver, this);
        this.driver = appiumDriver;
    }

    public GoogleHomePage typeSearchQuery(String query) {
        searchField.sendKeys(query);
        return this;
    }

    public GoogleResultPage sendSearchQuery() {
        searchField.sendKeys(Keys.ENTER);
        return new GoogleResultPage(driver);
    }
}