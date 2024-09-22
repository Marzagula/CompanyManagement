package org.gminds.accounting_service.model;

import jakarta.persistence.*;
import org.gminds.accounting_service.model.enums.LimitCondition;
import org.gminds.accounting_service.model.enums.TaxType;

@Entity
public class FiscalValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer fiscalYear;
    @Enumerated(EnumType.STRING)
    private TaxType taxType;
    private String taxSubtype;
    private Double limitValue;
    @Enumerated(EnumType.STRING)
    private LimitCondition limitCondition;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public String getTaxSubtype() {
        return taxSubtype;
    }

    public void setTaxSubtype(String taxSubtype) {
        this.taxSubtype = taxSubtype;
    }

    public Double getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(Double limitValue) {
        this.limitValue = limitValue;
    }

    public LimitCondition getLimitCondition() {
        return limitCondition;
    }

    public void setLimitCondition(LimitCondition limitCondition) {
        this.limitCondition = limitCondition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
