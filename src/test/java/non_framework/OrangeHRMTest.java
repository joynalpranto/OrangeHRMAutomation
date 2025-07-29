package non_framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrangeHRMTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testOrangeHRMLoginAndPIMValidation() {
        try {
            // Step 1: Navigate to Login Page
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

            // Step 2: Login with credentials
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));

            usernameField.sendKeys("Admin");
            passwordField.sendKeys("admin123");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // Step 3: Wait for Dashboard
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Dashboard']")));

            // Screenshot after login
            takeScreenshot("dashboard_loaded");

            // Step 4: Validate Dashboard Elements
            assertVisible(By.xpath("//h6[text()='Dashboard']"), "Dashboard Title");
            assertVisible(By.xpath("//span[text()='Admin']"), "Admin Module Tab");
            assertVisible(By.xpath("//span[text()='PIM']"), "PIM Module Tab");
            assertVisible(By.xpath("//p[@class='oxd-userdropdown-name']"), "User Profile Name");

            // Step 5: Navigate to PIM
            driver.findElement(By.xpath("//span[text()='PIM']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[text()='Employee Information']")));

            // Step 6: Extract and validate first 5 employee names
            List<WebElement> employeeRows = wait.until(ExpectedConditions
                    .visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='oxd-table-body']//div[@role='row']")));

            List<String> employeeNames = new ArrayList<>();
            int count = Math.min(5, employeeRows.size());

            for (int i = 0; i < count; i++) {
                WebElement nameCell = employeeRows.get(i)
                        .findElement(By.xpath(".//div[@role='cell'][3]")); // 3rd column
                String name = nameCell.getText().trim();
                employeeNames.add(name);

                Assert.assertFalse(name.isEmpty(), "Employee name at row " + (i + 1) + " is empty.");
            }

            System.out.println("Employee Names: " + employeeNames);

            // Step 7: Validate "Full-Time Permanent" status exists
            List<WebElement> statusCells = driver.findElements(By.xpath("//div[@role='cell'][6]"));
            boolean statusFound = statusCells.stream()
                    .anyMatch(cell -> cell.getText().equalsIgnoreCase("Full-Time Permanent"));

            Assert.assertTrue(statusFound, "No 'Full-Time Permanent' employee status found.");
            System.out.println("Full-Time Permanent' status found");

        } catch (Exception e) {
            takeScreenshot("error_occurred");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // assert element is visible
    private void assertVisible(By locator, String name) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("Element is visible: " + name);
        } catch (TimeoutException e) {
            takeScreenshot("missing_" + name.replaceAll("\\s+", "_").toLowerCase());
            Assert.fail("Element not visible: " + name);
        }
    }

    // take screenshots
    private void takeScreenshot(String name) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File dest = new File("screenshots/" + name + "_" + timestamp + ".png");

        try {
            Files.createDirectories(dest.getParentFile().toPath());
            Files.copy(src.toPath(), dest.toPath());
            System.out.println("Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Screenshot failed: " + e.getMessage());
        }
    }
}
