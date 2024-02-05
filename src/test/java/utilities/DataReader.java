/*
**** Pavan bu Utility File/Class'ı anlamazsanız kendi otomasyon frameworklerinizde "olduğu gibi" kullanırsınız dedi ****

-> Bu "DataReader" Utility file/classının görevi, "Opencart_LoginData" Excel dosyasındaki tablodan verileri çekip,
   bu classda tanımlanan "mydata" HashMap nesnesine göndermek ve bu nesneyi burada "return" ile döndürmek (ki başka classda çekip kullanabilelim).
   Biz de bu sayede bu nesneyi, "LoginSteps" StepDefinition classının en altında, "LoginDDTExcel.feature"daki "Then" step'ine karşılık oluşturulan metotta
   "datamap = DataReader.data(System.getProperty("user.dir")+"\\testData\\Opencart_LoginData.xlsx", "Sheet1")" kodu ile,
   o classda Global class nesnesi olarak tanımlanan "datamap" HashMap nesnesine gönderiyoruz.
   Yani buradaki "mydata" HashMap nesnesi Excel'den verileri çekip depoluyor ve akabinde döndürüyor.
   Biz de dönen bu "mydata" HashMap nesnesini, "LoginSteps" StepDefinition classındaki "datamap" HashMap nesnesine gönderiyoruz.
   Daha sonra da o "datamap" HashMap nesnesi, kendisine buradaki "mydata" HashMap nesnesi tarafından gönderilen her bir Excel satırında bulunan
   hücredeki veriyi, o classda bulunan "email, pwd, exp_res" değişkenlerine gönderiyor.
   Daha sonra da bu değişkenler ilgili POM classlardaki metotlara (örneğin; "LoginPage") metot parametresi olarak gönderiliyor ve sonuç olarak
   Excel'deki farklı farklı email-password verileri ile OpenCart sitesinde Login işlemleri tekrar tekrar otomatize edilmiş bir halde yapılıyor.
   !!!!!!!!!! BÜYÜK RESİM, TAM OLARAK BU !!!!!!!!!!

-> Son bir ekleme daha yapacak olursam; en son çekilen Excel verileri, "LoginDDTExcel.feature"daki veri sağlayıcı "Examples" bloğuna gönderiliyor.
   "Examples" bloğu da o Feature'a veri sağlıyor ve Excel'den çekilen ilgili satır numaralarındaki verileri (row_index = 1,2,3) tek tek alıp
   "Then" step'indeki "<row_index>" input/parametresine gönderiyor ve test, çekilen Excel verileri ile, çekilen email-password çifti sayısı kadar
   tekrar tekrar çalıştırılmış oluyor (Excel'de 5 tane email-password çifti varsa, Login işlemi de 5 defa bu verilerle otomatize bir halde yapılıyor).

-> Zaten Apache POI ile Data-Driven Testing olayının özü de bu.
   Düşün ki 10.000 tane email-password çifti ile Login testi yapılacak.
   Bunu Manuel olarak yapmak büyük zaman kaybı olur.
   Tüm email-password bilgilerini bir Excel dosyasında tutup onları bu şekilde çekerek Login işlemlerini otomatize etmek
   zamandan büyük tasarruf sağlar.
*/

package utilities;

import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//----------------------------------------------------------------------------------------------------------------------

public class DataReader
{
	public static HashMap <String,String> storeValues = new HashMap();
	// Bunun ne olduğunu anlamadım, ilk defa görüyorum. Pavan da bu bir HashMap deyip geçiverdi.

	public static List<HashMap <String,String>> data (String filepath, String sheetName)  // Bu metot sadece "LoginSteps" StepDefinition classında kullanılmış.
	{
		List<HashMap<String, String>> mydata = new ArrayList<>();

		try  // TRY bloğu, Excel'den veri okuma işini yapıyor.
		{
			FileInputStream fs = new FileInputStream(filepath);
			XSSFWorkbook workbook = new XSSFWorkbook(fs);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			Row HeaderRow = sheet.getRow(0);
			/*
			-> "filepath" => Projemizdeki "Opencart_LoginData" Excel dosyasının path'i.
			   "LoginSteps" StepDefinition classının en altındaki metotta bu "data()" metodu çağırıldığında
			   projemizdeki "Opencart_LoginData" Excel dosyasının mevcut konumu belirtilmiş.
			-> "sheetName" => Projemizdeki "Opencart_LoginData" Excel dosyasında yer alan tablonun bulunduğu Excel sayfasının adı.
			   Excel'de sol alta bakarsan "Sheet1" yazdığını görebilirsin.
			   Aynı şekilde "LoginSteps" StepDefinition classının en altındaki metotta bu "data()" metodu çağırıldığında
			   projemizdeki "Opencart_LoginData" Excel dosyasının içerisindeki tablonun bulunduğu Excel sayfa adı belirtilmiş.
			-> "LoginSteps" StepDefinition classının en altındaki THEN annotation'lı metotta yer alan ilgili kod:
			   datamap = DataReader.data(System.getProperty("user.dir")+"\\testData\\Opencart_LoginData.xlsx", "Sheet1");
			   (System.getProperty("user.dir")+"\\testData\\Opencart_LoginData.xlsx" => String filepath)
			   (Sheet1 => String sheetName)
			*/

			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++)  // "getPhysicalNumberOfRows()" metodu, "XSSFSheet" ApachePOI classına ait bir metot.
			{
				Row currentRow = sheet.getRow(i);
				HashMap <String,String> currentHash = new HashMap<String, String>();

				for (int j = 0; j < currentRow.getPhysicalNumberOfCells(); j++)
				{
					Cell currentCell = currentRow.getCell(j);

					switch (currentCell.getCellType())
					{
						case STRING:  // "currentCell.getCellType() == String" ise | Mevcut Excel hücresindeki verinin tipi String ise...
							currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue());  // (key, value)
							break;
							/*
							-> "currentHash", buradaki for döngüsünde tanımlanan bir HashMap nesnesi.
							   HashMap'lere eleman eklemek için "put()" metodu kullanılıyor.
							   Notlar'daki Kasım Adalan'ın SS'ine de bakabilirsin.
							-> "currentHash.put()" kodunda parantez içerisinde 2 tane parametre mevcut => (key, value).
							*/
					}
				}
				mydata.add(currentHash);
			}

			fs.close();

		}  // TRY bloğu.

		catch (Exception e)
		{
			e.printStackTrace();
		}

		return mydata;

	}  // public static List<HashMap <String,String>> data (String filepath, String sheetName)

}  // public class DataReader