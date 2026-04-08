package com.velocity;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

public class WebTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromiumdriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.setBinary("/usr/bin/chromium-browser");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // -------------------------------------------------------
    // Form Authentication Tests
    // -------------------------------------------------------

    @Test
    public void testValidLogin() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("/secure"));
        String message = driver.findElement(By.cssSelector(".flash.success")).getText();
        assertTrue(message.contains("You logged into a secure area!"));
    }

    @Test
    public void testInvalidUsername() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("wronguser");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String message = driver.findElement(By.cssSelector(".flash.error")).getText();
        assertTrue(message.contains("Your username is invalid!"));
    }

    @Test
    public void testInvalidPassword() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String message = driver.findElement(By.cssSelector(".flash.error")).getText();
        assertTrue(message.contains("Your password is invalid!"));
    }

    @Test
    public void testEmptyFieldsLogin() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String message = driver.findElement(By.cssSelector(".flash.error")).getText();
        assertTrue(message.contains("Your username is invalid!"));
    }

    @Test
    public void testLogout() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.cssSelector("a.button")).click();
        String message = driver.findElement(By.cssSelector(".flash.success")).getText();
        assertTrue(message.contains("You logged out of the secure area!"));
    }

    // -------------------------------------------------------
    // Dropdown Tests
    // -------------------------------------------------------

    @Test
    public void testDefaultDropdownOption() {
        driver.get("https://the-internet.herokuapp.com/dropdown");
        Select dropdown = new Select(driver.findElement(By.id("dropdown")));
        String defaultOption = dropdown.getFirstSelectedOption().getText();
        assertEquals("Please select an option", defaultOption);
    }

    @Test
    public void testSelectOptionOne() {
        driver.get("https://the-internet.herokuapp.com/dropdown");
        Select dropdown = new Select(driver.findElement(By.id("dropdown")));
        dropdown.selectByVisibleText("Option 1");
        String selected = dropdown.getFirstSelectedOption().getText();
        assertEquals("Option 1", selected);
    }

    @Test
    public void testSelectOptionTwo() {
        driver.get("https://the-internet.herokuapp.com/dropdown");
        Select dropdown = new Select(driver.findElement(By.id("dropdown")));
        dropdown.selectByVisibleText("Option 2");
        String selected = dropdown.getFirstSelectedOption().getText();
        assertEquals("Option 2", selected);
    }

    @Test
    public void testSelectByValue() {
        driver.get("https://the-internet.herokuapp.com/dropdown");
        Select dropdown = new Select(driver.findElement(By.id("dropdown")));
        dropdown.selectByValue("1");
        String selected = dropdown.getFirstSelectedOption().getText();
        assertEquals("Option 1", selected);
    }

    @Test
    public void testDropdownHasTwoOptions() {
        driver.get("https://the-internet.herokuapp.com/dropdown");
        Select dropdown = new Select(driver.findElement(By.id("dropdown")));
        int optionCount = dropdown.getOptions().size();
        assertEquals(3, optionCount);
    }
}
