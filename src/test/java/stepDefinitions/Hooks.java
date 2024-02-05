/*
-> Bu class, Browser'ı başlatma (launch) ve URL'i açma işlemlerinden sorumlu olan class.
   Buradaki metotlar, her bir StepDefinition classından önce ve sonra DAİMA ÇALIŞACAK !!!
   Ondan sonra "LoginSteps" ve "RegistrationSteps" StepDefinition classları çalışacak.
   Yani "Hooks" classı da bir nevi StepDefinition classların Base'i gibi !!!
*/

package stepDefinitions;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

//----------------------------------------------------------------------------------------------------------------------

public class Hooks
{
    // OBJECTS:
    WebDriver driver;
    Properties p;

    //-----------------------------------------------------------------------------
     
	@Before  // Feature dosyasındaki her bir Scenario çalışmadan önce 1 kere çalışır.
    public void setup() throws IOException  // Metot parametresi YOK.
    {
        driver = BaseClass.initilizeBrowser();
        /*
        -> "BaseClass"daki "static initilizeBrowser()" metodu, "return driver" ile driver'ı döndürüyor.
           Biz de "return driver" kodundan dönecek olan "driver"ı burada, classın en başında tanımladığımız
           "WebDriver driver" Global class nesnesine/objesine/değişkenine gönderiyoruz.
        */
    	    	
    	p = BaseClass.getProperties();
        /*
        -> Yine aynı şekilde "BaseClass"daki "getProperties()" metodu, "return p" ile projemizde bulunan "config.properties"
           Properties file'ını/class'ını döndürüyor (p => config.properties).
           Biz de oradan "return p" kodu ile dönecek olan "p => config.properties" file'ını/class'ını, yine burada classın
           en başında tanımladığımız "Properties p" Global class nesnesine/objesine/değişkenine gönderiyoruz.
        */

    	driver.get(p.getProperty("appURL"));  // driver.get("http://localhost/opencart/upload")
        /*
        -> "p", yani config.properties'deki "appURL" key'inin value'sunu "getProperty()" metodu ile çekiyoruz:
           appURL = http://localhost/opencart/upload
           (yani bildiğin driver.get("URL") ile site açma olayı).
        */

    	driver.manage().window().maximize();
	}

    //--------------------------------------

    @After  // Feature dosyasındaki her bir Scenario çalışmasını tamamladıktan sonra 1 kere çalışır.
    public void tearDown(Scenario scenario)  // Metot parametresi VAR | "Scenario" is predefined class (package io.cucumber.java)
    {
       driver.quit();
    }

    //--------------------------------------

    @AfterStep  // Feature dosyasındaki her bir ScenarioStep çalışmasını tamamladıktan sonra 1 kere çalışır.
    public void addScreenshot(Scenario scenario)  // Metot parametresi VAR | "Scenario" is predefined class (package io.cucumber.java)
    {
        /*
        -> ********** This is for "Cucumber-JUnit" report **********
        -> Scenario'da her failed durumunda bir SS alınacak ve alınan bu SS, test raporuna otomatik olarak ataçlanacak.
        */

        if (scenario.isFailed())
        {
            TakesScreenshot ts = (TakesScreenshot) driver;  // TypeCasting (DownCasting) !!!
            /*
            -> "driver", bir WebDriver nesnesi ve WebDriver ile TakesScreenshot interface'leri arasında bir bağlantı yok
               (Notlar'daki SS'den bakabilirsin).
               Bundan dolayı aralarında tür dönüşümü (TypeCasting) yapılması gerekli.
            -> Ayrıca WebDriver'ın bir class değil, interface olduğunu unutmuş olabilirsin.
               Şöyle ki hatırlarsan WebDriver bizim otomasyon scriptlerimizle Browser arasında bir nevi aracılık eden bir yapı idi.
               Bundan dolayı bir API görevi görüyordu.
               Bunun için WebDriver, bir interface.
            */

            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            /*
            -> "TakesScreenshot ts" nesnesine "getScreenshotAs()" metodu uygulanarak bir SS alınıyor.
               Alınan SS'in formatı da "BYTES" olarak belirtilmiş.
               Eşitliğin sol tarafında, alınan SS'i tutacak olan değişken/array de "byte" tipinde olmalı => byte[] screenshot
               ("screenshot" adında, elemanlarının tipinin "byte" olduğu bir Array).
            -> SELENIUM bölümünde SS alma işlemini yaparken alınan SS için "OutputType.FILE" yapmıştık.
               Yani SS'i bir "File" olarak kaydedip o şekilde rapora eklemiştik.
            */

            scenario.attach(screenshot,"image/png",scenario.getName());
            /*
            -> Senaryodaki hata ile ilgili alınan SS, SS'in uzantısı ve hatanın gerçekleştiği ilgili Scenario ismi
               bilgileriyle Scenario'ya (rapora) ataçlanıyor.
            */
        }
    }

}  // public class Hooks