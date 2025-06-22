# About the API

![Java](https://img.shields.io/badge/Java-%23F89820.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-%238BC34A.svg?style=for-the-badge&logo=spring&logoColor=white) ![Swagger](https://img.shields.io/badge/Swagger-%238D6E63.svg?style=for-the-badge&logo=swagger&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-%232496ED.svg?style=for-the-badge&logo=docker&logoColor=white)

A REST API developed using Java Spring that enables users to interact with League of Legends (LoL) champions through a chat completion service.

# Installation Guide

## 1. Running the Application with Docker Compose

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Steps

#### 1. Obtain an API Key for the Chat Completion Service

This API connects to the Llama3-8b-8192 model provided by Groq Cloud. To acquire an API key, follow [these instructions](https://console.groq.com/keys) and add the key to the `CHAT_SERVICE_KEY` environment variable in the `docker-compose.yaml` file. Also, set the `CHAT_SERVICE_URL` accordingly.

#### 2. Starting the Application
Run the following command to start the containers:

```bash
docker-compose up -d
```

By default, the application will be available at `http://localhost:8080`.

### Clean Architecture
![REST API Architectural Diagram](docs/images/architectural-diagram.png)

The project structure includes five main directories:
- `application`: Contains use cases and interfaces for accessing resources such as databases and HTTP clients.
- `domain`: Defines system entities and business-rule exceptions.
- `infrastructure`: Implements the application layer's gateways, providing access to databases, repositories, HTTP client interfaces, controllers, DTOs, framework-specific exceptions, and other Spring resources.
- `configuration`: Holds configuration files with dependency injection beans.
- `shared`: Contains utility classes accessible across multiple layers.

# Tests

## Running Tests
### Prerequisites

- [Java 21](https://www.oracle.com/br/java/technologies/downloads/#java21)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (required for integration tests)

Run the following commands in the terminal from the application's root directory:

- **Only unit tests:**

```bash
./mvnw test
```

- **For integration and unit tests:**
```bash
./mvnw verify
```

> **Note:** Ensure Docker is running, as the application uses TestContainers to create a PostgreSQL database in Docker for each integration test class.

# Documentation

## API Endpoints Preview
```text
GET /champions - Retrieve a list of champions.

POST /champions/ask/{id} - Ask a question to a specific champion by ID and retrieve the champion's response.
```

## OpenAPI Documentation
- To view the full API documentation, including endpoints and data schemas, open the Swagger UI at:
  `/swagger-ui/index.html`

- For API documentation in JSON format suitable for tools like Postman, Insomnia, and other API clients, visit: `/v3/api-docs`.

---
