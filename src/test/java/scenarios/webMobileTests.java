package scenarios;

import data.TestData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pageObjects.pages.GoogleHomePage;
import setup.BaseTest;

import java.util.List;

import static org.testng.Assert.*;

public class webMobileTests extends BaseTest {

    @Test(groups = {"web"}, description = "Make sure that we've opened IANA homepage")
    public void simpleWebTest() {
        getDriver().get("http://iana.org");
        waitForPageLoad();
        assertEquals(
                ((WebDriver) getDriver()).getTitle(),
                "Internet Assigned Numbers Authority",
                "This is not IANA homepage");
    }

    /*
    Here I've decided to use simple PageObjects with fluent interaction.
    If I understand correctly it is not necessary to use that "multiplatform" architecture
     */
    @Test(priority = 1, groups = {"web"}, description = "Check Google search engine",
            dataProvider = "DataForGoogleSearchScenario", dataProviderClass = TestData.class)
    public void googleSearchTest(String url, String searchQuery) {
        getDriver().get(url);
        waitForPageLoad();
        List<WebElement> resultsSearchBlock =
                new GoogleHomePage(getDriver())
                .typeSearchQuery(searchQuery)
                .sendSearchQuery()
                .getResultsSearchBlock();
        System.out.println(resultsSearchBlock.size());
        assertTrue(resultsSearchBlock.size() > 2);
    }
}