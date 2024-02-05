/*
-> ********** WORK-FLOW **********

   !!! Bu class, "Login.feature" Feature file'ının StepDefinition'ı !!!
   !!! Ek olarak da "LoginDDTExcel.feature" Feature file'ındaki "Data-Driven Scenario Outline" senaryosundaki "THEN" step'ine karşılık gelen
   !!! metodu da en altta barındırıyor !!!

-> Buradaki metotlar (ya da Feature'lardaki stepler) çalışmaya başlamadan önce, her Scenario öncesinde çalışan
   "@Before" Hook'una sahip olan metot çalışır. O metot da "Hooks" classından gelir.
   Yani aşağıdaki metotlar çalışmaya başlamadan önce, "Hooks" classındaki "@Before" Hook'una sahip olan "setup()" metodu çalışır.
   Daha sonra buradaki metotlar sırayla çalışır ve her metot/step çalıştıktan sonra da "Hooks" classındaki "@AfterStep" Hook'una sahip olan
   "addScreenshot()" metodu çalışır.
   Tüm metotlar/stepler çalışmalarını tamamladıktan sonra da (yani bir Scenario tamamlandığında) "Hooks" classındaki "@After" Hook'una sahip olan
   "tearDown()" metodu çalışır.
   Olay bu.
*/

package stepDefinitions;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;

import org.openqa.selenium.WebDriver;

import factory.BaseClass;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;

import utilities.DataReader;

//----------------------------------------------------------------------------------------------------------------------

public class LoginSteps
{
    // OBJECTS:

    WebDriver driver;

    HomePage hp;
    LoginPage lp;
    MyAccountPage macc;

    List < HashMap<String,String> > datamap;
    /*
    -> Önce HashMap'i anlamak için Notlar'daki SS'e bak (Kasım Adalan-Java).
    -> Excel tablosundaki her bir hücrede yer alan String veriyi ("value") çekmek için,
       o hücredeki verinin ("value") yine String başlığı olan "username, password, res" ifadelerini birer "key" olarak kullanacağız.
       Her satır için String başlıklara/key'lere "get()" metodunu uygulayarak, ilgili satırdaki hücrede yer alan String veriyi ("value") çekmiş olacağız
       (HashMap'de key'lere "get()" metodu uygulanarak o key'e ait olan value çekiliyor).
       Bunu da bu classın en altındaki metotta yapıyoruz.
       Olay bu
       (yani String başlıkları birer "key" ve onlara ait olan hücrelerdeki String verileri de birer "value" olarak düşünebilirsin).
    -> Her satır, 1 tane HashMap nesnesine karşılık geliyor.
       Excel tablomuzda birden fazla satır olduğu için de bir "List of HashMap" nesnesine ihtiyacımız var.
       Bu nesneyi oluşturmamızın nedeni de bu.
       *****************************************************************************************************************
    -> Normalde Excel'deki verileri List Collection kullanmadan direkt olarak bir HashMap'e atabiliriz.
       Yani HashMap'i bir List Collection içine atmaya gerek yok.
       Ancak bu projedeki "LoginDDTExcel.feature"da yer alan Examples bloğunda sadece satır numaraları kullanılarak veri çekilmiş.
       HashMap'den de sadece satır numaraları kullanarak veri çekmek istersek bu durumda HashMap'i bir List Collection'a atmamız gerekiyor.
       Yani bu sayede HashMap nesnelerini (ki her bir HashMap nesnesi aslında Excel'deki bir satıra karşılık geliyor)
       bir List Collection'ın elemanları haline getiriyoruz ve bu sayede satır numaraları aracılığıyla ("row_index")
       HashMap'den veri çekebiliyoruz.
       Yani Excel'den HashMap'e verileri çektikten sonra HashMap'den verileri satır numarası kullanarak Feature'a çekmek istediğimizde
       HashMap'leri bir List Collection içerisine atmamız gerekli.
       Çünkü HashMap'den veri çekmek için "key" gerekli.
       İşte biz de burada o satır numaralarını ("LoginDDTExcel.feature"daki "row_index") "key" olarak kullanıyoruz.
       Bu "key"lerin karşılığındaki değerler de ("value") bir adet HashMap nesnesine, yani bir Excel satırına karşılık geliyor.
    -> List yapısını kullanmadan da Excel'den HashMap'e veri çekebiliriz.
       Ancak "key" olmadan HashMap'den veriyi nasıl çekeceğiz?
       İşte List kullandığımızda, her Excel satırı aslında 1 tane HashMap'e karşılık gelmiş oluyor.
       Sonra da satır numarası/index ("row_index") kullanarak HashMap'den veri çekebiliyoruz.
    -> Eğer "LoginDDTExcel.feature"da "row_index" kullanmasaydık, o zaman List Collection yapısına ihtiyacımız olmazdı.
       Bu durumda direkt olarak Excel'den HashMap'e verileri aktarabilirdik.
       HashMap'den de verileri "key" kullanarak çekebilirdik.
       Ancak bu durumda artık Excel'deki username'ler "key", password'ler de "value" olurdu ve biz "username" key'lerini kullanarak "password"leri çekerdik.
       Dediğim gibi, burada HashMap'leri bir List Collection yapısı içerisine atarak "LoginDDTExcel.feature"da "row_index" index numarası ile
       bu index'leri birer "key" ve karşılığındaki her bir HashMap nesnesini (yani her bir Excel satırını) birer "value" haline getirmiş olduk.
       ********** Yani satır numaralarını "KEY" olarak kullandık **********
    */

    //------------------------------------------------------------------------------------------------------------------

    @Given ("the user navigates to login page")  // "Login.feature"daki "Scenario" + "Scenario Outline" ile "LoginDDTExcel.feature"daki "Given" için.
    public void user_navigate_to_login_page()
    {
        BaseClass.getLogger().info("Goto my account-->Click on Login.. ");
        /*
        -> "BaseClass"daki "static getLogger()" metodu, "return logger" ile logger'ı döndürüyor (metottaki açıklamaya bak).
           Yani aslında "BaseClass.getLogger().info()" ifadesi, "BaseClass.logger.info()" ifadesiyle aynı anlama geliyor
           (bu Loglama olayını Hybrid Framework projesinde yapmıştık).
           Yani bu kod aslında bir "Logging" kodu, hepsi bu.
        */

        hp = new HomePage(BaseClass.getDriver());
        /*
        -> "HomePage" classına bakarsak "public HomePage(WebDriver driver)" CTOR'unun olduğunu görüyoruz.
           Yani HomePage CTOR'una bir "WebDriver driver" parametresi göndermeliyiz.
           Onun için burada parantez içinde parametre olarak "BaseClass.getDriver()" yapılmış.
           "BaseClass"daki "static getDriver()" metodu "return driver" ile driver nesnesi döndürüyor.
           Yani burada aslında "hp = new HomePage(driver)" yapmış oluyoruz ki zaten böyle yapmamız lazım.
           Ayrıca "HomePage" classındaki CTOR "super(driver)" kodu ile kendisine gönderilen bu driver nesnesini,
           kendisinin parent classı olan "BasePage" classındaki CTOR'a parametre olarak gönderiyor
           ("BasePage" classı, "pageObjects" package'ındaki tüm POM classlara "extends" ile inherite edilmiş; yani tüm POM classların parent'ı)
           ( "BasePage" classındaki CTOR => public BasePage(WebDriver driver) ).
           "BasePage" classındaki CTOR da "PageFactory.initElements()" işlemini yapıyor
           ("PageFactory" classı, POM Design Pattern'ı destekleyen bir Selenium classı).
        */
    	
    	hp.clickMyAccount();
    	hp.clickLogin();
        /*
        -> OpenCart'ın güncel versiyonunda "Login" ve "Register" seçenekleri anasayfada ayrı ayrı mevcut.
           Pavan ise burada OpenCart'ın eski versiyonuna göre bu 2 satırı yazmış ki onun için => "https://tutorialsninja.com/demo".
           Çünkü eski versiyonda anasayfada bir "MyAccount" butonu mevcut ve ona tıklandığında içerisinde "Login" mevcut.
           Yani önce anasayfadaki "MyAccount" butonuna tıklayıp, sonra da açılan seçeneklerden "Login" seçeneğine tıklamalıyız.
           Pavan da buna göre bu 2 satırı yazmış.
        */
    }

    @When ("user enters email as {string} and password as {string}")  // "Login.feature"daki "Scenario" + "Scenario Outline" için.
    public void user_enters_email_as_and_password_as (String email, String pwd)
    {
        BaseClass.getLogger().info("Entering email and password.. ");

        lp = new LoginPage(BaseClass.getDriver());

       	lp.setEmail(email);
        lp.setPassword(pwd);

        /*
        -> "String email" ve "String pwd" metot parametreleri, "Login.feature"daki parametrelerden gelecek:
           pavanoltraining@gmail.com - test@123
        */
    }

    @When ("the user clicks on the Login button")  // "Login.feature"daki "Scenario" + "Scenario Outline" için ("And").
    public void click_on_login_button()
    {
        lp.clickLogin();

        BaseClass.getLogger().info("clicked on login button...");
    }

    @Then ("the user should be redirected to the MyAccount Page")  // "Login.feature"daki "Scenario" + "Scenario Outline" için.
    public void user_navigates_to_my_account_page()
    {
        macc = new MyAccountPage(BaseClass.getDriver());
        // "MyAccountPage macc" Global class nesnesi.

        boolean targetpage = macc.isMyAccountPageExists();
        /*
        -> "isMyAccountPageExists()" metodu, "MyAccountPage" classında bulunuyor.
           Bu metot, Login işlemi sonrası gelen "My Account" sayfasındaki "My Account" yazan text elementinin görüntülenip görüntülenmediğini
           kontrol ediyor ve görüntüleniyorsa "true" (yani üyeliğimizle giriş işlemi başarılı şekilde yapılmıştır),
           görüntülenmiyorsa "false" (yani giriş işlemi başarısız) döndüren metot.
           Buradaki "boolean targetpage" değişkeni de bu bilgiyi tutuyor ve aşağıda da Assertion için kullanılmış.
        */

		Assert.assertEquals(targetpage, true);
        /*
        -> "targetpage" ve "true" ifadelerinin yer değiştirmesi gerekiyor (expected: true , actual: targetpage).
           Ama büyük ihtimalle zaten eşit oldukları için sorun olmayacak.
        -> Cucumber'da default olarak JUnit implemente halde olduğu için Assertion'lar da JUnit'den gelmeli.
           İlgili package'ı import ederken dikkat et => "import org.junit.Assert"
        */
    }

    //--------------------------------------------------------------------------

    // ********** Data-Driven Testing **********
    // "LoginDDTExcel.feature"daki "Scenario Outline"da "THEN" step'ine karşılık oluşturulan metot:
    @Then ("the user should be redirected to the MyAccount Page by passing email and password with excel row {string}")
    public void check_user_navigates_to_my_account_page_by_passing_email_and_password_with_excel_data (String rows)
    {
        datamap = DataReader.data(System.getProperty("user.dir")+"\\testData\\Opencart_LoginData.xlsx", "Sheet1");
        /*
        -> Bu metotta "Then" kısmının sonunda yer alan "{string}" ifadesi, "LoginDDTExcel.feature"daki "Then" step'inde yer alan
           "<row_index>" input/parametresine karşılık otomatik olarak tanımlanmış.
           Bu parametre aslında Excel'deki satır numaralarına karşılık geliyor.
           Bunun metot parametresi de (String rows) otomatik olarak oluşturuluyor.
           Yani aslında; {string} = (String rows) = "LoginDDTExcel.feature"daki "<row_index>".
        -> "DataReader" Utility classındaki "static data()" metodu 2 tane parametre alıyor => String filepath, String sheetName:
           public static List<HashMap <String,String>> data (String filepath, String sheetName).
           Yani mevcut projedeki Excel belgesinin location/path bilgisi ile Excel sheet/sayfa adı.
           Ve bu metot bir "List<HashMap <String,String>>" nesnesi döndürüyor ki buna karşılık olarak da yine aynı tipteki
           bu classın en başında Global class nesnesi olarak tanımladığımız "datamap" nesnesini yazdık.
        */

        int index = Integer.parseInt(rows)-1;  // "rows" => Excel satır numaralarına karşılık gelen "LoginDDTExcel.feature"daki "<row_index>" String değişkeni.
        /*
        -> "LoginDDTExcel.feature"daki "Examples" bloğunda "row_index" sırasıyla 1,2,3 değerlerini almış.
           Bunlar Excel satır numaraları oluyor ve "row_index"in ilk değeri 1 ile başlıyor.
           List Collection'da index no 0'dan başladığı için ve "datamap" de bir List Collection nesnesi olduğu için
           Excel'in 1. satırı, "datamap"in 0. index'ine karşılık gelir.
           Bundan dolayı "rows"u (yani "row_index"i) String'den Integer'a çevirdikten sonra değerini 1 azaltıyoruz ki
           Excel'in 1. satırı, "datamap" List Collection nesnesinin 0.index'ine gönderilsin.
           Daha sonra da ilk iterasyonda bu "index = 1-1 = 0" değerini kullanarak bir alttaki kod bloğunda Excel'deki ilk satır verilerini çekiyoruz.
        */

        // Excel'deki her bir satır için (ilk iterasyonda "index" = 1-1 = 0 olmuştu):
        String email = datamap.get(index).get("username");
        String pwd = datamap.get(index).get("password");
        String exp_res = datamap.get(index).get("res");
        /*
        -> Bu kodları Excel'deki 1.satır ("datamap"in 0.index'i) için düşünürsek;
           1. satırdaki "username" header/key'inin value'su çekilip (1.satır - 1.sütun), "String email"   değişkenine atılacak.
           1. satırdaki "password" header/key'inin value'su çekilip (1.satır - 2.sütun), "String pwd"     değişkenine atılacak.
           1. satırdaki "res"      header/key'inin value'su çekilip (1.satır - 3.sütun), "String exp_res" değişkenine atılacak.
        */

        lp = new LoginPage(BaseClass.getDriver());
        lp.setEmail(email);
        lp.setPassword(pwd);
        lp.clickLogin();
        /*
        -> "LoginPage" POM classında "setEmail(String email)" ve "setPassword(String pwd)" metotları mevcut.
           Bu metotlar kendisine gönderilen parametreleri, OpenCart sitesinde Login sayfasındaki email-password
           alanlarına "sendKeys()" metodu ile gönderiyor.
           Burada bu metot parametreleri de, bir üstteki kod sayesinde Excel tablosundan çekilen verilerden geliyor.
           Yani birden fazla veri ile Data-Driven Testing yapılıyor ve Excel tablosundaki email-password çiftleri otomatik olarak çekilerek
           OpenCart sitesinde Login sayfasındaki email-password alanlarına gönderiliyor ve ardından "Login" butonuna tıklama işlemi yapılıyor.
        */

        //****************************************************

        /*
        -> Bu olay Hybrid Framework projesinde daha temiz bir şekilde kodlanmış.
           İleride projelerimde böyle bir yapı kullanırsam bu yapıya buradan değil, Hybrid Framework projesinden bak.
        */

        macc = new MyAccountPage(BaseClass.getDriver());
        // Hybrid Framework'de böyle yapmışız => MyAccountPage myAccountPage = new MyAccountPage(driver);

        try
        {
            boolean targetpage = macc.isMyAccountPageExists();
            /*
            -> "isMyAccountPageExists()" metodu, "MyAccountPage" POM classında bulunan bir metot.
               Bu metodun görevi, OpenCart sitesinde başarılı bir Login işlemi sonrası gelen "My Account / Hesabım" sayfasında bulunan
               "msgHeading" ismiyle tanımlanan elementin görüntülenip görüntülenmeme durumuna göre Login işleminin başarılı ya da başarısız
               olduğunu kontrol etmek
               ("msgHeading" elementi de aynı POM classında).
               Yani buradan ya "true" ya da "false" gelir ve bu boolean değer "targetpage" değişkeninde tutulur.
               Amacımız, Excel'deki son sütun olan "res / exp_res / Expected Result" sütunundan çekilen değerin Valid/Invalid
               olma durumu ile "targetpage" boolean değerinin true/false olma durumunu birlikte karşılaştırmak.
               Şöyle ki;
               1) Eğer Excel'den çekilen "Expected Result" VALID ise ve "targetpage" değeri "TRUE" gelmişse ki bu
                  başarılı bir Login işlemi yapıldığını gösterir, bu durumda VALID email-password bilgileriyle BAŞARILI bir Login işlemi yapılmıştır
                  ve bu istediğimiz bir sonuçtur. Bu durumda testi "PASSED" yaparız.
               2) Eğer Excel'den çekilen "Expected Result" VALID ise ve "targetpage" değeri "FALSE" gelmişse ki bu
                  başarısız bir Login işlemi yapıldığını gösterir, bu durumda VALID email-password bilgileriyle BAŞARISIZ bir Login işlemi yapılmıştır
                  ve bu istemediğimiz bir sonuçtur. Bu durumda testi "FAILED" yaparız.
               3) Eğer Excel'den çekilen "Expected Result" INVALID ise ve "targetpage" değeri "TRUE" gelmişse ki bu
                  başarılı bir Login işlemi yapıldığını gösterir, bu durumda INVALID email-password bilgileriyle BAŞARILI bir Login işlemi yapılmıştır
                  ve bu istemediğimiz bir sonuçtur. Bu durumda testi "FAILED" yaparız.
               4) Eğer Excel'den çekilen "Expected Result" INVALID ise ve "targetpage" değeri "FALSE" gelmişse ki bu
                  başarısız bir Login işlemi yapıldığını gösterir, bu durumda INVALID email-password bilgileriyle BAŞARISIZ bir Login işlemi yapılmıştır
                  ve bu istediğimiz bir sonuçtur. Bu durumda testi "PASSED" yaparız.
            */

            System.out.println("target page: "+ targetpage);

            /*
            -> 1. DURUM:
               Excel'deki 3.sütun olan "res = Expected Result" verisi "Valid" olduğunda Login işleminin gerçekleşip (targetPage = true)
               gerçekleşmediğine (else | targetPage = false) göre testi PASSED(true) ya da FAILED(false) yapma:
            */
            if (exp_res.equals("Valid"))  // Excel'den çekilen "res / exp_res / Expected Result" sütunundaki veri "Valid" ise...
            {
                if (targetpage == true)  // Login işlemi başarılı ise...
                {
                    MyAccountPage myaccpage = new MyAccountPage(BaseClass.getDriver());
                    /*
                    -> Bu kodu neden yazdığını anlamadım; çünkü zaten TRY bloğunun başında bir MyAccountPage instance'ı oluşturmuş.
                       Zaten Hybrid Framework projesinde de böyle birşey yapmamışız.
                    */

                    myaccpage.clickLogout();
                    Assert.assertTrue(true);
                }
                else  // Login işlemi başarısız ise...
                {
                    Assert.assertTrue(false);
                }
            }

            /*
            -> 2. DURUM:
               Excel'deki 3.sütun olan "res = Expected Result" verisi "Invalid" olduğunda Login işleminin gerçekleşip (targetPage = true)
               gerçekleşmediğine (else | targetPage = false) göre testi PASSED(true) ya da FAILED(false) yapma:
            */
            if (exp_res.equals("Invalid"))
            {
                if (targetpage == true)  // Login işlemi başarılı ise...
                {
                    macc.clickLogout();
                    Assert.assertTrue(false);
                }
                else  // Login işlemi başarısız ise...
                {
                    Assert.assertTrue(true);
                }
            }
        }  // TRY bloğu.

        catch (Exception e)
        {
            Assert.assertTrue(false);
            // Alternatif olarak => Assert.fail();
        }

    }  // public void check_user_navigates_to_my_account_page_by_passing_email_and_password_with_excel_data (String rows)

}  // public class LoginSteps