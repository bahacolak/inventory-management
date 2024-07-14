# Inventory Management

This project is a small inventory management built using Java 17 and PostgreSQL.

## Overview

The project provides basic CRUD (Create, Read, Update, Delete) operations for inventory items and interacts with a PostgreSQL database. It includes business logic such as calculating the total value of inventory items and applying discounts.

## Technologies Used

- Java 17
- Spring Boot
- PostgreSQL

## Features

- RESTful API endpoints for managing inventory items
- Database schema with relationships (e.g., items and categories)
- Business logic for calculating total inventory value and applying discounts
- Unit and integration tests for robust testing coverage
- Error handling and logging
- Basic security practices implemented

## Setup and Installation

To run this project locally, make sure you have Java 17 and PostgreSQL installed. Here's how to get started:

1. Clone the repository:

   ```bash
   git clone https://github.com/bahacolak/inventory-management.git
   cd inventory-management

# Inventory Management API

This project provides an API for performing CRUD operations on users, inventory items, and categories. It is developed using PostgreSQL for data storage and Java 17.

## API Structure

### Users

- **GET /api/v1/users**: Fetch all users.
- **GET /api/v1/users/{id}**: Fetch a specific user by ID.
- **POST /api/v1/users/register**: Register a new user.
- **POST /api/v1/users/authenticate**: Authenticate a user.
- **PUT /api/v1/users/{id}**: Update a user.
- **DELETE /api/v1/users/{id}**: Delete a user.
- **GET /api/v1/users/{id}/inventory**: List inventory items of a user.
- **GET /api/v1/users/{id}/inventory/total-value**: Calculate the total value of a user's inventory.
- **PUT /api/v1/users/{id}/inventory/apply-discount**: Apply a discount to a user's inventory.

### Inventory

- **POST /api/v1/inventory/items**: Add a new inventory item.
- **GET /api/v1/inventory/items**: List all inventory items.
- **GET /api/v1/inventory/items/{id}**: Fetch a specific inventory item by ID.
- **PUT /api/v1/inventory/items/{id}**: Update an inventory item.
- **DELETE /api/v1/inventory/items/{id}**: Delete an inventory item.

### Categories

- **POST /api/v1/categories**: Add a new category.
- **GET /api/v1/categories**: List all categories.
- **GET /api/v1/categories/{id}**: Fetch a specific category by ID.
- **PUT /api/v1/categories/{id}**: Update a category.
- **DELETE /api/v1/categories/{id}**: Delete a category.

## Technologies Used

- **Java 17**: Primary programming language.
- **Spring Boot**: Framework used for creating RESTful APIs.
- **PostgreSQL**: Relational database management system used for data storage.

## Installation and Usage

1. Create your PostgreSQL database and add connection details to `application.properties`.
2. Build and run the application using your preferred IDE or Maven:
   ```bash
   mvn spring-boot:run

