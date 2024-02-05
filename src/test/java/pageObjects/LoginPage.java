package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage
{
	// CONSTRUCTOR:
	public LoginPage(WebDriver driver)
	{
		super(driver);
	}

	//------------------------------------------------------------------------------------------------------------------

	// ELEMENTS:

	@FindBy(xpath = "//input[@id='input-email']")
	WebElement txtEmailAddress;

	@FindBy(xpath = "//input[@id='input-password']")
	WebElement txtPassword;

	@FindBy(xpath = "//input[@value='Login']")
	WebElement btnLogin;

	//------------------------------------------------------------------------------------------------------------------

	// ACTIONS METHODS:

	public void setEmail(String email)
	{
		txtEmailAddress.sendKeys(email);
	}

	public void setPassword(String pwd)
	{
		txtPassword.sendKeys(pwd);
	}

	public void clickLogin()
	{
		btnLogin.click();
	}

}