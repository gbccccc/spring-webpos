CREATE DATABASE IF NOT EXISTS deliveryDB;

USE deliveryDB;

DROP TABLE IF EXISTS delivery;

CREATE TABLE delivery
(
    deliveryId      VARCHAR(20) NOT NULL,
    orderId VARCHAR(20) NOT NULL,
    PRIMARY KEY (deliveryId)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;