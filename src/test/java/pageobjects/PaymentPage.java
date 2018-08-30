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
    String[] regionsToSet   = new String[]{"�. ������", "�. �����-���������"};
    String[] regionsToCheck = new String[]{"������", "�����-����������"};
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
        //�������� �� ��� ����� ������� �������
        zhkhLink = By.xpath("//div[@data-qa-file='PaymentsCategories']/..//div[text()='" + paymentType + "']");

        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(zhkhLink));
        selenium.findElement(zhkhLink).click();

    }


    public void setRegion(String regionToSetName, String regionToCheckName){

        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(regionSelectLink));

        //���� �� ��� ������, �� ������ �� ���
        if (!selenium.findElement(regionSelectLink).getText().equals(regionToCheckName)) {

            //��������� �� �������� ������ �������
            selenium.findElement(regionSelectLink).click();

            //��� ��� ��������� ������� ������ �� ������, �������� �� ������
            regionSetLink = By.xpath("//div[@data-qa-file='UIPopupRegions']/..//span[text()='" + regionToSetName + "']");
            new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(regionSetLink));
            selenium.findElement(regionSetLink).click();

            new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(regionSelectLink));

        }
        //���������, ��� ���������� ��������� ������
        new WebDriverWait(selenium, 3).until(ExpectedConditions.textToBe(regionSelectLink, regionToCheckName));

    }


    public void checkFirstProvider(String firstProviderName){
        //��������� ������� ���������� �����
        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(providersListFirstItem));
        Assert.assertEquals(selenium.findElement(providersListFirstItem).getText(), firstProviderName);

    }


    public String zhkhLookingForName(){
        //��������� �������� ��������
        zhkhLookingForName = selenium.findElement(providersListFirstItem).getText();

        return zhkhLookingForName;

    }


    public void navigateToZhkhPage(String zhkhName){
        //��������� �� �������� ���������� �� ������ �����������
        selenium.findElement(providersListFirstItem).click();
        new WebDriverWait(selenium, 3).until(ExpectedConditions.titleContains(zhkhName));

    }


    public void searchZhkh(String request, Boolean pressEnter, Boolean checkPresence, Boolean click){

        //��������� ����� ��������� ����������
        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(searchInput));
        selenium.findElement(searchInput).sendKeys(request);

        if (pressEnter) {
            //�������� ENTER, ���� ���������
            selenium.findElement(searchInput).sendKeys(Keys.ENTER);

        }

        if (checkPresence) {
            //��������� ������� �������� � ���������� ������, ����������, ��� �� �� ������ �����
            new WebDriverWait(selenium, 3).until(ExpectedConditions.visibilityOfElementLocated(searchSuggestedDropdown));
            new WebDriverWait(selenium, 3).until(ExpectedConditions.textToBePresentInElement(searchSuggestedDropdownFirstElement, zhkhLookingForName));

        }

        if (click) {
            //������� �� ��������
            selenium.findElement(searchSuggestedDropdownFirstElement).click();
            new WebDriverWait(selenium, 3).until(ExpectedConditions.titleContains(request));

        }

    }


    public boolean elementIsNotPresent(String text){
        //��������� ���������� �������� � �������
        By xpath = By.xpath("//span[text()='" + text + "']");

        try {

            selenium.findElement(xpath);
            return false;

        } catch (NoSuchElementException e) {

            return true;

        }

    }

}
