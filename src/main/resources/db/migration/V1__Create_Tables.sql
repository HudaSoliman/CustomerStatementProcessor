

CREATE TABLE IF NOT EXISTS account  (
  account_number      VARCHAR                 NOT NULL PRIMARY KEY,
  balance             DECIMAL(16,4)           NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction(
  reference      BIGINT                      NOT NULL PRIMARY KEY,
  account_number varchar(255)                NOT NULL,
  start_balance  DECIMAL(16,4)               NOT NULL,
  end_balance    DECIMAL(16,4)               NOT NULL,
  mutation       DECIMAL(16,4)               NOT NULL,
  description    TEXT                        NOT NULL
);