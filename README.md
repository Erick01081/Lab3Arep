# Java Web Server and Dependency Injection Framework with POJO Integration

This project implements a basic Java web server and a dependency injection framework, using custom annotations to simulate a Spring Boot-like experience. It demonstrates how to build a web server that handles HTTP requests, processes dependencies through annotations, and integrates Plain Old Java Objects (POJOs).

## Project Architecture

The "Java Web Server and Dependency Injection Framework with POJO Integration" project is designed to demonstrate basic web server functionalities and dependency injection using a minimalistic approach. Below is an overview of the project architecture:

### Components

1. **Annotations**: Custom annotations are used to define request mappings and dependency injections.
   - `@GetMapping`: Maps HTTP GET requests to specific methods.
   - `@RequestParam`: Binds request parameters to method parameters.
   - `@RestController`: Indicates that a class handles HTTP requests.

2. **Web Server**: Implements a simple HTTP server to handle client requests.
   - **`SimpleHttpServer`**: Listens on a specified port, processes incoming requests, and routes them to appropriate service methods or static file handlers.
   - **Endpoint Mapping**: Methods annotated with `@GetMapping` are mapped to their respective paths, and requests are dispatched based on these mappings.

3. **Service Layer**: Contains the logic for handling HTTP requests and serving responses.
   - **`HelloService`**: Example service class demonstrating various endpoints, including serving static files and processing dynamic requests.

4. **Dependency Injection**: Simulated through annotations to manage method parameters and inject values from the request.
   - **`SpringECI`**: Utility class to demonstrate method invocation with parameters extracted from query strings, simulating a basic dependency injection mechanism.

### Workflow

1. **Request Handling**:
   - The `SimpleHttpServer` class accepts incoming HTTP requests.
   - Requests are parsed to extract paths and query parameters.

2. **Routing**:
   - Requests are matched to methods annotated with `@GetMapping` based on the request path.
   - Methods are invoked using reflection, with parameters resolved from the query string if applicable.

3. **Static File Serving**:
   - Static files are served from a designated directory, with content-type handling and base64 encoding for images.

4. **Response Construction**:
   - Responses are constructed and sent back to the client with appropriate status codes and content types.

### Directory Structure

```
└───src
    ├───main
    │   ├───java
    │   │   └───edu
    │   │       └───escuelaing
    │   │           └───arem
    │   │               └───ASE
    │   │                   └───app
    │   │                       ├───MyOwnJUnit
    │   │                       └───MyOwnSpringboot
    │   └───resources
    └───test
        └───java
            └───edu
                └───escuelaing
                    └───arem
                        └───ASE
                            └───app
```


### Dependencies

- **Java SDK**: The project relies on Java SE for compiling and running the server.
- **Custom Annotations**: Implemented within the project to handle request mapping and parameter injection.

This architecture provides a simplified view of how a web server and dependency injection framework can be structured, offering a foundational understanding of these concepts.


## Getting Started

These instructions will guide you through setting up the project on your local machine for development and testing purposes.

### Prerequisites

You need to have Java and an appropriate IDE to run the project.

- **Java Development Kit (JDK)**: Ensure you have version 11 or above.

## Running the Project

To run different components of the project, use the following commands:

1. **Clone the repository:**
   ``` bash
   git clone https://github.com/Erick01081/Lab3Arep.git cd Lab3Arep
   ```
2. **Compile the project:**
   ``` bash
   javac -d bin src/edu/escuelaing/arem/ASE/app/*.java
   ```


