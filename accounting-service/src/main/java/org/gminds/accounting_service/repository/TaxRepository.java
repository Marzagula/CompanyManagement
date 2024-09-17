package org.gminds.accounting_service.repository;

import org.gminds.accounting_service.model.Tax;
import org.gminds.accounting_service.model.enums.TaxCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxRepository extends JpaRepository<Tax, Long> {
    List<Tax> findByFiscalYear(Integer fiscalYear);

    List<Tax> findByFiscalYearAndTaxType(Integer fiscalYear, TaxCategory taxType);
}
