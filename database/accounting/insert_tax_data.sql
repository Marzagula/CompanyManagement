-- ZUS składki
INSERT INTO tax (tax_type, tax_subtype, percentage, fiscal_year, created_date, last_modified_date)
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
INSERT INTO tax (tax_type, tax_subtype, percentage, fiscal_year, created_date, last_modified_date)
VALUES
('PIT', 'first_bracket', 12.00, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PIT', 'second_bracket', 32.00, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Składka zdrowotna
INSERT INTO tax (tax_type, tax_subtype, percentage, fiscal_year, created_date, last_modified_date)
VALUES
('HEALTH', NULL, 9.00, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- VAT example (w Polsce standardowa stawka VAT wynosi 23%)
INSERT INTO tax (tax_type, tax_subtype, percentage, fiscal_year, created_date, last_modified_date)
VALUES
('VAT', NULL, 23.00, 2024, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
