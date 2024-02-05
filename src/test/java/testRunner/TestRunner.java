package testRunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith (Cucumber.class)
@CucumberOptions(
		// features = {".//Features/"},                       // Projemizdeki (.//) "Features" package'ı içerisindeki tüm feature'lar çalıştırılır.
		// features = {".//Features/Login.feature"},          // to execute only "Login.feature".
		// features = {".//Features/LoginDDTExcel.feature"},  // to execute only "LoginDDTExcel.feature".
		// features = {".//Features/Login.feature", ".//Features/Registration.feature"},
		features = {".//Features/Registration.feature"},
		// features = {"@target/rerun.txt"},                  // This is for "executing" only failed scenarios.

		glue = "stepDefinitions",

		plugin = { "pretty", "html:reports/myreport.html",                                  // This is for generating "Cucumber-JUnit" report.
				   "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",  // This is for generating "ExtentReport".
				   "rerun:target/rerun.txt"                                                 // This is for "capturing" failed scenarios.
		         },

		dryRun = false,                           // checks mapping ("eşleşmeler") between Scenario Steps and Step Definition methods.
		monochrome = true,                        // to avoid "junk characters" in output.
		publish = true                  		  // (URL in Console) to publish "Cucumber-JUnit Report" in Cucumber server (not for "ExtentReport").

		// tags = "@sanity"                       // to execute every scenario with "@sanity" tag.
		// tags = "@regression"					  // to execute every scenario with "@regression" tag.
		// tags = "@sanity and @regression"       // to execute scenarios with both "@sanity" tag and "@regression" tag.
		// tags = "@sanity and not @regression"   // to execute scenarios with "@sanity" tag but not with "@regression" tag.
		// tags = "@sanity or @regression"        // to execute scenarios with either "@sanity" tag or "@regression" tag.
		)
public class TestRunner
{
	/*
	-> Bu classın içini daha önce de söylediğimiz gibi boş bırakıyoruz.
	   Sadece başına "@RunWith" JUnit annotation'ını ekleyip bunun bir Cucumber classı olduğunu belirtiyoruz => "Cucumber.class"
	   Ve "@CucumberOptions" Cucumber annotation'ı ile de gerekli olan ayarları vs yapıyoruz.
	*/
}


//----------------------------------------------------------------------------------------------------------------------


/*
													*** NOTLAR ***

-> "target", projemizde otomatik olarak oluşturulan bir folder.
   İçine bakarsan, yine otomatik olarak oluşturulan "rerun.txt" isimli bir belge mevcut.

-> ********** "plugin" bloğunu inceleyelim (rapor oluşturma, failed senaryoları yeniden execute etme işlemleri için) **********

   İki tip rapor vardır:
   1) Rapor-1 - Cucumber-JUnit => bunlar default raporlardır ve kullandığımız "pretty" option'ı sayesinde oluşturulurlar.
                               => "html:reports/myreport.html" ile de oluşturulacak olan bu raporun projemizde nereye ve hangi isim-uzantı ile
                                  kaydedileceğini belirtiyoruz.

   2) Rapor-2 - "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
   Bu da "Cucumber-JUnit" ExtentReport'u oluşturmak için pom.xml'e eklediğimiz dependency.
   Bunu buradaki "plugin" bloğu içerisinde belirtiyoruz ki otomatik olarak ExtentReport oluşturulsun
   (pom.xml'den ilgili dependency'ye bakabilirsin).
   ---------------------------------------------------------------------------------------------------------------------
   "rerun:target/rerun.txt" => Feature'lardaki senaryolarımızdan herhangi birinde bir hata olduğunda, projede otomatik olarak oluşturulan
                               "target" folder'ı içerisindeki, yine otomatik olarak oluşturulan "rerun.txt" belgesi bu hataları yakalayacak.
                               Biz de 2. execute round'ında, "@CucumberOptions" içerisindeki execute edilen "features" satırını "//" ile comment edip
                               sadece hatalı olan senaryo/senaryoları yeniden çalıştırmak için "features = {"@target/rerun.txt"}" satırını
                               yorum satırı olmaktan çıkarıp "Enabled/Aktif" hale getireceğiz ve bu sayede 2. execute round'ında
                               sadece hatalı olan senaryo/senaryolar, "target" klasörü içerisindeki "rerun.txt" belgesi tarafından çalıştırılacak.
                               Olay bu.
                               Hatırlarsan benzerini TestNG'de de yapmıştık. Orada da 2. execution'da sadece hatalı olan metotları çalıştırmak için
                               projeyi 2. kez execute ettiğimizde, projedeki herşeyin çalıştırılmasından sorumlu "master.xml" yerine,
                               sadece hatalı olan classa ait ilgili metotları yakalayan ve otomatik olarak oluşturulan
                               "testng-failed.xml" belgesini çalıştırıyorduk ve başarılı olan metotları yeniden çalıştırmayarak
                               zamandan tasarruf etmiş oluyorduk.
                               Cucumber'da da benzer bir mantık söz konusu.
                            => Ancak ilk execute da dahil olmak üzere "plugin" bloğu içerisinde belirttiğimiz "rerun:target/rerun.txt" satırını
                               her daim (ilk execution da dahil) "Enabled/Aktif" bırakıyoruz (yani "//" ile hiçbir zaman yorum satırı haline getirmiyoruz).
                               Çünkü bu "plugin" sayesinde ilk execution'da hatalı olan senaryo/senaryolar "rerun.txt" tarafından yakalanıyor
                               ve biz de ikinci execution'da, en başta execute edilen "features" satırını "//" ile comment edip
                               "features = {"@target/rerun.txt"}" satırını aktif ederek (yorum satırı olmaktan çıkararak)
                               ikinci execution'da, sadece hatalı olan senaryo/senaryoları yakalamış olan "rerun.txt" belgesini çalıştırıyoruz.
                               (aynı "testng-failed.xml" gibi).
                            => Projede yer alan "target" klasöründeki "rerun.txt" dosyası, yakaladığı hatanın sebebi olan kodun satır numarasını belirtiyor
                               (Örneğin; "Features package'ındaki Login.feature'ın 4. satırı" gibi).
                               Eğer hatayı düzeltirsek, bu dosya içerisinde hata kaynağını belirten satır otomatik olarak siliniyor.
   ---------------------------------------------------------------------------------------------------------------------

   "dryRun = false/true" => Bu özellik "false" olduğunda projenin execute edilmesinin yanında Feature file'daki her bir senaryo adımına karşılık gelen
                            StepDefinition metotlarının oluşturulup oluşturulmadığı Console kısmında Check/Verify ediliyor.
                         => Bu özellik "true" olduğunda ise proje execute EDİLMİYOR, sadece Feature file'daki her bir senaryo adımına karşılık gelen
                            StepDefinition metotlarının oluşturulup oluşturulmadığı Console kısmında Check/Verify ediliyor.
                            "dryRun" özelliğinin olayı bu.

   ---------------------------------------------------------------------------------------------------------------------

   "monochrome = true" => Feature file'ları çalıştırdığımızda bazen Console'da junk(gereksiz/çöp) karakterler oluşuyor (*, ? gibi).
                          Bu durum özellikle testlerimizi Jenkins ile çalıştırdığımızda Jenkins'de görülüyor.
                          Bu özelliği "true" yaparak bu junk karakterlerden kaçınıyoruz.

   ---------------------------------------------------------------------------------------------------------------------

   "publish = true" => Bu özelliği "true" yaparak Cucumber için oluşturulan Cucumber-JUnit raporunu takımla paylaşabiliyoruz ("ExtentReport" değil!).
                       Şöyle ki; normalde "reports" package'ı içerisinde oluşturulan "myreport.html" Cucumber-JUnit raporunu
                       herhangi bir Browser'da local sistemimiz üzerinden açıp görebiliyoruz.
                       Ancak "publish = true" yaptığımızda, projemizi Run ettikten sonra oluşturulan Cucumber-JUnit raporu için Console'da bir URL oluşuyor.
                       Bu URL'i takımdaki diğer üyelerle paylaşarak herhangi bir email göndermeye vs gerek kalmadan
                       Cucumber-JUnit raporunun diğer takım üyeleri tarafından da görülebilmesini sağlayabiliyoruz.
                    => ExtentReport'ları publish EDEMİYORUZ; sadece kendi local sistemimizde görüntüleyebiliyoruz.

   ---------------------------------------------------------------------------------------------------------------------
   ---------------------------------------------------------------------------------------------------------------------
   ---------------------------------------------------------------------------------------------------------------------

-> "@CucumberOptions" içerisinde kullandığımız "features", "glue" vb tüm keyword'ler, Gherkin keyword'leridir.

---------------------------------------------------------------------------------------------------------------------

-> "tags":
   TestNG'deki "grouping" olayının aynısını burada "tags" keyword'ü ile sağlıyoruz.
   TestNG'de bu işlemi yapmak için "grouping.xml" belgesini oluşturuyorduk.
   BDD yaklaşımını kullandığımızda ise JUnit'de bu işlem şöyle yapılıyor:
   Öncelikle her Feature file'daki her senaryonun başına istediğimiz tag/tag'leri koyuyoruz (@sanity, @regression vs).
   Daha sonra da çalıştırmak istediğimiz gruplara ait tag/tag'leri bu TestRunner classının başındaki "@CucumberOptions"
   annotation'ı içerisinde belirtiyoruz.
   Olay bu.
*/