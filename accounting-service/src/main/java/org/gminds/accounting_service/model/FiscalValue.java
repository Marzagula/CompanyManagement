package org.gminds.accounting_service.model;

import jakarta.persistence.*;
import org.gminds.accounting_service.model.enums.FiscalValueType;
import org.gminds.accounting_service.model.enums.LimitCondition;

@Entity
public class FiscalValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer fiscalYear;
    @Enumerated(EnumType.STRING)
    private FiscalValueType fiscalValueType;
    private String fiscalValueSubtype;
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

    public FiscalValueType getFiscalValueType() {
        return fiscalValueType;
    }

    public void setFiscalValueType(FiscalValueType fiscalValueType) {
        this.fiscalValueType = fiscalValueType;
    }

    public String getFiscalValueSubtype() {
        return fiscalValueSubtype;
    }

    public void setFiscalValueSubtype(String fiscalValueSubtype) {
        this.fiscalValueSubtype = fiscalValueSubtype;
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
