# Etsy Clone Web Application

## Table of Contents
- [Project Description](#project-description)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Screenshots](#screenshots)
- [Development Setup](#development-setup)
- [Installation Instructions](#installation-instructions)
- [Contributing](#contributing)

## Project Description
Etsy Clone is an e-commerce web application that allows users to buy and sell handmade or vintage items, art, and supplies. Users can create an account, list items for sale, and purchase items from other users. This project adopts a monolithic modular MVC architecture with clear layering and separation of concerns.

## Tech Stack
- **Backend:** Java, Spring Boot (MVC, REST, SECURITY, DATA JPA), Hibernate, MySQL
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript
- **Tools/Libraries:** Lombok, Bootstrap, Postman, MySQL Workbench, GUAVA, JWT

## Features
- RESTful API with full CRUD operations
- User authentication and authorization with Spring Security and JWT
- Server-Side Rendering
- Data transfer using DTOs between layers
- Database interaction using Hibernate
- Reduced boilerplate code using Lombok
- HTML template rendering with Thymeleaf
- Styling with Bootstrap
- Client-side validation using JavaScript

## Screenshots
![img.png](img.png)

## Development & Installation Instructions
To set up the development environment, follow these steps:

## Installation Instructions
To clone and run the Etsy Clone web application on your local environment, follow these steps:

1. **Prerequisites:**
    - Java Development Kit (JDK) 17
    - Apache Maven
    - MySQL (WORKBENCH or CLI)
    - Intellij IDE (recommended)

2. **Clone the repository:**
    ```bash
    git clone https://github.com/M-Elsayed9/Etsy-clone-CISC-4900.git
    cd etsy-clone
    ```

3. **Setup:**
    - Open the project in your preferred IDE (Intellij recommended).
    - Create a MySQL database named `etsy_clone`.
    - Update the `application.properties` file in `src/main/resources` with your MySQL credentials:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/etsy_clone
        spring.datasource.username=your_username
        spring.datasource.password=your_password
        ```
    - Build and run the Spring Boot application:
        ```bash
        ./mvnw spring-boot:run
        ```

4**Access the application:**
    - Open your web browser and go to `http://localhost:8080`.

You now have the Etsy Clone web application running on your local environment. Happy coding!

## Contributing
Contributions are welcome! Please follow these steps to contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature/your-feature`).
6. Open a Pull Request.
7. Your changes will be merged once they are approved.