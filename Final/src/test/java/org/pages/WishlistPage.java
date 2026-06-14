package org.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class WishlistPage {

    WebDriver driver;

    @FindBy(css = "input[value='Add to wishlist']")
    public WebElement addToWishlistButton;

    @FindBy(css = ".bar-notification.success")
    public WebElement successNotification;

    @FindBy(css = ".wishlist-qty")
    public WebElement wishlistCount;

    @FindBy(css = ".cart-item-row")
    public List<WebElement> wishlistItems;

    public WishlistPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickAddToWishlist() {
        addToWishlistButton.click();
    }

    public boolean isSuccessNotificationDisplayed() {
        return successNotification.isDisplayed();
    }

    public String getWishlistCount() {
        return wishlistCount.getText();
    }

    public int getWishlistItemCount() {
        return wishlistItems.size();
    }
}