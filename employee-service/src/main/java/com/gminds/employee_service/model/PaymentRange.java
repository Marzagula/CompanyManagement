package com.gminds.employee_service.model;

import com.gminds.employee_service.model.enums.EmplAgreementType;
import jakarta.persistence.*;

/**
 * TODO
 * In future entity it should be moved to accounting-service
 */
@Entity
public class PaymentRange extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double minSalary;
    private Double maxSalary;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
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

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
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
