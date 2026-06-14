package org.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "small-searchterms")
    private WebElement searchBox;

    @FindBy(css = "input.button-1.search-box-button")
    private WebElement searchButton;

    @FindBy(css = ".product-item")
    private List<WebElement> searchResults;

    @FindBy(css = ".product-item h2.product-title a")
    public WebElement firstProductLink;

    @FindBy(id = "Q")
    private WebElement advancedSearchInput;

    @FindBy(id = "As")
    private WebElement advancedSearchCheckbox;

    @FindBy(id = "Pf")
    private WebElement priceFrom;

    @FindBy(id = "Pt")
    private WebElement priceTo;

    @FindBy(css = "input.button-1.search-button")
    private WebElement advancedSearchButton;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void searchProduct(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        searchBox.clear();
        searchBox.sendKeys(keyword);
        searchButton.click();
    }

    public void clickFirstProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(firstProductLink));
        firstProductLink.click();
    }

    public void applyPriceFilter(String keyword, String minPrice, String maxPrice) {
        wait.until(ExpectedConditions.visibilityOf(advancedSearchInput));
        advancedSearchInput.clear();
        advancedSearchInput.sendKeys(keyword);
        if (!advancedSearchCheckbox.isSelected()) {
            advancedSearchCheckbox.click();
        }
        wait.until(ExpectedConditions.visibilityOf(priceFrom));
        priceFrom.clear();
        priceFrom.sendKeys(minPrice);
        priceTo.clear();
        priceTo.sendKeys(maxPrice);
        advancedSearchButton.click();
    }

    public int getSearchResultCount() {
        return searchResults.size();
    }

    public boolean isResultsDisplayed() {
        return searchResults.size() > 0;
    }
}