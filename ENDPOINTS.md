# Endpoints

Projektin endpointit:

## CUSTOMERS

GET - haetaan kaikki asiakkaat (50 per sivu).

```HTTP
GET http://localhost:8080/customers
```

GET - haetaan asiakas id:n perusteella.

```HTTP
GET http://localhost:8080/customers/{id}
```

POST - uuden asiakkaan lisääminen.

```HTTP
POST http://localhost:8080/customers
Content-Type: application/json
{
  "firstName": "TESTI",
  "lastName": "KAYTTAJA",
  "email": "test@example.org",
  "phoneNumber": "1234567890" // valinnainen
}
```

PUT - asiakkaan tietojen päivittäminen id:n perusteella.

```HTTP
PUT http://localhost:8080/customers/{id}
Content-Type: application/json
{
  "firstName": "PAIVITETTY",
  "lastName": "KAYTTAJA",
  "email": "updated@example.org",
  "phoneNumber": "0987654321"
}
```

DELETE - asiakkaan poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/customers/{id}
```

## ADDRESSES

GET - haetaan kaikki osoitteet.

```HTTP
GET http://localhost:8080/customer-addresses
```

GET - haetaan osoite id:n perusteella.

```HTTP
GET http://localhost:8080/customer-addresses/{id}
```

POST - uuden osoitteen lisääminen.

```HTTP
POST http://localhost:8080/customer-addresses
Content-Type: application/json

{
  "customerId": 100002,
  "street": "Uudenmaankatu 10",
  "postalCode": "00120",
  "city": "Helsinki",
  "country": "Finland"
}
```

PUT - osoitteen tietojen päivittäminen id:n perusteella.

```HTTP
PUT http://localhost:8080/customer-addresses/{id}
Content-Type: application/json

{
  "customerId": 100002,
  "street": "Malminkaari 2",
  "postalCode": "00700",
  "city": "Helsinki",
  "country": "Finland"
}
```

DELETE - osoitteen poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/customer-addresses/{id}
```

## PRODUCTS

GET - haetaan kaikki tuotteet.

```HTTP
GET http://localhost:8080/products
```

GET - haetaan tuote id:n perusteella.

```HTTP
GET http://localhost:8080/products/{id}
```

POST - uuden tuotteen lisääminen.

```HTTP
POST http://localhost:8080/products
Content-Type: application/json

{
  "categoryId": 1,
  "description": "TESTITESTI",
  "name": "TESTITUOTE",
  "price": 100.50,
  "stockQuantity": 10,
  "supplierId": 1
}
```

PUT - tuotteen tietojen päivittäminen id:n perusteella.

```HTTP
PUT http://localhost:8080/products/{id}
Content-Type: application/json

{
  "categoryId": 1,
  "description": "PÄIVITETTY KUVAUS",
  "name": "PÄIVITETTY TUOTE",
  "price": 150.75,
  "stockQuantity": 20,
  "supplierId": 1
}
```

DELETE - tuotteen poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/products/{id}
```

### N:M-SUHDE: Product ↔ Supplier

GET - haetaan tuotteen kaikki toimittajat (N:M-suhde). Alussa saattaa olla vastauksena tyhjä lista, koska tuotetta ja jälleenmyyjää ei ole linkitetty POST metodin kautta, mikä on tässä alempana.

```HTTP
GET http://localhost:8080/products/{id}/suppliers
```

POST - lisätään toimittaja tuotteelle (N:M-suhde).

```HTTP
POST http://localhost:8080/products/{productId}/suppliers/{supplierId}
```

DELETE - poistetaan toimittaja tuotteelta (N:M-suhde).

```HTTP
DELETE http://localhost:8080/products/{productId}/suppliers/{supplierId}
```

## PRODUCT CATEGORIES

GET - haetaan kaikki tuotekategoriat (50 per sivu).

```HTTP
GET http://localhost:8080/productcategories
```

GET - haetaan tuotekategoria id:n perusteella.

```HTTP
GET http://localhost:8080/productcategories/{id}
```

GET - haetaan tuotekategoria ja sen kaikki tuotteet (LAZY loading).

```HTTP
GET http://localhost:8080/productcategories/{id}/products
```

POST - uuden tuotekategorian lisääminen.

```HTTP
POST http://localhost:8080/productcategories
Content-Type: application/json

{
  "name": "testi",
  "description": "testikategoria"
}
```

PUT - tuotekategorian päivittäminen id:n perusteella.

```HTTP
PUT http://localhost:8080/productcategories/{id}
Content-Type: application/json

{
  "name": "testipäivitys",
  "description": "testikategoriapäivitys"
}
```

DELETE - tuotekategorian poistaminen (HUOM. POISTAA SAMALLA KAIKKI TUOTTEET SEN KATEGORIASTA)

```HTTP
DELETE http://localhost:8080/productcategories/{id}
```

## ORDERS

GET - haetaan kaikki tilaukset.

```HTTP
GET http://localhost:8080/orders
```

GET - haetaan tilaus id:n perusteella.

```HTTP
GET http://localhost:8080/orders/{id}
```

GET - haetaan tilaukset asiakkaan id:n perusteella.

```HTTP
GET http://localhost:8080/orders/by-customer/{customerId}
```

POST - uuden tilauksen lisääminen.

```HTTP
POST http://localhost:8080/orders
Content-Type: application/json

{
  "customerId": 1,
  "orderDate": "2024-06-02T00:00:00",
  "deliveryDate": "2024-06-05T00:00:00",
  "shippingAddressId": 1,
  "status": "NEW"
}
```

## SUPPLIERS

GET - haetaan kaikki toimittajat (50 per sivu).

```HTTP
GET http://localhost:8080/suppliers
```

GET - haetaan toimittaja id:n perusteella.

```HTTP
GET http://localhost:8080/suppliers/{id}
```

POST - uuden toimittajan lisääminen.

```HTTP
POST http://localhost:8080/suppliers
Content-Type: application/json

{
  "name": "Tech Wholesale Oy",
  "contactName": "Matti Meikäläinen",
  "phone": "+358401234567",
  "email": "matti@tech.fi"
}
```

PUT - toimittajan tietojen päivittäminen id:n perusteella.

```HTTP
PUT http://localhost:8080/suppliers/{id}
Content-Type: application/json

{
  "name": "Päivitetty yritys",
  "contactName": "Liisa Virtanen",
  "phone": "+358409876543",
  "email": "liisa@updated.fi"
}
```

DELETE - toimittajan poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/suppliers/{id}
```

### N:M-SUHDE: Supplier ↔ Product

GET - haetaan toimittajan kaikki tuotteet (N:M-suhde).

```HTTP
GET http://localhost:8080/suppliers/{id}/products
```

POST - lisätään tuote toimittajalle (N:M-suhde).

```HTTP
POST http://localhost:8080/suppliers/{supplierId}/products/{productId}
```

DELETE - poistetaan tuote toimittajalta (N:M-suhde).

```HTTP
DELETE http://localhost:8080/suppliers/{supplierId}/products/{productId}
```

PATCH - massaoperaatio: kasvatetaan toimittajan kaikkien tuotteiden `stockQuantity`-arvoa.

```HTTP
PATCH http://localhost:8080/suppliers/{supplierId}/products/stock/increase?amount=10
```

## PRODUCTS (Criteria API haku)

POST - dynaaminen haku tuotteille usealla ehdolla (Criteria API).

```HTTP
POST http://localhost:8080/products/search
Content-Type: application/json

{
  "nameContains": "test",
  "minPrice": 10.00,
  "maxPrice": 500.00,
  "minStock": 5,
  "categoryId": 1,
  "supplierId": 1
}
```
