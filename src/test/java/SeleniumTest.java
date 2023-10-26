import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

public class SeleniumTest {
    private static final String url = "https://www.kevin.eu/";
    private static final String amount = "0.01";
    private static final String email = "agnieszka.plicner@gmail.com";

    @Test
    public void testTest(){
        Logger LOGGER = Logger.getLogger(SeleniumTest.class.getName());
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, 30);

        driver.get(url);    // Go to the kevin. web page
        wait.until(elementToBeClickable(By.xpath("//button[contains(text(), 'Accept all')]"))).click();   // Accept all the cookies

        WebElement demoButton = wait
                .until(elementToBeClickable(By.linkText("Demo")));  // Find a navigation button titled "demo"
        LOGGER.info("Button DEMO has been found");
        demoButton.click();       // Click on it and go to kevin. demo page

        driver.switchTo().window(driver.getWindowHandles().stream().reduce((s, s2) -> s2).get());

        WebElement redirectPaymentFlowButton = wait
                .until(elementToBeClickable(By.xpath("//a[@href='/donate/LT']")));      // Choose redirect payment flow
        redirectPaymentFlowButton.click();

        WebElement inputAmount = driver.findElement(By.cssSelector("input[id='amount']"));
        inputAmount.sendKeys(amount);

        WebElement inputEmail = driver.findElement(By.cssSelector("input[id='email']"));            // Fill all the fields with data
        inputEmail.sendKeys(email);

        WebElement checkbox = driver.findElement(By.className("sc-iklIKw"));                // Find the checkbox for terms and conditions

        WebElement swedishBank = (new WebDriverWait(driver, 30))
                .until(elementToBeClickable(By.className("sc-iemXMA")));
        swedishBank.click();          // Choose Swedbank as the payment bank

        driver.findElement(By.className("sc-bqGHjH")).click();       // Click PAY

        WebElement errorMessage = driver.findElement(By.className("sc-bkbjAj"));
        assertThat(errorMessage.getText()).isEqualTo("You have to agree to the terms and conditions and privacy policy");
        LOGGER.info("Error message appeared in case of not marked T&C checkbox."); // Check if the error message for Terms and Conditions checkmark appears
        assertThat(errorMessage.getCssValue("color")).isEqualTo("rgba(255, 59, 48, 1)");  //  Assert the color of the message text

        checkbox.click();        // Check the checkmark for terms and conditions

        driver.findElement(By.className("sc-bqGHjH")).click();  // Proceed with payment

        driver.get(url);
        Boolean invisible = wait.until(invisibilityOfElementLocated((By.xpath("//button[contains(text(), 'Accept all')]"))));

        if (!invisible) {
            LOGGER.info("User is asked about accept cookies again.");
        } else {
            LOGGER.info("There is no cookies banner visible on current url. User has already accepted cookies.");
        }  // revisit kevin. web page and assert the presence of previously accepted cookies

    }


}