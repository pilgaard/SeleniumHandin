package test;

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CarsTest1 {

    private static final int WAIT_MAX = 4;
    static WebDriver driver;

    @BeforeClass
    public static void setup() {
        /*########################### IMPORTANT ######################*/
 /*## Change this, according to your own OS and location of driver(s) ##*/
 /*############################################################*/
        System.setProperty("webdriver.gecko.driver", "/Users/Emil/Drivers/geckodriver");
        System.setProperty("webdriver.chrome.driver", "/Users/Emil/Drivers/chromedriver");

        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
        //Reset Database 
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
    }

    @Test
    //Verify that page is loaded and all expected data are visible
    public void test1() throws Exception {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            assertThat(rows.size(), is(5));
            return true;
        });
    }

    @Test
    //Verify the filter functionality 
    public void test2() throws Exception {
        //No need to WAIT, since we are running test in a fixed order, we know the DOM is ready (because of the wait in test1)
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys("2002");
        element = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = element.findElements(By.tagName("tr"));
        assertThat(rows.size(), is(2));
    }

    @Test
    //Verify the reverse filter functionality 
    public void test3() throws Exception {
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys(Keys.COMMAND + "a");
        element.sendKeys(Keys.BACK_SPACE);
        element = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = element.findElements(By.tagName("tr"));
        assertThat(rows.size(), is(5));
    }

    @Test
    //Verify the sorting button functionality
    public void test4() throws Exception {
        WebElement element = driver.findElement(By.id("h_year"));
        element.click();
        element = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = element.findElements(By.tagName("tr"));
        WebElement row = rows.get(0);
        assertThat(row.findElement(By.tagName("td")).getText(), is("938"));
        row = rows.get((rows.size() - 1));
        assertThat(row.findElement(By.tagName("td")).getText(), is("940"));
    }
    
    @Test
    //Verify the sorting button functionality
    public void test5() throws Exception {
        WebElement element = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = element.findElements(By.tagName("tr"));
        WebElement row = rows.get(0);
        WebElement edit = row.findElement(By.tagName("a"));
        edit.click();
        element = driver.findElement(By.id("description"));
        element.clear();
        element.sendKeys("Cool car");
        element = driver.findElement(By.id("save"));
        element.click();
        List<WebElement> colums = row.findElements(By.tagName("td"));
        WebElement field = colums.get(5);
        assertThat(field.getText(), is("Cool car"));
    }
    
    @Test
    //Verify the sorting button functionality
    public void test6() throws Exception {
        WebElement element = driver.findElement(By.id("new"));
        element.click();
        element = driver.findElement(By.id("save"));
        element.click();
        element = driver.findElement(By.id("submiterr"));
        assertThat(element.getText(), is("All fields are required"));
    }
    
    @Test
    //Verify the sorting button functionality
    public void test7() throws Exception {
        WebElement element = driver.findElement(By.id("new"));
        element.click();

        driver.findElement(By.id("year")).sendKeys("2008");
        driver.findElement(By.id("registered")).sendKeys("2002-05-05");
        driver.findElement(By.id("make")).sendKeys("Kia");
        driver.findElement(By.id("model")).sendKeys("Rio");
        driver.findElement(By.id("description")).sendKeys("As new");
        driver.findElement(By.id("price")).sendKeys("31000");

        element = driver.findElement(By.id("save"));
        element.click();

        element = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = element.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(6));
    }
}
