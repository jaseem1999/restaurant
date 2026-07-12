# Table Service API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

---

## Table Endpoints

### 1. Get All Tables
**Endpoint:** `GET /tables`

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| restaurantId | Long | Yes | - | Restaurant ID |
| page | Integer | No | 0 | Page number (0-indexed) |
| size | Integer | No | 20 | Page size |

**Response:** Paginated `TableProjection` objects

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/tables?restaurantId=100&page=0&size=10"
```

**Example Response:**
```json
{
  "content": [
    {
      "id": 1,
      "tableNumber": "T01",
      "capacity": 4,
      "status": "AVAILABLE",
      "tableType": "REGULAR",
      "location": "Window Seat",
      "floor": "Ground",
      "section": "Dining Area A",
      "active": true
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 15,
    "totalPages": 2
  }
}
```

---

### 2. Get Table by Status
**Endpoint:** `GET /tables/status/{status}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| status | TableStatus | AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE, CLEANING |

**Query Parameters:**
| Parameter | Type | Required | Default |
|-----------|------|----------|---------|
| restaurantId | Long | Yes | - |
| page | Integer | No | 0 |
| size | Integer | No | 20 |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/tables/status/AVAILABLE?restaurantId=100&page=0&size=10"
```

---

### 3. Filter Tables (Advanced Query)
**Endpoint:** `POST /tables/filter`

**Request Body:**
```json
{
  "restaurantId": 100,
  "status": "AVAILABLE",
  "tableType": "VIP",
  "capacity": 4,
  "floor": "Ground",
  "section": "Dining Area A",
  "active": true,
  "pageNumber": 0,
  "pageSize": 20
}
```

**Example Request:**
```bash
curl -X POST "http://localhost:8080/api/v1/tables/filter" \
  -H "Content-Type: application/json" \
  -d '{
    "restaurantId": 100,
    "status": "AVAILABLE",
    "tableType": "VIP",
    "active": true,
    "pageNumber": 0,
    "pageSize": 20
  }'
```

---

### 4. Get Table by ID
**Endpoint:** `GET /tables/{tableId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| tableId | Long | Table ID |

**Response:** `TableDetailProjection` object with full details

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/tables/1"
```

---

### 5. Get Available Tables for Capacity
**Endpoint:** `GET /tables/available`

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| restaurantId | Long | Yes | Restaurant ID |
| guestCount | Integer | Yes | Number of guests |

**Response:** List of available `TableProjection` objects

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/tables/available?restaurantId=100&guestCount=4"
```

---

### 6. Get Tables by Floor
**Endpoint:** `GET /tables/floor/{floor}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| floor | String | Floor name/number |

**Query Parameters:**
| Parameter | Type | Required |
|-----------|------|----------|
| restaurantId | Long | Yes |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/tables/floor/Ground?restaurantId=100"
```

---

### 7. Get Tables by Section
**Endpoint:** `GET /tables/section/{section}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| section | String | Section name |

**Query Parameters:**
| Parameter | Type | Required |
|-----------|------|----------|
| restaurantId | Long | Yes |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/tables/section/Dining%20Area%20A?restaurantId=100"
```

---

### 8. Get Table Count by Status
**Endpoint:** `GET /tables/count-by-status`

**Query Parameters:**
| Parameter | Type | Required |
|-----------|------|----------|
| restaurantId | Long | Yes |
| status | TableStatus | Yes |

**Response:** Long (count)

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/tables/count-by-status?restaurantId=100&status=AVAILABLE"
```

---

## Reservation Endpoints

### 1. Get All Reservations
**Endpoint:** `GET /reservations`

**Query Parameters:**
| Parameter | Type | Required | Default |
|-----------|------|----------|---------|
| restaurantId | Long | Yes | - |
| page | Integer | No | 0 |
| size | Integer | No | 20 |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reservations?restaurantId=100&page=0&size=10"
```

---

### 2. Get Reservations by Status
**Endpoint:** `GET /reservations/status/{status}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| status | ReservationStatus | PENDING, CONFIRMED, CHECKED_IN, COMPLETED, CANCELLED |

**Query Parameters:**
| Parameter | Type | Required | Default |
|-----------|------|----------|---------|
| restaurantId | Long | Yes | - |
| page | Integer | No | 0 |
| size | Integer | No | 20 |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reservations/status/CONFIRMED?restaurantId=100&page=0&size=10"
```

---

### 3. Get Customer Reservations
**Endpoint:** `GET /reservations/customer/{customerId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| customerId | Long | Customer ID |

**Query Parameters:**
| Parameter | Type | Required | Default |
|-----------|------|----------|---------|
| page | Integer | No | 0 |
| size | Integer | No | 20 |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reservations/customer/201?page=0&size=10"
```

---

### 4. Get Customer Reservations at Restaurant
**Endpoint:** `GET /reservations/customer/{customerId}/restaurant/{restaurantId}`

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reservations/customer/201/restaurant/100?page=0&size=10"
```

---

### 5. Filter Reservations (Advanced Query)
**Endpoint:** `POST /reservations/filter`

**Request Body:**
```json
{
  "restaurantId": 100,
  "customerId": 201,
  "status": "CONFIRMED",
  "reservationDateFrom": "2026-07-12T00:00:00",
  "reservationDateTo": "2026-07-13T23:59:59",
  "noShow": false,
  "pageNumber": 0,
  "pageSize": 20
}
```

**Example Request:**
```bash
curl -X POST "http://localhost:8080/api/v1/reservations/filter" \
  -H "Content-Type: application/json" \
  -d '{
    "restaurantId": 100,
    "status": "CONFIRMED",
    "pageNumber": 0,
    "pageSize": 20
  }'
```

---

### 6. Get Reservation by ID
**Endpoint:** `GET /reservations/{reservationId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| reservationId | Long | Reservation ID |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reservations/1"
```

---

### 7. Get Pending Reservations
**Endpoint:** `GET /reservations/pending`

**Query Parameters:**
| Parameter | Type | Required |
|-----------|------|----------|
| restaurantId | Long | Yes |
| fromDate | LocalDateTime | Yes |
| toDate | LocalDateTime | Yes |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reservations/pending?restaurantId=100&fromDate=2026-07-12T00:00:00&toDate=2026-07-13T23:59:59"
```

---

### 8. Get Confirmed Reservations
**Endpoint:** `GET /reservations/confirmed`

**Query Parameters (same as pending)**

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reservations/confirmed?restaurantId=100&fromDate=2026-07-12T00:00:00&toDate=2026-07-13T23:59:59"
```

---

### 9. Get No-Show Reservations
**Endpoint:** `GET /reservations/no-show`

**Query Parameters (same as pending)**

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reservations/no-show?restaurantId=100&fromDate=2026-07-12T00:00:00&toDate=2026-07-13T23:59:59"
```

---

### 10. Get Reservation Count by Status
**Endpoint:** `GET /reservations/count-by-status`

**Query Parameters:**
| Parameter | Type | Required |
|-----------|------|----------|
| restaurantId | Long | Yes |
| status | ReservationStatus | Yes |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reservations/count-by-status?restaurantId=100&status=CONFIRMED"
```

---

## Table Assignment Endpoints

### 1. Get All Assignments
**Endpoint:** `GET /assignments`

**Query Parameters:**
| Parameter | Type | Required | Default |
|-----------|------|----------|---------|
| restaurantId | Long | Yes | - |
| page | Integer | No | 0 |
| size | Integer | No | 20 |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments?restaurantId=100&page=0&size=10"
```

---

### 2. Get Active Assignments
**Endpoint:** `GET /assignments/active`

**Query Parameters:**
| Parameter | Type | Required | Default |
|-----------|------|----------|---------|
| restaurantId | Long | Yes | - |
| page | Integer | No | 0 |
| size | Integer | No | 20 |

**Response:** Page of active `TableAssignmentProjection` objects

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/active?restaurantId=100&page=0&size=10"
```

---

### 3. Get Customer Assignments
**Endpoint:** `GET /assignments/customer/{customerId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| customerId | Long | Customer ID |

**Query Parameters:**
| Parameter | Type | Required | Default |
|-----------|------|----------|---------|
| page | Integer | No | 0 |
| size | Integer | No | 20 |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/customer/201?page=0&size=10"
```

---

### 4. Filter Assignments (Advanced Query)
**Endpoint:** `POST /assignments/filter`

**Request Body:**
```json
{
  "restaurantId": 100,
  "customerId": 201,
  "orderId": 5001,
  "tableId": 1,
  "active": true,
  "assignedFromDate": "2026-07-12T00:00:00",
  "assignedToDate": "2026-07-13T23:59:59",
  "pageNumber": 0,
  "pageSize": 20
}
```

**Example Request:**
```bash
curl -X POST "http://localhost:8080/api/v1/assignments/filter" \
  -H "Content-Type: application/json" \
  -d '{
    "restaurantId": 100,
    "active": true,
    "pageNumber": 0,
    "pageSize": 20
  }'
```

---

### 5. Get Assignment by ID
**Endpoint:** `GET /assignments/{assignmentId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| assignmentId | Long | Assignment ID |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/1"
```

---

### 6. Get Assignment by Order ID
**Endpoint:** `GET /assignments/order/{orderId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| orderId | Long | Order ID |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/order/5001"
```

---

### 7. Get Table Assignments
**Endpoint:** `GET /assignments/table/{tableId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| tableId | Long | Table ID |

**Response:** List of `TableAssignmentProjection` objects

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/table/1"
```

---

### 8. Get Active Table Assignment
**Endpoint:** `GET /assignments/table/{tableId}/active`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| tableId | Long | Table ID |

**Query Parameters:**
| Parameter | Type | Required |
|-----------|------|----------|
| restaurantId | Long | Yes |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/table/1/active?restaurantId=100"
```

---

### 9. Get Customer Active Assignments
**Endpoint:** `GET /assignments/customer/{customerId}/active`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| customerId | Long | Customer ID |

**Response:** List of active `TableAssignmentProjection` objects

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/customer/201/active"
```

---

### 10. Get Assignments by Date Range
**Endpoint:** `GET /assignments/date-range`

**Query Parameters:**
| Parameter | Type | Required |
|-----------|------|----------|
| restaurantId | Long | Yes |
| fromDate | LocalDateTime | Yes |
| toDate | LocalDateTime | Yes |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/date-range?restaurantId=100&fromDate=2026-07-12T00:00:00&toDate=2026-07-13T23:59:59"
```

---

### 11. Get Active Assignment Count
**Endpoint:** `GET /assignments/count-active`

**Query Parameters:**
| Parameter | Type | Required |
|-----------|------|----------|
| restaurantId | Long | Yes |

**Response:** Long (count)

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/count-active?restaurantId=100"
```

---

## Projection Types

### TableProjection
Lightweight projection for list views:
```json
{
  "id": 1,
  "tableNumber": "T01",
  "capacity": 4,
  "status": "AVAILABLE",
  "tableType": "REGULAR",
  "location": "Window Seat",
  "floor": "Ground",
  "section": "Dining Area A",
  "active": true
}
```

### TableDetailProjection
Full projection for detail views:
```json
{
  "id": 1,
  "tableNumber": "T01",
  "capacity": 4,
  "status": "AVAILABLE",
  "tableType": "REGULAR",
  "restaurantId": 100,
  "location": "Window Seat",
  "floor": "Ground",
  "section": "Dining Area A",
  "active": true,
  "notes": "Recently refurbished",
  "createdAt": "2026-07-01T10:30:00Z",
  "updatedAt": "2026-07-12T14:00:00Z"
}
```

### ReservationProjection
Lightweight reservation view:
```json
{
  "id": 1,
  "reservationDateTime": "2026-07-12T19:00:00",
  "guestCount": 4,
  "customerId": 201,
  "guestName": "John Doe",
  "guestPhone": "+91-9876543210",
  "status": "CONFIRMED",
  "tableId": 1,
  "tableNumber": "T01",
  "checkInDateTime": null,
  "checkOutDateTime": null,
  "noShow": false
}
```

### TableAssignmentProjection
Lightweight assignment view:
```json
{
  "id": 1,
  "orderId": 5001,
  "customerId": 201,
  "tableId": 1,
  "tableNumber": "T01",
  "assignedAt": "2026-07-12T19:05:00",
  "vacatedAt": null,
  "active": true
}
```

---

## Query Examples

### Example 1: Find available VIP tables for 6 guests
```bash
curl -X POST "http://localhost:8080/api/v1/tables/filter" \
  -H "Content-Type: application/json" \
  -d '{
    "restaurantId": 100,
    "status": "AVAILABLE",
    "tableType": "VIP",
    "capacity": 6,
    "active": true,
    "pageNumber": 0,
    "pageSize": 20
  }'
```

### Example 2: Find confirmed reservations for next 7 days
```bash
curl -X GET "http://localhost:8080/api/v1/reservations/confirmed?restaurantId=100&fromDate=2026-07-12T00:00:00&toDate=2026-07-19T23:59:59"
```

### Example 3: Find all active assignments for a customer
```bash
curl -X GET "http://localhost:8080/api/v1/assignments/customer/201/active"
```

### Example 4: Get all tables in a specific section
```bash
curl -X GET "http://localhost:8080/api/v1/tables/section/VIP%20Area?restaurantId=100"
```

### Example 5: Count available tables
```bash
curl -X GET "http://localhost:8080/api/v1/tables/count-by-status?restaurantId=100&status=AVAILABLE"
```

---

## Pagination

All list endpoints support pagination using:
- `page`: Zero-indexed page number (default: 0)
- `size`: Number of items per page (default: 20)

**Response Format:**
```json
{
  "content": [],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 50,
    "totalPages": 3
  }
}
```

---

## Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 400 | Bad Request |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## Error Response Format

```json
{
  "error": "Table not found with id: 999",
  "timestamp": "2026-07-12T18:00:00Z"
}
```
