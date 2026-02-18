CREATE DATABASE prog3_pushdown;

CREATE USER prog3_user WITH PASSWORD 'prog3_pass';
GRANT ALL PRIVILEGES ON DATABASE prog3_pushdown TO prog3_user;

\c prog3_pushdown

GRANT ALL ON SCHEMA public TO prog3_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO prog3_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO prog3_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL ON TABLES TO prog3_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL ON SEQUENCES TO prog3_user;


CREATE TYPE invoice_status AS ENUM ('DRAFT', 'CONFIRMED', 'PAID');

CREATE TABLE invoice (
                         id SERIAL PRIMARY KEY,
                         customer_name VARCHAR(100) NOT NULL,
                         status invoice_status NOT NULL
);

CREATE TABLE invoice_line (
                              id SERIAL PRIMARY KEY,
                              invoice_id INT NOT NULL REFERENCES invoice(id) ON DELETE CASCADE,
                              label VARCHAR(150) NOT NULL,
                              quantity INT NOT NULL CHECK (quantity > 0),
                              unit_price NUMERIC(10,2) NOT NULL CHECK (unit_price >= 0)
);

-- CREATE INDEX idx_invoice_line_invoice_id ON invoice_line(invoice_id);
