package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by sidrom on 29.08.2018.
 */
public class ProviderPage {

    private WebDriver selenium;

    By zhkuPaymentPageTabs = By.xpath("//div[@data-qa-file='SubscriptionProvider']/..//div[@data-qa-file='Tabs']");


    public ProviderPage(WebDriver selenium) {

        this.selenium = selenium;

    }


    public void selectTab(String tabName){
        //переходим нанужную вкладку
        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(zhkuPaymentPageTabs));
        selenium.findElement(zhkuPaymentPageTabs).findElement(By.linkText(tabName)).click();

    }

}
