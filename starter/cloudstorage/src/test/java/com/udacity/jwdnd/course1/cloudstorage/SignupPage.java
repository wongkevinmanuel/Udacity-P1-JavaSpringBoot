package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(css= "#inputFirstName")
    private WebElement firstName;

    @FindBy(css= "#inputLastName")
    private WebElement lastName;

    @FindBy(css= "#inputUsername")
    private WebElement userName;

    @FindBy(css= "#inputPassword")
    private WebElement password;

    @FindBy(css= "#submitButton")
    private WebElement submitButton;

    public SignupPage(WebDriver webDriver){
        PageFactory.initElements(webDriver , this);
    }

    public void signup(String nombre, String ultimoNombre
                        ,String nombreUser, String clave){
        this.firstName.sendKeys(nombre);
        this.lastName.sendKeys(ultimoNombre);
        this.userName.sendKeys(nombreUser);
        this.password.sendKeys(clave);
        this.submitButton.click();

    }

}
