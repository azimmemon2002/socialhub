# 🌐 SocialHub Microservices Application

Welcome to **SocialHub**, a microservices-based social networking application. This project demonstrates a microservices architecture using Spring Boot, Spring Cloud, Docker, and Docker Compose. The application includes services for authentication, user management, and an API gateway, all orchestrated using Eureka for service discovery.


## 📑 Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setting Up the Environment](#setting-up-the-environment)
- [Building and Running the Application](#building-and-running-the-application)
- [Accessing the Services](#accessing-the-services)
- [API Documentation](#api-documentation)
- [Environment Variables](#environment-variables)
- [Troubleshooting](#troubleshooting)
- [Acknowledgments](#acknowledgments)


## <h2 id="project-overview">🌟 Project Overview</h2>

**SocialHub** is a sample social networking application designed to showcase a microservices architecture using modern Java technologies. It consists of multiple microservices that handle different aspects of the application, such as authentication, user profiles, and an API gateway for routing.


## <h2 id="architecture">🏛️ Architecture</h2>

- **API Gateway (`sh-api-gateway`)**: Acts as a single entry point to the microservices, handling request routing, composition, and protocol translation.
- **Authentication Service (`sh-auth`)**: Manages user authentication, including registration and login, and issues JWT tokens for secure communication.
- **User Service (`sh-user`)**: Handles user profiles, friends management, posts, and other user-related functionalities.
- **Eureka Server (`sh-eureka`)**: Service discovery server that allows microservices to find and communicate with each other without hard-coding their locations.
- **MySQL Database**: Stores persistent data for the authentication and user services.



## <h2 id="project-structure">🗂️ Project Structure</h2>

```bash
socialhub/
├── mysql-init/
│   └── init.sql
├── scripts/
│   └── build.sh
├── sh-api-gateway/
├── sh-auth/
├── sh-user/
├── sh-eureka/
├── docker-compose-with-build.yml
├── .env
└── README.md
```



## <h2 id="prerequisites">🛠️ Prerequisites</h2>

Before running the application, ensure you have the following installed:

- **Java JDK 17**: The application is built using Java 17.
- **Maven 3.8.5 or higher**: For building the Spring Boot applications.
- **Docker Engine 20.10 or higher**: For containerization.
- **Docker Compose 1.29 or higher**: For orchestrating multi-container Docker applications.


## <h2 id="setting-up-the-environment">🌱 Setting Up the Environment</h2>

### 1. Clone the Repository

```bash
git clone https://github.com/azimmemon2002/socialhub.git
cd socialhub
```

### 2. Set Up Environment Variables

Create a `.env` file at the root of the project to store environment variables used by Docker Compose.

**`.env`**:

```dotenv
MYSQL_ROOT_PASSWORD=your_mysql_root_password
MYSQL_DATABASE=socialhub_db
```

- Replace `your_mysql_root_password` with a secure password of your choice.


## <h2 id="building-and-running-the-application"> 🚀 Building and Running the Application</h2>

### 1. Use the `build.sh` Script

The `build.sh` script automates the process of building all the microservices and starting them using Docker Compose.

#### Steps:

1. **Navigate to the `scripts` Directory**:

   ```bash
   cd scripts
   ```

2. **Run the Build Script**:

   ```bash
   ./build.sh
   ```

   **Script Actions**:

   - Builds services (`sh-api-gateway`, `sh-auth`, `sh-eureka`, `sh-user`) using Maven.
   - Executes Docker Compose to start the services using `docker-compose-with-build.yml`.

   **Note:** Ensure you have the correct Java and Maven setup, as the script uses Java 17 (`JAVA_HOME` is set to `C:/Program Files/Java/jdk-17`). Modify this path if your Java installation is in a different directory.
3. **Verify the Services**:

   ```bash
   docker-compose ps
   ```

#### Start the Services:

To start services:

```bash
docker-compose -f docker-compose-with-build.yml up -d
```

#### Stop the Services:

To stop all services:

```bash
docker-compose down
```

#### View Service Logs:

```bash
docker-compose logs -f
```


## <h2 id="accessing-the-services"> 🌐 Accessing the Services</h2>

### Local URLs:

| Service              | Local Port | URL                                            |
| -------------------- | ---------- | ---------------------------------------------- |
| **Eureka Dashboard** | 8761       | [http://localhost:8761](http://localhost:8761) |
| **API Gateway**      | 8080       | [http://localhost:8080](http://localhost:8080) |
| **Authentication**   | 8081       | [http://localhost:8081](http://localhost:8081) |
| **User Service**     | 8082       | [http://localhost:8082](http://localhost:8082) |

### Docker URLs:

| Service              | Docker Exposed Port | URL                                            |
| -------------------- | ------------------- | ---------------------------------------------- |
| **Eureka Dashboard** | 8762                | [http://localhost:8762](http://localhost:8762) |
| **API Gateway**      | 8090                | [http://localhost:8090](http://localhost:8090) |
| **Authentication**   | 8091                | [http://localhost:8091](http://localhost:8091) |
| **User Service**     | 8092                | [http://localhost:8092](http://localhost:8092) |



## <h2 id="api-documentation"> 📖 API Documentation</h2>

The **SocialHub** application includes Swagger UI for API exploration and testing.

### Local Access:

- **Authentication Service Swagger UI**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- **User Service Swagger UI**: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)

### Docker Access:

- **Authentication Service Swagger UI**: [http://localhost:8091/swagger-ui/index.html](http://localhost:8091/swagger-ui/index.html)
- **User Service Swagger UI**: [http://localhost:8092/swagger-ui/index.html](http://localhost:8092/swagger-ui/index.html)



## <h2 id="environment-variables"> ⚙️ Environment Variables</h2>

### Required:

- **MYSQL_ROOT_PASSWORD**: Root password for the MySQL database.
- **MYSQL_DATABASE**: The name of the MySQL database to create.

### Optional:

- **PORT**: Service ports (`8761`, `8080`, `8081`, `8082` for local; `8762`, `8090`, `8091`, `8092` for Docker).
- **SPRING_PROFILES_ACTIVE**: Active Spring profiles (set to `docker` in Docker Compose).
- **EUREKA_CLIENT_SERVICEURL_DEFAULTZONE**: Eureka server URL.
- **SPRING_DATASOURCE_URL**: JDBC URL for the database.
- **SPRING_DATASOURCE_USERNAME**: Database username.
- **SPRING_DATASOURCE_PASSWORD**: Database password.
- **AUTH_SERVICE_URL**: URL of the authentication service.


## <h2 id="troubleshooting"> 🔧 Troubleshooting</h2>

### Common Issues:

- **Services Not Starting**: Check for port conflicts or configuration errors in `application.yml`.
- **MySQL Connection Issues**: Verify database credentials and ensure the `sh-mysql-db` service is healthy.

### Logs:

```bash
docker-compose logs -f
```

### Rebuild Services:

```bash
docker-compose up --build
```



## <h2 id="acknowledgments"> 📜 Acknowledgments</h2>

- **Spring Boot**: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
- **Spring Cloud**: [https://spring.io/projects/spring-cloud](https://spring.io/projects/spring-cloud)
- **Docker**: [https://www.docker.com/](https://www.docker.com/)
- **MySQL**: [https://www.mysql.com/](https://www.mysql.com/)

---

Happy coding! 💻✨
