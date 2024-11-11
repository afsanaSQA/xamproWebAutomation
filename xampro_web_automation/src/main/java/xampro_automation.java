import com.google.gson.Gson;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;

public class xampro_automation {

    private static WebDriver driver;

    // Register a user and return the user details in JSON format
    public static void registerUser() {
        try {
            String fullName = "Afsana Mim";
            String email = "splendida174@gmail.com";
            String phone = "01330097237";
            String password = "Khandaker123";

            // Open the registration page
            driver.get("https://www.xampro.org/signup");

            // Fill out the registration form
            driver.findElement(By.id("name")).sendKeys(fullName);
            Thread.sleep(1000);
            driver.findElement(By.id("email")).sendKeys(email);
            Thread.sleep(1000);
            driver.findElement(By.id("phoneNumber")).sendKeys(phone);
            Thread.sleep(1000);
            driver.findElement(By.id("password")).sendKeys(password);
            Thread.sleep(1000);
            driver.findElement(By.id("confirmPassword")).sendKeys(password);
            Thread.sleep(1000);

            // Submit the form
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[2]/div[3]/div/div[2]/form/div[6]/div/button/div")).click();

            // Save user data to a JSON file
            User user = new User(fullName, email, phone, password);
            Gson gson = new Gson();
            try (FileWriter writer = new FileWriter("userData.json")) {
                gson.toJson(user, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Print registration details
            System.out.println("Registered user: " + gson.toJson(user));
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }
    }

    // Login using saved user data from JSON
    public static void loginUser() {
        try {
            // Read user data from the JSON file
            Gson gson = new Gson();
            try (Reader reader = new FileReader("userData.json")) {
                User user = gson.fromJson(reader, User.class);

                // Open the login page
                driver.get("https://www.xampro.org/login");

                Thread.sleep(1000);
                // Fill out the login form
                driver.findElement(By.id("email")).sendKeys(user.email);
                Thread.sleep(1000);
                driver.findElement(By.id("password")).sendKeys(user.password);
                Thread.sleep(1000);

                // Submit the form
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/div[3]/div/div/div[1]/form/div[4]/div/button/div")).click();
                System.out.println("Logged in as: " + user.email);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }

    }

    // Updating Profile after login with the last registered info
    public static void updateProfile() {
        try {
            Gson gson = new Gson();
            try (Reader reader = new FileReader("userData.json")) {
                User user = gson.fromJson(reader, User.class);

                Thread.sleep(1000);
                // Click on the user profile icon
                driver.findElement(By.xpath("/html/body/div/div/div[2]/nav/div/div/div/div[4]/div/a")).click();
                Thread.sleep(1000);
                driver.findElement(By.xpath("//*[@id=\"basic-navbar-nav\"]/div/div[4]/div/div/ul/li[8]/a")).click();
                Thread.sleep(1000);
                driver.manage().window().maximize();
                Thread.sleep(1000);

                //name update
                driver.findElement(By.xpath("//*[@id=\"fullName\"]")).sendKeys(Keys.CONTROL + "a");
                Thread.sleep(1000);
                driver.findElement(By.xpath("//*[@id=\"fullName\"]")).sendKeys(Keys.DELETE);
                Thread.sleep(1000);
                driver.findElement(By.xpath("//*[@id=\"fullName\"]")).sendKeys("Afsana Mim");
                Thread.sleep(1000);

                //gender select
                JavascriptExecutor js1 = (JavascriptExecutor) driver;
                WebElement genderButton = driver.findElement(By.xpath("//*[@id=\"radio-gender-female\"]"));
                js1.executeScript("arguments[0].click();", genderButton);
                Thread.sleep(1000);

                //date of birth entry
                driver.findElement(By.xpath("//*[@id=\"dob\"]")).sendKeys("01/01/1998");
                Thread.sleep(1000);
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(100));

                JavascriptExecutor js3 = (JavascriptExecutor) driver;
                WebElement button = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[3]/div/div/div/div/div/form/div[8]/div/button"));
                js3.executeScript("arguments[0].click();", button);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }
    }

    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"); // Change to your path
        driver = new ChromeDriver(options);

        try {
            // Register a new user
            registerUser();
            Thread.sleep(3000);

            // Login with the registered user
            loginUser();
            Thread.sleep(3000);

            // Update profile
            updateProfile();
            Thread.sleep(3000);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    // User class to store user details
    public static class User {
        String fullName;
        String email;
        String phone;
        String password;

        public User(String fullName, String email, String phone, String password) {
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.password = password;
        }
    }
}