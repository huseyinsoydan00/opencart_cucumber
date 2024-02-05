/*
-> Bu class, "pageObjects" içerisindeki tüm POM classlarına "extends" kalıtım keyword'ü ile implemente edilmiş durumda.
   Yani bu class bir nevi "pageObjects" içerisindeki tüm POM classların Base'i / BaseClass'ı.
*/

package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage
{
	WebDriver driver;

	// CONSTRUCTOR:
	public BasePage(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver,this);
	}

}