package org.pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {
    WebDriver driver;

    @FindBy(linkText = "Register")
    public WebElement registerLink;

    @FindBy(id = "gender-male")
    public WebElement genderMale;

    @FindBy(id = "FirstName")
    public WebElement firstNameField;

    @FindBy(id = "LastName")
    public WebElement lastNameField;

    @FindBy(id = "Email")
    public WebElement emailField;

    @FindBy(id = "Password")
    public WebElement passwordField;

    @FindBy(id = "ConfirmPassword")
    public WebElement confirmPasswordField;

    @FindBy(id = "register-button")
    public WebElement registerButton;

    @FindBy(className = "result")
    public WebElement registrationResult;

    @FindBy(css = ".field-validation-error span")
    public WebElement validationError;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickRegisterLink() {
        registerLink.click();
    }

    public void enterFirstName(String firstName) {
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirmPassword);
    }

    public void clickRegister() {
        registerButton.click();
    }

    public void selectGenderMale() {
        genderMale.click();
    }

    public String getRegistrationResultText() {
        return registrationResult.getText();
    }

    public void register(String firstName, String lastName, String email,
                         String password, String confirmPassword) {
        selectGenderMale();
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickRegister();
    }
}	