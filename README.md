# Tietokantaratkaisut_API

## Tietokanta info

Luo `.env` tiedosto `Verkkokauppa_API` kansion sisälle, käyttäen `.env.template` tiedostoa mallina.

Aseta tietokannan nimi, käyttäjä ja salasana.

```bash
DB_URL=jdbc:mariadb://localhost:3306/{tietokannan nimi}
DB_USER={käyttäjä}
DB_PASSWORD={salasana}
```

Tietoturvan merkeissä, sovelluksessa käytettävän käyttäjän luonti:

```sql
USE `tkr-projekti`;

CREATE USER IF NOT EXISTS 'dbuser'@'localhost' IDENTIFIED BY 'tkr-projekti';

GRANT SELECT, INSERT, UPDATE, DELETE, ALTER, CREATE, DROP, INDEX ON `tkr-projekti`.* TO 'dbuser'@'localhost';

FLUSH PRIVILEGES;
```

Tietokannassa on käytetty `schema_populated_dump` skeemaa. Ja tämän lisäksi erillisenä ajettu `schema-changes.sql` tiedostoa, joka sisältää tarvittavat muutokset tietokantaan.

`schema-changes.sql` tiedosto löytyy schema-kansiosta.

## Endpoints

Endpointit löytyvät [ENDPOINTS.md](/ENDPOINTS.md) tiedostosta.
