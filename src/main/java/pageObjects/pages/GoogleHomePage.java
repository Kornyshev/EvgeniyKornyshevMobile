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

    @FindBy(css = "[name='q']")
    WebElement searchField;

    public GoogleHomePage(AppiumDriver appiumDriver) {
        PageFactory.initElements(appiumDriver, this);
        this.driver = appiumDriver;
    }

    public GoogleHomePage typeSearchQuery(String query) {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("[name='q']")));
        searchField.sendKeys(query);
        return this;
    }

    public GoogleResultPage sendSearchQuery() {
        searchField.sendKeys(Keys.ENTER);
        return new GoogleResultPage(driver);
    }
}