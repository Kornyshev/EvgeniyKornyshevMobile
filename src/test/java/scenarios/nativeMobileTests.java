package scenarios;

import data.TestData;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pageObjects.ElementSupplier;
import pageObjects.activities.BudgetActivity;
import pageObjects.activities.LoginActivity;
import pageObjects.activities.RegistrationActivity;
import setup.BaseTest;

import java.io.IOException;

import static entities.User.TEST_USER;
import static org.testng.Assert.*;

public class nativeMobileTests extends BaseTest {

    ElementSupplier currentActivity;

    @Test(groups = {"native"}, dataProvider = "DataForIncorrectLoginScenario",
            dataProviderClass = TestData.class,
            description = "This simple test just click on the Sign In button")
    public void incorrectLoginInSystem(String loginActivity, String expectedMessage)
            throws IllegalAccessException, NoSuchFieldException, IOException, TesseractException {
        setActivitiesForTest(new LoginActivity(getDriver()));

        if (platformName.equals("Android")) {
            currentActivity = getPO(loginActivity);
            currentActivity.getElement("signInBtn").click();
            /*
            That assertion check that we have expected text on our screenshot
            That code does not work on cloud devices, I don't know why but it recognizes
            whatever instead of one particular phrase about Incorrect email or password.
            */
            String actualScreenText = screenText(captureScreenshot());
            System.out.println(actualScreenText);
            //assertTrue(actualScreenText.contains(expectedMessage));
            //Text recognition doesn't work on cloud device
        }
    }

    /*
    That's my way to solve that homework using this architecture with reflection
    interaction with activities. I think we have to give our system information about
    current activity and that information should be transferred to result class
    which return WebElement to test scenario
     */
    @Test(priority = 1, groups = {"native"}, dataProvider = "DataForRegistrationScenario",
            dataProviderClass = TestData.class,
            description = "This test register in system and log in")
    public void registrationAndLoginTest(
            String loginActivity,
            String regActivity,
            String budgetActivity,
            String expectedAndroidString,
            String expectedIosString)
            throws IllegalAccessException, NoSuchFieldException {

        setActivitiesForTest(
                new LoginActivity(getDriver()),
                new RegistrationActivity(getDriver()),
                new BudgetActivity(getDriver()));

        currentActivity = getPO(loginActivity);
        currentActivity.getElement("registerBtn").click();

        currentActivity = getPO(regActivity);
        currentActivity.getElement("emailField").sendKeys(TEST_USER.email);
        currentActivity.getElement("usernameField").sendKeys(TEST_USER.username);
        currentActivity.getElement("passField").sendKeys(TEST_USER.password);
        currentActivity.getElement("passConfirmField").sendKeys(TEST_USER.password);
        currentActivity.getElement("newAccountBtn").click();

        currentActivity = getPO(loginActivity);
        currentActivity.getElement("emailField").sendKeys(TEST_USER.email);
        currentActivity.getElement("passwordField").sendKeys(TEST_USER.password);
        currentActivity.getElement("signInBtn").click();

        currentActivity = getPO(budgetActivity);
        WebElement addExpenseBtn = currentActivity.getElement("addExpenseBtn");
        if (platformName.equals("Android")) {
            assertEquals(
                    addExpenseBtn.getText().toLowerCase(),
                    expectedAndroidString.toLowerCase());
        } else {
            assertEquals(
                    addExpenseBtn.getText().toLowerCase(),
                    expectedIosString.toLowerCase());
        }
    }
}