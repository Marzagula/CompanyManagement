package org.gminds.accounting_service.service.taxes.deduction;

import org.gminds.accounting_service.model.TransactionItem;
import org.gminds.accounting_service.model.enums.FiscalValueType;
import org.gminds.accounting_service.service.taxes.TaxDeduction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DeductionFactory<T extends TransactionItem> {


    private final Map<String, TaxDeduction<T>> processorMap;

    public DeductionFactory(List<TaxDeduction<T>> processors
    ) {
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(taxDeduction -> taxDeduction.getFiscalValueType() + "/" + taxDeduction.getFiscalValueSubtype(), Function.identity()));

    }

    public TaxDeduction<T> getProcessor(FiscalValueType fiscalValueType, String fiscalValueSubtype) {
        return processorMap.get(fiscalValueType + "/" + fiscalValueSubtype);
    }

}
