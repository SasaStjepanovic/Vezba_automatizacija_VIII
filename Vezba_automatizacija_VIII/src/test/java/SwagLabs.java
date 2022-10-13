import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class SwagLabs {

    WebDriver driver;

    //    @Parameters({"type","url"})
//    @BeforeMethod
//    public void setup(String type, String url){
//        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
//        if(type.equalsIgnoreCase("chrome")) {
//            driver = new ChromeDriver();
//        } else if( type.equalsIgnoreCase("firefox")) {
//            driver = new FirefoxDriver();
//        } else {
//            Assert.assertTrue(false, "No browser or no good type sent");
//        }
//        driver.manage().window().maximize();
//        driver.get(url);
//    }

    // Ako bi se stavilo @BeforeMethod onda bi se oopozivala pre svake metode
    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    @Test(priority = 0)
    @Parameters({"username", "password", "testType", "errorMessage"})
    public void loginToSwagLabs(String username, String password, String testType, @Optional String errorMessage) {
        driver.findElement(By.xpath("//input[@id='user-name']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(password);
        driver.findElement(By.xpath("//form/input")).click();

        if (testType.equals("positive")) {
            Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Products']")).getText(), "PRODUCTS");
        } else {
            Assert.assertEquals(driver.findElement(By.xpath("//form//h3")).getText(), errorMessage, "Something went wrong");
        }
    }

    @Test(priority = 1)
    @Parameters({"testType", "logoutMessage"})
    public void logOut(String testType, String logoutMessage) throws InterruptedException {
        driver.findElement(By.cssSelector("#react-burger-menu-btn")).click();
        driver.findElement(By.cssSelector("#inventory_sidebar_link")).isDisplayed();
        driver.findElement(By.cssSelector("#about_sidebar_link")).isDisplayed();
        driver.findElement(By.cssSelector("#logout_sidebar_link")).isDisplayed();
        driver.findElement(By.cssSelector("#reset_sidebar_link")).isDisplayed();
        driver.findElement(By.cssSelector("#logout_sidebar_link")).click();
        Thread.sleep(2000);

        if (testType.equals("positive")) {
            String LoginText = driver.findElement(By.cssSelector(".submit-button.btn_action,[value]:nth-child(2)")).getAttribute("value");
            Assert.assertEquals(LoginText, logoutMessage);
        } else {
            Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Products']")).getText(), "PRODUCTS");
        }
    }

    @Test(priority = 2)
    public void checkParametersOfFirstProduct() {
        driver.findElement(By.cssSelector("#item_4_img_link")).click();
        String actualTitle1 = driver.findElement(By.cssSelector(".inventory_details_name.large_size")).getText();
        String expectedTitle1 = "Sauce Labs Backpack";
        Assert.assertEquals(actualTitle1, expectedTitle1);

        String buttonDefaultName1 = driver.findElement(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory")).getText();
        String buttonExpectedName1 = "ADD TO CART";
        Assert.assertEquals(buttonDefaultName1, buttonExpectedName1);
        driver.findElement(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory")).click();
        String buttonNotDefaultName1 = driver.findElement(By.cssSelector(".btn.btn_secondary.btn_small.btn_inventory")).getText();
        String buttonNotExpectedName1 = "REMOVE";
        Assert.assertEquals(buttonNotDefaultName1, buttonNotExpectedName1);
    }

    @Test(priority = 3)
    public void AddProductsAndCheckBasket() {
        driver.findElement(By.cssSelector("#item_4_img_link")).click();
        driver.findElement(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory")).click();
        driver.navigate().back();
        driver.findElement(By.cssSelector("#item_1_img_link")).click();
        driver.findElement(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory")).click();
        driver.navigate().back();
        driver.findElement(By.cssSelector("#item_2_img_link")).click();
        driver.findElement(By.cssSelector(".btn.btn_primary.btn_small.btn_inventory")).click();
        driver.navigate().back();

        driver.findElement(By.cssSelector(".shopping_cart_container>a")).click();
        String basketNumberActual = driver.findElement(By.cssSelector(".shopping_cart_badge")).getText();
        String basketNumberExpexted = "3";
        Assert.assertEquals(basketNumberActual, basketNumberExpexted);
    }

    @Test(priority = 3)
    @Parameters({"firstName", "lastName", "zipCode"})
    public void checkOut(String firstName, String lastName, String zipCode) {
        driver.findElement(By.xpath("//form/input")).click();
        driver.findElement(By.xpath(".btn.btn_action.btn_medium.checkout_button")).click();
        driver.findElement(By.xpath("//input[@id='user-name']")).sendKeys(firstName);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(lastName);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(zipCode);
        driver.findElement(By.xpath(".submit-button.btn.btn_primary.cart_button.btn_action")).click();
    }
}


