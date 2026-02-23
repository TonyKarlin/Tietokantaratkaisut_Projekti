# Endpoints

Projektin endpointit:

## CUSTOMERS

...

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
