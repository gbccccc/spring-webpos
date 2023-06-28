CREATE DATABASE IF NOT EXISTS webpos;

USE webpos;

DROP TABLE IF EXISTS products;

CREATE TABLE products
(
    id       VARCHAR(20) NOT NULL,
    title    TEXT DEFAULT NULL,
    main_cat TEXT DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    id       VARCHAR(20) NOT NULL,
    category TEXT DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS image_url_highres;

CREATE TABLE image_url_highres
(
    id                VARCHAR(20) NOT NULL,
    image_url_highres TEXT DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS order_info;

CREATE TABLE order_info
(
    id       VARCHAR(20) NOT NULL,
    userId   VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS order_detail;

CREATE TABLE order_detail
(
    orderId       VARCHAR(20) NOT NULL,
    itemAsin VARCHAR(20) NOT NULL,
    itemNum  INT DEFAULT 1
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;