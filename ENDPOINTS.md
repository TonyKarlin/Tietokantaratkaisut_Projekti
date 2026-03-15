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

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "content": [
    {
      "email": "vsmith1@example.com",
      "firstName": "Dustin",
      "id": 1,
      "lastName": "Carey",
      "phone": "001-789-824-7188x591"
    },
    {
      "email": "nunezmary2@example.net",
      "firstName": "Jeanette",
      "id": 2,
      "lastName": "Hendrix",
      "phone": "(941)966-5615"
    }
  ]
}
```

GET - haetaan asiakas id:n perusteella.

```HTTP
GET http://localhost:8080/customers/{id}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/100005"
    },
    "customers": {
      "href": "http://localhost:8080/customers"
    }
  },
  "email": "test@example.com",
  "firstName": "testitesti",
  "id": 100005,
  "lastName": "testinen",
  "phone": "1231423354"
}
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

```HTTP
HTTP/1.1 201 Created
Content-Type: application/json
```

```JSON
{
  "email": "test@example.org",
  "firstName": "TESTI",
  "id": 100006,
  "lastName": "KAYTTAJA",
  "phone": "1234567890"
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

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "email": "updated@example.org",
  "firstName": "PAIVITETTY",
  "id": 100006,
  "lastName": "KAYTTAJA",
  "phone": "0987654321"
}
```

DELETE - asiakkaan poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/customers/{id}
```

```HTTP
HTTP/1.1 204 No Content
```

```JSON
<Response body is empty>
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
  "streetAddress": "Malminkaari 2"
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
      "id": 5,
      "postalCode": "00100",
      "streetAddress": "testitie 123",
      "supplierId": 1
    },
    {
      "city": "Helsinki",
      "country": "Finland",
      "id": 6,
      "postalCode": "00100",
      "streetAddress": "testitie 123",
      "supplierId": 2
    }
  ]
}
```

GET - haetaan toimittajaosoite id:n perusteella.

```HTTP
GET http://localhost:8080/supplieraddresses/{id}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/supplieraddresses/5"
    },
    "supplieraddresses": {
      "href": "http://localhost:8080/supplieraddresses"
    }
  },
  "city": "Helsinki",
  "country": "Finland",
  "id": 5,
  "postalCode": "00100",
  "streetAddress": "testitie 123",
  "supplierId": 1
}
```

GET - haetaan kaikki tietyn toimittajan osoitteet supplierId:n perusteella.

```HTTP
GET http://localhost:8080/supplieraddresses/supplier/{supplierId}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
[
  {
    "city": "Helsinki",
    "country": "Finland",
    "id": 5,
    "postalCode": "00100",
    "streetAddress": "testitie 123",
    "supplierId": 1
  }
]
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

```HTTP
HTTP/1.1 201 Created
Content-Type: application/json
```

```JSON
{
  "city": "Tampere",
  "country": "Finland",
  "id": 7,
  "postalCode": "33100",
  "streetAddress": "Teollisuustie 5",
  "supplierId": 3
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

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "city": "Helsinki",
  "country": "Finland",
  "id": 7,
  "postalCode": "00160",
  "streetAddress": "Satamakatu 12",
  "supplierId": 1
}
```

DELETE - toimittajaosoitteen poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/supplieraddresses/{id}
```

```HTTP
HTTP/1.1 204 No Content
```

```JSON
<Response body is empty>
```

## PRODUCTS

GET - haetaan kaikki tuotteet.

```HTTP
GET http://localhost:8080/products
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "content": [
    {
      "categoryId": 2,
      "description": "Her fall move current him.",
      "id": 1,
      "name": "Super Bug 360",
      "price": 22.22,
      "stockQuantity": 136,
      "supplierId": 35
    },
    {
      "categoryId": 10,
      "description": "Score million throw thing instead ball line think.",
      "id": 2,
      "name": "Happy Pack 382",
      "price": 546.08,
      "stockQuantity": 408,
      "supplierId": 16
    }
  ]
}
```

GET - haetaan tuote id:n perusteella.

```HTTP
GET http://localhost:8080/products/{id}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/products/9"
    },
    "products": {
      "href": "http://localhost:8080/products"
    }
  },
  "categoryId": 7,
  "description": "Tonight style share thus study wrong interest.",
  "id": 9,
  "name": "Swift Pad 577",
  "price": 526.56,
  "stockQuantity": 151,
  "supplierId": 54
}
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

```HTTP
HTTP/1.1 201 Created
Content-Type: application/json
```

```JSON
{
  "categoryId": 6,
  "description": "TESTITESTI",
  "id": 1001,
  "name": "TESTITUOTE",
  "price": 100.50,
  "stockQuantity": 10,
  "supplierId": null
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

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "categoryId": 1,
  "description": "PÄIVITETTY KUVAUS",
  "id": 6,
  "name": "PÄIVITETTY TUOTE",
  "price": 150.75,
  "stockQuantity": 20,
  "supplierId": null
}
```

DELETE - tuotteen poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/products/{id}
```

```HTTP
HTTP/1.1 204 No Content
```

```JSON
<Response body is empty>
```

### N:M-SUHDE: Product ↔ Supplier

GET - haetaan tuotteen kaikki toimittajat (N:M-suhde). Alussa saattaa olla vastauksena tyhjä lista, koska tuotetta ja
jälleenmyyjää ei ole linkitetty POST metodin kautta, mikä on tässä alempana.

```HTTP
GET http://localhost:8080/products/{id}/suppliers
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
[
  {
    "contactName": "Mia Manninen",
    "email": "mia.manninen@polarelec.fi",
    "id": 1,
    "name": "Polar Electronics Oy",
    "phone": "0401001000"
  }
]
```

POST - lisätään toimittaja tuotteelle (N:M-suhde).

```HTTP
POST http://localhost:8080/products/{productId}/suppliers/{supplierId}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "categoryId": 2,
  "description": "Her fall move current him.",
  "id": 1,
  "name": "Super Bug 360",
  "price": 22.22,
  "stockQuantity": 146,
  "supplierId": 35
}
```

DELETE - poistetaan toimittaja tuotteelta (N:M-suhde).

```HTTP
DELETE http://localhost:8080/products/{productId}/suppliers/{supplierId}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "categoryId": 2,
  "description": "Her fall move current him.",
  "id": 1,
  "name": "Super Bug 360",
  "price": 22.22,
  "stockQuantity": 146,
  "supplierId": 35
}
```

## PRODUCT CATEGORIES

GET - haetaan kaikki tuotekategoriat (50 per sivu).

```HTTP
GET http://localhost:8080/productcategories
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "content": [
    {
      "description": "Sähkölaitteet, komponentit ja elektroniikkatuotteet",
      "id": 1,
      "name": "Elektroniikka"
    }
  ]
}
```

GET - haetaan tuotekategoria id:n perusteella.

```HTTP
GET http://localhost:8080/productcategories/{id}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/productcategories/1"
    },
    "productcategories": {
      "href": "http://localhost:8080/productcategories"
    }
  },
  "description": "Sähkölaitteet, komponentit ja elektroniikkatuotteet",
  "id": 1,
  "name": "Elektroniikka"
}
```

GET - haetaan tuotekategoria ja sen kaikki tuotteet (LAZY loading).

```HTTP
GET http://localhost:8080/productcategories/{id}/products
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "name": "Elektroniikka",
  "description": "Sähkölaitteet, komponentit ja elektroniikkatuotteet",
  "id": 1,
  "products": [
    {
      "categoryId": 1,
      "description": "Focus test but out catch million with see view.",
      "id": 7,
      "name": "Magic Bit 840",
      "price": 352.35,
      "stockQuantity": 378,
      "supplierId": 100,
      "suppliers": []
    },
    {
      "categoryId": 1,
      "description": "Us really personal yes into yourself task happen.",
      "id": 8,
      "name": "Shiny Planet 299",
      "price": 697.49,
      "stockQuantity": 179,
      "supplierId": 93,
      "suppliers": []
    }
  ]
}
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

```HTTP
HTTP/1.1 201 Created
Location: http://localhost:8080/productcategories/11
Content-Type: application/json
```

```JSON
{
  "description": "testikategoria",
  "id": 11,
  "name": "testi"
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

```HTTP
HTTP/1.1 200 OK
Location: http://localhost:8080/productcategories/11
Content-Type: application/json
```

```JSON
{
  "description": "testikategoriapäivitys",
  "id": 11,
  "name": "testipäivitys"
}
```

DELETE - tuotekategorian poistaminen (HUOM. POISTAA SAMALLA KAIKKI TUOTTEET SEN KATEGORIASTA)

```HTTP
DELETE http://localhost:8080/productcategories/{id}
```

```HTTP
HTTP/1.1 204 No Content
```

```JSON
<Response body is empty>
```

## ORDERS

GET - haetaan kaikki tilaukset.

```HTTP
GET http://localhost:8080/orders
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "content": [
    {
      "addressId": 51914,
      "customerId": 51914,
      "deliveryDate": "2024-04-06T16:16:43",
      "id": 1,
      "orderDate": "2024-04-03T17:51:08",
      "status": "CANCELLED"
    },
    {
      "addressId": 3791,
      "customerId": 3791,
      "deliveryDate": null,
      "id": 2,
      "orderDate": "2024-06-22T23:08:14",
      "status": "CANCELLED"
    }
  ]
}
```

GET - haetaan tilaus id:n perusteella.

```HTTP
GET http://localhost:8080/orders/{id}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/orders/200004"
    },
    "orders": {
      "href": "http://localhost:8080/orders"
    }
  },
  "addressId": 8,
  "customerId": 1,
  "deliveryDate": "2024-06-06T00:00:00",
  "id": 200004,
  "orderDate": "2024-06-02T00:00:00",
  "status": "PENDING"
}
```

GET - haetaan tilaukset asiakkaan id:n perusteella.

```HTTP
GET http://localhost:8080/orders/by-customer/{customerId}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "content": [
    {
      "addressId": 51914,
      "customerId": 51914,
      "deliveryDate": "2024-04-06T16:16:43",
      "id": 1,
      "orderDate": "2024-04-03T17:51:08",
      "status": "CANCELLED"
    },
    {
      "addressId": 51914,
      "customerId": 51914,
      "deliveryDate": "2024-07-21T23:16:54",
      "id": 4632,
      "orderDate": "2024-07-20T11:24:06",
      "status": "NEW"
    }
  ]
}
```

POST - uuden tilauksen lisääminen.

```HTTP
POST http://localhost:8080/orders
Content-Type: application/json

{
  "customerId": 1,
  "orderDate": "2024-06-02T00:00:00",
  "deliveryDate": "2024-06-05T00:00:00",
  "shippingAddressId": 8,
  "status": "NEW"
}
```

```HTTP
HTTP/1.1 201 Created
Content-Type: application/json
```

```JSON
{
  "addressId": 8,
  "customerId": 1,
  "deliveryDate": "2024-06-05T00:00:00",
  "id": 200005,
  "orderDate": "2024-06-02T00:00:00",
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

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "addressId": 1,
  "customerId": 1,
  "deliveryDate": "2024-06-06T00:00:00",
  "id": 200004,
  "orderDate": "2024-06-02T00:00:00",
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

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "updatedCount": 1
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

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "updatedCount": 4
}
```

DELETE - tilauksen poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/orders/{id}
```

```HTTP
HTTP/1.1 204 No Content
```

```JSON
<Response body is empty>
```

## ORDER ITEMS

GET - haetaan kaikki tilausrivit (sivutettu).

```HTTP
GET http://localhost:8080/order-items
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "content": [
    {
      "orderId": 1,
      "productId": 6,
      "quantity": 2,
      "unitPrice": 316.47,
      "discountedPrice": 284.82
    },
    {
      "orderId": 1,
      "productId": 426,
      "quantity": 2,
      "unitPrice": 736.44,
      "discountedPrice": 662.80
    }
  ]
}
```

GET - haetaan tilausrivi order_id:n ja product_id:n perusteella.

```HTTP
GET http://localhost:8080/order-items/{orderId}/product/{productId}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/order-items/1/product/426"
    },
    "orderItems": {
      "href": "http://localhost:8080/order-items"
    }
  },
  "orderId": 1,
  "productId": 426,
  "quantity": 2,
  "unitPrice": 736.44,
  "discountedPrice": 662.80
}
```

PATCH - lisää alennus tilauksen kaikille riveille (discount = prosentti).

```HTTP
PATCH http://localhost:8080/order-items/{orderId}/discount?discount=10
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
3
```

PATCH - poista alennus tilauksen kaikilta riveiltä.

```HTTP
PATCH http://localhost:8080/order-items/{orderId}/remove-discount
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
3
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

```HTTP
HTTP/1.1 201 Created
Content-Type: application/json
```

```JSON
{
  "orderId": 200000,
  "productId": 1,
  "quantity": 2,
  "unitPrice": 22.22,
  "discountedPrice": null
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

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "orderId": 200004,
  "productId": 1,
  "quantity": 6,
  "unitPrice": 22.22,
  "discountedPrice": null
}
```

DELETE - poista tilausrivi orderId:n ja productId:n perusteella.

```HTTP
DELETE http://localhost:8080/order-items/{orderId}/product/{productId}
```

```HTTP
HTTP/1.1 204 No Content
```

```JSON
<Response body is empty>
```

## ORDER TOTALS (NÄKYMÄ)

GET - haetaan tilauksen kokonaistiedot, jossa tilauksen summa, näkymästä orderId:n perusteella.

```HTTP
GET http://localhost:8080/order-totals/{orderId}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/order-totals/1"
    },
    "byCustomer": {
      "href": "http://localhost:8080/order-totals/by-customer/51914"
    }
  },
  "orderId": 1,
  "customerId": 51914,
  "orderDate": "2024-04-03",
  "deliveryDate": "2024-04-06",
  "shippingAddressId": 51914,
  "status": "CANCELLED",
  "itemLines": 3,
  "totalQuantity": 7,
  "totalAmount": 3940.83
}
```

GET - haetaan asiakkaan kaikkien tilausten kokonaistiedot näkymästä customerId:n perusteella.

```HTTP
GET http://localhost:8080/order-totals/by-customer/{customerId}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "_embedded": {
    "orderTotalsDTOList": [
      {
        "_links": {
          "self": {
            "href": "http://localhost:8080/order-totals/35760"
          },
          "byCustomer": {
            "href": "http://localhost:8080/order-totals/by-customer/2"
          }
        },
        "orderId": 35760,
        "customerId": 2,
        "orderDate": "2025-05-18",
        "deliveryDate": "2025-05-28",
        "shippingAddressId": 2,
        "status": "NEW",
        "itemLines": 1,
        "totalQuantity": 3,
        "totalAmount": 2464.77
      },
      {
        "_links": {
          "self": {
            "href": "http://localhost:8080/order-totals/95702"
          },
          "byCustomer": {
            "href": "http://localhost:8080/order-totals/by-customer/2"
          }
        },
        "orderId": 95702,
        "customerId": 2,
        "orderDate": "2025-04-03",
        "deliveryDate": "2025-04-07",
        "shippingAddressId": 2,
        "status": "NEW",
        "itemLines": 4,
        "totalQuantity": 9,
        "totalAmount": 2114.99
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/order-totals/by-customer/2"
    }
  }
}

```

## SUPPLIERS

GET - haetaan kaikki toimittajat (50 per sivu).

```HTTP
GET http://localhost:8080/suppliers
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "content": [
    {
      "contactName": "Mia Manninen",
      "email": "mia.manninen@polarelec.fi",
      "id": 1,
      "name": "Polar Electronics Oy",
      "phone": "0401001000"
    },
    {
      "contactName": "Samira Morgan",
      "email": "samira.morgan@ecotech.com",
      "id": 2,
      "name": "EcoTech Ltd",
      "phone": "0507236312"
    }
  ]
}
```

GET - haetaan toimittaja id:n perusteella.

```HTTP
GET http://localhost:8080/suppliers/{id}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/hal+json
```

```JSON
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/suppliers/1"
    },
    "suppliers": {
      "href": "http://localhost:8080/suppliers"
    }
  },
  "contactName": "Mia Manninen",
  "email": "mia.manninen@polarelec.fi",
  "id": 1,
  "name": "Polar Electronics Oy",
  "phone": "0401001000"
}
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

```HTTP
HTTP/1.1 201 Created
Location: http://localhost:8080/suppliers/101
Content-Type: application/json
```

```JSON
{
  "contactName": "Testi Timo",
  "email": "timo@testi.com",
  "id": 101,
  "name": "Testi firma",
  "phone": "+01010101001"
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

```HTTP
HTTP/1.1 200 OK
Location: http://localhost:8080/suppliers/101
Content-Type: application/json
```

```JSON
{
  "contactName": "Jane Smith",
  "email": "jane@supplier.com",
  "id": 101,
  "name": "Updated Supplier Name",
  "phone": "+358409876543"
}
```

DELETE - toimittajan poistaminen id:n perusteella.

```HTTP
DELETE http://localhost:8080/suppliers/{id}
```

```HTTP
HTTP/1.1 204 No Content
```

```JSON
<Response body is empty>
```

### N:M-SUHDE: Supplier ↔ Product

GET - haetaan toimittajan kaikki tuotteet (N:M-suhde).

```HTTP
GET http://localhost:8080/suppliers/{id}/products
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
[
  {
    "contactName": "Mia Manninen",
    "email": "mia.manninen@polarelec.fi",
    "id": 1,
    "name": "Polar Electronics Oy",
    "phone": "0401001000"
  }
]
```

POST - lisätään tuote toimittajalle (N:M-suhde).

```HTTP
POST http://localhost:8080/suppliers/{supplierId}/products/{productId}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "categoryId": 2,
  "description": "Her fall move current him.",
  "id": 1,
  "name": "Super Bug 360",
  "price": 22.22,
  "stockQuantity": 146,
  "supplierId": 35
}
```

DELETE - poistetaan tuote toimittajalta (N:M-suhde).

```HTTP
DELETE http://localhost:8080/suppliers/{supplierId}/products/{productId}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
{
  "categoryId": 2,
  "description": "Her fall move current him.",
  "id": 1,
  "name": "Super Bug 360",
  "price": 22.22,
  "stockQuantity": 146,
  "supplierId": 35
}
```

GET - haetaan toimittajan tuotteiden varastotiedot.

```HTTP
GET http://localhost:8080/suppliers/{supplierId}/products/stock
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
[
  {
    "categoryId": 3,
    "description": "Ask decide need next very capital.",
    "id": 3,
    "name": "Quantum Widget 457",
    "price": 849.66,
    "stockQuantity": 323,
    "supplierId": 17
  },
  {
    "categoryId": 2,
    "description": "Her fall move current him.",
    "id": 1,
    "name": "Super Bug 360",
    "price": 22.22,
    "stockQuantity": 146,
    "supplierId": 35
  }
]
```

PATCH - massaoperaatio: kasvatetaan toimittajan kaikkien tuotteiden `stockQuantity`-arvoa.

```HTTP
PATCH http://localhost:8080/suppliers/{supplierId}/products/stock/increase?amount=10
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
6
```

## PRODUCTS (Criteria API haku)

POST - dynaaminen haku tuotteille usealla ehdolla (Criteria API). Kaikki kentät ovat valinnaisia — `null` = ei rajausta. Jos kaikki null, palautetaan kaikki tuotteet.

```HTTP
POST http://localhost:8080/products/search
Content-Type: application/json

{
  "nameContains": null,
  "minPrice": null,
  "maxPrice": null,
  "minStock": null,
  "categoryId": null,
  "supplierId": null
}
```

Esimerkki: tuotteet joiden nimessä "Shiny":

```HTTP
POST http://localhost:8080/products/search
Content-Type: application/json

{
  "nameContains": "Shiny",
  "minPrice": null,
  "maxPrice": null,
  "minStock": null,
  "categoryId": null,
  "supplierId": null
}
```

Esimerkki: tuotteet hinnalla 10–500, vähintään 5 kpl varastossa:

```HTTP
POST http://localhost:8080/products/search
Content-Type: application/json

{
  "nameContains": null,
  "minPrice": 10.00,
  "maxPrice": 500.00,
  "minStock": 5,
  "categoryId": null,
  "supplierId": null
}
```

```HTTP
HTTP/1.1 200 OK
Content-Type: application/json
```

```JSON
[
  {
    "categoryId": 2,
    "description": "Her fall move current him.",
    "id": 1,
    "name": "Super Bug 360",
    "price": 22.22,
    "stockQuantity": 156,
    "supplierId": 35
  },
  {
    "categoryId": 5,
    "description": "After game authority quickly science single put from film behavior.",
    "id": 4,
    "name": "Zippy Key 143",
    "price": 327.95,
    "stockQuantity": 280,
    "supplierId": 81
  }
]
```
