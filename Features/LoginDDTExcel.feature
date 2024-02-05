Feature: Login Data Driven with Excel

  Scenario Outline: Login Data Driven Excel
    Given the user navigates to login page
    Then the user should be redirected to the MyAccount Page by passing email and password with excel row "<row_index>"

    Examples:
      | row_index |
      | 1         |
      | 2         |
      | 3         |


# "Login.feature" içerisindeki "Scenario Outline" Data-Driven senaryosu için test verileri, ilgili Feature'da bulunan "Examples" bloğundan çekiliyor idi.
# Ancak çoğu zaman bu yaklaşımı KULLANMIYORUZ !!!
# Çünkü bazı durumlarda elimizde çok fazla test datası olabilir.
# Bundan dolayı verileri bir Excel tablosunda tutuyoruz ve oradan çekip burada Feature içerisindeki "Scenario Outline"
# Data-Driven Test senaryosuna veri sağlayan "Examples" bloğuna gönderiyoruz.
# "Examples" bloğunda da sadece Excel tablosundaki satır numaralarını belirtiyoruz (1,2,3) ve Excel'deki o satır numarasına ait veri çekilip
# ilgili step'e parametre olarak gönderiliyor ("row_index").
# Excel tablosundaki verilerin okunup buradaki Examples'a gönderilmesi işini de "utilities" package'ındaki "DataReader" Utility file/classı yapıyor.
# Bu sayede Excel'deki veriler çekiliyor ve OpenCart sitesinde Login için gerekli olan email-password alanları Excel'den çekilen verilerle
# otomatik olarak dolduruluyor.

# Burada "Given" step'i, "Login.feature"daki steple aynı.
# Yani bu step'e karşılık gelen metot, "Login.feature"ın StepDefinition classı olan "LoginSteps" classında mevcut.
# Yani bunun için ayrı bir StepDefinition classı oluşturmaya gerek yok.
# Tek fark, "Then" step'inde.
# Çünkü "Then", Validation amaçlı kullanılan bir Gherkin keyword'ü idi.
# "Then the user should be redirected to the MyAccount Page" step'i "Login.feature"da da bulunuyor.
# Ancak burada step'in devamına " by passing email and password with excel row "<row_index>" " ifadesi eklenmiş.
# Aslında bu ifade bir Action ifadesi ve normalde biz actionları "When" Gherkin keyword'ü ile belirtiyorduk.
# Yani buradaki "Then" step'inde "the user should be redirected to the MyAccount Page" ifadesi bir Validation ifadesi => "Then".
# Ancak devamına eklenen " by passing email and password with excel row "<row_index>" " ifadesi ise bir Action ifadesi => "When".
# Bu iki ifade "Then" keyword'ü ile birleştirilmiş.

# "row_index = 1" => Excel'de 1. satırdaki "email-password" çifti.
# "row_index = 2" => Excel'de 2. satırdaki "email-password" çifti.
# "row_index = 3" => Excel'de 3. satırdaki "email-password" çifti.

# Normalde "Opencart_LoginData" Excel dosyasındaki tabloda başlık satırı hariç toplam 5 satır email-password çifti mevcut.
# Ancak burada sadece ilk 3 satırdaki email-password ile Login işlemi yapılıyor.
# İstersek Examples bloğundaki "row_index" parametresine "4" ve "5" eklemesi yaparak Excel'deki 5 satırın tamamındaki
# email-password verileriyle Login işlemi gerçekleştirebiliriz.