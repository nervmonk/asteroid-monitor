CREATE TABLE `asteroid_data` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `approach_date` varchar(255) DEFAULT NULL,
  `distance_from_earth_km` varchar(255) DEFAULT NULL,
  `relative_velocity_kph` varchar(255) DEFAULT NULL,
  `is_hazardous` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `asteroid_diameter` (
  `asteroid_id` varchar(255) NOT NULL,
  `min_diameter` decimal(15,10) DEFAULT NULL,
  `max_diameter` decimal(15,10) DEFAULT NULL,
  PRIMARY KEY (`asteroid_id`),
  CONSTRAINT `asteroid_diameter_ibfk_1` FOREIGN KEY (`asteroid_id`) REFERENCES `asteroid_data` (`id`)
) ENGINE=InnoDB;