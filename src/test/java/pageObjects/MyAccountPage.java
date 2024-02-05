package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends BasePage
{
	// CONSTRUCTOR:

	public MyAccountPage(WebDriver driver)
	{
		super(driver);
	}

	//------------------------------------------------------------------------------------------------------------------

	// ELEMENTS:

	@FindBy(xpath = "//h2[text()='My Account']")
	WebElement msgHeading;
	// Login işlemi sonrası gelen "My Account" sayfasındaki "My Account" yazan text elementi.
	
	@FindBy(xpath = "//div[@class='list-group']//a[text()='Logout']")
	WebElement lnkLogout;

	//------------------------------------------------------------------------------------------------------------------

	// ACTION METHODS:

	public boolean isMyAccountPageExists()
	{
		try
		{
			return (msgHeading.isDisplayed());  // TRY bloğu çalıştığı takdirde bu kod "boolean true" döndürür.
			/*
            -> Tanımladığımız "msgHeading" elementi, başarılı Login işlemi sonrası gelen "MyAccount / Hesabım" sayfasında bulunan bir element.
               Ve bu element görüntüleniyorsa başarılı bir Login işlemi yapılmıştır.
               Yani bu TRY bloğu çalışırsa başarılı bir Login işlemi gerçekleşmiştir.
            */
		}
		catch (Exception e)
		{
			return (false);  // TRY bloğu çalışmazsa CATCH bloğu çalışır ve bu kod da "boolean false" döndürür.
			/*
            -> TRY bloğunda "msgHeading" elementi görüntülenemezse, ya başarılı bir Login işlemi gerçekleşmemiştir ya da başka bir hata olmuştur.
               Bu durumda programın Exception fırlatmaması için metot tipi olan "boolean" tipindeki "false" değerini döndürüyoruz.
            */
		}
	}

	public void clickLogout()
	{
		lnkLogout.click();
	}
	
}