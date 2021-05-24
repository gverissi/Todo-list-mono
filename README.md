# Todo-List App

## Getting started Locally

### Using a database
Using PostgreSQL create a database.  
Then, rename the file `application-dev.properties.example` to  `application-dev.properties` and
fil it up with your database credentials.  
Make sure the variable `spring.profiles.active` is set to `dev` in the `application.properties` file.  

### Using in memory storage
You just need to set the variable `spring.profiles.active` to `memory` in the `application.properties` file

### Run tests
In a command line just type: `mvn test`

### Run the Application
In a command line just type: `mvn spring-boot:run`  
Then go to `http://localhost:8080/home`  