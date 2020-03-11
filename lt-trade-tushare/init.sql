CREATE TABLE `lt_daily_basic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ts_code` varchar(12) DEFAULT NULL,
  `trade_date` varchar(20) DEFAULT NULL,
  `turnover_rate` double(20,4) DEFAULT NULL,
  `turnover_rate_f` double(20,4) DEFAULT NULL,
  `volume_ratio` double(20,4) DEFAULT NULL,
  `ps` double(20,4) DEFAULT NULL,
  `ps_ttm` double(20,4) DEFAULT NULL,
  `total_share` double(20,4) DEFAULT NULL,
  `float_share` double(20,4) DEFAULT NULL,
  `free_share` double(20,4) DEFAULT NULL,
  `total_mv` double(20,4) DEFAULT NULL,
  `circ_mv` double(20,4) DEFAULT NULL,
  `pct_chg` double(20,4) DEFAULT NULL,
  `emphasis` char(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26884 DEFAULT CHARSET=utf8;

CREATE TABLE `lt_fund_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stock_code` varchar(15) DEFAULT NULL,
  `makers_fund_in` double(20,2) DEFAULT NULL,
  `makers_fund_out` double(20,2) DEFAULT NULL,
  `makers_in_flow` double(20,2) DEFAULT NULL,
  `retail_funt_in` double(20,2) DEFAULT NULL,
  `retail_funt_out` double(20,2) DEFAULT NULL,
  `retail_in_flow` double(20,2) DEFAULT NULL,
  `amounts` double(20,2) DEFAULT NULL,
  `redo_minute_pct` text,
  `redo_all_pct` double(10,4) DEFAULT NULL,
  `create_time` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20306 DEFAULT CHARSET=utf8;

CREATE TABLE `lt_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `lt_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `lt_role_permission` (
  `id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `lt_stock_basic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ts_code` varchar(10) DEFAULT NULL,
  `symbol` varchar(10) DEFAULT NULL,
  `name` varchar(15) DEFAULT NULL,
  `area` varchar(10) DEFAULT NULL,
  `industry` varchar(20) DEFAULT NULL,
  `market` varchar(10) DEFAULT NULL,
  `list_status` varchar(10) DEFAULT NULL,
  `is_hs` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5983 DEFAULT CHARSET=utf8;

CREATE TABLE `lt_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) DEFAULT NULL,
  `pass_word` varchar(32) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `lt_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;