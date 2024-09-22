package com.gminds.employee_service.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class EmployeeAgreementClause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "clause_id", nullable = false)
    Clause clause;
    LocalDate startDate;
    LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "employee_agreement_clause_id", nullable = false)
    EmployeeAgreement employeeAgreement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clause getClause() {
        return clause;
    }

    public void setClause(Clause clause) {
        this.clause = clause;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public EmployeeAgreement getEmployeeAgreement() {
        return employeeAgreement;
    }

    public void setEmployeeAgreement(EmployeeAgreement employeeAgreement) {
        this.employeeAgreement = employeeAgreement;
    }
}
