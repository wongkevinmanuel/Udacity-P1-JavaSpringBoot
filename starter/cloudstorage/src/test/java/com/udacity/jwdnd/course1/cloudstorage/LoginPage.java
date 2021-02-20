package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(css="#inputUsername")
    private WebElement userNameField;

    @FindBy(css="#inputPassword")
    private WebElement password;

    @FindBy( css ="#submit-button")
    private WebElement submitButton;

    public LoginPage(WebDriver webDriver){
        PageFactory.initElements(webDriver,this);
    }

    public void login(String nombreUser,String passwordUser){
        this.userNameField.sendKeys(nombreUser);
        this.password.sendKeys(passwordUser);
        this.submitButton.click();
    }

}
