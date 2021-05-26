

CREATE TABLE IF NOT EXISTS account  (
  account_number      VARCHAR                 NOT NULL PRIMARY KEY,
  balance             DECIMAL(16,4)           NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction(
  reference      BIGINT                      NOT NULL PRIMARY KEY,
  account_number VARCHAR                     NOT NULL,
  start_balance  DECIMAL(16,4)               NOT NULL,
  end_balance    DECIMAL(16,4)               NOT NULL,
  mutation       DECIMAL(16,4)               NOT NULL,
  description    TEXT                        NOT NULL
  -- CONSTRAINT     `fk_acc_tr_acc_num`         FOREIGN KEY (account_number)    REFERENCES account(account_number) ON DELETE CASCADE

);