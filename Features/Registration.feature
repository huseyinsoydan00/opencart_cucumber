Feature: Account Registration

  @regression
  Scenario: Successful Account Registration
    Given the user navigates to Register Account page
    When the user enters the details into below fields
      | firstName | John       |
      | lastName  | Kenedy     |
      | telephone | 1234567890 |
      | password  | test@123   |
    And the user selects Privacy Policy
    And the user clicks on Continue button
    Then the user account should get created successfully

# NOT-1:
# Burada When step'inde görüldüğü gibi 4 tane veri mevcut.
# OpenCart sitesinde (daha doğrusu OpenCart'ın eski versiyonu olan "TutorialsNinja" sitesinde) önce anasayfadaki "MyAccount" ve ardından "Register"
# seçeneğine tıkladıktan sonra gelen hesap oluşturma sayfasında çeşitli input alanları mevcut (SS'leri Notlar'a kaydettim).
# Buradaki her bir alanın doldurulması için ayrı ayrı When stepleri oluşturmak yerine (When the user enters firstName, When the user enters lastName etc...)
# bunun için tek bir When step'i oluşturup, step'in altında da görüldüğü gibi verilerin olduğu bir blok oluşturmak daha doğru bir yöntem.
# Çünkü öbür türlü her bir When step'i için StepDefinition classında ayrı bir metot oluşturmak gerekir.
# Ancak bunu böyle yapınca sadece 1 tane When step'i oluşturmak yeterli oluyor.
# Verileri bu Feature'dan nasıl çektiğimiz için ise bu Feature'ın StepDefinition'ı olan "RegistrationSteps" classındaki
# ilgili "When" annotation'lı metoda bakabilirsin ("DataTable" Cucumber concept).

# NOT-2:
# Bir Feature dosyasında tek bir step'de birden fazla data belirteceğimiz zaman ("When"), Cucumber'a ait olan "DataTable" yapısını kullanıyoruz.
# Java'daki "HashMap" ne ise, bunun Cucumber'daki karşılığı da bu "DataTable" yapısı oluyor.
# Bu Feature'ın StepDefinition'ı olan "RegistrationSteps" classında, "When" step'ine karşılık oluşturulan metotta bu yapıyı kullandık.
# Ve o metoda, buradaki "When" step'inde bulunan verilerin tamamını sadece 1 metot parametresi olarak gönderdik => "DataTable dataTable".

# NOT-3:
# "When" step'indeki datalardan sol taraftakiler aslında birer "key" ve sağ taraftakiler de aslında birer "value":
# "Map" Collection in Java.