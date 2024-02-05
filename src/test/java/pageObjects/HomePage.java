package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage
{
	// CONSTRUCTOR:
	public HomePage(WebDriver driver)
	{
		super(driver);
	}

	//------------------------------------------------------------------------------------------------------------------

	// ELEMENTS:

	@FindBy(xpath = "//span[text()='My Account']")
	WebElement lnkMyaccount;

	@FindBy(linkText = "Register")
	WebElement lnkRegister;

	@FindBy(linkText = "Login")  // Login link added in Step-6.
	WebElement linkLogin;
	
	@FindBy(xpath = "//input[@placeholder='Search']")  // For Search Product Test.
	WebElement txtSearchbox;

	@FindBy(xpath = "//div[@id='search']//button[@type='button']")  // For Search Product Test.
	WebElement btnSearch;

	//------------------------------------------------------------------------------------------------------------------

	// ACTION METHODS:
	public void clickMyAccount()
	{
		lnkMyaccount.click();
	}

	public void clickRegister()
	{
		lnkRegister.click();
	}

	public void clickLogin()  // added in Step-6.
	{
		linkLogin.click();
	}
	
	public void enterProductName(String pName)  // For Search Product Test.
	{
		txtSearchbox.sendKeys(pName);
	}
	
	public void clickSearch()  // For Search Product Test.
	{
		btnSearch.click();
	}

}