package hr.fer.progi.ticketmestar;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class SystemTests {
	WebDriver driver = new ChromeDriver();
	private static final Logger logger = LoggerFactory.getLogger(SystemTests.class);

	@Test
	public void testGoogleLogin() {
		logger.info("Testing Google login with good credentials");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		logger.info("Navigating to homepage");
		driver.get("http://localhost:3000/");
		logger.info("Page loaded successfully.");

		logger.info("Clicking 'Log In With Google' button");
		driver.findElement(By.xpath("//button[contains(text(), 'Log In With Google')]")).click();

		logger.info("Filling up the email input field with 'probaprogi507@gmail.com'");
		WebElement element = driver.findElement(By.cssSelector("input[type='email']"));
		element.sendKeys("probaprogi507@gmail.com");

		logger.info("Clicking the button 'Next'");
		driver.findElement(By.xpath("//span[contains(text(), 'Next')]")).click();

		logger.info("Filling up the password input field with 'Proba123-'");
		element = driver.findElement(By.cssSelector("input[aria-label='Enter your password']"));
		element.sendKeys("Proba123-");

		logger.info("Clicking the button 'Next'");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'Next')]")));
		button.click();

		logger.info("Clicking the button 'Nastavi'");
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
			logger.info("Test passed successfully.");
		} catch (TimeoutException e) {
			logger.error("Test failed.");
			fail("Page did not redirect to /home or /select-role in time.");

		} finally {
			driver.quit();
			logger.info("Test finished. Browser closed.");
		}
	}

	@Test
	public void testClassicLoginWithGoodCredentials() {
		logger.info("Testing classic log in with good credentials");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		logger.info("Navigating to homepage");
		driver.get("http://localhost:3000/");
		logger.info("Page loaded successfully.");

		logger.info("Filling up the email input field with 'admin@admin.com'");
		WebElement element = driver.findElement(By.cssSelector("input[placeholder='Email']"));
		element.sendKeys("admin@admin.com");

		logger.info("Filling up the password input field with 'admin'");
		element = driver.findElement(By.cssSelector("input[placeholder='Password']"));
		element.sendKeys("admin");

		logger.info("Clicking the button 'Log in'");
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			wait.until(ExpectedConditions.urlContains("/home"));
			String redirectURL = driver.getCurrentUrl();
			assertTrue(redirectURL.contains("/home"), "Expected URL to contain /home, URL was: " + redirectURL);
			assertTrue(driver.findElement(By.cssSelector("span[class='WelcomeText']")).getText().equals("Welcome, admin"), "Expected to have a 'Welcome, admin' message");
			logger.info("Test passed successfully.");
		} catch (TimeoutException e) {
			logger.error("Test failed.");
			fail("Page did not redirect to /home in time.");
		} finally {
			driver.quit();
			logger.info("Test finished. Browser closed.");
		}
	}

	@Test
	public void testClassicLoginWithBadPassword() {
		logger.info("Testing classic log in with wrong password");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		logger.info("Navigating to homepage");
		driver.get("http://localhost:3000/");
		logger.info("Page loaded successfully.");

		logger.info("Filling up the email input field with 'admin@admin.com'");
		WebElement element = driver.findElement(By.cssSelector("input[placeholder='Email']"));
		element.sendKeys("admin@admin.com");

		logger.info("Filling up the password input field with 'wronngpassword'");
		element = driver.findElement(By.cssSelector("input[placeholder='Password']"));
		element.sendKeys("wrongpassword");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			assertEquals("Login failed: Invalid email or password", alertText, "Expected alert message about wrong credentials, but alert text is" + alertText);
			logger.info("Test passed successfully.");
			alert.accept();
		} catch (Exception e) {
			logger.error("Test failed.");
			fail("No alert was shown for bad credentials.");
		} finally {
			driver.quit();
			logger.info("Test finished. Browser closed.");
		}
	}

	@Test
	public void testClassicLoginWithBadEmail() {
		logger.info("Testing classic log in with wrong email");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		logger.info("Navigating to homepage");
		driver.get("http://localhost:3000/");
		logger.info("Page loaded successfully.");

		logger.info("Filling up the email input field with 'wrong@admin.com'");
		WebElement element = driver.findElement(By.cssSelector("input[placeholder='Email']"));
		element.sendKeys("wrong@admin.com");

		logger.info("Filling up the password input field with 'wrongpassword'");
		element = driver.findElement(By.cssSelector("input[placeholder='Password']"));
		element.sendKeys("wrongpassword");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			assertEquals("Login failed: Invalid email or password", alertText, "Expected alert message about wrong credentials, but alert text is" + alertText);
			logger.info("Test passed successfully.");
			alert.accept();
		} catch (Exception e) {
			logger.error("Test failed.");
			fail("No alert was shown for bad credentials.");
		} finally {
			driver.quit();
			logger.info("Test finished. Browser closed.");
		}
	}
}