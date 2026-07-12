# Table Service - Entity Documentation

## Overview

The Table Service manages restaurant table operations including table management, reservations, and table assignments for orders. This service handles table availability, customer reservations, and table-to-order mappings.

---

## Entity Architecture

### Entity Relationship Diagram

```
Table (1) ──────────── (N) TableReservation
  │                           │
  └───────────────── (N) TableAssignment
```

---

## Entities

### 1. Table

**Purpose**: Represents a physical table in the restaurant.

**Fields**:
| Field | Type | Nullable | Unique | Description |
|-------|------|----------|--------|-------------|
| id | Long | No | Yes | Primary key (auto-generated) |
| tableNumber | String | No | Yes | Unique table identifier (e.g., "T01", "T02") |
| capacity | Integer | No | - | Number of seats at this table |
| status | TableStatus Enum | No | - | Current status of the table |
| tableType | TableType Enum | No | - | Type/category of the table |
| restaurantId | Long | No | - | Reference to restaurant |
| location | String | Yes | - | Physical location (e.g., "Window Seat") |
| floor | String | Yes | - | Floor number/name |
| section | String | Yes | - | Section/area name (e.g., "Dining Area A") |
| active | Boolean | No | - | Whether table is active/in use |
| notes | String | Yes | - | Additional notes |
| createdAt | Instant | No | - | Creation timestamp |
| updatedAt | Instant | Yes | - | Last update timestamp |
| createdBy | Long | Yes | - | User who created record |
| updatedBy | Long | Yes | - | User who last updated record |

**Enums**:
- **TableStatus**: AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE, CLEANING
- **TableType**: REGULAR, VIP, PRIVATE, OUTDOOR

#### Example Data

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
  "notes": "Corner table with good ambiance",
  "createdAt": "2026-07-01T10:30:00Z",
  "updatedAt": "2026-07-12T14:00:00Z",
  "createdBy": 5,
  "updatedBy": 5
}
```

```json
{
  "id": 2,
  "tableNumber": "T02",
  "capacity": 6,
  "status": "OCCUPIED",
  "tableType": "VIP",
  "restaurantId": 100,
  "location": "Private Corner",
  "floor": "Ground",
  "section": "VIP Area",
  "active": true,
  "notes": "Preferred seating for premium guests",
  "createdAt": "2026-07-01T10:30:00Z",
  "updatedAt": "2026-07-12T16:45:00Z",
  "createdBy": 5,
  "updatedBy": 8
}
```

```json
{
  "id": 3,
  "tableNumber": "T03",
  "capacity": 2,
  "status": "MAINTENANCE",
  "tableType": "REGULAR",
  "restaurantId": 100,
  "location": "Near Bar",
  "floor": "Ground",
  "section": "Dining Area B",
  "active": false,
  "notes": "Chair repair in progress",
  "createdAt": "2026-07-01T10:30:00Z",
  "updatedAt": "2026-07-12T09:00:00Z",
  "createdBy": 5,
  "updatedBy": 6
}
```

---

### 2. DiningReservation

**Purpose**: Manages dining reservations for customers.

**Fields**:
| Field | Type | Nullable | Unique | Description |
|-------|------|----------|--------|-------------|
| id | Long | No | Yes | Primary key (auto-generated) |
| reservationDateTime | LocalDateTime | No | - | Date and time of reservation |
| guestCount | Integer | No | - | Number of guests |
| customerId | Long | No | - | Customer ID from user service |
| guestName | String | Yes | - | Name of guest/reservation holder |
| guestPhone | String | Yes | - | Contact phone number |
| guestEmail | String | Yes | - | Contact email address |
| status | ReservationStatus Enum | No | - | Current reservation status |
| restaurantId | Long | No | - | Reference to restaurant |
| dining | Dining (Foreign Key) | No | - | Reserved dining reference |
| specialRequests | String | Yes | - | Special dietary/seating requests |
| checkInDateTime | LocalDateTime | Yes | - | When guest checked in |
| checkOutDateTime | LocalDateTime | Yes | - | When guest checked out |
| noShow | Boolean | No | - | Whether customer didn't show up |
| createdAt | Instant | No | - | Creation timestamp |
| updatedAt | Instant | Yes | - | Last update timestamp |
| createdBy | Long | Yes | - | User who created record |
| updatedBy | Long | Yes | - | User who last updated record |

**Enums**:
- **ReservationStatus**: PENDING, CONFIRMED, CHECKED_IN, COMPLETED, CANCELLED

#### Example Data

```json
{
  "id": 1,
  "reservationDateTime": "2026-07-12T19:00:00",
  "guestCount": 4,
  "customerId": 201,
  "guestName": "John Doe",
  "guestPhone": "+91-9876543210",
  "guestEmail": "john.doe@example.com",
  "status": "CONFIRMED",
  "restaurantId": 100,
  "dining": {
    "id": 1,
    "diningNumber": "D01"
  },
  "specialRequests": "Window seat preferred, no onions",
  "checkInDateTime": null,
  "checkOutDateTime": null,
  "noShow": false,
  "createdAt": "2026-07-10T15:30:00Z",
  "updatedAt": "2026-07-11T09:00:00Z",
  "createdBy": 5,
  "updatedBy": 5
}
```

```json
{
  "id": 2,
  "reservationDateTime": "2026-07-12T20:30:00",
  "guestCount": 6,
  "customerId": 202,
  "guestName": "Jane Smith",
  "guestPhone": "+91-9876543211",
  "guestEmail": "jane.smith@example.com",
  "status": "CHECKED_IN",
  "restaurantId": 100,
  "dining": {
    "id": 2,
    "diningNumber": "D02"
  },
  "specialRequests": "Celebration dinner - please arrange flowers",
  "checkInDateTime": "2026-07-12T20:15:00",
  "checkOutDateTime": null,
  "noShow": false,
  "createdAt": "2026-07-08T11:20:00Z",
  "updatedAt": "2026-07-12T20:15:00Z",
  "createdBy": 5,
  "updatedBy": 8
}
```

```json
{
  "id": 3,
  "reservationDateTime": "2026-07-11T18:00:00",
  "guestCount": 2,
  "customerId": 203,
  "guestName": "Robert Johnson",
  "guestPhone": "+91-9876543212",
  "guestEmail": "robert.j@example.com",
  "status": "COMPLETED",
  "restaurantId": 100,
  "dining": {
    "id": 4,
    "diningNumber": "D04"
  },
  "specialRequests": "Vegetarian menu",
  "checkInDateTime": "2026-07-11T18:05:00",
  "checkOutDateTime": "2026-07-11T19:45:00",
  "noShow": false,
  "createdAt": "2026-07-09T14:00:00Z",
  "updatedAt": "2026-07-11T19:45:00Z",
  "createdBy": 5,
  "updatedBy": 7
}
```

```json
{
  "id": 4,
  "reservationDateTime": "2026-07-10T19:00:00",
  "guestCount": 3,
  "customerId": 204,
  "guestName": "Sarah Wilson",
  "guestPhone": "+91-9876543213",
  "guestEmail": "sarah.w@example.com",
  "status": "CANCELLED",
  "restaurantId": 100,
  "dining": null,
  "specialRequests": "High chair for baby",
  "checkInDateTime": null,
  "checkOutDateTime": null,
  "noShow": false,
  "createdAt": "2026-07-05T10:00:00Z",
  "updatedAt": "2026-07-09T16:30:00Z",
  "createdBy": 5,
  "updatedBy": 6
}
```

---

### 3. DiningAssignment

**Purpose**: Maps orders to dining for tracking which dining is serving which order.

**Fields**:
| Field | Type | Nullable | Unique | Description |
|-------|------|----------|--------|-------------|
| id | Long | No | Yes | Primary key (auto-generated) |
| orderId | Long | No | - | Order ID from order service |
| customerId | Long | No | - | Customer ID from user service |
| restaurantId | Long | No | - | Reference to restaurant |
| dining | Dining (Foreign Key) | No | - | Assigned dining reference |
| assignedAt | LocalDateTime | No | - | Assignment timestamp |
| vacatedAt | LocalDateTime | Yes | - | When dining was vacated |
| active | Boolean | No | - | Whether assignment is still active |
| notes | String | Yes | - | Additional notes |
| createdAt | Instant | No | - | Creation timestamp |
| updatedAt | Instant | Yes | - | Last update timestamp |
| createdBy | Long | Yes | - | User who created record |
| updatedBy | Long | Yes | - | User who last updated record |

#### Example Data

```json
{
  "id": 1,
  "orderId": 5001,
  "customerId": 201,
  "restaurantId": 100,
  "dining": {
    "id": 1,
    "diningNumber": "D01"
  },
  "assignedAt": "2026-07-12T19:05:00",
  "vacatedAt": null,
  "active": true,
  "notes": "Order for dining 1 - party of 4",
  "createdAt": "2026-07-12T19:05:00Z",
  "updatedAt": "2026-07-12T19:05:00Z",
  "createdBy": 8,
  "updatedBy": 8
}
```

```json
{
  "id": 2,
  "orderId": 5002,
  "customerId": 202,
  "restaurantId": 100,
  "dining": {
    "id": 2,
    "diningNumber": "D02"
  },
  "assignedAt": "2026-07-12T20:20:00",
  "vacatedAt": null,
  "active": true,
  "notes": "VIP dining - celebration dinner",
  "createdAt": "2026-07-12T20:20:00Z",
  "updatedAt": "2026-07-12T20:20:00Z",
  "createdBy": 8,
  "updatedBy": 8
}
```

```json
{
  "id": 3,
  "orderId": 5000,
  "customerId": 200,
  "restaurantId": 100,
  "dining": {
    "id": 3,
    "diningNumber": "D03"
  },
  "assignedAt": "2026-07-12T18:30:00",
  "vacatedAt": "2026-07-12T19:50:00",
  "active": false,
  "notes": "Completed order",
  "createdAt": "2026-07-12T18:30:00Z",
  "updatedAt": "2026-07-12T19:50:00Z",
  "createdBy": 8,
  "updatedBy": 7
}
```

---

## Database Schema

### SQL Equivalent

```sql
-- Dining entity
CREATE TABLE dining (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dining_number VARCHAR(50) NOT NULL UNIQUE,
    capacity INT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    table_type VARCHAR(50) NOT NULL DEFAULT 'REGULAR',
    restaurant_id BIGINT NOT NULL,
    location VARCHAR(255),
    floor VARCHAR(100),
    section VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT true,
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    INDEX idx_restaurant (restaurant_id),
    INDEX idx_status (status)
);

-- DiningReservation entity
CREATE TABLE dining_reservation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reservation_date_time DATETIME NOT NULL,
    guest_count INT NOT NULL,
    customer_id BIGINT NOT NULL,
    guest_name VARCHAR(255),
    guest_phone VARCHAR(20),
    guest_email VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    restaurant_id BIGINT NOT NULL,
    dining_id BIGINT NOT NULL,
    special_requests TEXT,
    check_in_date_time DATETIME,
    check_out_date_time DATETIME,
    no_show BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (dining_id) REFERENCES dining(id),
    INDEX idx_restaurant (restaurant_id),
    INDEX idx_status (status),
    INDEX idx_reservation_date (reservation_date_time)
);

-- DiningAssignment entity
CREATE TABLE dining_assignment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    dining_id BIGINT NOT NULL,
    assigned_at DATETIME NOT NULL,
    vacated_at DATETIME,
    active BOOLEAN NOT NULL DEFAULT true,
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (dining_id) REFERENCES dining(id),
    INDEX idx_restaurant (restaurant_id),
    INDEX idx_order (order_id),
    INDEX idx_active (active)
);
```

---

## Common Use Cases

### 1. Create a New Dining
```java
Dining dining = Dining.builder()
    .diningNumber("D05")
    .capacity(4)
    .status(TableStatus.AVAILABLE)
    .tableType(TableType.REGULAR)
    .restaurantId(100L)
    .location("Main Dining Area")
    .floor("Ground")
    .section("Dining Area C")
    .active(true)
    .notes("Recently refurbished")
    .build();
```

### 2. Create a Reservation
```java
DiningReservation reservation = DiningReservation.builder()
    .reservationDateTime(LocalDateTime.of(2026, 7, 12, 19, 0))
    .guestCount(4)
    .customerId(201L)
    .guestName("John Doe")
    .guestPhone("+91-9876543210")
    .guestEmail("john@example.com")
    .status(ReservationStatus.PENDING)
    .restaurantId(100L)
    .dining(dining)
    .specialRequests("Window seat preferred")
    .noShow(false)
    .build();
```

### 3. Assign Dining to Order
```java
DiningAssignment assignment = DiningAssignment.builder()
    .orderId(5001L)
    .customerId(201L)
    .restaurantId(100L)
    .dining(dining)
    .assignedAt(LocalDateTime.now())
    .active(true)
    .notes("Order for dining 1")
    .build();
```

### 4. Check In Reservation
```java
reservation.setStatus(ReservationStatus.CHECKED_IN);
reservation.setCheckInDateTime(LocalDateTime.now());
// Save updated reservation
```

### 5. Check Out / Vacate Dining
```java
assignment.setVacatedAt(LocalDateTime.now());
assignment.setActive(false);
dining.setStatus(TableStatus.AVAILABLE);
// Save updates
```

---

## Entity Relationships

### Dining → DiningReservation (One-to-Many)
- One dining can have multiple reservations (across different time slots)
- Managed via `@OneToMany` in Dining entity (not shown in basic entity but recommended)

### Dining → DiningAssignment (One-to-Many)
- One dining can serve multiple orders (sequentially)
- Managed via `@OneToMany` in Dining entity (not shown in basic entity but recommended)

### DiningReservation → Dining (Many-to-One)
- Multiple reservations reference one dining
- `@ManyToOne` annotation with FetchType.LAZY

### DiningAssignment → Dining (Many-to-One)
- Multiple assignments reference one dining
- `@ManyToOne` annotation with FetchType.LAZY

---

## Status Transitions

### TableStatus Flow
```
AVAILABLE → RESERVED → OCCUPIED → AVAILABLE
   ↓
MAINTENANCE → AVAILABLE
   ↓
CLEANING → AVAILABLE
```

### ReservationStatus Flow
```
PENDING → CONFIRMED → CHECKED_IN → COMPLETED
   ↓
CANCELLED
```

### TableAssignment Flow
```
CREATED (active=true) → VACATED (active=false)
```

---

## Best Practices

1. **Always set timestamps**: Use `@PrePersist` and `@PreUpdate` callbacks
2. **Set audit fields**: Track createdBy and updatedBy for audit trails
3. **Use enums**: TableStatus and ReservationStatus should be validated via enums
4. **Lazy loading**: Use FetchType.LAZY for relationships to avoid N+1 queries
5. **Indexing**: Add indexes on frequently queried fields (restaurantId, status, dates)
6. **Soft deletes**: Consider adding a `deletedAt` field for audit requirements
7. **Validation**: Add @NotNull, @NotBlank annotations for required fields
8. **Transaction management**: Use @Transactional for operations affecting multiple entities

---

## Integration Points

- **User Service**: For customerId and createdBy/updatedBy references
- **Order Service**: For orderId in TableAssignment
- **Notification Service**: For reservation confirmations and reminders
- **Analytics Service**: For table utilization and reservation statistics

---

## Related Files

- Table Entity: `src/main/java/com/restaurant/table_service/entity/table/Table.java`
- TableReservation Entity: `src/main/java/com/restaurant/table_service/entity/table/TableReservation.java`
- TableAssignment Entity: `src/main/java/com/restaurant/table_service/entity/table/TableAssignment.java`
- Enums: `src/main/java/com/restaurant/table_service/entity/table/enums/`

---

## Version History

- **v1.0** - Initial entity creation with Table, TableReservation, and TableAssignment
