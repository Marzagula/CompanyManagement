Feature: Employee Management

    Scenario: Create a new employee
        Given I am authenticated as "user" with password "password"
        And the employee service is running
        When I create a new employee with name "John Doe" and position "Developer"
        Then the employee should be added to the database