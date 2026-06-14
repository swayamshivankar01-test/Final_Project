package org.tests;
import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.utilities.BrowserManage;
import org.utilities.ExcelUtil;

@Listeners(org.listeners.TestListener.class)
public class SearchTest extends BrowserManage {

    public SearchTest() throws IOException {
        super();
    }

    @Test(priority = 1)
    public void searchByKeywordTest() throws Exception {
        driver.get(pro.getProperty("url"));
        String keyword = ExcelUtil.getExcelData("SearchData")[0][0].toString();

        SearchPage sp = new SearchPage(driver);
        sp.searchProduct(keyword);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("search"));

        Assert.assertTrue(sp.getSearchResultCount() > 0,
                "No search results found for keyword: " + keyword);
    }

    @Test(priority = 2)
    public void priceFilterTest() throws Exception {
        driver.get(pro.getProperty("url") + "search");
        String keyword  = ExcelUtil.getExcelData("SearchData")[0][0].toString();
        String priceMin = ExcelUtil.getExcelData("SearchData")[0][1].toString();
        String priceMax = ExcelUtil.getExcelData("SearchData")[0][2].toString();

        SearchPage sp = new SearchPage(driver);
        sp.applyPriceFilter(keyword, priceMin, priceMax);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("search"));

        int resultCount = sp.getSearchResultCount();

        Assert.assertTrue(driver.getCurrentUrl().contains("search"),
                "Search page did not load correctly");
    }
}