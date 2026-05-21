# Restaurant Schema Design

This is a Spring Boot application that defines the database schema for a restaurant menu management system using JPA (Java Persistence API).

## Project Structure

The project follows a standard Maven structure with entities located in `src/main/java/com/restaurant/schema_design/`.

## Database Schema

The schema consists of the following entities:

### MenuCategory
Represents categories for menu items (e.g., Appetizers, Main Course).

- `categoryId` (Long, Primary Key)
- `name` (String, required, max 100 chars)
- `description` (Text)
- `image` (String)
- `displayOrder` (Integer, default 0)
- `active` (Boolean, default true)
- `restaurantId` (Long, required)
- Relationship: One-to-Many with MenuItem

### MenuItem
Represents individual menu items.

- `itemId` (Long, Primary Key)
- `itemName` (String, required, max 150 chars)
- `description` (Text)
- `itemType` (Enum: FOOD, BEVERAGE, DESSERT, APPETIZER)
- `foodType` (Enum: VEGETARIAN, NON_VEGETARIAN, VEGAN, GLUTEN_FREE)
- `basePrice` (BigDecimal, required)
- `preparationTime` (Integer)
- `calories` (Integer)
- `image` (String)
- `available` (Boolean, default true)
- `featured` (Boolean, default false)
- `taxPercentage` (BigDecimal, default 0)
- `createdAt` (LocalDateTime)
- `restaurantId` (Long, required)
- Relationships:
    - Many-to-One with MenuCategory
    - One-to-Many with MenuItemVariant
    - One-to-Many with MenuItemAddon
    - One-to-Many with MenuItemImage
    - One-to-Many with MenuOffer

### MenuItemVariant
Represents variations of a menu item (e.g., sizes: Small, Medium, Large).

- `variantId` (Long, Primary Key)
- `variantName` (String, required, max 100 chars)
- `priceAdjustment` (BigDecimal, required, default 0)
- `additionalPreparationTime` (Integer)
- `additionalCalories` (Integer)
- `available` (Boolean, default true)
- Relationship: Many-to-One with MenuItem

### MenuItemAddon
Represents add-ons for a menu item (e.g., extra cheese, sides).

- `addonId` (Long, Primary Key)
- `addonName` (String, required, max 100 chars)
- `price` (BigDecimal, required, default 0)
- `additionalPreparationTime` (Integer)
- `additionalCalories` (Integer)
- `available` (Boolean, default true)
- Relationship: Many-to-One with MenuItem

### MenuItemImage
Represents images associated with a menu item.

- `imageId` (Long, Primary Key)
- `imageUrl` (String, required)
- `altText` (String)
- `displayOrder` (Integer, default 0)
- `active` (Boolean, default true)
- Relationship: Many-to-One with MenuItem

### MenuOffer
Represents offers or promotions on menu items.

- `offerId` (Long, Primary Key)
- `offerName` (String, required, max 150 chars)
- `description` (Text)
- `offerType` (Enum: PERCENTAGE_DISCOUNT, FIXED_DISCOUNT, BUY_ONE_GET_ONE, FREE_ITEM)
- `discountValue` (BigDecimal)
- `minimumOrderValue` (BigDecimal)
- `startDate` (LocalDateTime)
- `endDate` (LocalDateTime)
- `active` (Boolean, default true)
- Relationship: Many-to-One with MenuItem

## Enums

- **ItemType**: FOOD, BEVERAGE, DESSERT, APPETIZER
- **FoodType**: VEGETARIAN, NON_VEGETARIAN, VEGAN, GLUTEN_FREE
- **OfferType**: PERCENTAGE_DISCOUNT, FIXED_DISCOUNT, BUY_ONE_GET_ONE, FREE_ITEM

## Relationships

- MenuCategory (1) -> MenuItem (N)
- MenuItem (1) -> MenuItemVariant (N)
- MenuItem (1) -> MenuItemAddon (N)
- MenuItem (1) -> MenuItemImage (N)
- MenuItem (1) -> MenuOffer (N)

All relationships use LAZY fetching for performance.

## Technologies Used

- Java 17+
- Spring Boot
- JPA/Hibernate
- Maven

## Example Data

Here are sample data examples for each entity:

### MenuCategory Examples
```json
{
  "categoryId": 1,
  "name": "Appetizers",
  "description": "Light starters to begin your meal",
  "displayOrder": 1,
  "active": true,
  "restaurantId": 1
}
```
```json
{
  "categoryId": 2,
  "name": "Main Course",
  "description": "Hearty main dishes",
  "displayOrder": 2,
  "active": true,
  "restaurantId": 1
}
```

### MenuItem Examples
```json
{
  "itemId": 1,
  "itemName": "Margherita Pizza",
  "description": "Classic pizza with tomato sauce, mozzarella, and basil",
  "itemType": "FOOD",
  "foodType": "VEGETARIAN",
  "basePrice": 12.99,
  "preparationTime": 15,
  "calories": 800,
  "available": true,
  "featured": true,
  "taxPercentage": 8.5,
  "restaurantId": 1,
  "category": {"categoryId": 2}
}
```
```json
{
  "itemId": 2,
  "itemName": "Caesar Salad",
  "description": "Crisp romaine lettuce with Caesar dressing",
  "itemType": "FOOD",
  "foodType": "VEGETARIAN",
  "basePrice": 8.99,
  "preparationTime": 5,
  "calories": 350,
  "available": true,
  "restaurantId": 1,
  "category": {"categoryId": 1}
}
```

### MenuItemVariant Examples
```json
{
  "variantId": 1,
  "variantName": "Small",
  "priceAdjustment": -2.00,
  "additionalPreparationTime": 0,
  "additionalCalories": -200,
  "available": true,
  "menuItem": {"itemId": 1}
}
```
```json
{
  "variantId": 2,
  "variantName": "Large",
  "priceAdjustment": 3.00,
  "additionalPreparationTime": 5,
  "additionalCalories": 400,
  "available": true,
  "menuItem": {"itemId": 1}
}
```

### MenuItemAddon Examples
```json
{
  "addonId": 1,
  "addonName": "Extra Cheese",
  "price": 1.50,
  "additionalPreparationTime": 2,
  "additionalCalories": 100,
  "available": true,
  "menuItem": {"itemId": 1}
}
```
```json
{
  "addonId": 2,
  "addonName": "Pepperoni",
  "price": 2.00,
  "additionalPreparationTime": 1,
  "additionalCalories": 150,
  "available": true,
  "menuItem": {"itemId": 1}
}
```

### MenuItemImage Examples
```json
{
  "imageId": 1,
  "imageUrl": "https://example.com/images/pizza1.jpg",
  "altText": "Margherita Pizza",
  "displayOrder": 1,
  "active": true,
  "menuItem": {"itemId": 1}
}
```

### MenuOffer Examples
```json
{
  "offerId": 1,
  "offerName": "Weekend Special",
  "description": "20% off on all pizzas",
  "offerType": "PERCENTAGE_DISCOUNT",
  "discountValue": 20.00,
  "minimumOrderValue": 15.00,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59",
  "active": true,
  "menuItem": {"itemId": 1}
}
```

## How to Build and Run

1. Ensure you have Java 17+ installed.
2. Clone or navigate to the project directory.
3. Run the Maven wrapper to compile:
   ```
   .\mvnw.cmd compile
   ```
4. To run the application:
   ```
   .\mvnw.cmd spring-boot:run
   ```

Note: This is a schema definition project. For a full application, you would need to add controllers, services, and configure a database.
