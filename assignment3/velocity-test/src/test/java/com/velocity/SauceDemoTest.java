import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

public class SauceDemoTest {

    private WebDriver driver;
    private WebDriverWait wait;

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) driver.quit();
    }

    private void login(String username, String password) {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    @Test
    public void testValidLogin() {
        login("standard_user", "secret_sauce");
        assertTrue(driver.getCurrentUrl().contains("/inventory"));
    }

    @Test
    public void testInvalidLogin() {
        login("wrong_user", "wrong_pass");
        String error = driver.findElement(
            By.cssSelector(".error-message-container")).getText();
        assertTrue(error.contains("Username and password do not match"));
    }

    @Test
    public void testLockedOutUser() {
        login("locked_out_user", "secret_sauce");
        String error = driver.findElement(
            By.cssSelector(".error-message-container")).getText();
        assertTrue(error.contains(
            "Sorry, this user has been locked out"));
    }

    @Test
    public void testProductsPageLoads() {
        login("standard_user", "secret_sauce");
        assertEquals("Products",
            driver.findElement(By.cssSelector(".title")).getText());
    }

    @Test
    public void testProductsAreDisplayed() {
        login("standard_user", "secret_sauce");
        assertTrue(driver.findElements(
            By.cssSelector(".inventory_item")).size() > 0);
    }

    @Test
    public void testSortProductsLowToHigh() {
        login("standard_user", "secret_sauce");
        new Select(driver.findElement(
            By.cssSelector(".product_sort_container")))
            .selectByValue("lohi");
        String selected = new Select(driver.findElement(
            By.cssSelector(".product_sort_container")))
            .getFirstSelectedOption().getText();
        assertEquals("Price (low to high)", selected);
    }

    @Test
    public void testAddItemToCart() {
        login("standard_user", "secret_sauce");
        driver.findElement(
            By.cssSelector(".inventory_item button")).click();
        assertEquals("1", driver.findElement(
            By.cssSelector(".shopping_cart_badge")).getText());
    }

    @Test
    public void testAddMultipleItemsToCart() {
        login("standard_user", "secret_sauce");
        java.util.List<WebElement> buttons = driver.findElements(
            By.cssSelector(".inventory_item button"));
        buttons.get(0).click();
        buttons.get(1).click();
        assertEquals("2", driver.findElement(
            By.cssSelector(".shopping_cart_badge")).getText());
    }

    @Test
    public void testViewCart() {
        login("standard_user", "secret_sauce");
        driver.findElement(
            By.cssSelector(".inventory_item button")).click();
        driver.findElement(
            By.cssSelector(".shopping_cart_link")).click();
        assertTrue(driver.getCurrentUrl().contains("/cart"));
        assertEquals(1, driver.findElements(
            By.cssSelector(".cart_item")).size());
    }

    @Test
    public void testRemoveItemFromCart() {
        login("standard_user", "secret_sauce");
        driver.findElement(
            By.cssSelector(".inventory_item button")).click();
        driver.findElement(
            By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(
            By.cssSelector(".cart_item button")).click();
        assertEquals(0, driver.findElements(
            By.cssSelector(".cart_item")).size());
    }

    @Test
    public void testCheckoutStepOne() {
        login("standard_user", "secret_sauce");
        driver.findElement(
            By.cssSelector(".inventory_item button")).click();
        driver.findElement(
            By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        assertTrue(driver.getCurrentUrl()
            .contains("/checkout-step-one"));
    }

    @Test
    public void testCheckoutWithEmptyFieldsShowsError() {
        login("standard_user", "secret_sauce");
        driver.findElement(
            By.cssSelector(".inventory_item button")).click();
        driver.findElement(
            By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("continue")).click();
        String error = driver.findElement(
            By.cssSelector(".error-message-container")).getText();
        assertTrue(error.contains("First Name is required"));
    }

    @Test
    public void testCheckoutStepTwo() {
        login("standard_user", "secret_sauce");
        driver.findElement(
            By.cssSelector(".inventory_item button")).click();
        driver.findElement(
            By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        assertTrue(driver.getCurrentUrl()
            .contains("/checkout-step-two"));
    }

    @Test
    public void testCompleteOrder() {
        login("standard_user", "secret_sauce");
        driver.findElement(
            By.cssSelector(".inventory_item button")).click();
        driver.findElement(
            By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        assertEquals("Thank you for your order!",
            driver.findElement(
                By.cssSelector(".complete-header")).getText());
    }

    @Test
    public void testLogout() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
            By.id("logout_sidebar_link"))).click();
        assertEquals("https://www.saucedemo.com/",
            driver.getCurrentUrl());
    }
}