package pageobjects;

import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

/**
 * Created by sidrom on 29.08.2018.
 */
public class ProviderPage {

    private WebDriver selenium;

    By zhkuPaymentPageTabs = By.xpath("//div[@data-qa-file='SubscriptionProvider']/..//div[@data-qa-file='Tabs']");
    public By payerCodeInput      = By.xpath("//div[contains(@class, 'ui-form__row_text')]/.//input[@id='payerCode']");
    public By periodInput         = By.xpath("//div[contains(@class, 'ui-form__row_date')]/.//input[@id='period']");
    public By combinationInput    = By.xpath("//div[contains(@class, 'ui-form__row_combination')]/.//input");
    By errorMessageLabel   = By.xpath("//div[@data-qa-file='UIFormRowError']");


    public ProviderPage(WebDriver selenium) {

        this.selenium = selenium;

    }


    public void selectTab(String tabName){
        //переходим на нужную вкладку
        // tabName - имя вкладки

        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(zhkuPaymentPageTabs));
        selenium.findElement(zhkuPaymentPageTabs).findElement(By.linkText(tabName)).click();

    }


    @DataProvider
    //набор данных для проверки поля Код плательщика
    public static Object[][] dataCheckPayerCodeInputValidnost() {
        return new Object[][]{
                {"", "", "Поле обязательное"},
                {"123", "123", "Поле неправильно заполнено"},
                {"абракадабра", "", "Поле обязательное"},
                {"qwerty", "", "Поле обязательное"},
                {"123456789", "123456789", "Поле неправильно заполнено"},
                {"-12345", "12345", "Поле неправильно заполнено"},
                {"<alert>000</alert>", "000", "Поле неправильно заполнено"}
        };
    }

    @DataProvider
    //набор данных для проверки поля Период
    public static Object[][] dataCheckPeriodInputValidnost() {
        return new Object[][]{
                {"", "", "Поле обязательное"},
                {"123", "123", "Поле заполнено некорректно"},
                {"абракадабра", "", "Поле обязательное"},
                {"qwerty", "", "Поле обязательное"},
                {"421245", "421245", "Поле заполнено некорректно"},
                {"<alert>000</alert>", "000", "Поле заполнено некорректно"}
        };
    }


    @DataProvider
    //набор данных для проверки поля Сумма платежа
    public static Object[][] dataCheckCombinationInputValidnost() {
        return new Object[][]{
                {"1", "1", "Минимум — 10 \u20BD"},
                {"абракадабра", "", "Поле обязательное"},
                {"qwerty", "", "Поле обязательное"},
                {"15001", "15001", "Максимум — 15 000 \u20BD"}
        };
    }


    public void checkValidity(By elementInput, Byte positionInput, String text, String value, String messageText) {
        //проверяем поля
        // elementInput - поле, которое будем проверять
        // positionInput - порядковый номер поля, порядковый номер сообщения об ошибке
        // text - текст, который будем вводить в проверяемое поле
        // value - значение, которое ожидаем, после того, как текст введён в поле
        // messageText - текст сообщения об ошибке

        new WebDriverWait(selenium, 3).until(ExpectedConditions.presenceOfElementLocated(elementInput));
        selenium.findElement(elementInput).click();
        selenium.findElement(elementInput).clear();
        selenium.findElement(elementInput).sendKeys(text + Keys.ENTER);
        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(elementInput));
        Assert.assertEquals(3, selenium.findElements(errorMessageLabel).size());
        Assert.assertEquals("Неверное сообщение об ошибке", messageText, selenium.findElements(errorMessageLabel).get(positionInput).getText());

    }

}
