package org.gminds.accounting_service;

import org.gminds.accounting_service.model.*;
import org.gminds.accounting_service.model.enums.Currency;
import org.gminds.accounting_service.model.enums.LimitCondition;
import org.gminds.accounting_service.model.enums.TaxCategory;
import org.gminds.accounting_service.model.enums.TaxType;
import org.gminds.accounting_service.repository.FiscalValuesRepository;
import org.gminds.accounting_service.repository.LedgerAccountRepository;
import org.gminds.accounting_service.repository.TaxRepository;
import org.gminds.accounting_service.service.taxes.PITCalculator;
import org.gminds.accounting_service.service.taxes.ZUSCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class TaxesParametrizedTest {

    @Parameterized.Parameter(0)
    public double incomeAmount;
    @Parameterized.Parameter(1)
    public double expectedZUSTax;
    @Parameterized.Parameter(2)
    public double expectedPITTax;
    @Parameterized.Parameter(3)
    public int fiscalYear;

    @Mock
    TaxRepository taxRepository;
    @Mock
    FiscalValuesRepository fiscalValuesRepository;
    @Mock
    LedgerAccountRepository ledgerAccountRepository;
    ZUSCalculator zusProcessor;
    PITCalculator pitCalculator;

    @Parameterized.Parameters(name = "{index}: Test with salary={0}, expectedZusTax={1}, expectedPitTax={2}, fiscalYear={3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {10000, 4196.0, 705.0, 2024},
                {20000, 8391.0, 3162.0, 2024},
                {18000, 7552.0, 2590.0, 2024},
                {17000, 7133.0, 2314.0, 2024},
                {15000, 6293.0, 1762.0, 2024},
                {22000, 9230.0, 3802.0, 2024},
                {32000, 13426.0, 7002.0, 2024},
                {40000, 16782.0, 9562.0, 2024},
                {120000, 10800.0, 35162.0, 2024},//2 wyplaty sprawily ze z wplat do zusu jest wliczana juz tylko zdrowotna
                {130000, 11700.0, 38362.0, 2024},//2 wyplaty sprawily ze z wplat do zusu jest wliczana juz tylko zdrowotna
                {30000, 12587.0, 6362.0, 2024},
                {16687, 7001.0, 2228.0, 2024},
                {250000, 22500.0, 76762.0, 2024},//pierwsza wyplata sprawila ze z wplat do zusu jest wliczana juz tylko zdrowotna

        });
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.zusProcessor = new ZUSCalculator(taxRepository, fiscalValuesRepository, ledgerAccountRepository);
        this.pitCalculator = new PITCalculator(taxRepository, fiscalValuesRepository, ledgerAccountRepository);
    }


    @Test
    public void TaxesCalculateTest() {
        Salary salary = new Salary();
        salary.setAmount(incomeAmount);
        salary.setEmployeeId(3L);
        salary.setTransactionDate(LocalDate.of(2024, Month.MARCH, 1));

        when(taxRepository.findByFiscalYear(fiscalYear)).thenReturn(taxes2024());
        when(fiscalValuesRepository.findByFiscalYear(fiscalYear)).thenReturn(fiscalValues2024());
        when(ledgerAccountRepository.findByAccountNameWithTransactions("UOP")).thenReturn(uopLedgerAccount());
        Double zusTaxAmount = zusProcessor.calculateTax(salary);
        assertEquals(expectedZUSTax, zusTaxAmount);

        Double pitTaxAmount = pitCalculator.calculateTax(salary);
        assertEquals(expectedPITTax, pitTaxAmount);
    }

    List<FiscalValue> fiscalValues2024() {
        List<FiscalValue> fiscalValues = new ArrayList<>();

        FiscalValue fiscalValue1 = new FiscalValue();
        fiscalValue1.setFiscalYear(2024);
        fiscalValue1.setTaxType(TaxType.TAX_BRACKET);
        fiscalValue1.setTaxSubtype("first_bracket");
        fiscalValue1.setLimitValue(120_000.0);
        fiscalValue1.setLimitCondition(LimitCondition.LESS_EQUAL);
        fiscalValue1.setDescription("Maksymalny dochód dla pierwszego progu podatkowego");
        fiscalValues.add(fiscalValue1);

        FiscalValue fiscalValue2 = new FiscalValue();
        fiscalValue2.setFiscalYear(2024);
        fiscalValue2.setTaxType(TaxType.TAX_BRACKET);
        fiscalValue2.setTaxSubtype("second_bracket");
        fiscalValue2.setLimitValue(120_000.0);
        fiscalValue2.setLimitCondition(LimitCondition.MORE);
        fiscalValue2.setDescription("Dochód powyżej pierwszego progu, dla drugiego progu");
        fiscalValues.add(fiscalValue2);

        FiscalValue fiscalValue3 = new FiscalValue();
        fiscalValue3.setFiscalYear(2024);
        fiscalValue3.setTaxType(TaxType.TAX_DEDUCTION);
        fiscalValue3.setTaxSubtype("maximum_deduction");
        fiscalValue3.setLimitValue(3600.0);
        fiscalValue3.setDescription("Maksymalna kwota odliczenia od podatku PIT w roku");
        fiscalValues.add(fiscalValue3);

        FiscalValue fiscalValue4 = new FiscalValue();
        fiscalValue4.setFiscalYear(2024);
        fiscalValue4.setTaxType(TaxType.COST);
        fiscalValue4.setTaxSubtype("employee_costs");
        fiscalValue4.setLimitValue(250.0);
        fiscalValue4.setDescription("Koszt pracownika miesięcznie");
        fiscalValues.add(fiscalValue4);

        FiscalValue fiscalValue5 = new FiscalValue();
        fiscalValue5.setFiscalYear(2024);
        fiscalValue5.setTaxType(TaxType.LIMIT);
        fiscalValue5.setTaxSubtype("retirement_contribution_limit");
        fiscalValue5.setLimitValue(234_720.0);
        fiscalValue5.setDescription("Roczne ograniczenie podstawy wymiaru składek na ubezpieczenie emerytalne i rentowe");
        fiscalValues.add(fiscalValue5);

        return fiscalValues;
    }

    List<Tax> taxes2024() {
        List<Tax> taxes = new ArrayList<>();

        Tax zus1 = new Tax();
        zus1.setTaxCategory(TaxCategory.ZUS);
        zus1.setTaxSubcategory("emerytalna pracownika");
        zus1.setFiscalYear(2024);
        zus1.setPercentage(9.76);
        taxes.add(zus1);

        Tax zus2 = new Tax();
        zus2.setTaxCategory(TaxCategory.ZUS);
        zus2.setTaxSubcategory("emerytalna pracodawcy");
        zus2.setFiscalYear(2024);
        zus2.setPercentage(9.76);
        taxes.add(zus2);

        Tax zus3 = new Tax();
        zus3.setTaxCategory(TaxCategory.ZUS);
        zus3.setTaxSubcategory("rentowa pracownika");
        zus3.setFiscalYear(2024);
        zus3.setPercentage(1.5);
        taxes.add(zus3);

        Tax zus4 = new Tax();
        zus4.setTaxCategory(TaxCategory.ZUS);
        zus4.setTaxSubcategory("rentowa pracodawcy");
        zus4.setFiscalYear(2024);
        zus4.setPercentage(6.5);
        taxes.add(zus4);

        Tax zus5 = new Tax();
        zus5.setTaxCategory(TaxCategory.ZUS);
        zus5.setTaxSubcategory("chorobowa");
        zus5.setFiscalYear(2024);
        zus5.setPercentage(2.45);
        taxes.add(zus5);

        Tax zus6 = new Tax();
        zus6.setTaxCategory(TaxCategory.ZUS);
        zus6.setTaxSubcategory("wypadkowa");
        zus6.setFiscalYear(2024);
        zus6.setPercentage(1.67);
        taxes.add(zus6);

        Tax zus7 = new Tax();
        zus7.setTaxCategory(TaxCategory.ZUS);
        zus7.setTaxSubcategory("fundusz pracy");
        zus7.setFiscalYear(2024);
        zus7.setPercentage(2.45);
        taxes.add(zus7);

        Tax zus11 = new Tax();
        zus11.setTaxCategory(TaxCategory.ZUS);
        zus11.setTaxSubcategory("fundusz gwarantowanych świadczeń pracowniczych");
        zus11.setFiscalYear(2024);
        zus11.setPercentage(0.1);
        taxes.add(zus11);

        Tax zus8 = new Tax();
        zus8.setTaxCategory(TaxCategory.HEALTH);
        zus8.setFiscalYear(2024);
        zus8.setPercentage(9.0);
        taxes.add(zus8);

        Tax zus9 = new Tax();
        zus9.setTaxCategory(TaxCategory.PIT);
        zus9.setTaxSubcategory("first_bracket");
        zus9.setFiscalYear(2024);
        zus9.setPercentage(12.0);
        taxes.add(zus9);

        Tax zus10 = new Tax();
        zus10.setTaxCategory(TaxCategory.PIT);
        zus10.setTaxSubcategory("second_bracket");
        zus10.setFiscalYear(2024);
        zus10.setPercentage(32.0);
        taxes.add(zus10);
        return taxes;
    }

    LedgerAccount uopLedgerAccount() {
        LedgerAccount ledgerAccount = new LedgerAccount();
        ledgerAccount.setAccountName("UOP");
        ledgerAccount.setAccountNumber("12345");
        ledgerAccount.setStartDate(LocalDate.now().minus(30, ChronoUnit.DAYS));
        ledgerAccount.setCurrency(Currency.PLN);
        ledgerAccount.setActive(true);

        Salary salary = new Salary();
        salary.setTransactionDate(LocalDate.of(2024, Month.JANUARY, 1));
        salary.setAmount(incomeAmount);
        salary.setEmployeeId(3L);

        TaxTransaction zus1 = new TaxTransaction();
        zus1.setTaxBase(incomeAmount);
        zus1.setTransactionDate(LocalDate.of(2024, Month.JANUARY, 1));
        zus1.setTaxCategory(TaxCategory.ZUS);
        zus1.setAmount(incomeAmount * 0.1371);
        zus1.setEmployeeId(3L);

        Salary salary2 = new Salary();
        salary2.setTransactionDate(LocalDate.of(2024, Month.FEBRUARY, 1));
        salary2.setAmount(incomeAmount);
        salary2.setEmployeeId(3L);

        TaxTransaction zus2 = new TaxTransaction();
        zus2.setTaxBase(incomeAmount);
        zus2.setTransactionDate(LocalDate.of(2024, Month.JANUARY, 1));
        zus2.setTaxCategory(TaxCategory.ZUS);
        zus2.setAmount(incomeAmount * 0.1371);
        zus2.setEmployeeId(3L);

        ledgerAccount.getTransactions().add(salary);
        ledgerAccount.getTransactions().add(zus1);
        ledgerAccount.getTransactions().add(salary2);
        ledgerAccount.getTransactions().add(zus2);
        ledgerAccount.setBalance(BigDecimal.valueOf(salary.getAmount() + salary2.getAmount()));

        return ledgerAccount;
    }
}
