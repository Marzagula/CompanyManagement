package com.gminds.employee_service.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class EmployeeCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String certificateName;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column
    private LocalDate expiryDate;

    @Column
    private String issuedBy;


    public EmployeeCertificate() {
    }

    public EmployeeCertificate(Employee employee, String certificateName, LocalDate issueDate, LocalDate expiryDate, String issuedBy) {
        this.employee = employee;
        this.certificateName = certificateName;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.issuedBy = issuedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    @Override
    public String toString() {
        return "EmployeeCertificate{" +
                "id=" + id +
                ", employee=" + employee +
                ", certificateName='" + certificateName + '\'' +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                ", issuedBy='" + issuedBy + '\'' +
                '}';
    }
}
