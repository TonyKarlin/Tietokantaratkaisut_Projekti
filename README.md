# Tietokantaratkaisut_API

## Tietokanta info

Luo `.env` tiedosto `Verkkokauppa_API` kansion sisälle, käyttäen `.env.template` tiedostoa mallina.

Aseta tietokannan nimi, käyttäjä ja salasana.

```bash
DB_URL=jdbc:mariadb://localhost:3306/{tietokannan nimi}
DB_USER={käyttäjä}
DB_PASSWORD={salasana}
```

Tyypillisen käyttäjän luonti:

```sql
USE `tkr-projekti`;

CREATE USER IF NOT EXISTS 'dbuser'@'localhost' IDENTIFIED BY 'tkr-projekti';

GRANT SELECT, INSERT, UPDATE, DELETE, ALTER, CREATE, DROP, INDEX ON `tkr-projekti`.* TO 'dbuser'@'localhost';

FLUSH PRIVILEGES;
```

## Endpoints

Endpointit löytyvät [ENDPOINTS.md](/ENDPOINTS.md) tiedostosta.
