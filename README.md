# Library Management System
This is a simple library management REST API developed with Spring Boot.

## Core Features
 - Add, get, update and delete books.
 - Add, get, update and delete patrons.
 - Borrow and return books.

## Additional Features Implemented
 - Basic authentication.
 - Caching books and patrons.
 - Logging for all endpoints using Aspect-Oriented Programming (AOP).

## API Endpoints
 - Book endpoints:
	 - GET /api/books: Retrieve a list of all books.
	 - GET /api/books/{id}: Retrieve details of a specific book by ID.
	 - POST /api/books: Add a new book to the library.
	 - PUT /api/books/{id}: Update an existing book's information.
	 - DELETE /api/books/{id}: Remove a book from the library.
 - Patron management endpoints:
	 - GET /api/patrons: Retrieve a list of all patrons.
	 - GET /api/patrons/{id}: Retrieve details of a specific patron by ID.
	 - POST /api/patrons: Add a new patron to the system.
	 - PUT /api/patrons/{id}: Update an existing patron's information.
	 - DELETE /api/patrons/{id}: Remove a patron from the system.
 - Borrowing endpoints:
	 - POST /api/borrow/{bookId}/patron/{patronId}: Allow a patron to borrow a book.
	 - PUT /api/return/{bookId}/patron/{patronId}: Record the return of a borrowed book by a patron.

## Users
Currently, the users are hardcoded in the application. Use any of the following users with their corresponding passwords with basic authentication when testing the endpoints:

| **User** | **Password** |
|--|--|
| user1 | password1 |
| user2 | password2 |
| user3 | password3 |

## Running and Testing the Application
 - Clone the project to your machine.
 - Import the project in your favorite Java IDE (optional).
 - Start the application by clicking the start button in your IDE or by running `./gradlew bootRun` in the root directory of the project.
 - Test the different endpoints using an API testing tool such as curl or Postman.
 - You can inspect the database contents while testing by using the database console which is available at [http://localhost:8080/api/h2-console](http://localhost:8080/api/h2-console) while the application is running. When you open the console, you will be prompted with some information. The JDBC URL can be found in the application logs. Leave the other fields as they are. Click on Connect to connect to the console.
 - You can run the unit tests from inside your IDE or by running `./gradlew test` in the root directory of the project.
