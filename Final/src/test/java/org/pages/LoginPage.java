package org.pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    WebDriver driver;

    @FindBy(linkText = "Log in")
    public WebElement loginLink;

    @FindBy(id = "Email")
    public WebElement usernameField;

    @FindBy(id = "Password")
    public WebElement passwordField;

    @FindBy(className = "login-button")
    public WebElement loginButton;

    @FindBy(css = ".validation-summary-errors")
    public WebElement loginErrorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickLoginLink() {
        loginLink.click();
    }

    public void username(String un) {
        usernameField.clear();
        usernameField.sendKeys(un);
    }

    public void password(String pwd) {
        passwordField.clear();
        passwordField.sendKeys(pwd);
    }

    public void Login_btn() {
        loginButton.click();
    }

    public void login(String un, String pwd) {
        username(un);
        password(pwd);
        Login_btn();
    }

    public void clickLoginWithoutCredentials() {
        loginButton.click();
    }

    public String getLoginErrorMessage() {
        return loginErrorMessage.getText();
    }

    public boolean isErrorMessageDisplayed() {
        return loginErrorMessage.isDisplayed();
    }
}