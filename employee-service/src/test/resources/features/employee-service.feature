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

  Scenario Outline: Add new certificate to employee
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
    And I create new certificate with the following details:
      | key                | value                |
      | name               | <name>               |
      | surname            | <surname>            |
      | newCertificateName | <newCertificateName> |
      | issueDate          | <issueDate>          |
      | issuedBy           | <issuedBy>           |

    Then New certificate should be created for "<name>" "<surname>" with certificateName "<newCertificateName>", issuedBy "<issuedBy>" on day "<issueDate>"
    Examples:
      | username | password | name    | surname   | department | jobTitle                | salary | agreementType | certificateName                       | companyName        | newCertificateName                       | issuedBy          | issueDate  |
      | user     | password | Hiroshi | Nakamura  | IT         | Junior Developer        | 7800   | B2B           | AWS Certified Developer               | ABC Tech           | AWS Certified Solutions Architect        | Amazon            | 2023-01-15 |
      | user     | password | Elena   | Petrov    | IT         | Senior Developer        | 18720  | B2B           | Oracle Certified Java Developer       | XYZ Solutions      | Certified Kubernetes Administrator       | Linux Foundation  | 2023-02-10 |
      | user     | password | Carlos  | Oliveira  | PRODUCT    | Product Manager         | 17160  | B2B           | PMP - Project Management Professional | Global Innovations | Scrum Master Certified                   | Scrum.org         | 2023-03-20 |
      | user     | password | Amina   | Kovacevic | SALES      | Sales Manager           | 23400  | B2B           | Certified Sales Executive             | SalesPro           | Advanced Sales Strategy                  | HubSpot           | 2023-04-25 |
      | user     | password | Laszlo  | Kovacs    | ACCOUNTING | Finance Manager         | 23400  | B2B           | CFA - Chartered Financial Analyst     | FinCorp            | Financial Risk Manager                   | GARP              | 2023-05-10 |
      | user     | password | Sofia   | Rossi     | HR         | HR Manager              | 20280  | B2B           | SHRM-CP - Certified Professional      | PeopleSolutions    | SHRM-SCP - Senior Certified Professional | SHRM              | 2023-06-18 |
      | user     | password | Ismail  | Farah     | HR         | Recruitment Coordinator | 10920  | B2B           | AIRS Certified Internet Recruiter     | TalentFinders      | Professional Recruiter Certification     | Recruiter Academy | 2023-07-22 |

  Scenario Outline: Change name and surname of employee
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

    Then: His name should be changed to "<newName>" and surname to "<newSurname>"
    Examples: