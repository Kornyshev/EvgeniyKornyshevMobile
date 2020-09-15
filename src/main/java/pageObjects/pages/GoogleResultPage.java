package pageObjects.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class GoogleResultPage {

    private AppiumDriver driver;

    @FindBy(xpath = "//*[contains(text(), 'EPAM')]")
    private List<WebElement> searchResults;

    public GoogleResultPage(AppiumDriver appiumDriver) {
        PageFactory.initElements(appiumDriver, this);
        this.driver = appiumDriver;
    }

    public List<WebElement> getResultsSearchBlock() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[text()='More results']")));
        return searchResults;
    }
}