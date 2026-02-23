# Endpoints

Projektin endpointit:

## CUSTOMERS

...

## PRODUCTS

GET kaikki tuotteet

```HTTP
GET http://localhost:8080/products
```

GET tuote id:n perusteella.

```HTTP
GET http://localhost:8080/products/{id}
```

POST - uuden tuotteen lisääminen

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
