CREATE TABLE tax_config (
    id SERIAL PRIMARY KEY,
    label VARCHAR(100) NOT NULL UNIQUE,
    rate NUMERIC(5,2) NOT NULL CHECK (rate >= 0)
);

INSERT INTO tax_config (label, rate) VALUES
    ('TVA STANDARD', 20);

