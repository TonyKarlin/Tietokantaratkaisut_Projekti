USE `tkr-projekti`;

CREATE USER IF NOT EXISTS 'dbuser'@'localhost' IDENTIFIED BY 'tkr-projekti';

GRANT SELECT, INSERT, UPDATE, DELETE, ALTER, CREATE, DROP, INDEX ON `tkr-projekti`.* TO 'dbuser'@'localhost';

FLUSH PRIVILEGES;