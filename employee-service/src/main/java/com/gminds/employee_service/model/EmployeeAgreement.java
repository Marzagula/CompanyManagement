package com.gminds.employee_service.model;

import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.model.enums.EmploymentPaymentType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class EmployeeAgreement extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double salary;
    private LocalDate fromDate;
    private LocalDate toDate;
    @Enumerated(EnumType.STRING)
    private AgreementStatus status;
    @Enumerated(EnumType.STRING)
    private EmplAgreementType agreementType;
    @Enumerated(EnumType.STRING)
    private EmploymentPaymentType paymentType;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    @OneToMany(mappedBy = "employeeAgreement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeAgreementClause> clauses;

    public EmployeeAgreement() {
    }

    public EmployeeAgreement(Long id, Double salary, LocalDate from, LocalDate to, AgreementStatus status, EmplAgreementType agreementType, EmploymentPaymentType paymentType) {
        this.id = id;
        this.salary = salary;
        this.fromDate = from;
        this.toDate = to;
        this.status = status;
        this.agreementType = agreementType;
        this.paymentType = paymentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public AgreementStatus getStatus() {
        return status;
    }

    public void setStatus(AgreementStatus status) {
        this.status = status;
    }

    public EmplAgreementType getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(EmplAgreementType agreementType) {
        this.agreementType = agreementType;
    }

    public EmploymentPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(EmploymentPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
