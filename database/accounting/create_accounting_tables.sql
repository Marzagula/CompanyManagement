CREATE TABLE PAYMENT_RANGE (
    ID BIGSERIAL PRIMARY KEY,
    MIN_SALARY DECIMAL(19,2) NOT NULL,
    MAX_SALARY DECIMAL(19,2) NOT NULL,
    JOB_ID BIGINT NOT NULL,
    EMPLOYMENT_AGREEMENT_TYPE VARCHAR(50) NOT NULL,
    FISCAL_YEAR INTEGER NOT NULL,
    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_BY VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP
);

CREATE TABLE ledger_account (
    id BIGSERIAL PRIMARY KEY,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    account_number VARCHAR(255) NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    plan VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    start_date DATE,
    end_date DATE
);

CREATE TABLE transaction (
    id BIGSERIAL PRIMARY KEY,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    transaction_date DATE,
    description VARCHAR(255),
    counterparty VARCHAR(255),
    invoice_number VARCHAR(255),
    vat_tax DOUBLE PRECISION,
    amount DECIMAL(19,2),
    invoice_type VARCHAR(255),
    employee_id BIGINT,
    transaction_type VARCHAR(255) NOT NULL,
    ledger_account_id BIGINT,
    CONSTRAINT fk_ledger_account FOREIGN KEY (ledger_account_id) REFERENCES ledger_account(id),
    CONSTRAINT transaction_type_check CHECK (transaction_type IN ('INVOICE', 'SALARY'))
);

CREATE INDEX idx_transaction_type ON transaction (transaction_type);
CREATE INDEX idx_transaction_date ON transaction (transaction_date);
CREATE INDEX idx_transaction_id ON transaction (id);
