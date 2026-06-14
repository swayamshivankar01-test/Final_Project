package org.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CheckoutPage {

    WebDriver driver;
    WebDriverWait wait;

    @FindBy(css = "button.button-1.checkout-button")
    public WebElement checkoutButton;

    @FindBy(id = "termsofservice")
    public WebElement termsCheckbox;

    @FindBy(css = ".cart-item-row")
    public List<WebElement> cartItems;

    @FindBy(id = "BillingNewAddress_FirstName")
    public WebElement billingFirstName;

    @FindBy(id = "BillingNewAddress_LastName")
    public WebElement billingLastName;

    @FindBy(id = "BillingNewAddress_Email")
    public WebElement billingEmail;

    @FindBy(id = "BillingNewAddress_CountryId")
    public WebElement billingCountry;

    @FindBy(id = "BillingNewAddress_City")
    public WebElement billingCity;

    @FindBy(id = "BillingNewAddress_Address1")
    public WebElement billingAddress;

    @FindBy(id = "BillingNewAddress_ZipPostalCode")
    public WebElement billingZip;

    @FindBy(id = "BillingNewAddress_PhoneNumber")
    public WebElement billingPhone;

    @FindBy(css = "input.button-1.new-address-next-step-button")
    public WebElement billingContinueButton;

    @FindBy(css = "#shipping-buttons-container input.button-1")
    public WebElement shippingAddressContinueButton;

    @FindBy(css = "input.button-1.shipping-method-next-step-button")
    public WebElement shippingMethodContinueButton;

    @FindBy(css = "input[value='Payments.CashOnDelivery']")
    public WebElement codPaymentOption;

    @FindBy(css = "input.button-1.payment-method-next-step-button")
    public WebElement paymentMethodContinueButton;

    @FindBy(css = "input.button-1.payment-info-next-step-button")
    public WebElement paymentInfoContinueButton;

    @FindBy(css = "input.button-1.confirm-order-next-step-button")
    public WebElement confirmOrderButton;

    @FindBy(css = ".order-completed .title strong")
    public WebElement orderSuccessMessage;

    @FindBy(name = "discountcouponcode")
    public WebElement couponCodeField;

    @FindBy(name = "applydiscountcouponcode")
    public WebElement applyCouponButton;

    @FindBy(css = ".coupon-box div.message")
    public WebElement couponResultMessage;

    @FindBy(css = ".coupon-box")
    public List<WebElement> couponBox;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void acceptTermsAndCheckout() {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                org.openqa.selenium.By.id("termsofservice")));
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('termsofservice').click();");
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        checkoutButton.click();
    }

    public void waitForCartItems() {
        wait.until(ExpectedConditions.visibilityOfAllElements(cartItems));
    }

    public void fillBillingAddress(String firstName, String lastName, String email,
                                   String country, String city, String address,
                                   String zip, String phone) {
        billingFirstName.clear();
        billingFirstName.sendKeys(firstName);
        billingLastName.clear();
        billingLastName.sendKeys(lastName);
        billingEmail.clear();
        billingEmail.sendKeys(email);
        try {
            Select countryDropdown = new Select(billingCountry);
            countryDropdown.selectByVisibleText(country);
        } catch (Exception e) {
            new Select(billingCountry).selectByVisibleText("United States");
        }
        billingCity.clear();
        billingCity.sendKeys(city);
        billingAddress.clear();
        billingAddress.sendKeys(address);
        billingZip.clear();
        billingZip.sendKeys(zip);
        billingPhone.clear();
        billingPhone.sendKeys(phone);
    }

    public void clickBillingContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(billingContinueButton));
        billingContinueButton.click();
    }

    public void clickShippingAddressContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(shippingAddressContinueButton));
        shippingAddressContinueButton.click();
    }

    public void clickShippingMethodContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(shippingMethodContinueButton));
        shippingMethodContinueButton.click();
    }

    public void selectCODPayment() {
        wait.until(ExpectedConditions.elementToBeClickable(codPaymentOption));
        if (!codPaymentOption.isSelected()) {
            codPaymentOption.click();
        }
        paymentMethodContinueButton.click();
    }

    public void clickPaymentInfoContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(paymentInfoContinueButton));
        paymentInfoContinueButton.click();
    }

    public void confirmOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton));
        confirmOrderButton.click();
    }

    public String getOrderSuccessMessage() {
        return orderSuccessMessage.getText();
    }

    public boolean isCouponBoxPresent() {
        return !couponBox.isEmpty();
    }

    public void applyCoupon(String couponCode) {
        wait.until(ExpectedConditions.visibilityOf(couponCodeField));
        couponCodeField.clear();
        couponCodeField.sendKeys(couponCode);
        applyCouponButton.click();
    }

    public String getCouponResultMessage() {
        wait.until(ExpectedConditions.visibilityOf(couponResultMessage));
        String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].textContent;", couponResultMessage);
        return text != null ? text.trim() : "";
    }
}