package org.gminds.accounting_service.model;

import jakarta.persistence.*;
import org.gminds.accounting_service.model.enums.employee.EmplAgreementType;

@Entity
public class PaymentRange extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double minSalary;
    private Double maxSalary;
    private Long jobId;
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_agreement_type")
    private EmplAgreementType emplAgreementType;
    private Integer fiscalYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJob(Long jobId) {
        this.jobId = jobId;
    }

    public EmplAgreementType getEmplAgreementType() {
        return emplAgreementType;
    }

    public void setEmplAgreementType(EmplAgreementType emplAgreementType) {
        this.emplAgreementType = emplAgreementType;
    }

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

}
