package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class CredentialsTabPage {
    //Objetos para guardar Credentials
    @FindBy(id="credential-url")
    private WebElement inputCredendialUrl;

    @FindBy(id="credential-username")
    private WebElement inputCredendialUserName;

    @FindBy(id="credential-password")
    private WebElement inputCredentialPassword;

    @FindBy(id="credential-modal-enviar")
    private WebElement submitCredentialModal;

    //Objetos para recuperar Credenciales de la tabla
    @FindBy(id="row-credencial-url")
    private List<WebElement> urls;

    @FindBy(id="row-credencial-username")
    private List<WebElement> userNames;

    @FindBy(id="row-credencial-password")
    private List<WebElement> passwords;

    //Nombre de botones para editar y eliminar
    @FindBy(id="editar-credencial")
    private List<WebElement> edditButtons;

    @FindBy(id="eliminar-credencial")
    private List<WebElement> deleteButtons;


    public CredentialsTabPage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    public  void guardarCredencial(WebDriver webDriver,String url,String username,String password){
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOf(inputCredendialUrl)).sendKeys(url);
        wait.until(ExpectedConditions.visibilityOf(inputCredendialUserName)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOf(inputCredentialPassword)).sendKeys(password);

        wait.until(ExpectedConditions.visibilityOf(submitCredentialModal)).click();
    }

    public List<String> obtenerDetalleCredencial(WebDriver webDriver){
        WebDriverWait driverWait = new WebDriverWait(webDriver, 10);
        List<String> detalleCredencial = new ArrayList<>(
                List.of(urls.get(0).getText()
                ,userNames.get(0).getText()
                ,passwords.get(0).getText()));
        return detalleCredencial;
    }

    public void editarCredencial(WebDriver driver, String url
                                ,String userName, String password){
        WebDriverWait wait = new WebDriverWait(driver,10);
        //*[@id="editar-credencial"]
        WebElement editarCredencial = null;
        try {
            editarCredencial = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"editar-credencial\"]")));
            editarCredencial.click();
        }catch (TimeoutException exception){
            editarCredencial = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"editar-credencial\"]")));
            editarCredencial.click();
        }
        //wait.until(ExpectedConditions.visibilityOf(edditButtons.get(0))).click();

        wait.until(ExpectedConditions.visibilityOf(inputCredendialUrl));
        inputCredendialUrl.clear();
        inputCredendialUrl.sendKeys(url);

        wait.until(ExpectedConditions.visibilityOf(inputCredendialUserName));
        inputCredendialUserName.clear();
        inputCredendialUserName.sendKeys(userName);

        wait.until(ExpectedConditions.visibilityOf(inputCredentialPassword));
        inputCredentialPassword.clear();
        inputCredentialPassword.sendKeys(password);

        wait.until(ExpectedConditions.visibilityOf(submitCredentialModal)).click();
    }

    public void eliminarCredencial(WebDriver webDriver){
        WebDriverWait wait = new WebDriverWait(webDriver,10);
        WebElement eliminarBotones = null;

        try
        {
          eliminarBotones = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"eliminar-credencial\"]")));
          eliminarBotones.click();
        }catch (TimeoutException ex){
            eliminarBotones = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"eliminar-credencial\"]")));
            eliminarBotones.click();
        }
        //wait.until(ExpectedConditions.visibilityOf(deleteButtons.get(0))).click();
    }
}
