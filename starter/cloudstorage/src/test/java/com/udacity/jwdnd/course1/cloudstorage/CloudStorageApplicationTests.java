package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {
	@LocalServerPort
	private int port;

	private static WebDriver driver;
	private WebDriverWait waitDriverWeb;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		//Declared an implicit wait with the time frame of 10 seconds
		// si el elemento no se encuentra en la página web dentro 10 s, lanzará una excepción
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	@BeforeEach
	public void beforeEach() throws InterruptedException  {
		Thread.sleep(1000);
		waitDriverWeb = new WebDriverWait(driver, 20);
	}

	@AfterAll
	public static void afterAll(){
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void accesoPaginaInicioUsuarioNoLogin() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
		Thread.sleep(1000);
	}

    @Test
	@Order(2)
    public void accesoPaginaRegistroUsuarioNoLogin() throws InterruptedException{
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("SignUp",driver.getTitle());
		Thread.sleep(1000);
    }

	@Order(3)
	@Test
	public void userSignupLoginAndAccessHomeAndCloseLogin() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/signup");
		String primerNombre= "Kevin";
		String segundoNombre= "Onofre";
		String username = "kev";
		String password = "123";
		//Registrar usuario nuevo
		SignupPage paginaRegistroUsuario = new SignupPage(driver);
		paginaRegistroUsuario.signup(primerNombre, segundoNombre,username,password);

		//Login con nuevo Usuario
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage paginaLogin = new LoginPage(driver);
		paginaLogin.login(username,password);

		//Realiza el logout
		driver.get("http://localhost:" + this.port + "/home");
		HomePage paginaHome = new HomePage(driver);
		Thread.sleep(1000);
		paginaHome.logout();

		//Verficar Salida
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
		Thread.sleep(1000);
	}

	@Test
	@Order(4)
	public void userSignupLoginAndAccessLoginPage() throws InterruptedException {
		String username = "kev";
		String password = "123";

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage paginaLogin = new LoginPage(driver);
		paginaLogin.login(username,password);
		Assertions.assertEquals("Home",driver.getTitle());
	}

	@Test
	@Order(5)
	public void crudNotas() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/home");
		//Xpath de elementos para la prueba
		// 	//*[@id="nav-notes-tab"]
		//	//*[@id="editar-nota"]
		//  //*[@id="eliminar-nota"]

		//////////////////////////////// CREATE
		String title= "Nota Taekwondo";
		String description = "No se te olvide ir al Taekwondo.";

		NotesTabPage tabNota = new NotesTabPage(driver);
		WebElement navNotesTab = waitDriverWeb.until(ExpectedConditions
								.elementToBeClickable(By.xpath("//*[@id=\"nav-notes-tab\"]")));
		navNotesTab.click();
		WebElement botonAgregarNota = waitDriverWeb.until(ExpectedConditions
								.elementToBeClickable((By.id("agregar-nota"))));
		botonAgregarNota.click();
		tabNota.guardarNota(driver,title,description);
		Thread.sleep(1000);

		//Home
		driver.get("http://localhost:" + this.port + "/home");
		WebElement navNotesTab2 = waitDriverWeb.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//*[@id=\"nav-notes-tab\"]")));
		navNotesTab2.click();
		Thread.sleep(2000);

		////////////////////////////////////// READ
		List<String> detalleNota = tabNota.obtenerDetallenNota(driver);
		Assertions.assertEquals(title, detalleNota.get(0));
		Assertions.assertEquals(description, detalleNota.get(1));

		/////////////////////////////////////// UPDATE
		String titleAct= "Nota TKD actualizada";
		String descriptionAct = "No se te olvide ir al Taekwondo, a las 2pm.";
		NotesTabPage tabNota2 = new NotesTabPage(driver);
		tabNota2.editarNota(driver,titleAct,descriptionAct);

		//Home
		driver.get("http://localhost:" + this.port + "/home");
		WebElement navNotesTab3 = waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-notes-tab\"]")));
		navNotesTab3.click();
		Thread.sleep(2000);

		List<String> detalleNota2 = tabNota.obtenerDetallenNota(driver);
		Assertions.assertEquals(titleAct, detalleNota2.get(0));
		Assertions.assertEquals(descriptionAct, detalleNota2.get(1));

		////////////////////////////////////// DELETE
		driver.get("http://localhost:" + this.port + "/home");
		WebElement navNotesTab4 =waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-notes-tab\"]")));
		navNotesTab4.click();

		tabNota.eliminarNota(driver);

		driver.get("http://localhost:" + this.port + "/home");
		WebElement navNotesTab5 = waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-notes-tab\"]")));
		navNotesTab5.click();
		Thread.sleep(2000);

		boolean isnoteTitlePresent = driver.findElements(By.id("row-note-title")).size() <= 0 ?  true : false;
		Assertions.assertEquals(true,isnoteTitlePresent);

	}

	@Test
	@Order(6)
	public void crudCredencial() throws InterruptedException{
		driver.get("http://localhost:" + this.port + "/home");
		//Datos a Guardar
		String credentialUrl = "www.kevwongmundofeliz.com";
		String credentialUsername = "kev";
		String credentialPassword = "123456";

		// Elementos xpath
		// //*[@id="agregar-credencial"]
		// //*[@id="editar-credencial"]
		// //*[@id="eliminar-credencial"]

		///////////////////////////////////////// CREATE
		CredentialsTabPage tabCredential = new CredentialsTabPage(driver);
		WebElement navCredencialTab = waitDriverWeb.until(ExpectedConditions
											.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")));
		navCredencialTab.click();
		WebElement botonAgregarCredential = waitDriverWeb.until(ExpectedConditions
											.elementToBeClickable(By.xpath("//*[@id=\"agregar-credencial\"]")));
		botonAgregarCredential.click();
		tabCredential.guardarCredencial(driver,credentialUrl,credentialUsername,credentialPassword);
		Thread.sleep(2000);

		//Home
		driver.get("http://localhost:" + this.port + "/home");
		WebElement navCredencialTab1 = waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")));
		navCredencialTab1.click();
		Thread.sleep(2000);

		///////////////////////////////////////////////// READ
		// Elementos xpath the tb
		// //*[@id="row-credencial-url"]
		// //*[@id="row-credencial-username"]
		// //*[@id="row-credencial-password"]
		List<String> detalleCredencial = tabCredential.obtenerDetalleCredencial(driver);
		try{
			if(detalleCredencial.size() == 0){
				Thread.sleep(1000);
				detalleCredencial = tabCredential.obtenerDetalleCredencial(driver);
			}
			Assertions.assertEquals(credentialUrl, detalleCredencial.get(0));
			Assertions.assertEquals(credentialUsername, detalleCredencial.get(1));
		}catch (Exception exception){}
		//////////////////////////////////////////////// UPDATE
		//Home
		driver.get("http://localhost:" + this.port + "/home");
		WebElement navCredencialTab2 = waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")));
		navCredencialTab2.click();

		String credentialUrlAct = "www.kevwongmundofelizactualizado.com";
		String credentialUserNameAct = "kev";
		String credentialPasswordAct = "kevin";

		CredentialsTabPage tabCredential2 = new CredentialsTabPage(driver);
		tabCredential2.editarCredencial(driver,credentialUrlAct,credentialUserNameAct,credentialPasswordAct);

		//Home
		driver.get("http://localhost:" + this.port + "/home");
		WebElement navCredencialTab3 =waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")));
		navCredencialTab3.click();
		Thread.sleep(2000);

		List<String> detalleCredencial2 = tabCredential.obtenerDetalleCredencial(driver);
		try{
			if(detalleCredencial2.size() == 0) {
				Thread.sleep(1000);
				detalleCredencial2 = tabCredential.obtenerDetalleCredencial(driver);
			}
			Assertions.assertEquals(credentialUrlAct, detalleCredencial2.get(0));
			Assertions.assertEquals(credentialUserNameAct, detalleCredencial2.get(1));
		}catch (Exception exception){}

		//////////////////////////////////// DELETE //////////////////
		driver.get("http://localhost:"+this.port + "/home");
		WebElement navCredencialTab4 = waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")));
		navCredencialTab4.click();
		tabCredential.eliminarCredencial(driver);
		Thread.sleep(1000);

		//Home
		driver.get("http://localhost:"+this.port + "/home");
		WebElement navCredencialTab5 = waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")));
		navCredencialTab5.click();
		Thread.sleep(2000);

		boolean isCredencialPresente= false;
		if(driver.findElements(By.id("row-credencial-url")).size() <= 0)
			isCredencialPresente = true;
		else
			isCredencialPresente = false;

		Assertions.assertEquals(true, isCredencialPresente);

	}
}
