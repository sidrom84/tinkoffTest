package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * Created by sidrom on 29.08.2018.
 */
public class PaymentPage {

    private WebDriver selenium;

    String paymentType;
    String[] regionsToSet   = new String[]{"г. Москва", "г. Санкт-Петербург"};
    String[] regionsToCheck = new String[]{"Москве", "Санкт-Петербурге"};
    Byte position = 0;
    String zhkhLookingForName = "";

    By zhkhLink = By.xpath("//div[@data-qa-file='PaymentsCategories']/..//div[text()='" + paymentType + "']");
    By regionSelectLink = By.xpath("//span[@data-qa-file='PaymentsCatalogHeader']/span[@data-qa-file='Link']");
    By regionSetLink = By.xpath("//div[@data-qa-file='UIPopupRegions']/..//span[text()='" + regionsToSet[position] + "']");
    By providersListFirstItem = By.xpath("//section[@data-qa-file='UILayoutSection']/..//li[1]");
    By searchInput = By.xpath("//input[@data-qa-file='SearchInput']");
    By searchSuggestedDropdown = By.xpath("//div[@data-qa-file='SearchSuggested']");
    By searchSuggestedDropdownFirstElement = By.xpath("//div[@data-qa-file='SearchSuggested']/..//div[@data-qa-node='Tag']");


    public PaymentPage(WebDriver selenium) {

        this.selenium = selenium;

    }


    public void clickPaymentType(String paymentType){
        //выбираем за что будем платить сегодня
        zhkhLink = By.xpath("//div[@data-qa-file='PaymentsCategories']/..//div[text()='" + paymentType + "']");

        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(zhkhLink));
        selenium.findElement(zhkhLink).click();

    }


    public void setRegion(String regionToSetName, String regionToCheckName){

        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(regionSelectLink));

        //если не тот регион, то меняем на тот
        if (!selenium.findElement(regionSelectLink).getText().equals(regionToCheckName)) {

            //переходим на страницу выбора Региона
            selenium.findElement(regionSelectLink).click();

            //так как требуется выбрать Регион из списка, выбираем из списка
            regionSetLink = By.xpath("//div[@data-qa-file='UIPopupRegions']/..//span[text()='" + regionToSetName + "']");
            new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(regionSetLink));
            selenium.findElement(regionSetLink).click();

            new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(regionSelectLink));

        }
        //проверяем, что установлен требуемый регион
        new WebDriverWait(selenium, 3).until(ExpectedConditions.textToBe(regionSelectLink, regionToCheckName));

    }


    public void checkFirstProvider(String firstProviderName){
        //проверяем первого Поставщика услуг
        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(providersListFirstItem));
        Assert.assertEquals(selenium.findElement(providersListFirstItem).getText(), firstProviderName);

    }


    public String zhkhLookingForName(){
        //сохраняем название искомого
        zhkhLookingForName = selenium.findElement(providersListFirstItem).getText();

        return zhkhLookingForName;

    }


    public void navigateToZhkhPage(String zhkhName){
        //переходим на страницу Поставшика из списка Поставшиков
        selenium.findElement(providersListFirstItem).click();
        new WebDriverWait(selenium, 3).until(ExpectedConditions.titleContains(zhkhName));

    }


    public void searchZhkh(String request, Boolean pressEnter, Boolean checkPresence, Boolean click){

        //выполняем поиск трубемого Поставщика
        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(searchInput));
        selenium.findElement(searchInput).sendKeys(request);

        if (pressEnter) {
            //нажимаем ENTER, если требуется
            selenium.findElement(searchInput).sendKeys(Keys.ENTER);

        }

        if (checkPresence) {
            //проверяем наличие искомого в выпадающем списке, убеждаемся, что он на первом месте
            new WebDriverWait(selenium, 3).until(ExpectedConditions.visibilityOfElementLocated(searchSuggestedDropdown));
            new WebDriverWait(selenium, 3).until(ExpectedConditions.textToBePresentInElement(searchSuggestedDropdownFirstElement, zhkhLookingForName));

        }

        if (click) {
            //кликаем по искомому
            selenium.findElement(searchSuggestedDropdownFirstElement).click();
            new WebDriverWait(selenium, 3).until(ExpectedConditions.titleContains(request));

        }

    }


    public boolean elementIsNotPresent(String text){
        //проверяем отсутствие элемента с искомым
        By xpath = By.xpath("//span[text()='" + text + "']");

        try {

            selenium.findElement(xpath);
            return false;

        } catch (NoSuchElementException e) {

            return true;

        }

    }

}
