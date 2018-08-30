package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by sidrom on 29.08.2018.
 */
public class CommonObjects {

    private WebDriver selenium;

    String pageTitle = "Tinkoff.ru: платежи и переводы денег";

    public CommonObjects(WebDriver selenium) {

        this.selenium = selenium;

    }


    public void clickMenuItem(String linkText){
        //клик по пункту меню
        // linkText - пункт

        new WebDriverWait(selenium, 3).until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
        selenium.findElement(By.linkText(linkText)).click();
        new WebDriverWait(selenium, 3).until(ExpectedConditions.titleIs(pageTitle));

    }

}
