CREATE TABLE `entidad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nit` varchar(100) NOT NULL,
  `nombre` varchar(500) NOT NULL,
  `autonomo` bit(1) DEFAULT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `nit_UNIQUE` (`nit`)
) ENGINE=InnoDB AUTO_INCREMENT=297 DEFAULT CHARSET=latin1;

CREATE TABLE `estrategia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_entidad` int(11) NOT NULL,
  `codigo` varchar(100) NOT NULL,
  `nombre` varchar(500) NOT NULL,
  `multientidad` bit(1) DEFAULT NULL,
  `complejidad` varchar(100) DEFAULT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `codigo_UNIQUE` (`codigo`),
  KEY `fk_entidad_idx` (`id_entidad`),
  CONSTRAINT `fk_entidad_estrategia` FOREIGN KEY (`id_entidad`) REFERENCES `entidad` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=738 DEFAULT CHARSET=latin1;

CREATE TABLE `requerimiento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_estrategia` int(11) DEFAULT NULL,
  `prod` varchar(100) NOT NULL,
  `estado` varchar(100) NOT NULL,
  `fecha_radicado` date DEFAULT NULL,
  `fecha_ultimo_movimiento` date DEFAULT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_estrategia_idx` (`id_estrategia`),
  CONSTRAINT `fk_estrategia_req` FOREIGN KEY (`id_estrategia`) REFERENCES `estrategia` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4086 DEFAULT CHARSET=latin1;

CREATE TABLE `transacciones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_estrategia` int(11) DEFAULT NULL,
  `nit` varchar(100) DEFAULT NULL,
  `entidad` varchar(500) DEFAULT NULL,
  `transacciones` int(11) NOT NULL,
  `anio` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fr_estrategia_transacciones_idx` (`id_estrategia`),
  CONSTRAINT `fr_estrategia_transacciones` FOREIGN KEY (`id_estrategia`) REFERENCES `estrategia` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=latin1;

CREATE TABLE `migracion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_estrategia` int(11) DEFAULT NULL,
  `cq_2016` int(11) DEFAULT NULL,
  `cq_2017` int(11) DEFAULT NULL,
  `fecha_ultimo_cambio` date DEFAULT NULL,
  `frecuencia_cambios` varchar(100) DEFAULT NULL,
  `complejidad` varchar(100) DEFAULT NULL,
  `transacciones` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_estrategia_mig_idx` (`id_estrategia`),
  CONSTRAINT `fk_estrategia_mig` FOREIGN KEY (`id_estrategia`) REFERENCES `estrategia` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1215 DEFAULT CHARSET=latin1;

