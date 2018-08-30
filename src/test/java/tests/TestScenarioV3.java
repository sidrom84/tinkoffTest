package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import pageobjects.CommonObjects;
import pageobjects.PaymentPage;
import pageobjects.ProviderPage;


/**
 * Created by sidrom on 29.08.2018.
 */
public class TestScenarioV3 {

    private WebDriver selenium;

    String chromeBinary = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
    String chromeDriver = "D:\\dev\\ChromeDriver\\chromedriver.exe";

    private CommonObjects commonObjects;
    private PaymentPage paymentPage;
    private ProviderPage providerPage;

    String baseURL          = "https://www.tinkoff.ru/";
    String[] regionsToSet   = new String[]{"г. Москва", "г. Санкт-Петербург"};
    String[] regionsToCheck = new String[]{"Москве", "Санкт-Петербурге"};

    String menuItem         = "Платежи";
    String paymentType      = "ЖКХ";
    String zhkhName         = "ЖКУ-Москва";
    String zhkhLookingForName = "";
    String[] zhkhPaymentTitleContainer = new String[]{"Узнайте задолженность по ЖКУ в Москве", "Оплатите ЖКУ в Москве без комиссии"};
    String zhkuPaymentTabName = "ОПЛАТИТЬ ЖКУ В МОСКВЕ";

    Byte position = 0;


    @BeforeSuite
    public void setUp() throws InterruptedException {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(chromeBinary);
        System.setProperty("webdriver.chrome.driver", chromeDriver);

        selenium = new ChromeDriver();

        commonObjects = new CommonObjects(selenium);
        paymentPage = new PaymentPage(selenium);
        providerPage = new ProviderPage(selenium);

//        1 Переходом по адресу http://///..tink.ff/ загрузить стартовую страницу Titink ati.
        System.out.println("Переходим на главную страницу");
        selenium.navigate().to(baseURL);
        new WebDriverWait(selenium, 3).until(ExpectedConditions.titleIs("Кредит наличными и кредитные карты онлайн"));
        selenium.manage().window().maximize();

//        2 Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
        System.out.println("Нажимаем на пункт меню Платежи");
        commonObjects.clickMenuItem(menuItem);

//        3 В списке категорий платежей, нажатием на пункт “Коммунальные платежи“, перейти на страницу выбора поставщиков услуг.
        System.out.println("Выбираем тип Платежа");
        paymentPage.clickPaymentType(paymentType);

//        4 Убедиться, что текущий регион – “г. Москва” (в противном случае выбрать регион “г. Москва” из списка регионов).
        System.out.println("Выбираем требуемый Регион, если он уже не установлен");
        paymentPage.setRegion(regionsToSet[position], regionsToCheck[position]);

//        5 Со страницы выбора поставщиков услуг, выбрать 1-ый из списка (Должен быть “ЖКУ-Москва”). Сохранить его наименование (далее “искомый”) и нажатием на соответствующий элемент перейти на страницу оплаты “ЖКУ-Москва“.
        System.out.println("Выбираем требуемый Провайдера, первого из списка, проверяем");
        paymentPage.checkFirstProvider(zhkhName);

        //сохраняем Наименование
        zhkhLookingForName = paymentPage.zhkhLookingForName();

        //переходим на страницу "ЖКУ-Москва"
        paymentPage.navigateToZhkhPage(zhkhName);

//        6 На странице оплаты, перейти на вкладку “Оплатить ЖКУ в Москве“.
        System.out.println("Переходим на страницу Оплаты, выбираем требуемую вкладку");
        providerPage.selectTab(zhkuPaymentTabName);

    }


//        7 Выполнить проверки на невалидные значения для обязательных полей: проверить все текстовые сообщения об ошибке (и их содержимое), которые появляются под соответствующим полем ввода в результате ввода некорректных данных.
    //обязательных полей три, каждое поле проверяется отдельно
    @Test(dataProviderClass = ProviderPage.class, dataProvider = "dataCheckPayerCodeInputValidnost")
    public void testCheckPayerCodeInputValidnost(String text, String value, String messageText) {
        //проверяем поле Код
        System.out.println("Проверяем поле Код плательщика");

        By elementInput = providerPage.payerCodeInput;
        Byte positionInput = 0;

        providerPage.checkValidity(elementInput, positionInput, text, value, messageText);

    }


    @Test(dataProviderClass = ProviderPage.class, dataProvider = "dataCheckPeriodInputValidnost")
    public void testCheckPeriodInputValidnost(String text, String value, String messageText) {
        //проверяем поле Период
        System.out.println("Проверяем поле Период");

        By elementInput = providerPage.periodInput;
        Byte positionInput = 1;

        providerPage.checkValidity(elementInput, positionInput, text, value, messageText);

    }


    @Test(dataProviderClass = ProviderPage.class, dataProvider = "dataCheckCombinationInputValidnost")
    public void testCheckCombinationInputValidnost(String text, String value, String messageText) throws InterruptedException {
        //проверяем поле Сумма
        System.out.println("Проверяем поле Сумма платежа");

        By elementInput = providerPage.combinationInput;
        Byte positionInput = 2;

        providerPage.checkValidity(elementInput, positionInput, text, value, messageText);

    }


    @Test(dependsOnMethods = {
            "testCheckPayerCodeInputValidnost",
            "testCheckPeriodInputValidnost",
            "testCheckCombinationInputValidnost"})
    public void testCheckSearchResults(){
        System.out.println("Проверяем поиск по Поставщику и результаты поиска");

//        8 Повторить шаг (2).
//        2 Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
        commonObjects.clickMenuItem(menuItem);

//        9 В строке быстрого поиска поставщика услуг ввести наименование искомого (ранее сохраненного).
//        10 Убедиться, что в списке предложенных провайдеров искомый поставщик первый.
//        11 Нажатием на элемент, соответствующий искомому, перейти на страницу “Оплатить ЖКУ в Москве“. Убедиться, что загруженная страница та же, что и страница, загруженная в результате шага (5).
        paymentPage.searchZhkh(zhkhLookingForName, false, true, true);

    }


    @Test(dependsOnMethods = {"testCheckSearchResults"})
    public void testCheckInAnotherRegion(){
        System.out.println("Проверяем отсутствие искомого Поставщика при смене Региона");
//        12 Выполнить шаги (2) и (3).
//        2 Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
//        3 В списке категорий платежей, нажатием на пункт “Коммунальные платежи“, перейти на страницу выбора поставщиков услуг.
        commonObjects.clickMenuItem(menuItem);

        paymentPage.clickPaymentType(paymentType);

//        13 В списке регионов выбрать “г. Санкт-Петербург”.
        position = 1;
        paymentPage.setRegion(regionsToSet[position], regionsToCheck[position]);

//        14 Убедится, что в списке поставщиков на странице выбора поставщиков услуг отсутствует искомый.
        Assert.assertTrue(paymentPage.elementIsNotPresent(zhkhLookingForName));

    }


    @AfterSuite
    public void tearDown(){
        //убираем за собой
        selenium.quit();

    }

}
