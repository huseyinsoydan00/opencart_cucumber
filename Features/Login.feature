Feature: Login with Valid Credentials

  #tags
  @sanity @regression  # Normal Scenario => "Scenario"
  Scenario: Successful Login with Valid Credentials
    Given the user navigates to login page
    When user enters email as "pavanoltraining@gmail.com" and password as "test@123"
    And the user clicks on the Login button
    Then the user should be redirected to the MyAccount Page

  @regression  #Data-Driven Scenario => "Scenario Outline"
  Scenario Outline: Login Data Driven
    Given the user navigates to login page
    When user enters email as "<email>" and password as "<password>"
    And the user clicks on the Login button
    Then the user should be redirected to the MyAccount Page

    Examples:
      | email                     | password |
      | pavanol@gmail.com         | test123  |
      | pavanoltraining@gmail.com | test@123 |


# Buradaki ilk "Scenario"ya karşılık olarak "LoginSteps" StepDefinition'ı oluşturulmuş.
# "Scenario Outline" da ilk Scenario ile aynı steplere sahip olduğu için (dolayısıyla aynı StepDefinition metotlarına sahip)
# "Scenario Outline" için ayrı bir StepDefinition oluşturmaya gerek yok.

# İkinci "Data-Driven Scenario Outline"ın ilk Scenario'ya göre tek farkı, "When" keyword'üne sahip olan step.
# Bu step'in tek farkı, bu Feature'a karşılık oluşturulan "LoginSteps" StepDefinition classındaki ilgili metot sadece tek bir
# email-password çifti ile değil de (ilk Scenario'daki email-password çifti), "Examples" bloğundaki 2 tane email-password çifti için çalıştırılacak.

# "Examples" bloğundaki ilk veri "INVALID", ikinci veri ise "VALID" imiş.