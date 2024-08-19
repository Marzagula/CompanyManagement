Feature: Employee Management

  Scenario Outline: Create a new employee with dynamic data
    Given I am authenticated as "<username>" with password "<password>"
    And the employee service is running
    When I create a new employee with the following details:
      | key             | value             |
      | name            | <name>            |
      | surname         | <surname>         |
      | departmentName  | <department>      |
      | jobTitle        | <jobTitle>        |
      | salary          | <salary>          |
      | agreementType   | <agreementType>   |
      | certificateName | <certificateName> |
      | companyName     | <companyName>     |

    Then the employee should be added to the database with name "<name>" and surname "<surname>"
    Examples:
      | username | password | name | surname | department | jobTitle         | salary | agreementType | certificateName           | companyName              |
      | user     | password | Adam | Johnson | IT         | Junior Developer | 5500   | B2B           | Java Professional         | Amsterdam Tech Solutions |
      | user     | password | John | Doe     | IT         | Junior Developer | 5500   | B2B           | Java Professional         | Amsterdam Tech Solutions |
      | user     | password | Jane | Smith   | HR         | HR Specialist    | 8000   | EMPLOYMENT    | Certified HR Professional | Global HR Solutions      |

  Scenario Outline: Prevent from creating incorrect agreements
    Given I am authenticated as "<username>" with password "<password>"
    And the employee service is running
    When I create a new employee with the following details:
      | key             | value             |
      | name            | <name>            |
      | surname         | <surname>         |
      | departmentName  | <department>      |
      | jobTitle        | <jobTitle>        |
      | salary          | <salary>          |
      | agreementType   | <agreementType>   |
      | certificateName | <certificateName> |
      | companyName     | <companyName>     |

    And I update the agreements for these employees with the following details:
      | name   | surname   | newSalary   |
      | <name> | <surname> | <newSalary> |

    Then New agreement for "<name>" "<surname>" with salary "<newSalary>" should be prevented

    Examples:
      | username | password | name    | surname     | department | jobTitle                | salary | agreementType | certificateName                           | companyName              | newSalary |
      | user     | password | Adam    | Thompson    | IT         | Junior Developer        | 5500   | B2B           | Java Professional                         | Amsterdam Tech Solutions | 26000     |
      | user     | password | Anna    | Testowa     | HR         | HR Specialist           | 6000   | EMPLOYMENT    | Certified HR Professional                 | Global HR Solutions      | 30000     |
      | user     | password | Mark    | Brown       | SALES      | Sales Executive         | 7000   | B2B           | Sales Mastery                             | Sales International      | 40000     |
      | user     | password | Marek   | Brazowy     | HR         | Recruitment Coordinator | 7000   | EMPLOYMENT    | Wciskanie pracownikom niekorzystnych umow | Januszex                 | 100       |
      | user     | password | Andrzej | Synojcowski | ACCOUNTING | Finance Manager         | 20000  | B2B           | Bycie dobrym synem swojego ojca           | Tata                     | 23500     |
