package org.gminds.accounting_service;

import org.gminds.accounting_service.model.Salary;
import org.gminds.accounting_service.model.Tax;
import org.gminds.accounting_service.model.enums.TaxType;
import org.gminds.accounting_service.repository.TaxRepository;
import org.gminds.accounting_service.service.taxes.PITCalculator;
import org.gminds.accounting_service.service.taxes.ZUSCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
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
    ZUSCalculator zusProcessor;
    PITCalculator pitCalculator;

    @Parameterized.Parameters(name = "{index}: Test with salary={0}, expectedZusTax={1}, expectedPitTax={2}, fiscalYear={3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {10000, 4319.0, 798.18, 2024},
                {20000, 8638.0, 4185.06, 2024}
        });
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.zusProcessor = new ZUSCalculator(taxRepository);
        this.pitCalculator = new PITCalculator(taxRepository);
    }


    @Test
    public void TaxesCalculateTest() {
        Salary salary = new Salary();
        salary.setAmount(incomeAmount);
        salary.setTransactionDate(LocalDate.ofYearDay(fiscalYear, 15));

        when(taxRepository.findByFiscalYear(fiscalYear)).thenReturn(taxes2024());
        Double zusTaxAmount = zusProcessor.calculateTax(salary);
        assertEquals(expectedZUSTax, zusTaxAmount);

        Double pitTaxAmount = pitCalculator.calculateTax(salary);
        assertEquals(expectedPITTax, pitTaxAmount);
    }

    List<Tax> taxes2024() {
        List<Tax> taxes = new ArrayList<>();

        Tax zus1 = new Tax();
        zus1.setTaxType(TaxType.ZUS);
        zus1.setTaxSubtype("emerytalna pracownika");
        zus1.setFiscalYear(2024);
        zus1.setPercentage(9.76);
        taxes.add(zus1);

        Tax zus2 = new Tax();
        zus2.setTaxType(TaxType.ZUS);
        zus2.setTaxSubtype("emerytalna pracodawcy");
        zus2.setFiscalYear(2024);
        zus2.setPercentage(9.76);
        taxes.add(zus2);

        Tax zus3 = new Tax();
        zus3.setTaxType(TaxType.ZUS);
        zus3.setTaxSubtype("rentowa pracownika");
        zus3.setFiscalYear(2024);
        zus3.setPercentage(1.5);
        taxes.add(zus3);

        Tax zus4 = new Tax();
        zus4.setTaxType(TaxType.ZUS);
        zus4.setTaxSubtype("rentowa pracodawcy");
        zus4.setFiscalYear(2024);
        zus4.setPercentage(6.5);
        taxes.add(zus4);

        Tax zus5 = new Tax();
        zus5.setTaxType(TaxType.ZUS);
        zus5.setTaxSubtype("chorobowa");
        zus5.setFiscalYear(2024);
        zus5.setPercentage(2.45);
        taxes.add(zus5);

        Tax zus6 = new Tax();
        zus6.setTaxType(TaxType.ZUS);
        zus6.setTaxSubtype("wypadkowa");
        zus6.setFiscalYear(2024);
        zus6.setPercentage(1.67);
        taxes.add(zus6);

        Tax zus7 = new Tax();
        zus7.setTaxType(TaxType.ZUS);
        zus7.setTaxSubtype("fundusz pracy");
        zus7.setFiscalYear(2024);
        zus7.setPercentage(2.45);
        taxes.add(zus7);

        Tax zus11 = new Tax();
        zus11.setTaxType(TaxType.ZUS);
        zus11.setTaxSubtype("fundusz gwarantowanych świadczeń pracowniczych");
        zus11.setFiscalYear(2024);
        zus11.setPercentage(0.1);
        taxes.add(zus11);

        Tax zus8 = new Tax();
        zus8.setTaxType(TaxType.HEALTH);
        zus8.setFiscalYear(2024);
        zus8.setPercentage(9.0);
        taxes.add(zus8);

        Tax zus9 = new Tax();
        zus9.setTaxType(TaxType.PIT);
        zus9.setTaxSubtype("first_bracket");
        zus9.setFiscalYear(2024);
        zus9.setPercentage(12.0);
        taxes.add(zus9);

        Tax zus10 = new Tax();
        zus10.setTaxType(TaxType.PIT);
        zus10.setTaxSubtype("second_bracket");
        zus10.setFiscalYear(2024);
        zus10.setPercentage(32.0);
        taxes.add(zus10);
        return taxes;
    }
}
