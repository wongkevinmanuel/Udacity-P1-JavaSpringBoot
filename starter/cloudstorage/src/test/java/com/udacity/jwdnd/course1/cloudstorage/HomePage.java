package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


public class HomePage {
    @FindBy(id="logoutButton")
    private WebElement logoutButton;

    public HomePage(WebDriver webDriver){
        PageFactory.initElements(webDriver,this);
    }

    public void logout(){
        this.logoutButton.click();
    }
}


