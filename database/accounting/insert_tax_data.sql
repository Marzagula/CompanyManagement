-- ZUS składki
INSERT INTO tax (tax_category, tax_subcategory, percentage, fiscal_year, created_date, last_modified_date)
VALUES
('ZUS', 'emerytalna pracownika', 9.76, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ZUS', 'emerytalna pracodawcy', 9.76, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ZUS', 'rentowa pracownika', 1.50, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ZUS', 'rentowa pracodawcy', 6.50, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ZUS', 'chorobowa', 2.45, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ZUS', 'wypadkowa', 1.67, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ZUS', 'fundusz pracy', 2.45, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ZUS', 'fundusz gwarantowanych świadczeń pracowniczych', 0.10, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Podatek dochodowy PIT (pierwszy próg 17%, drugi próg 32%)
INSERT INTO tax (tax_category, tax_subcategory, percentage, fiscal_year, created_date, last_modified_date)
VALUES
('PIT', 'first_bracket', 12.00, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PIT', 'second_bracket', 32.00, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Składka zdrowotna
INSERT INTO tax (tax_category, tax_subcategory, percentage, fiscal_year, created_date, last_modified_date)
VALUES
('HEALTH', NULL, 9.00, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- VAT example (w Polsce standardowa stawka VAT wynosi 23%)
INSERT INTO tax (tax_category, tax_subcategory, percentage, fiscal_year, created_date, last_modified_date)
VALUES
('VAT', NULL, 23.00, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO fiscal_value (fiscalYear, taxType, taxSubtype, limitValue, limitCondition, description)
VALUES
    (2024, 'TAX_BRACKET', 'first_bracket', 120000.00, 'LESS_EQUAL', 'Maksymalny dochód dla pierwszego progu podatkowego'),
    (2024, 'TAX_BRACKET', 'second_bracket', 120000.00, 'MORE', 'Dochód powyżej pierwszego progu, dla drugiego progu'),
    (2024, 'TAX_DEDUCTION', 'maximum_deduction', 3600.00, NULL, 'Maksymalna kwota odliczenia od podatku PIT w roku'),
    (2024, 'COST', 'employee_costs', 250.00, NULL, 'Koszt pracownika miesięcznie gdy pracuje w miejscu zamieszkania');
    (2024, 'COST', 'employee_costs_2', 300.00, NULL, 'Koszt pracownika miesięcznie gdy nie pracuje w miejscu zamieszkania');
    (2024, 'LIMIT', 'retirement_contribution_limit', 234720.0, NULL, 'Roczne ograniczenie podstawy wymiaru składek na ubezpieczenie emerytalne i rentowe');
