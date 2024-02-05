/*
********** Bu class, "Registration.feature"ın StepDefinition classı **********
*/

package stepDefinitions;


import java.util.Map;

import org.junit.Assert;

import org.openqa.selenium.WebDriver;

import factory.BaseClass;

import io.cucumber.datatable.DataTable;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;

//----------------------------------------------------------------------------------------------------------------------

public class RegistrationSteps
{
	// OBJECTS:

	WebDriver driver;


	HomePage hp;

	LoginPage lp;

	AccountRegistrationPage regpage;

	//------------------------------------------------------------------------------------------------------------------
     
	@Given ("the user navigates to Register Account page")
	public void user_navigates_to_register_account_page()
	{
		hp = new HomePage(BaseClass.getDriver());  // BaseClass.getDriver(); => return driver;
		// HomePage hp = new HomePage(driver);

    	hp.clickMyAccount();
        hp.clickRegister();
	}

	@When ("the user enters the details into below fields")
	public void user_enters_the_details_into_below_fields (DataTable dataTable)
	{
		/*
		!!!!!!!!!! Java'daki "HashMap" ne ise, bunun Cucumber'daki karşılığı da "DataTable" !!!!!!!!!!
		-> "DataTable", Cucumber'a ait bir class.
		   Ve adından da anlaşılacağı üzere "DataTable" aslında bir tür tablodur.
		   Bir Feature dosyasında tek bir step'de birden fazla data belirteceğimiz zaman, Cucumber'a ait olan bu yapıyı kullanıyoruz
		   ("Registration.feature"daki "When" step'i için).
		-> Buradaki "DataTable dataTable" metot parametresi, "Registration.feature"daki "When" step'ine ait dataların tamamını tutan tek bir parametre (tablo).
		-> Bu olayla ilgili Cucumber dökümanı için ilgili linki Chrome'da "Yazılım-Cucumber" klasörüne kaydettim:
		   "https://www.baeldung.com/cucumber-data-tables"
		   Linkte "List of Maps" başlığına bakacaksın.
		   Pavan da bu kodları muhtemelen oradan çekmiştir.
		*/

		Map <String,String> dataMap = dataTable.asMap(String.class, String.class);
		/*
		-> "dataTable" nesnesi, "Registration.feature"daki "When" step'inde bulunan dataların tamamını tutan bir tablo.
		   Bu tablodan verileri direkt olarak okuyamıyoruz ("DataTable" tipinden direkt olarak veri okuma yapılamıyor).
		   Bunun için burada, o verileri tutan "dataTable" nesnesini HashMap'e çevirme işlemini yapıyoruz ("asMap()" metodu ile).
		   "asMap()" metodu da "DataTable" Cucumber classına ait bir metot ve bu metot 2 tane parametre alıyor => (Class keyType, Class valueType).
		   Dikkat edersen ilgili Feature'daki ilgili step'de bulunan verilerin hem "key" hem de "value" değerleri String tipinde:
		   firstName John | lastName Kenedy | telephone 1234567890 | password test@123
		   Yani şu anda "Registration.feature"daki "When" step'inden datalar çekilmiş ve "DataTable dataTable" metot parametresine gönderilmiş
		   (zaten bu metotta hangi metot parametresini belirtirsen otomatik olarak When step'indeki datalar buradaki metot parametresine gönderilmiş oluyor)
		   (çünkü buradaki "DataTable dataTable" metot parametresi, bu metodun step'i olan When step'indeki parametrelere karşılık otomatik olarak oluşturuluyor);
		   ardından da When step'indeki dataları tutan "dataTable" nesnesinin tipi, verilerin okunabilmesi için "DataTable"dan "HashMap"e çevrilmiş durumda:
		   !!! "dataTable" => "dataMap" dönüşümü !!!
		-> Java'da Collection'lar => List, Set ve Map.
		   HashMap de aslında bir Map Collection.
		   Notlar'daki "Java Collections" SS'ine bakabilirsin.
		*/

		regpage = new AccountRegistrationPage(BaseClass.getDriver());  // BaseClass.getDriver(); => return driver;
		// AccountRegistrationPage regpage = new AccountRegistrationPage(driver);

		regpage.setFirstName(dataMap.get("firstName"));                   // regpage.setFirstName("John");
		regpage.setLastName(dataMap.get("lastName"));                     // regpage.setLastName("Kenedy");
		regpage.setEmail(BaseClass.randomAlphaNumeric() + "@gmail.com");  // regpage.setEmail("abcd123@gmail.com");  |  "abcd123", rastgele üretilen bir değer.
		regpage.setTelephone(dataMap.get("telephone"));                   // regpage.setTelephone("1234567890");
		regpage.setPassword(dataMap.get("password"));                     // regpage.setPassword("test@123");
		regpage.setConfirmPassword(dataMap.get("password"));              // regpage.setPassword("test@123");
		/*
		-> "OpenCart" sitesinin eski versiyonu olan "TutorialsNinja" sitesindeki "Register" sayfasına bakarsan, sırasıyla şu InputBox alanları mevcut:
		   firstName | lastName | eMail | telephone | password | passwordConfirm.
		   Bu sıralamaya Notlar'daki SS'den de bakabilirsin.
		   Burada da "DataTable dataTable" metot parametresini belirttiğimizde "Registration.feature"daki When step'inde bulunan tüm datalar (key-value)
		   bu metot parametresine otomatik olarak çekiliyor (ki bu zaten Cucumber'ın default bir özelliği), daha sonra da bu nesne içerisindeki verilerin
		   okunabilmesi/çekilebilmesi için "DataTable dataTable", "Map dataMap" nesnesine dönüştürülüyor.
		   Sonra da burada, "Registration.feature"daki When step'inde yer alan key-value datalarını tutan "Map dataMap" nesnesine
		   "get("key")" metodu uygulanarak ilgili step'deki key'ler kullanılarak her bir key'e karşılık gelen value'lar çekilip
		   "AccountRegistrationPage" POM classındaki ilgili metotlara parametre olarak gönderiliyor ki o metotlar da kendilerine gönderilen
		   bu parametreleri/value'ları alıp OpenCart/TutorialsNinja sitesindeki kayıt sayfasında yer alan InputBox'lara "sendKeys()" metodu ile gönderiyorlar.
		   Yani sırasıyla;
		   1) "Registration.feature"daki When step'inde yer alan datalar burada metot parametresi olan "DataTable dataTable" nesnesine gönderildi.
		   2) Bu datalar (key-value) DataTable tipinde olduğunda okunamadığı için bunlar "Map dataMap" nesnesine dönüştürüldü.
		   3) Bu Map nesnesine "get("key")" metodu uygulanarak ilgili Feature'daki "When" step'inde yer alan key'lere karşılık gelen value'lar çekilerek
		      ilgili POM classdaki ActionMethod'lara metot parametresi olarak gönderildi.
		      Bu metotlar da kendilerine gönderilen bu parametreleri (value'ları) alıp kayıt sayfasındaki InputBox'lara ("sendKeys()" metodu ile) gönderdiler.
		-> Burada tek farklı olan şey, Email kısmı.
		   Email, "BaseClass"daki rastgele değer üreten "randomAlphaNumeric()" metodundan gelecek.
		   Bu metot, rakam-harf karışık olarak rastgele bir değer üretiyor ve ürettiği bu değeri "return str + num" ile döndürüyor.
		   Pavan da burada, rastgele üretilen bu değerin sonuna "+ @gmail.com" ifadesini koymuş ki üretilen değerin sonuna bu ifade eklensin
		   ve ifade bir Email formatını alsın. Örneğin; "abcd123@gmail.com" gibi.
		   Daha sonra da bu değer "regpage.setEmail()" ile ilgili POM classdaki ilgili metoda ( "setEmail(String email)" ) bir parametre olarak gönderilecek
		   ve "setEmail()" metodu da OpenCart/TutorialsNinja sitesindeki kayıt sayfasında yer alan Email alanına bu Email'i "sendKeys()" metodu ile gönderecek.
		   !!!!!!!!!! BÜYÜK RESİM BU !!!!!!!!!!
		*/
	}

	@When ("the user selects Privacy Policy")
	public void user_selects_privacy_policy()
	{
		regpage.setPrivacyPolicy();
	}

	@When ("the user clicks on Continue button")
	public void user_clicks_on_continue_button()
	{
		regpage.clickContinue();
	}

	@Then ("the user account should get created successfully")
	public void user_account_should_get_created_successfully()
	{
		String confmsg = regpage.getConfirmationMsg();
		/*
		-> "getConfirmationMsg()" metodu, "msgConfirmation = Your Account Has Been Created!" elementini döndüren bir metot (TRY bloğunda).
		   İlgili mesaj, başarılı bir kayıt işlemi sonrası gelen sayfada bulunan bir text elementi.
		   Yani kayıt işlemi başarılı ise bu element return ile döndürülebilir ki zaten bu olay POM classında TRY-CATCH bloğuna alınmış.
		   Yani eğer kayıt işlemi başarısız olursa bu element döndürülemez ve CATCH bloğuna geçilir ki bu sayede program Exception fırlatmaz.
		   Yani burada "regpage.getConfirmationMsg()" metodu, ya "return (msgConfirmation.getText())" döndürür ki bu durumda
		   "msgConfirmation" elementi "Your Account Has Been Created!" text'ini tuttuğu için eğer bu ifade dönerse kayıt işlemi başarılı demektir,
		   bu durumda "msgConfirmation = confmsg = Your Account Has Been Created!" olur.
		   Ya da ilgili POM classdaki ilgili metottan CATCH bloğundaki "return (e.getMessage())" kodu döner ki bu da "msgConfirmation"
		   elementinin döndürülemediği anlamına gelir, yani kayıt işlemi başarısız olmuştur.
		   Bu durumda da "msgConfirmation = confmsg = Your Account Has Been Created!" ifadesi DÖNMEZ !!!
		   Aşağıda da bunun karşılaştırması (Validation) yapılmış.
		*/

		Assert.assertEquals(confmsg, "Your Account Has Been Created!");
		/*
		-> Buradaki Assertion, JUnit'den geliyor; TestNG'den değil (Cucumber'a default olarak JUnit implemente edilmiş halde idi!!!).
		-> Burada "gerçek zamanlı olarak" gerçekleşen şey, yani "actual" olan şey "confmsg".
		   Beklenen ("expected") ise, bu "actual" ifadenin "Your Account Has Been Created!" ifadesine eşit olması.
		   Yani ifadeler ters yazılmış; şöyle olması gerekirdi:
		   Assert.assertEquals(expected: "Your Account Has Been Created!", actual: confmsg);
		   Ama ifadeler zaten ya "eşit" ya da "eşit değil" şeklinde olacağı için herhalde ters yazıldığında da bir sorun olmaz.
		*/
	}

}