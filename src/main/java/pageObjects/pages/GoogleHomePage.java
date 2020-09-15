package pageObjects.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        //searchField.sendKeys(Keys.ENTER); - и так не работает
        //driver.getKeyboard().pressKey(Keys.ENTER); - и так не работает
        return new GoogleResultPage(driver);
    }
}