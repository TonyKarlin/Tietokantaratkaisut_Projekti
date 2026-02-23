# Tietokantaratkaisut_API

## Tietokanta info

Sovelluksen käyttäjän luonti.

```sql
USE `tkr-projekti`;

CREATE USER IF NOT EXISTS 'dbuser'@'localhost' IDENTIFIED BY 'tkr-projekti';

GRANT SELECT, INSERT, UPDATE ON `tkr-projekti`.* TO 'dbuser'@'localhost';
```
