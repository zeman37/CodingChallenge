CREATE TABLE IF NOT EXISTS monetary_operations
(
    id              INTEGER GENERATED BY DEFAULT AS IDENTITY,
    account_number  BIGINT NOT NULL,
    operation_date  DATETIME NOT NULL ,
    beneficiary     VARCHAR(255) NOT NULL ,
    comment         VARCHAR(255),
    amount          DOUBLE NOT NULL,
    currency        VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);