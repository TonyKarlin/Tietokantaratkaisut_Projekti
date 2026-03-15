# Endpoints

Projektin endpointit:

## INDEX

GET - Index

```HTTP
GET http://localhost:8080/
```

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
```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```
```JSON
{
  "content": [
    {
      "city": "Helsinki",
      "country": "Finland",
      "customerId": 1,
      "id": 1,
      "postalCode": "00120",
      "streetAddress": "Uudenmaankatu 10"
    }
  ]
}
```

GET - haetaan osoite id:n perusteella.

```HTTP
GET http://localhost:8080/customer-addresses/{id}
```
```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```
```JSON
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/customer-addresses/1"
    },
    "addresses": {
      "href": "http://localhost:8080/customer-addresses"
    }
  },
  "city": "Helsinki",
  "country": "Finland",
  "customerId": 1,
  "id": 1,
  "postalCode": "00120",
  "streetAddress": "Uudenmaankatu 10"
}
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
```HTTP
HTTP/1.1 201 Created
Content-Type: application/json
```
```JSON
{
  "city": "Helsinki",
  "country": "Finland",
  "customerId": 1,
  "id": 2,
  "postalCode": "00120",
  "streetAddress": "Uudenmaankatu 10"
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
```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```
```JSON
{
  "city": "Helsinki",
  "country": "Finland",
  "customerId": 1,
  "id": 2,
  "postalCode": "00700",
  "streetAddress": "Malminkaari 7"
}
```

DELETE - osoitteen poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/customer-addresses/{id}
```
```HTTP
HTTP/1.1 204 No Content
```
```JSON
<Response body is empty>
```

## SUPPLIER ADDRESSES

GET - haetaan kaikki toimittajaosoitteet (50 per sivu).

```HTTP
GET http://localhost:8080/supplieraddresses
```

GET - haetaan toimittajaosoite id:n perusteella.

```HTTP
GET http://localhost:8080/supplieraddresses/{id}
```

GET - haetaan kaikki tietyn toimittajan osoitteet supplierId:n perusteella.

```HTTP
GET http://localhost:8080/supplieraddresses/supplier/{supplierId}
```

POST - uuden toimittajaosoitteen lisääminen.

```HTTP
POST http://localhost:8080/supplieraddresses
Content-Type: application/json

{
  "supplierId": 1,
  "street": "Teollisuustie 5",
  "postalCode": "33100",
  "city": "Tampere",
  "country": "Finland"
}
```

PUT - toimittajaosoitteen tietojen päivittäminen id:n perusteella.

```HTTP
PUT http://localhost:8080/supplieraddresses/{id}
Content-Type: application/json

{
  "supplierId": 1,
  "street": "Satamakatu 12",
  "postalCode": "00160",
  "city": "Helsinki",
  "country": "Finland"
}
```

DELETE - toimittajaosoitteen poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/supplieraddresses/{id}
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

PUT - päivitetään tilauksen tiedot id:n perusteella.

```HTTP
PUT http://localhost:8080/orders/{id}
Content-Type: application/json

{
  "customerId": 1,
  "orderDate": "2024-06-02T00:00:00",
  "deliveryDate": "2024-06-06T00:00:00",
  "shippingAddressId": 1,
  "status": "PENDING"
}
```

PATCH - päivitä tilausten statukset (bulk).

```HTTP
PATCH http://localhost:8080/orders/batch/status
Content-Type: application/json

{
  "orderIds": [100, 1000, 10000],
  "fromStatus": "NEW",
  "toStatus": "PENDING"
}
```

PATCH - päivitä asiakkaan tilausten statukset (bulk by customer).

```HTTP
PATCH http://localhost:8080/orders/batch/status/by-customer
Content-Type: application/json

{
  "customerId": 1,
  "fromStatus": "NEW",
  "toStatus": "PENDING"
}
```

DELETE - tilauksen poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/orders/{id}
```

## ORDER ITEMS

GET - haetaan kaikki tilausrivit (sivutettu).

```HTTP
GET http://localhost:8080/order-items
```

GET - haetaan tilausrivi order_id:n ja product_id:n perusteella.

```HTTP
GET http://localhost:8080/order-items/{orderId}/product/{productId}
```

PATCH - lisää alennus tilauksen kaikille riveille (discount = prosentti).

```HTTP
PATCH http://localhost:8080/order-items/{orderId}/discount?discount=10
```

PATCH - poista alennus tilauksen kaikilta riveiltä.

```HTTP
PATCH http://localhost:8080/order-items/{orderId}/remove-discount
```

POST - luo uusi tilausrivi.

```HTTP
POST http://localhost:8080/order-items
Content-Type: application/json
{
  "orderId": 200003,
  "productId": 1,
  "quantity": {
    "amount": 2
  }
}
```

PUT - päivitä tilausrivin tietoja (esim. määrä) orderId:n ja productId:n perusteella.

```HTTP
PUT http://localhost:8080/order-items/{orderId}/product/{productId}
Content-Type: application/json
{
  "amount": 6
}
```

DELETE - poista tilausrivi orderId:n ja productId:n perusteella.

```HTTP
DELETE http://localhost:8080/order-items/{orderId}/product/{productId}
```

## ORDER TOTALS (NÄKYMÄ)

GET - haetaan tilauksen kokonaistiedot, jossa tilauksen summa, näkymästä orderId:n perusteella.

```HTTP
GET http://localhost:8080/order-totals/{orderId}
```

GET - haetaan asiakkaan kaikkien tilausten kokonaistiedot näkymästä customerId:n perusteella.

```HTTP
GET http://localhost:8080/order-totals/by-customer/{customerId}
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

GET - haetaan toimittajan tuotteiden varastotiedot.

```HTTP
GET http://localhost:8080/suppliers/{supplierId}/products/stock
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
