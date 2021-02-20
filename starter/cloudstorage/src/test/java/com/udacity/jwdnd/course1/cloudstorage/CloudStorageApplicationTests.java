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
	}

	@BeforeEach
	public void beforeEach() throws InterruptedException  {
		Thread.sleep(1000);
		waitDriverWeb = new WebDriverWait(driver, 40);
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
		Thread.sleep(4000);
	}

    @Test
	@Order(2)
    public void accesoPaginaRegistroUsuarioNoLogin() throws InterruptedException{
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("SignUp",driver.getTitle());
		Thread.sleep(4000);
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
		Thread.sleep(2000);
		paginaHome.logout();

		//Verficar Salida
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
		Thread.sleep(2000);
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
		Thread.sleep(2000);

		//Home
		driver.get("http://localhost:" + this.port + "/home");
		waitDriverWeb.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//*[@id=\"nav-notes-tab\"]")))
					.click();

		////////////////////////////////////// READ
		List<String> detalleNota = tabNota.obtenerDetallenNota(driver);
		Assertions.assertEquals(title, detalleNota.get(0));
		Assertions.assertEquals(description, detalleNota.get(1));
		Thread.sleep(2000);

		/////////////////////////////////////// UPDATE
		String titleAct= "Nota TKD actualizada";
		String descriptionAct = "No se te olvide ir al Taekwondo, a las 2pm.";
		NotesTabPage tabNota2 = new NotesTabPage(driver);
		tabNota2.editarNota(driver,titleAct,descriptionAct);

		//Home
		driver.get("http://localhost:" + this.port + "/home");
		waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-notes-tab\"]")))
				.click();

		List<String> detalleNota2 = tabNota.obtenerDetallenNota(driver);
		Assertions.assertEquals(titleAct, detalleNota2.get(0));
		Assertions.assertEquals(descriptionAct, detalleNota2.get(1));
		Thread.sleep(2000);

		////////////////////////////////////// DELETE
		driver.get("http://localhost:" + this.port + "/home");
		waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-notes-tab\"]")))
				.click();

		tabNota.eliminarNota(driver);

		driver.get("http://localhost:" + this.port + "/home");
		waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-notes-tab\"]")))
				.click();

		boolean isnoteTitlePresent = driver.findElements(By.id("row-note-title")).size() <= 0 ?  true : false;
		Assertions.assertEquals(true,isnoteTitlePresent);
		Thread.sleep(3000);
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
		Thread.sleep(1000);

		//Home
		driver.get("http://localhost:" + this.port + "/home");
		waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")))
				.click();

		///////////////////////////////////////////////// READ
		List<String> detalleCredencial = tabCredential.obtenerDetalleCredencial(driver);
		Assertions.assertEquals(credentialUrl, detalleCredencial.get(0));
		Assertions.assertEquals(credentialUsername, detalleCredencial.get(1));
		Thread.sleep(3000);

		//////////////////////////////////////////////// UPDATE
		//Home
		driver.get("http://localhost:" + this.port + "/home");
		waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]"))).click();

		String credentialUrlAct = "www.kevwongmundofelizactualizado.com";
		String credentialUserNameAct = "kev";
		String credentialPasswordAct = "kevin";

		CredentialsTabPage tabCredential2 = new CredentialsTabPage(driver);
		tabCredential2.editarCredencial(driver,credentialUrlAct,credentialUserNameAct,credentialPasswordAct);

		//Home
		driver.get("http://localhost:" + this.port + "/home");
		waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")))
				.click();

		List<String> detalleCredencial2 = tabCredential.obtenerDetalleCredencial(driver);
		Assertions.assertEquals(credentialUrlAct, detalleCredencial2.get(0));
		Assertions.assertEquals(credentialUserNameAct, detalleCredencial2.get(1));
		Thread.sleep(3000);

		/******************* DELETE ****************************************/
		driver.get("http://localhost:"+this.port + "/home");
		waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")))
				.click();
		tabCredential.eliminarCredencial(driver);
		Thread.sleep(2000);

		//Home
		driver.get("http://localhost:"+this.port + "/home");
		waitDriverWeb.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials-tab\"]")))
				.click();

		boolean isCredencialPresente= false;
		if(driver.findElements(By.id("row-credencial-url")).size() <= 0)
			isCredencialPresente = true;
		else
			isCredencialPresente = false;

		Assertions.assertEquals(true, isCredencialPresente);
		Thread.sleep(2000);
	}
}
