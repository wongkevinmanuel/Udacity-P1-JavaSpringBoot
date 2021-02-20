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

public class NotesTabPage {
    //Objetos  Para Guardar Nota
    @FindBy(id="note-title")
    private WebElement inputTitle;

    @FindBy(id="note-description")
    private WebElement inputDescripcion;

    @FindBy(id="noteSubmit")
    private WebElement submitButton;

    @FindBy(id="nota-modal-enviar")
    private WebElement submitModalButton;

    //Objetos para recuperar Notas de la tabla
    @FindBy(id="row-note-title")
    private List<WebElement> titles;

    @FindBy(id="row-note-description")
    private List<WebElement> descriptions;

    //Nombres de botones apara editar y eliminar
    //@FindBy(id="editar-nota")
    //private List<WebElement> editButtons;

    @FindBy(id="eliminar-nota")
    private List<WebElement> deleteButtons;

    public NotesTabPage(WebDriver webDriver){
        PageFactory.initElements(webDriver,this);
    }

    public void guardarNota(WebDriver webDriver, String title, String descripcion){
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOf(inputTitle)).sendKeys(title);
        wait.until(ExpectedConditions.visibilityOf(inputDescripcion)).sendKeys(descripcion);

        wait.until(ExpectedConditions.visibilityOf(submitModalButton)).click();
    }

    public List<String> obtenerDetallenNota(WebDriver webDriver){
        WebDriverWait driverWait = new WebDriverWait(webDriver,10);

        try
        {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        List<String> detalleNota = new ArrayList<>(
                List.of(titles.get(0).getText()
                , descriptions.get(0).getText()));
        return detalleNota;
    }

    public void editarNota(WebDriver driver,String title, String desription){
        WebDriverWait wait = new WebDriverWait(driver,10);
        /*@FindBy(id="editar-nota")
        private List<WebElement> editButtons;*/
        WebElement editarBotones = null;
        try {
            //wait.until(ExpectedConditions.visibilityOf(editButtons.get(0))).click();
            editarBotones = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"editar-nota\"]")));//By.id("editar-nota")));
            editarBotones.click();
        }catch (TimeoutException ex){
            //wait.until(ExpectedConditions.visibilityOf(editButtons.get(0))).click();
            editarBotones = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"editar-nota\"]")));
            editarBotones.click();
        }

        wait.until(ExpectedConditions.visibilityOf(inputTitle));
        inputTitle.clear();
        inputTitle.sendKeys(title);

        wait.until(ExpectedConditions.visibilityOf(inputDescripcion));
        inputDescripcion.clear();
        inputDescripcion.sendKeys(desription);

        wait.until(ExpectedConditions.visibilityOf(submitModalButton)).click();
    }

    public void eliminarNota(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver,10);
        //wait.until(ExpectedConditions.visibilityOf(deleteButtons.get(0))).click();
        WebElement eliminarBotones = null;
        try
        {
            eliminarBotones = wait.until(ExpectedConditions
                                        .elementToBeClickable(By.xpath("//*[@id=\"eliminar-nota\"]")));
            eliminarBotones.click();
        }catch (TimeoutException ex){
            eliminarBotones = wait.until(ExpectedConditions
                                        .elementToBeClickable(By.xpath("//*[@id=\"eliminar-nota\"]")));
            eliminarBotones.click();
        }
    }
}
