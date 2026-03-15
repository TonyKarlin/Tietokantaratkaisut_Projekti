# Tietokantaratkaisut_API

`Tony Karlin, Onni Kivinen`

Spring Boot REST API verkkokaupan hallintaan (asiakkaat, tuotteet, tilaukset, toimittajat, osoitteet). Projekti luotu osana `Tietokantaratkaisut`-kurssia.

## Sisältö

- Teknologiat
  - Java 21 + Spring Boot 4
  - Spring WebMVC, Spring Data JPA, HATEOAS
  - MariaDB
- Arkkitehtuuri
  - Controller -> Service -> Repository -> Entity.
  - DTOt
  - HATEOAS- vastauksien luonti Assemblereilla
  - Custom-poikkeukset
- Endpointit (lisätietoa alempana...)
  - CRUD metodit kaikille tarvittaville tietokannan tauluille
  - N:M-suhde Product <-> Supplier
  - Bulk päivitykset tilausten statuspäivityksiin ja toimittajien tuotteiden varaston kasvattamiseen
- Lukitus (Supplier stock update)
  - Jos supplierin linkitetyissä tuotteissa on stock alle 10, sallitaan vain yksi samanaikainen stock update kyseiselle supplierille.
  - Rinnakkainen toinen pyyntö palauttaa HTTP 423 Locked.
- Tietokanta
  - Indeksit (`schema/indexing.sql`)
  - Näkymät (`schema/views.sql`)
  - Liipaisin (`schema/trigger.sql`)

## Ohjeet sovelluksen käyttöön

- Projektissa käytetään pohjana kurssilta saatu `schema_populated_dump.sql`
  - Sekä suorita `/schema`-kansiosta löytyvät `.sql`-tiedostot, jotta tarvittavat muutokset saadaan tietokantaan.
- Luo tietokantakäyttäjä, esimerkki alempana.
- Luo `.env` tiedosto `Verkkokauppa_API` kansion sisälle, käyttäen `.env.template` tiedostoa mallina.
- Aseta `.env` tiedostoon tietokannan nimi, käyttäjä ja salasana.
- Endpointtien testaaminen onnistuu suoraan `.rest`-tiedostojen kautta, mitkä löytyvät `/rest-files`-kansiosta.

## .env esimerkki

```bash
DB_URL=jdbc:mariadb://localhost:3306/{tietokannan nimi}
DB_USER={käyttäjä}
DB_PASSWORD={salasana}
```

## Tietoturva (tietokantakäyttäjä)

Tietoturvan merkeissä, sovelluksessa käytettävän käyttäjän luonti (sama löytyy `/schema/dbuser.sql`):

```sql
USE `tkr-projekti`;

CREATE USER IF NOT EXISTS 'dbuser'@'localhost' IDENTIFIED BY 'tkr-projekti';

GRANT SELECT, INSERT, UPDATE, DELETE, ALTER, CREATE, DROP, INDEX ON `tkr-projekti`.* TO 'dbuser'@'localhost';

FLUSH PRIVILEGES;
```

## Endpoints

Alempana projektin endpointit kuvattuna suppeasti. Tarkemmat tiedot endpointeista löytyvät [ENDPOINTS.md](/ENDPOINTS.md) tiedostosta.

### Index

| Metodi | Endpoint                 |       |
| :----- | :----------------------- | :---- |
| GET    | <http://localhost:8080/> | Index |

### Customers

| Metodi | Endpoint                               |                                     |
| :----- | :------------------------------------- | :---------------------------------- |
| GET    | <http://localhost:8080/customers>      | Asiakkaiden hakeminen               |
| GET    | <http://localhost:8080/customers/{id}> | Yksittäisen asiakkaan haku          |
| POST   | <http://localhost:8080/customers>      | Asiakkaan lisääminen                |
| PUT    | <http://localhost:8080/customers/{id}> | Yksittäisen asiakkaan päivittäminen |
| DELETE | <http://localhost:8080/customers/{id}> | Yksittäisen asiakkaan poistaminen   |

### Addresses

| Metodi | Endpoint                                        |                                     |
| :----- | :---------------------------------------------- | :---------------------------------- |
| GET    | <http://localhost:8080/customer-addresses>      | Osoitteiden hakeminen               |
| GET    | <http://localhost:8080/customer-addresses/{id}> | Yksittäisen osoitteen haku          |
| POST   | <http://localhost:8080/customer-addresses>      | Uuden osoitteen lisääminen          |
| PUT    | <http://localhost:8080/customer-addresses/{id}> | Yksittäisen osoitteen päivittäminen |
| DELETE | <http://localhost:8080/customer-addresses/{id}> | Yksittäisen osoitteen poistaminen   |

### Supplier Addresses

| Metodi | Endpoint                                                        |                                          |
| :----- | :-------------------------------------------------------------- | :--------------------------------------- |
| GET    | <http://localhost:8080/supplieraddresses>                       | Toimittajaosoitteiden hakeminen          |
| GET    | <http://localhost:8080/supplieraddresses/{id}>                  | Yksittäisen toimittajaosoitteen haku     |
| GET    | <http://localhost:8080/supplieraddresses/supplier/{supplierId}> | Toimittajan osoitteiden haku             |
| POST   | <http://localhost:8080/supplieraddresses>                       | Uuden toimittajaosoitteen lisääminen     |
| PUT    | <http://localhost:8080/supplieraddresses/{id}>                  | Yksittäisen toimittajaosoitteen päivitys |
| DELETE | <http://localhost:8080/supplieraddresses/{id}>                  | Yksittäisen toimittajaosoitteen poisto   |

### Products

| Metodi | Endpoint                                                            |                                                |
| :----- | :------------------------------------------------------------------ | :--------------------------------------------- |
| GET    | <http://localhost:8080/products>                                    | Tuotteiden hakeminen                           |
| GET    | <http://localhost:8080/products/{id}>                               | Yksittäisen tuotteen haku                      |
| POST   | <http://localhost:8080/products>                                    | Uuden tuotteen lisääminen                      |
| PUT    | <http://localhost:8080/products/{id}>                               | Yksittäisen tuotteen päivittäminen             |
| DELETE | <http://localhost:8080/products/{id}>                               | Yksittäisen tuotteen poistaminen               |
| GET    | <http://localhost:8080/products/{id}/suppliers>                     | Tuotteen toimittajien haku (N:M-suhde)         |
| POST   | <http://localhost:8080/products/{productId}/suppliers/{supplierId}> | Toimittajan lisääminen tuotteelle (N:M-suhde)  |
| DELETE | <http://localhost:8080/products/{productId}/suppliers/{supplierId}> | Toimittajan poistaminen tuotteelta (N:M-suhde) |
| POST   | <http://localhost:8080/products/search>                             | Dynaaminen Criteria API -haku tuotteille       |

### Product Categories

| Metodi | Endpoint                                                |                                                                 |
| :----- | :------------------------------------------------------ | :-------------------------------------------------------------- |
| GET    | <http://localhost:8080/productcategories>               | Tuotekategorioiden hakeminen                                    |
| GET    | <http://localhost:8080/productcategories/{id}>          | Yksittäisen tuotekategorian haku                                |
| GET    | <http://localhost:8080/productcategories/{id}/products> | Tuotekategorian tuotteiden haku                                 |
| POST   | <http://localhost:8080/productcategories>               | Uuden tuotekategorian lisääminen                                |
| PUT    | <http://localhost:8080/productcategories/{id}>          | Yksittäisen tuotekategorian päivittäminen                       |
| DELETE | <http://localhost:8080/productcategories/{id}>          | Yksittäisen tuotekategorian poistaminen (poistaa myös tuotteet) |

### Orders

| Metodi | Endpoint                                                |                                               |
| :----- | :------------------------------------------------------ | :-------------------------------------------- |
| GET    | <http://localhost:8080/orders>                          | Tilausten hakeminen                           |
| GET    | <http://localhost:8080/orders/{id}>                     | Yksittäisen tilauksen haku                    |
| GET    | <http://localhost:8080/orders/by-customer/{customerId}> | Asiakkaan tilausten haku                      |
| POST   | <http://localhost:8080/orders>                          | Uuden tilauksen lisääminen                    |
| PUT    | <http://localhost:8080/orders/{id}>                     | Tilauksen päivittäminen                       |
| PATCH  | <http://localhost:8080/orders/batch/status>             | Tilausten statusten päivitys (bulk)           |
| PATCH  | <http://localhost:8080/orders/batch/status/by-customer> | Asiakkaan tilausten statusten päivitys (bulk) |
| DELETE | <http://localhost:8080/orders/{id}>                     | Yksittäisen tilauksen poistaminen             |

### Order Items

| Metodi | Endpoint                                                           |                                           |
| :----- | :----------------------------------------------------------------- | :---------------------------------------- |
| GET    | <http://localhost:8080/order-items>                                | Tilausrivien hakeminen                    |
| GET    | <http://localhost:8080/order-items/{orderId}/product/{productId}>  | Yksittäisen tilausrivin haku              |
| POST   | <http://localhost:8080/order-items>                                | Uuden tilausrivin lisääminen              |
| PUT    | <http://localhost:8080/order-items/{orderId}/product/{productId}>  | Tilausrivin tietojen päivittäminen        |
| PATCH  | <http://localhost:8080/order-items/{orderId}/discount?discount=10> | Alennuksen lisääminen tilauksen riveille  |
| PATCH  | <http://localhost:8080/order-items/{orderId}/remove-discount>      | Alennuksen poistaminen tilauksen riveiltä |
| DELETE | <http://localhost:8080/order-items/{orderId}/product/{productId}>  | Tilausrivin poistaminen                   |

### Order Totals (NÄKYMÄ)

| Metodi | Endpoint                                                      |                                              |
| :----- | :------------------------------------------------------------ | :------------------------------------------- |
| GET    | <http://localhost:8080/order-totals/{orderId}>                | Tilauksen kokonaistiedot näkymästä           |
| GET    | <http://localhost:8080/order-totals/by-customer/{customerId}> | Asiakkaan tilausten kokonaistiedot näkymästä |

### Suppliers

| Metodi | Endpoint                                                                         |                                                           |
| :----- | :------------------------------------------------------------------------------- | :-------------------------------------------------------- |
| GET    | <http://localhost:8080/suppliers>                                                | Toimittajien hakeminen                                    |
| GET    | <http://localhost:8080/suppliers/{id}>                                           | Yksittäisen toimittajan haku                              |
| POST   | <http://localhost:8080/suppliers>                                                | Uuden toimittajan lisääminen                              |
| PUT    | <http://localhost:8080/suppliers/{id}>                                           | Yksittäisen toimittajan päivittäminen                     |
| DELETE | <http://localhost:8080/suppliers/{id}>                                           | Yksittäisen toimittajan poistaminen                       |
| GET    | <http://localhost:8080/suppliers/{id}/products>                                  | Toimittajan tuotteiden haku (N:M-suhde)                   |
| POST   | <http://localhost:8080/suppliers/{supplierId}/products/{productId}>              | Tuotteen lisääminen toimittajalle (N:M-suhde)             |
| DELETE | <http://localhost:8080/suppliers/{supplierId}/products/{productId}>              | Tuotteen poistaminen toimittajalta (N:M-suhde)            |
| GET    | <http://localhost:8080/suppliers/{supplierId}/products/stock>                    | Toimittajan tuotteiden varastotietojen haku               |
| PATCH  | <http://localhost:8080/suppliers/{supplierId}/products/stock/increase?amount=10> | Toimittajan tuotteiden varaston kasvatus (massaoperaatio) |
