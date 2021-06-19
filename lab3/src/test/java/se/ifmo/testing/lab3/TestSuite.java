package se.ifmo.testing.lab3;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.UNWRAP_ROOT_VALUE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.ifmo.testing.lab3.dtos.TempMessage;
import se.ifmo.testing.lab3.dtos.TempMessageContent;
import se.ifmo.testing.lab3.dtos.TempUser;

@ExtendWith(SeleniumJupiter.class)
@TestMethodOrder(OrderAnnotation.class)
class TestSuite {

  private static final String API_URL = "https://post-shift.ru/api.php?";
  private static final String API_ACTION_NEW = "action=new";
  private static final String API_ACTION_GET_LIST = "action=getlist";
  private static final String API_ACTION_GET_MESSAGE = "action=getmail";
  private static final String API_KEY = "key=";
  private static final String API_ID = "id=";
  private static final String API_HASH = "hash=cb8f78d6f2924a1de912cb5f98ed7ba2";

  private static ObjectMapper objectMapper;
  private static TempUser tempUser;

  @BeforeAll
  static void init() {
    objectMapper =
        new ObjectMapper()
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(UNWRAP_ROOT_VALUE, false);
  }

  @Test
  @Order(1)
  void shouldRegister(final FirefoxDriver driver) throws InterruptedException {
    initNewUser();
    driver.get("https://xtool.ru/");
    driver
        .findElement(
            By.xpath("//form[contains(@class, 'form-login')]/div/a[@href='/registration/']"))
        .click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 30);
      wait.until(
          ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//form[@action='/registration/']//input[@name='email']")));
    }
    driver
        .findElement(By.xpath("//form[@action='/registration/']//input[@name='email']"))
        .sendKeys("test");
    driver.findElement(By.xpath("//form[@action='/registration/']//input[@name='rules']")).click();
    driver.findElement(By.xpath("//form[@action='/registration/']//input[@name='rules']")).click();
    {
      List<WebElement> elements =
          driver.findElements(
              By.xpath(
                  "//form[@action='/registration/']//input[@type='submit' and @disabled='disabled']"));
      assertTrue(elements.size() > 0);
    }
    driver.findElement(By.xpath("//form[@action='/registration/']//input[@name='rules']")).click();
    {
      List<WebElement> elements =
          driver.findElements(
              By.xpath(
                  "//form[@action='/registration/']//input[@type='submit' and not(@disabled='disabled')]"));
      assertTrue(elements.size() > 0);
    }
    driver.findElement(By.xpath("//form[@action='/registration/']//input[@type='submit']")).click();
    assertThat(
        driver.switchTo().alert().getText(), containsString("Не верный формат e-mail адреса"));
    driver.switchTo().alert().accept();
    driver
        .findElement(By.xpath("//form[@action='/registration/']//input[@name='email']"))
        .sendKeys("mohife7963@threepp.com");
    driver.findElement(By.xpath("//form[@action='/registration/']//input[@type='submit']")).click();
    assertThat(
        driver.switchTo().alert().getText(),
        containsString("Данный e-mail уже зарегестрирован в системе"));
    driver.switchTo().alert().accept();
    driver.findElement(By.xpath("//form[@action='/registration/']//input[@name='email']")).clear();
    driver
        .findElement(By.xpath("//form[@action='/registration/']//input[@name='email']"))
        .sendKeys(tempUser.getEmail());
    driver.findElement(By.xpath("//form[@action='/registration/']//input[@type='submit']")).click();
    synchronized (driver) {
      driver.wait(5000L);
    }
    assertThat(
        driver.switchTo().alert().getText(),
        containsString(
            "Регистрация прошла успешно!\n"
                + "На указанный Вами e-mail отправлено письмо\n"
                + "с данными для авторизации на нашем сервисе."));
    driver.switchTo().alert().accept();
    setPasswordFromEmail(tempUser);
    assertNotNull(tempUser.getPassword());
  }

  @Test
  @Order(2)
  void shouldLoginPreviouslyRegistered(final FirefoxDriver driver) {
    driver.get("https://xtool.ru/");
    driver
        .findElement(By.xpath("//form[contains(@class, 'form-login')]//input[@name='login']"))
        .sendKeys(tempUser.getEmail());
    driver
        .findElement(By.xpath("//form[contains(@class, 'form-login')]//input[@name='pass']"))
        .sendKeys(tempUser.getPassword());
    driver
        .findElement(By.xpath("//form[contains(@class, 'form-login')]//button[@type='submit']"))
        .click();
    assertThat(
        driver.findElement(By.xpath("//div/div/div/div/span")).getText(), is(tempUser.getEmail()));
    driver.findElement(By.xpath("//div/div/div/div/a[contains(@class, 'btn')]")).click();
    {
      List<WebElement> elements =
          driver.findElements(By.xpath("//b[contains(.,'Ваш ИД в сервисе')]"));
      assertTrue(elements.size() > 0);
    }
    driver.findElement(By.xpath("//div/div/div/div/a[contains(@class, 'btn')]")).click();
    {
      List<WebElement> elements =
          driver.findElements(
              By.xpath("//form[contains(@class, 'form-login')]//input[@name='login']"));
      assertTrue(elements.size() > 0);
    }
  }

  @Test
  @Order(3)
  void shouldRecoverForgottenPassword(final FirefoxDriver driver) throws InterruptedException {
    driver.get("https://xtool.ru/");
    driver
        .findElement(
            By.xpath("//form[contains(@class, 'form-login')]//a[contains(@href, '/forget/')]"))
        .click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 30);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='email']")));
    }
    driver.findElement(By.xpath("//input[@name='email']")).sendKeys("test");
    driver.findElement(By.xpath("//table//input[@type='submit']")).click();
    assertThat(
        driver.switchTo().alert().getText(), containsString("Не верный формат e-mail адреса"));
    driver.switchTo().alert().accept();
    driver.findElement(By.xpath("//input[@name='email']")).clear();
    driver.findElement(By.xpath("//input[@name='email']")).sendKeys("test@72146.com");
    driver.findElement(By.xpath("//table//input[@type='submit']")).click();
    synchronized (driver) {
      driver.wait(3000L);
    }
    assertThat(driver.switchTo().alert().getText(), containsString("Данные в системе не найдены."));
    driver.switchTo().alert().accept();
    driver.findElement(By.xpath("//input[@name='email']")).clear();
    driver.findElement(By.xpath("//input[@name='email']")).sendKeys(tempUser.getEmail());
    driver.findElement(By.xpath("//table//input[@type='submit']")).click();
    synchronized (driver) {
      driver.wait(5000L);
    }
    assertThat(
        driver.switchTo().alert().getText(),
        containsString(
            "На указанный Вами e-mail отправлено письмо\n"
                + "с ссылкой для подтверждения операции"));
    /*driver.get(getRecoveryLink(tempUser));
    synchronized (driver) {
      driver.wait(2000L);
    }
    assertThat(
        driver.findElement(By.xpath("//div/div/div/div/span")).getText(), is(tempUser.getEmail()));*/
  }

  @Test
  void shouldShowSiteTrustWithoudXpr(final FirefoxDriver driver) {
    driver.get("https://xtool.ru/");
    driver.findElement(By.xpath("//form/div/input[@type='text']")).click();
    driver.findElement(By.xpath("//form/div/input[@type='text']")).sendKeys("vk.com");
    driver.findElement(By.xpath("//form/div/div/input[@type='checkbox']")).click();
    driver.findElement(By.xpath("//form/div/button[@type='submit']")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 30);
      wait.until(
          ExpectedConditions.presenceOfElementLocated(
              By.xpath("//div/p[@class='center backlinks_count']")));
    }
    {
      List<WebElement> elements =
          driver.findElements(By.xpath("//p[contains(@class, 'backlinks_count')]/span[2]/a"));
      assertEquals(0, elements.size());
    }
    {
      List<WebElement> elements =
          driver.findElements(By.xpath("//p[contains(@class, 'backlinks_count')]/span[2]"));
      assertTrue(elements.size() > 0);
    }
    assertThat(driver.findElement(By.xpath("//p/span[@class='trust']")).getText(), is("vk.com"));
    assertThat(
        driver
            .findElement(By.xpath("//p[position()=3 and contains(@class, 'trust_title')]"))
            .getText(),
        is("Добро пожаловать | ВКонтакте"));
  }

  @Test
  void shouldSendMessage(final FirefoxDriver driver) {
    driver.get("https://xtool.ru/");
    {
      WebDriverWait wait = new WebDriverWait(driver, 30);
      wait.until(
          ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='widget-body']/div")));
    }
    {
      List<WebElement> elements = driver.findElements(By.xpath("//div[@class='widget-body']/form"));
      assertEquals(0, elements.size());
    }
    driver.findElement(By.xpath("//div[@class='widget-body']/div")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//div[@class='widget-body']/form"));
      assertTrue(elements.size() > 0);
    }
    driver
        .findElement(By.xpath("//div[@class='widget-body']/form/input[@name='name']"))
        .sendKeys("Test");
    driver
        .findElement(By.xpath("//div[@class='widget-body']/form/input[@name='email']"))
        .sendKeys("test");
    driver
        .findElement(By.xpath("//div[@class='widget-body']/form/input[@name='subject']"))
        .sendKeys("Selenium Test");
    driver
        .findElement(By.xpath("//div[@class='widget-body']/form/textarea"))
        .sendKeys("Selenium Test");
    driver.findElement(By.xpath("//div[@class='widget-body']/form/input[@type='submit']")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//div[@class='widget-body']/form"));
      assertTrue(elements.size() > 0);
    }
    driver
        .findElement(By.xpath("//div[@class='widget-body']/form/input[@name='email']"))
        .sendKeys("test@test.com");
    driver.findElement(By.xpath("//div[@class='widget-body']/form/input[@type='submit']")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//div[@class='widget-body']/form"));
      assertEquals(0, elements.size());
    }
    {
      List<WebElement> elements =
          driver.findElements(By.xpath("//div[@class='widget-body']//input[@name='eula_accept']"));
      assertTrue(elements.size() > 0);
    }
    driver.findElement(By.xpath("//div[@class='widget-body']//input[@name='eula_accept']")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//div[@class='widget-exited']"));
      assertTrue(elements.size() > 0);
    }
  }

  @Test
  void shouldShowSiteTrustWithXprByDefault(final FirefoxDriver driver) {
    driver.get("https://xtool.ru/");
    driver.findElement(By.xpath("//form/div/input[@type='text']")).click();
    driver.findElement(By.xpath("//form/div/input[@type='text']")).sendKeys("vk.com");
    driver.findElement(By.xpath("//form/div/button[@type='submit']")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 30);
      wait.until(
          ExpectedConditions.presenceOfElementLocated(
              By.xpath("//div/p[@class='center backlinks_count']")));
    }
    {
      List<WebElement> elements =
          driver.findElements(By.xpath("//p[contains(@class, 'backlinks_count')]/span[2]/a"));
      assertTrue(elements.size() > 0);
    }
    assertThat(
        driver
            .findElement(By.xpath("//p[contains(@class, 'backlinks_count')]/span[2]/a"))
            .getText(),
        is("xPR"));
    assertThat(driver.findElement(By.xpath("//p/span[@class='trust']")).getText(), is("vk.com"));
    assertThat(
        driver
            .findElement(By.xpath("//p[position()=5 and contains(@class, 'trust_title')]"))
            .getText(),
        is("Добро пожаловать | ВКонтакте"));
  }

  @Test
  void shouldBase64EncodeDecode(final FirefoxDriver driver) throws InterruptedException {
    driver.get("https://xtool.ru/");
    // Login: mohife7963@threepp.com Password: tguRQQ
    synchronized (driver) {
      driver.wait(30000L);
    }
    driver.findElement(By.linkText("Base64 encode/decode")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 30);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.name("data-to-encode")));
    }
    final var stringToEncode = "Selenium Test";
    final var encodedString = Base64.getEncoder().encodeToString(stringToEncode.getBytes());
    driver.findElement(By.name("data-to-encode")).sendKeys(stringToEncode);
    driver.findElement(By.name("encode-submit")).click();
    assertEquals(
        encodedString, driver.findElement(By.name("data-to-decode")).getAttribute("value"));

    driver.findElement(By.name("data-to-encode")).clear();
    driver.findElement(By.name("data-to-decode")).clear();
    driver.findElement(By.name("data-to-decode")).sendKeys(encodedString);
    driver.findElement(By.name("decode-submit")).click();
    assertEquals(
        stringToEncode, driver.findElement(By.name("data-to-encode")).getAttribute("value"));
  }

  private void initNewUser() {
    final var httpClient = HttpClients.createDefault();
    final var httpGet = new HttpGet(API_URL + API_ACTION_NEW + "&" + API_HASH);
    try (final var response = httpClient.execute(httpGet)) {
      tempUser = objectMapper.readValue(response.getEntity().getContent(), TempUser.class);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void setPasswordFromEmail(final TempUser tempUser) {
    final var httpClient = HttpClients.createDefault();
    final var getMessages =
        new HttpGet(
            API_URL + API_ACTION_GET_LIST + "&" + API_KEY + tempUser.getKey() + "&" + API_HASH);
    long id = -1L;
    try (final var response = httpClient.execute(getMessages)) {
      final TempMessage[] messages =
          objectMapper.readValue(response.getEntity().getContent(), TempMessage[].class);
      id =
          Arrays.stream(messages)
              .filter(message -> message.getSubject().equals("Регистрация нового пользователя"))
              .map(TempMessage::getId)
              .findFirst()
              .orElseThrow();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    final var getMessage =
        new HttpGet(
            API_URL
                + API_ACTION_GET_MESSAGE
                + "&"
                + API_KEY
                + tempUser.getKey()
                + "&"
                + API_ID
                + id
                + "&"
                + API_HASH);
    try (final var response = httpClient.execute(getMessage)) {
      final var messageContent =
          objectMapper.readValue(response.getEntity().getContent(), TempMessageContent.class);
      final var pattern = Pattern.compile("Пароль: ([\\w]+)");
      final var matcher = pattern.matcher(messageContent.getMessage());
      if (matcher.find()) {
        tempUser.setPassword(matcher.group(1));
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private String getRecoveryLink(final TempUser tempUser) {
    final var httpClient = HttpClients.createDefault();
    final var getMessages =
        new HttpGet(
            API_URL + API_ACTION_GET_LIST + "&" + API_KEY + tempUser.getKey() + "&" + API_HASH);
    long id = -1L;
    try (final var response = httpClient.execute(getMessages)) {
      final TempMessage[] messages =
          objectMapper.readValue(response.getEntity().getContent(), TempMessage[].class);
      id =
          Arrays.stream(messages)
              .filter(message -> message.getSubject().equals("Восстановление доступа пользователя"))
              .map(TempMessage::getId)
              .findFirst()
              .orElseThrow();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    final var getMessage =
        new HttpGet(
            API_URL
                + API_ACTION_GET_MESSAGE
                + "&"
                + API_KEY
                + tempUser.getKey()
                + "&"
                + API_ID
                + id
                + "&"
                + API_HASH);
    try (final var response = httpClient.execute(getMessage)) {
      final var messageContent =
          objectMapper.readValue(response.getEntity().getContent(), TempMessageContent.class);
      final var pattern = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1");
      final var matcher = pattern.matcher(messageContent.getMessage());
      if (matcher.find()) {
        return matcher.group(2);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  private String generateRandomPassword() {
    final var random = new SecureRandom();
    final int leftLimit = 97;
    final int rightLimit = 122;
    final int length = 10;
    return random
        .ints(leftLimit, rightLimit + 1)
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}
