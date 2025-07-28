package framework.tests;

import framework.pages.DashboardPage;
import framework.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @Test
    public void verifyLoginAndEmployeeList() {
        try {
            loginPage.loginAs("Admin", "admin123");

            Assert.assertTrue(dashboardPage.isDashboardVisible(), "Dashboard not visible");
            Assert.assertTrue(dashboardPage.isAdminTabVisible(), "Admin tab not visible");
            Assert.assertTrue(dashboardPage.isPIMTabVisible(), "PIM tab not visible");
            Assert.assertTrue(dashboardPage.isProfileNameVisible(), "Profile name not visible");

            takeScreenshot("login_success");

            dashboardPage.clickPIMTab();
            List<WebElement> rows = dashboardPage.getFirstNEmployeeRows(5);

            List<String> employeeNames = new ArrayList<>();
            boolean fullTimePermanentFound = false;

            for (WebElement row : rows) {
                String name = dashboardPage.getEmployeeName(row);
                Assert.assertFalse(name.isEmpty(), "Empty employee name found.");
                employeeNames.add(name);

                String status = dashboardPage.getEmployeeStatus(row);
                if (status.equalsIgnoreCase("Full-Time Permanent")) {
                    fullTimePermanentFound = true;
                }
            }

            System.out.println("Extracted employee names: " + employeeNames);
            Assert.assertTrue(fullTimePermanentFound, "No 'Full-Time Permanent' employee found.");

        } catch (Exception e) {
            takeScreenshot("test_failure");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

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
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }
}
