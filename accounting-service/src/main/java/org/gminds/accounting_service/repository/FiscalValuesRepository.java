package org.gminds.accounting_service.repository;

import org.gminds.accounting_service.model.FiscalValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FiscalValuesRepository extends JpaRepository<FiscalValue, Long> {
    List<FiscalValue> findByFiscalYear(int fiscalYear);
}
