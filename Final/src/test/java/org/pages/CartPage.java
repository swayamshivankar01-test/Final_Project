package org.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {

    WebDriver driver;
    WebDriverWait wait;

    @FindBy(css = ".cart-qty")
    public WebElement cartCount;

    @FindBy(css = "input.button-1.add-to-cart-button")
    public WebElement addToCartButton;

    @FindBy(css = "input.button-1.add-to-cart-button")
    public List<WebElement> addToCartButtons;

    @FindBy(css = ".bar-notification.success")
    public WebElement successNotification;

    @FindBy(css = "input.qty-input")
    public List<WebElement> quantityFields;

    @FindBy(css = "input.button-2.update-cart-button")
    public WebElement updateCartButton;

    @FindBy(css = ".cart-total .order-total strong")
    public WebElement orderTotal;

    @FindBy(css = "input[name='removefromcart']")
    public List<WebElement> removeButtons;

    @FindBy(css = ".order-summary-content")
    public WebElement emptyCartMessage;

    @FindBy(css = ".cart-item-row")
    public List<WebElement> cartItems;

    @FindBy(css = ".product-name")
    public WebElement productName;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void waitForProductPage() {
        wait.until(ExpectedConditions.visibilityOf(productName));
    }

    public boolean isAddToCartPresent() {
        return !addToCartButtons.isEmpty();
    }

    public void clickAddToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        addToCartButton.click();
    }

    public String getCartCount() {
        return cartCount.getText();
    }

    public boolean isSuccessNotificationDisplayed() {
        return successNotification.isDisplayed();
    }

    public String getOrderTotal() {
        wait.until(ExpectedConditions.visibilityOf(orderTotal));
        return orderTotal.getText();
    }

    public String getQuantityValue(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(quantityFields));
        return quantityFields.get(index).getAttribute("value");
    }

    public void updateQuantity(int index, String quantity) {
        wait.until(ExpectedConditions.visibilityOfAllElements(quantityFields));
        WebElement qtyField = quantityFields.get(index);
        qtyField.clear();
        qtyField.sendKeys(quantity);
        updateCartButton.click();
    }

    public void clearCart() {
        if (!cartItems.isEmpty()) {
            for (WebElement removeBtn : removeButtons) {
                removeBtn.click();
            }
            updateCartButton.click();
            wait.until(ExpectedConditions.visibilityOf(emptyCartMessage));
        }
    }

    public void removeItem(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(removeButtons));
        removeButtons.get(index).click();
        updateCartButton.click();
    }

    public boolean isCartEmpty() {
        wait.until(ExpectedConditions.visibilityOf(emptyCartMessage));
        return emptyCartMessage.getText()
                .contains("Your Shopping Cart is empty!");
    }

    public int getCartItemCount() {
        return cartItems.size();
    }
}