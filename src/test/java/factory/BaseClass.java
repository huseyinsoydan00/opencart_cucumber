/*
-> ************************************************ BU CLASS ÇOK ÖNEMLİ ************************************************

!!! Pavan 52. derste (sondan bir önceki ders), bu classda "Selenium Grid configuration" işlemlerinin yapıldığını söyledi.
    İnternetten Selenium Grid'in ne olduğuna bakarsan, kısaca şu yazıyor:
    "Selenium Grid, yazılan testlerin farklı sunucular üzerinde, farklı tarayıcılar ve işletim sistemlerinde eşzamanlı (paralel olarak)
    koşulmasını sağlayan araçtır. Özellikle çok fazla test senaryosu içeren büyük sistemlerde zaman ve kaynak tasarrufu sağlamak adına
    bu araç sıklıkla tercih edilir."
!!! Ayrıca projedeki "config.properties" de Selenium Grid olayı için oluşturulmuş bir dosya.
    Çünkü o dosya içerisinde de "execution environment = local/remote", browser, işletim sistemi, projede testi yapılan websitesi (OpenCart/TutorialsNinja),
    email, password vb. bilgiler belirtilmiş.
    Zaten Selenium Grid için de browser, işletim sistemi vb. şeyler yazıyor.

-> This class is basically required for initializing the browser | "initialize" = başlatmak

-> Bu class içerisinde Driver, Browser, BrowserName, OperatingSystem vb. ayarlamaları yapıyoruz => "initilizeBrowser()"
   Ayrıca kullanacağımız reusable metotları da bu class içerisinde tanımlıyoruz (RandomString, RandomNumber vs).

-> Bu class içerisindeki tüm metotlar ve nesneler/değişkenler "static" (ANCAK CLASS'ın KENDİSİ "STATIC" DEĞİL !!!!!!!!).
   Bu sayede ilgili static metoda/nesneye/değişkene, sadece class adıyla erişebiliyoruz.
   Hatırlarsan static olmayan metotlara, değişkenlere vs erişmek için, önce bulundukları classdan bir nesne türetiyorduk:
   Örneğin; "BaseClass baseClass = new BaseClass;
   static keyword'ü ise bu işlemi yapmadan direkt olarak ilgili metoda, değişkene vs bulunduğu class adıyla erişebilmemizi sağlıyordu.

-> Benim anladığım şey şu:
   "Hooks" classına bakarsan Scenario öncesi-sonrası ile ("@Before", "@After") Feature'daki Stepler öncesi-sonrası çalışan
   ("@BeforeStep", "@AfterStep") metotlar oluşturulmuş (Pavan "@BeforeStep" Hook'una gerek duymadığı için kullanmadığını söyledi).
   Ordaki metotlardan "@Before setup()" metodu çalıştığında (ki her Scenario öncesi bu Hook'a sahip olan metot 1 kere çalışır),
   "BaseClass" classındaki "initilizeBrowser()" metodunun çalışması tetikleniyor.
   Çünkü "setup()" metodunda "driver = BaseClass.initilizeBrowser()" kodu mevcut.
   "initilizeBrowser()" metodu da bir "WebDriver driver" nesnesi döndürüyor
   (hatta döndürülen bu driver nesnesini belki başka yerlerde de kullanırız diye, görevi sadece bu nesneyi döndürmek olan
   "getDriver()" metodu da BaseClass'da oluşturulmuş).
   Yani Feature'daki bir Scenario çalışmaya başlamadan önce "Hooks" classındaki "@Before" Hook'una sahip olan "setup()" metodu çalışacak
   ve bu metot da browser'ı açabilmek için "BaseClass.initilizeBrowser()" kodu ile "initilizeBrowser()" metodunu tetikleyecek ve buradan da
   bir "driver" döndürüldüğü için gene "setup()" metodunda bu driver kullanılarak "driver.get("URL")" ve "driver.manage().window().maximize()"
   işlemleri yapılacak.
   Yani söylemeye çalıştığım şey, aşağıdaki "initilizeBrowser()" metodu sadece browser'ın (farklı bir yolla) başlatılması için oluşturulan bir metot.
   Bu metodu hiç kullanmayıp her zamanki geleneksel yoldan da browser'ı başlatabilirdik.

-> Bu "BaseClass" Pavan tarafından hazır olarak paylaşıldığı için anlayabildiğim kadarını anlamaya çalışıyorum.
   İleride bu classı kendi projelerimde Cucumber yaklaşımı için copy-paste yaparak kullanabilirim.
*/

package factory;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

//----------------------------------------------------------------------------------------------------------------------

public class BaseClass  // Bu class static değil; fakat içerisindeki tüm metotlar, nesneler, değişkenler vs "STATIC" !!!
{
	// OBJECTS:
	static WebDriver driver;  // "WebDriver", Selenium'a ait bir INTERFACE (hadi canım :D).
	static Properties p;  // "Properties", "java.util" package'ına ait olan bir Java classı.
	static Logger logger;  // "Logger", "org.apache.logging.log4j" package'ına ait olan bir INTERFACE !!!

	//----------------------------------------------------------------------------

    /*
	-> Aşağıdaki "initilizeBrowser()" metodu, bir nevi Main Metot.
	   Bu metot browser'ın başlatılmasından (launch) ve OpenCart app'inin URL'inden (sitenin açılmasından) sorumlu:
	*/
	public static WebDriver initilizeBrowser() throws IOException  // Bu metot, WebDriver nesnesi döndüren bir metot => "return driver".
	{
		// "execution_env = remote" demek, execution'ın (projenin çalıştırılması) Selenium Grid ortamında gerçekleşeceği anlamına geliyormuş...
		// Yani bu blok, Selenium Grid configuration/ayarları için...
		if (getProperties().getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			DesiredCapabilities capabilities = new DesiredCapabilities();
			/*
			-> "getProperties()" metodu, projemizdeki "config.properties" Properties classını döndürüyor.
			   "getProperty()" metodu da, bir key'in/parametrenin sahip olduğu value'yu çekmek için kullanılan bir metot.
			   Yani burada if parantezindeki "getProperties().getProperty("execution_env").equalsIgnoreCase("remote")" ifadesinin anlamı şu:
			   "getProperties() metodunun döndürdüğü "p = config.properties" dosyasındaki "execution_env" isimli parametrenin/key'in
			   "getProperty()" metodu ile çekilen value'su, büyük-küçük harf olayına dikkat edilmeden "remote" değerine eşitse..."
			-> "DesiredCapabilities" classı; Selenium test scriptleri içerisinde operating systems, browser combinations, browser versions vb.
			   temel test gereksinimlerini tanımlamak için kullanılır.
			   İlgili SS'i Notlar'a kaydettim.
			*/

			/*
			*** OS (Operating System) Ayarları ***
			-> "getProperties()" metodunun döndürdüğü "p = config.properties" dosyasındaki "os" parametresinin/key'inin sahip olduğu value,
			   büyük-küçük harf olayına dikkat edilmeden "windows" ifadesine eşitse "WIN11" platformu set ediliyor (Windows 11).
			   "os == mac" olduğunda da "MAC" platformu set ediliyor.
			   "os" parametresi/key'inin value'su "windows" ya da "mac" ifadesine eşit olmadığında ise "else" bloğu çalışıyor.
			*/
			if (getProperties().getProperty("os").equalsIgnoreCase("windows"))
			{
			    capabilities.setPlatform(Platform.WIN11);
			}
			else if (getProperties().getProperty("os").equalsIgnoreCase("mac"))
			{
			    capabilities.setPlatform(Platform.MAC);
			}
			else
			{
				System.out.println("No matching OS...");
			}

			/*
			*** Browser Ayarları ***
			-> "getProperties()" metodu, "p = config.properties" dosyasını döndürüyor.
			   config.properties'in içerisindeki "browser" key/parametresinin sahip olduğu value'nun küçük harflere çevrilmiş hali
			   "chrome" olduğunda "BrowserName = chrome" değerine, "edge" olduğunda ise "BrowserName = MicrosoftEdge" değerine set ediliyor.
			   default (else) durumunda ise herhangi bir işlem yapılmıyor ve Console'a "No matching browser..." basılıyor.
			*/
			switch (getProperties().getProperty("browser").toLowerCase())
			{
			    case "chrome":
					capabilities.setBrowserName("chrome");
					break;
				case "edge":
			        capabilities.setBrowserName("MicrosoftEdge");
			        break;
			    default:
			        System.out.println("No matching browser...");
			}

			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
			// ??? Bu kodun ne işe yaradığını bilmiyorum ???
		}  // if (getProperties().getProperty("execution_env").equalsIgnoreCase("remote"))

		/*
		-> Aşağıdaki "else-if" bloğunda, "getProperties()" metodunun döndürdüğü "p = config.properties" içerisindeki "execution_env" parametresinin
		   sahip olduğu value, büyük-küçük harf olayına bakılmadan "local" ifadesine eşit olduğunda, sadece "driver-->browser" atamaları yapılıyor.
		   Yukarıda "execution_env = remote" olduğunda ekstra olarak Platform (Windows/Mac) setlemesi de yapılmıştı.
		   "execution_env = local" olduğunda ise herhangi bir Platform setlemesine gerek yok.
		   !!!!!!!!!! ÇÜNKÜ BU DURUMDA PROJE, LOCAL SİSTEMDE EXECUTE EDİLİYOR !!!!!!!!!!
		   Bu durumda direkt olarak; "browser == chrome" => "driver = new ChromeDriver()" ya da,
		                             "browser == edge" => "driver = new EdgeDriver()" işlemlerini yapmak yeterli.
		   else durumunda ise "driver = null" yapılmış.
		*/
		else if (getProperties().getProperty("execution_env").equalsIgnoreCase("local"))  // "local" => "windows"
		{
			switch(getProperties().getProperty("browser").toLowerCase())
			{
				case "chrome":
			        driver = new ChromeDriver();
			        break;
			    case "edge":
			    	driver = new EdgeDriver();
			        break;
			    default:
			        System.out.println("No matching browser...");
			        driver = null;
			}
		}

		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));

		return driver;

	}  // public static WebDriver initilizeBrowser() throws IOException

	//----------------------------------------------------------------------------

    /*
    -> "getDriver()" metodu ile "driver" nesnesini döndürüyoruz.
    -> Bu metot ayrıca ExtentReport oluştururken de gerekli.
    */

	public static WebDriver getDriver()
	{
			return driver;
	}

	//----------------------------------------------------------------------------

    /*
    -> !!! Loading Properties file !!!
	-> Bu metot, "config.properties" dosyasının başlatılmasından (launch) sorumlu.
	*/

	public static Properties getProperties() throws IOException
	{
		FileReader file = new FileReader(System.getProperty("user.dir")+"\\src\\test\\resources\\config.properties");

        p = new Properties();
		p.load(file);
		return p;

		/*
		-> System.getProperty("user.dir")  =>  Current Project Location.
		   Devamında da bu proje içerisindeki "config.properties" Properties classı tanımlanmış.
		   Yani buradaki "file = config.properties" oluyor.
		   Daha sonra yeni bir Properties instance'ı oluşturulmuş => "p"
		   Bu "p" Properties classına da "file = config.properties" load edilmiş.
		   Yani "p = config.properties" olmuş.
		   Yani bu "getProperties()" metodu esasen "p = config.properties" Properties classını döndürüyor.
		*/
	}

	//----------------------------------------------------------------------------

    /*
    -> !!! Loading Logger file !!!
	-> Bu metot, logger'ın başlatılmasından sorumlu (sanırım "loglama işleminin başlatılmasından sorumlu" anlamında).
	*/

	public static Logger getLogger() 
	{
		logger = LogManager.getLogger();  // Log4j
		return logger;

		/*
		-> "LogManager", "org.apache.logging.log4j" package'ına ait olan bir class.
		-> "Logger getLogger()", LogManager classına ait olan bir metot.
		   Yani eşitliğin sağ tarafındaki "LogManager.getLogger()" ifadesi, programlama dünyasında bulunan (:D)
		   "LogManager" classına ait olan "getLogger()" metodu oluyor.
		   Sağ taraftan gelecek olan ifade de yine aynı tipteki Global class değişkeni/objesi olarak tanımlanan
		   "static Logger logger" objesine gönderilmiş ve daha sonra da kendi tanımladığımız bu obje döndürülmüş.
		*/
	}

	//----------------------------------------------------------------------------

    // ********** RE-USABLE METHODS **********
	
	public static String randomeString()
	{
		String generatedString = RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}

	public static String randomeNumber()
	{
		String generatedString = RandomStringUtils.randomNumeric(10);
		return generatedString;
	}

	public static String randomAlphaNumeric()
	{
		String str = RandomStringUtils.randomAlphabetic(5);
		String num = RandomStringUtils.randomNumeric(10);
		return str + num;
	}

}  // public class BaseClass