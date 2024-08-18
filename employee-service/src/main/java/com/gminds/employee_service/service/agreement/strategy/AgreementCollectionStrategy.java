package com.gminds.employee_service.service.agreement.strategy;

import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.service.utils.EmployeeCollectionStrategy;

public class AgreementCollectionStrategy implements EmployeeCollectionStrategy<EmployeeAgreement> {

    @Override
    public void addToCollection(Employee employee, EmployeeAgreement agreement) {
        employee.getAgreements().add(agreement);
        agreement.setEmployee(employee);
    }
}
