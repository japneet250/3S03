package com.velocity;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

public class SauceDemoTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromiumdriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-extensions");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-debugging-port=0");
        options.setBinary("/usr/bin/chromium-browser");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void login(String username, String password) {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    // -------------------------------------------------------
    // Step 1: Login
    // -------------------------------------------------------

    @Test
    public void testValidLogin() {
        login("standard_user", "secret_sauce");
        assertTrue(driver.getCurrentUrl().contains("/inventory"));
    }

    @Test
    public void testInvalidLogin() {
        login("wrong_user", "wrong_pass");
        String error = driver.findElement(By.cssSelector(".error-message-container")).getText();
        assertTrue(error.contains("Username and password do not match"));
    }

    @Test
    public void testLockedOutUser() {
        login("locked_out_user", "secret_sauce");
        String error = driver.findElement(By.cssSelector(".error-message-container")).getText();
        assertTrue(error.contains("Sorry, this user has been locked out"));
    }

    // -------------------------------------------------------
    // Step 2: Browse Products
    // -------------------------------------------------------

    @Test
    public void testProductsPageLoads() {
        login("standard_user", "secret_sauce");
        String title = driver.findElement(By.cssSelector(".title")).getText();
        assertEquals("Products", title);
    }

    @Test
    public void testProductsAreDisplayed() {
        login("standard_user", "secret_sauce");
        int productCount = driver.findElements(By.cssSelector(".inventory_item")).size();
        assertTrue(productCount > 0);
    }

    @Test
    public void testSortProductsLowToHigh() {
        login("standard_user", "secret_sauce");
        // select the option
        new Select(driver.findElement(By.cssSelector(".product_sort_container")))
            .selectByValue("lohi");
        // re-find the element after page updates to avoid stale reference
        String selected = new Select(driver.findElement(By.cssSelector(".product_sort_container")))
            .getFirstSelectedOption().getText();
        assertEquals("Price (low to high)", selected);
    }

    // -------------------------------------------------------
    // Step 3: Add Item to Cart
    // -------------------------------------------------------

    @Test
    public void testAddItemToCart() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.cssSelector(".inventory_item button")).click();
        String cartCount = driver.findElement(By.cssSelector(".shopping_cart_badge")).getText();
        assertEquals("1", cartCount);
    }

    @Test
    public void testAddMultipleItemsToCart() {
        login("standard_user", "secret_sauce");
        java.util.List<WebElement> buttons = driver.findElements(By.cssSelector(".inventory_item button"));
        buttons.get(0).click();
        buttons.get(1).click();
        String cartCount = driver.findElement(By.cssSelector(".shopping_cart_badge")).getText();
        assertEquals("2", cartCount);
    }

    // -------------------------------------------------------
    // Step 4: View Cart
    // -------------------------------------------------------

    @Test
    public void testViewCart() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.cssSelector(".inventory_item button")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        assertTrue(driver.getCurrentUrl().contains("/cart"));
        int cartItems = driver.findElements(By.cssSelector(".cart_item")).size();
        assertEquals(1, cartItems);
    }

    @Test
    public void testRemoveItemFromCart() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.cssSelector(".inventory_item button")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(By.cssSelector(".cart_item button")).click();
        int cartItems = driver.findElements(By.cssSelector(".cart_item")).size();
        assertEquals(0, cartItems);
    }

    // -------------------------------------------------------
    // Step 5: Checkout
    // -------------------------------------------------------

    @Test
    public void testCheckoutStepOne() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.cssSelector(".inventory_item button")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        assertTrue(driver.getCurrentUrl().contains("/checkout-step-one"));
    }

    @Test
    public void testCheckoutWithEmptyFieldsShowsError() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.cssSelector(".inventory_item button")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("continue")).click();
        String error = driver.findElement(By.cssSelector(".error-message-container")).getText();
        assertTrue(error.contains("First Name is required"));
    }

    @Test
    public void testCheckoutStepTwo() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.cssSelector(".inventory_item button")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        assertTrue(driver.getCurrentUrl().contains("/checkout-step-two"));
    }

    // -------------------------------------------------------
    // Step 6: Complete Order
    // -------------------------------------------------------

    @Test
    public void testCompleteOrder() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.cssSelector(".inventory_item button")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        String confirmation = driver.findElement(By.cssSelector(".complete-header")).getText();
        assertEquals("Thank you for your order!", confirmation);
    }

    @Test
    public void testLogout() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))).click();
        assertEquals("https://www.saucedemo.com/", driver.getCurrentUrl());
    }
}
