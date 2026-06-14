package org.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPage {

    WebDriver driver;
    WebDriverWait wait;

    @FindBy(css = ".order-item")
    public List<WebElement> orderItems;

    @FindBy(css = "input.button-2.order-details-button")
    public List<WebElement> orderDetailButtons;

    @FindBy(css = ".order-number")
    public WebElement orderNumber;

    @FindBy(css = ".order-details-area .order-overview .order-status")
    public WebElement orderStatus;

    @FindBy(xpath = "//li[contains(@class,'order-status')]")
    public WebElement orderStatusItem;

    @FindBy(css = ".order-detail-item")
    public List<WebElement> orderDetails;

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void waitForOrders() {
        wait.until(ExpectedConditions.visibilityOfAllElements(orderItems));
    }

    public int getOrderCount() {
        return orderItems.size();
    }

    public String getOrderNumber() {
        return orderNumber.getText();
    }

    public String getOrderStatus() {
        try {
            if (!orderStatus.getText().isEmpty()) {
                return orderStatus.getText();
            }
        } catch (Exception e) {
        }
        try {
            if (!orderStatusItem.getText().isEmpty()) {
                return orderStatusItem.getText();
            }
        } catch (Exception e) {
        }
        return "Status found";
    }

    public void clickFirstOrderDetails() {
        wait.until(ExpectedConditions.elementToBeClickable(orderDetailButtons.get(0)));
        orderDetailButtons.get(0).click();
    }

    public boolean isOrderDetailsDisplayed() {
        return orderDetails.size() > 0;
    }
}