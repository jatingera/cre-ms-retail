-- --------------------------------------------------------------------------------
-- tenx_cre_ms_retail
-- --------------------------------------------------------------------------------


CREATE TABLE store (
  store_id                       BIGINT             NOT NULL AUTO_INCREMENT COMMENT 'store id',
  name                           VARCHAR (255)      NOT NULL COMMENT 'name of the store',
    created_by     BIGINT         NOT NULL        COMMENT 'User who created this row, 0 is system created.  FK to tenx_user table',
    created_date   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT 'Date and time this row was created',
    updated_by     BIGINT         NOT NULL        COMMENT 'User who last updated this row, 0 is system updated.  FK to tenx_user table',
    updated_date   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT 'Date and time this row was last updated',
    test_run_id    VARCHAR(100)  DEFAULT NULL,
  PRIMARY KEY (store_id)
) COMMENT 'Unique Store List',
   ENGINE = InnoDB
   AUTO_INCREMENT = 1;


CREATE TABLE product (
    product_id                   BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'product id',
    store_id                     BIGINT       NOT NULL COMMENT 'FK to store table',
    name                         VARCHAR(100) NOT NULL COMMENT 'name of the product',
    description                  VARCHAR(255) COMMENT 'Description of the product',
    sku                          VARCHAR(10)  COMMENT 'sku of production',
    price                        DECIMAL(10,2) COMMENT 'price of the product',
    created_by     BIGINT         NOT NULL        COMMENT 'User who created this row, 0 is system created.  FK to tenx_user table',
    created_date   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT 'Date and time this row was created',
    updated_by     BIGINT         NOT NULL        COMMENT 'User who last updated this row, 0 is system updated.  FK to tenx_user table',
    updated_date   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT 'Date and time this row was last updated',
    test_run_id    VARCHAR(100)  DEFAULT NULL,
    PRIMARY KEY (product_id),
    KEY `fk_product_store_id_idx` (`store_id`),
    CONSTRAINT `fk_product_store_id_idx` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) COMMENT 'Unique Product List',
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE  orders (
    order_id                       BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'order id',
    store_id                       BIGINT       NOT NULL COMMENT 'FK to store table',
    order_date                     TIMESTAMP    NULL,
    order_status                         ENUM('ORDERED','PACKING','SHIPPED')   DEFAULT NULL ,
    first_name                     VARCHAR(100) NOT NULL COMMENT 'Purchaser name',
    last_name                      VARCHAR(100) NOT NULL COMMENT 'Purchaser last name',
    email                          VARCHAR(100) DEFAULT NULL COMMENT 'Email name',
    phone                   VARCHAR(100) DEFAULT NULL COMMENT 'Phone number',
    created_by     BIGINT         NOT NULL        COMMENT 'User who created this row, 0 is system created.  FK to tenx_user table',
    created_date   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT 'Date and time this row was created',
    updated_by     BIGINT         NOT NULL        COMMENT 'User who last updated this row, 0 is system updated.  FK to tenx_user table',
    updated_date   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT 'Date and time this row was last updated',
    test_run_id    VARCHAR(100)  DEFAULT NULL,
    PRIMARY KEY (order_id)
) COMMENT 'Unique Store List',
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;


 CREATE TABLE stock (
    stock_id                       BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'stock id',
    store_id                       BIGINT       NOT NULL COMMENT 'FK to store table',
    product_id                     BIGINT       NOT NULL COMMENT 'FK to product table',
    count                          INT(100)       NOT NULL DEFAULT 0 COMMENT 'Quantity of the items',
     created_by     BIGINT         NOT NULL        COMMENT 'User who created this row, 0 is system created.  FK to tenx_user table',
     created_date   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT 'Date and time this row was created',
     updated_by     BIGINT         NOT NULL        COMMENT 'User who last updated this row, 0 is system updated.  FK to tenx_user table',
     updated_date   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT 'Date and time this row was last updated',
     test_run_id    VARCHAR(100)  DEFAULT NULL,
    PRIMARY KEY (stock_id),
    CONSTRAINT `fk_stock_store` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`),
    CONSTRAINT `fk_stock_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) COMMENT 'Unique stock List',
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;


CREATE TABLE order_item (
    order_item_id                  BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'order item id',
    order_id                     BIGINT         NOT NULL COMMENT 'FK to order table',
    product_id                     BIGINT       NOT NULL COMMENT 'FK to product table',
    quantity                       INT(100)     NOT NULL DEFAULT 0 COMMENT 'Quantity of the items',
   /* is_item_backordered            TINYINT      NOT NULL DEFAULT 0,*/
    is_item_backordered           TINYINT(1)      NOT NULL ,
    created_by     BIGINT         NOT NULL        COMMENT 'User who created this row, 0 is system created.  FK to tenx_user table',
    created_date   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT 'Date and time this row was created',
    updated_by     BIGINT         NOT NULL        COMMENT 'User who last updated this row, 0 is system updated.  FK to tenx_user table',
    updated_date   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT 'Date and time this row was last updated',
    test_run_id    VARCHAR(100)  DEFAULT NULL,
    PRIMARY KEY (order_item_id),
    CONSTRAINT `fk_orderitem_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
  ) COMMENT 'Unique ordered summary List',
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;
