package hr.fer.progi.ticketmestar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class SystemTests {
	WebDriver driver = new ChromeDriver();

	@BeforeAll
	public static void setup() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Chrome Driver\\chromedriver.exe");
	}

	@Test
	public void testGoogleLogin() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://localhost:3000/");
		driver.findElement(By.xpath("//button[contains(text(), 'Log In With Google')]")).click();
		WebElement element = driver.findElement(By.cssSelector("input[type='email']"));
		element.sendKeys("probaprogi507@gmail.com");
		driver.findElement(By.xpath("//span[contains(text(), 'Next')]")).click();
		element = driver.findElement(By.cssSelector("input[aria-label='Enter your password']"));
		element.sendKeys("Proba123-");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'Next')]")));
		button.click();
		driver.findElement(By.xpath("//span[contains(text(), 'Nastavi')]")).click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(driver -> {
				String currentUrl = driver.getCurrentUrl();
				return currentUrl.contains("/home") || currentUrl.contains("/select-role");
			});
			String redirectURL = driver.getCurrentUrl();
			assertTrue(
					redirectURL.contains("/home") || redirectURL.contains("/select-role"),
					"Expected URL to contain /home or /choose-role, but URL was: " + redirectURL
			);
		} catch (TimeoutException e) {
			fail("Page did not redirect to /home or /select-role in time.");
		} finally {
			driver.quit();
		}
	}

	@Test
	public void testClassicLoginWithGoodCredentials() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://localhost:3000/");

		WebElement element = driver.findElement(By.cssSelector("input[placeholder='Email']"));
		element.sendKeys("admin@admin.com");
		element = driver.findElement(By.cssSelector("input[placeholder='Password']"));
		element.sendKeys("admin");
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.urlContains("/home"));
			String redirectURL = driver.getCurrentUrl();
			assertTrue(redirectURL.contains("/home"), "Expected URL to contain /home, URL was: " + redirectURL);
			assertTrue(driver.findElement(By.cssSelector("span[class='WelcomeText']")).getText().equals("Welcome, admin"), "Expected to have a 'Welcome, admin' message");
		} catch (TimeoutException e) {
			fail("Page did not redirect to /home in time.");
		} finally {
			driver.quit();
		}
	}

	@Test
	public void testClassicLoginWithBadPassword() {
		//we use existing email but wrong password
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://localhost:3000/");

		WebElement element = driver.findElement(By.cssSelector("input[placeholder='Email']"));
		element.sendKeys("admin@admin.com");
		element = driver.findElement(By.cssSelector("input[placeholder='Password']"));
		element.sendKeys("wrongpassword");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			assertEquals("Login failed: Invalid email or password", alertText, "Expected alert message about wrong credentials, but alert text is" + alertText);
			alert.accept();
		} catch (Exception e) {
			fail("No alert was shown for bad credentials.");
		} finally {
			driver.quit();
		}
	}

	@Test
	public void testClassicLoginWithBadEmail() {
		//we use non-existing email
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://localhost:3000/");

		WebElement element = driver.findElement(By.cssSelector("input[placeholder='Email']"));
		element.sendKeys("wrong@admin.com");
		element = driver.findElement(By.cssSelector("input[placeholder='Password']"));
		element.sendKeys("wrongpassword");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			assertEquals("Login failed: Invalid email or password", alertText, "Expected alert message about wrong credentials, but alert text is" + alertText);
			alert.accept();
		} catch (Exception e) {
			fail("No alert was shown for bad credentials.");
		} finally {
			driver.quit();
		}
	}
}