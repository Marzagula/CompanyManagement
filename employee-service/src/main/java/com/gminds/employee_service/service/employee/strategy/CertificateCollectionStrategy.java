package com.gminds.employee_service.service.employee.strategy;

import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeCertificate;
import com.gminds.employee_service.service.utils.EmployeeCollectionStrategy;

public class CertificateCollectionStrategy implements EmployeeCollectionStrategy<EmployeeCertificate> {

    @Override
    public void addToCollection(Employee employee, EmployeeCertificate certificate) {
        employee.getCertificates().add(certificate);
        certificate.setEmployee(employee);
    }
}
