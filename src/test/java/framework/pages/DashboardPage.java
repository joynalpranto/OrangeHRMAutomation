package framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By pimTab = By.xpath("//span[text()='PIM']");
    private By profileName = By.xpath("//p[@class='oxd-userdropdown-name']");
    private By employeeRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
    private By employeeNameCell = By.xpath(".//div[@role='cell'][3]");
    private By employeeStatusCell = By.xpath(".//div[@role='cell'][6]");

    // Constructor
    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Verification
    public boolean isDashboardVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader)).isDisplayed();
    }

    public boolean isAdminTabVisible() {
        return driver.findElement(adminTab).isDisplayed();
    }

    public boolean isPIMTabVisible() {
        return driver.findElement(pimTab).isDisplayed();
    }

    public boolean isProfileNameVisible() {
        return driver.findElement(profileName).isDisplayed();
    }

    public void clickPIMTab() {
        driver.findElement(pimTab).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[text()='Employee Information']")));
    }

    public List<WebElement> getFirstNEmployeeRows(int n) {
        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(employeeRows));
        return rows.subList(0, Math.min(n, rows.size()));
    }

    public String getEmployeeName(WebElement row) {
        return row.findElement(employeeNameCell).getText().trim();
    }

    public String getEmployeeStatus(WebElement row) {
        return row.findElement(employeeStatusCell).getText().trim();
    }
}
