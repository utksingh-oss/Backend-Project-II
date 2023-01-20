CREATE TABLE IF NOT EXISTS customer(
	customer_id BIGSERIAL PRIMARY KEY,
	name VARCHAR(100),
	address VARCHAR(100),
	emailId VARCHAR(100),
	contact_number VARCHAR(13),
	credit_limit numeric,
	loaned_amount numeric
);

CREATE TABLE IF NOT EXISTS merchant(
	merchant_id BIGSERIAL PRIMARY KEY,
	name VARCHAR(100),
	address VARCHAR(100),
	emailId VARCHAR(100),
	contact_number VARCHAR(13)
);


CREATE TABLE IF NOT EXISTS debit(
	debit_id BIGSERIAL PRIMARY KEY,
	customer_id BIGINT NOT NULL,
  	merchant_id BIGINT NOT NULL,
	debit_date DATE NOT NULL,
	debit_amount REAL NOT NULL,
	remaining_amount REAL NOT NULL,
  	CONSTRAINT fk_customer
      FOREIGN KEY(customer_id)
	  REFERENCES customer(customer_id),
    CONSTRAINT fk_merchant
      FOREIGN KEY(merchant_id)
	  REFERENCES merchant(merchant_id)
);


CREATE TABLE IF NOT EXISTS credit(
	credit_id BIGSERIAL PRIMARY KEY,
	customer_id BIGINT NOT NULL,
	credit_date DATE NOT NULL,
	credit_amount REAL NOT NULL,
    CONSTRAINT fk_customer
          FOREIGN KEY(customer_id)
    	  REFERENCES customer(customer_id)
);


CREATE TABLE IF NOT EXISTS debit_credit_link(
    link_id BIGSERIAL PRIMARY KEY,
    credit_id BIGINT NOT NULL,
    debit_id BIGINT NOT NULL,
    CONSTRAINT fk_credit
        FOREIGN KEY(credit_id)
        REFERENCES credit(credit_id),
    CONSTRAINT fk_debit
        FOREIGN KEY(debit_id)
        REFERENCES debit(debit_id)
);