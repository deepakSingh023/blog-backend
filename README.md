# Blog Backend 

![Java](https://img.shields.io/badge/Java-17-orange?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-green?logo=springsecurity&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248?logo=mongodb&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-Auth-3ECF8E?logo=supabase&logoColor=white)
![Cloudinary](https://img.shields.io/badge/Cloudinary-Images-blue?logo=cloudinary&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Authentication-black?logo=jsonwebtokens&logoColor=white)
![Micrometer](https://img.shields.io/badge/Micrometer-Metrics-purple)
![Actuator](https://img.shields.io/badge/Spring%20Actuator-Observability-brightgreen)
![Render](https://img.shields.io/badge/Render-Deployed-46E3B7?logo=render&logoColor=white)

---

## Overview

This is my first complete backend project built using Java and Spring Boot.

The objective of this project was not only to build a functional blog API, but to deeply understand:

- Spring Boot architecture
- Spring Security and JWT flow
- Stateless authentication
- MongoDB data modeling tradeoffs
- Concurrency and race conditions
- Denormalization strategies
- Observability using tracing and metrics

It is implemented as a monolithic REST API, but structured with separation of concerns to keep services decoupled and maintainable.

---

## Architecture

The application follows a layered architecture:

- **Controller Layer** – REST endpoints  
- **Service Layer** – Business logic  
- **Repository Layer** – MongoDB operations  
- **Security Layer** – JWT validation & request filtering  

Although it is a monolith, services are logically separated and designed to reduce tight coupling, making future modularization possible.

---

## Authentication

Authentication is handled using **Supabase** as the identity provider.

Flow:

1. User registers or logs in via `/api/auth`
2. Supabase validates credentials
3. A JWT is issued
4. Spring Security validates the JWT on each protected request
5. The authenticated user ID is extracted and used within the service layer

Authentication is fully stateless.

---

## Database Design (MongoDB)

All data is stored in a single MongoDB database with multiple collections:

- blogs  
- comments  
- likes  
- profiles  

### Denormalization Strategy

To optimize read performance, I intentionally denormalized user information.

Each **Blog document** stores:
- Username  
- Profile image  

Each **Comment document** stores:
- Username  
- Profile image  

This eliminates additional lookups during feed rendering and avoids N+1 query patterns.

Tradeoff:
- Slightly more complex update logic  
- Significantly faster reads  

This decision was made because read operations (feed rendering) are more frequent than profile updates.

---

## Handling Profile Updates (Denormalized Data)

When a user updates their profile image:

- The profile document is updated
- An asynchronous background process updates:
  - All blogs created by that user
  - All comments created by that user

This is implemented using:

- `@Async` execution
- `MongoTemplate.updateMulti`
- `@Retryable` with exponential backoff
- `@Recover` for failure handling

This ensures eventual consistency while keeping user-facing operations responsive.

The system prioritizes fast reads with background consistency updates.

---

## Concurrency & Race Condition Handling

Special care was taken to avoid race conditions for likes and comments.

Instead of fetching a document, modifying it, and saving it back (which can cause lost updates), the system uses atomic MongoDB operations:

```java
new Update().inc("likes", 1);
new Update().inc("comments", 1);
```

MongoDB guarantees document-level atomicity for these operations.

This ensures:

- No lost updates
- Safe concurrent increments
- Correct behavior under multiple simultaneous requests

Like and comment counters are updated using direct `$inc` queries to avoid race conditions.

Updates are executed asynchronously to improve responsiveness.

---

## Eventual Consistency

Because some operations (like counter updates and denormalization) are asynchronous, the system follows an **eventual consistency model**.

For example:
- A like may be reflected slightly after the request completes.
- Profile image updates propagate in the background.

This tradeoff improves performance and scalability.

---

## Observability

The project includes basic observability features.

### Structured Logging
- Trace IDs for request tracking
- Aspect-based controller logging
- Latency measurement per API

### Metrics
- Micrometer integration
- Custom API latency metrics
- Percentile tracking (P50, P95, P99)
- Spring Boot Actuator endpoints

This helped me understand how real systems measure latency beyond simple averages.

---

## Performance Considerations

Performance was considered during design:

- Cursor-based pagination for feed
- Denormalized read optimization
- Atomic `$inc` counters
- Asynchronous background updates
- Compressed images before Cloudinary upload
- Latency percentile monitoring

Although simple in scope, the system was designed with scalability principles in mind.

---

## API Endpoints

### Authentication
```
POST   /api/auth/register
POST   /api/auth/login
```

### Blog
```
POST   /api/blog/create
PUT    /api/blog/update
GET    /api/blog/getBlog
GET    /api/blog/getUserBlogs
GET    /api/blog/getall
DELETE /api/blog/delete
```

### Feed & Search
```
GET /api/map/feed
GET /api/map/search
GET /api/map/userInfo
```

### Likes & Comments
```
POST   /api/like&comment/like
POST   /api/like&comment/removeLike
POST   /api/like&comment/create
DELETE /api/like&comment/removeComment
GET    /api/like&comment/get-comments
```

### Profile
```
POST /api/profile/update
GET  /api/profile/getProfile
```

---

## Deployment

The application is deployed on Render.

External services used:
- MongoDB Atlas
- Supabase
- Cloudinary

Due to free-tier infrastructure, cold starts may temporarily increase latency.

---

## What I Learned

Through building this system, I gained practical understanding of:

- Spring Security filter chain
- JWT authentication lifecycle
- MongoDB atomic operations
- Denormalization tradeoffs
- Eventual consistency
- Asynchronous processing in backend systems
- API latency percentiles (P95, P99)
- Observability using logs and metrics
- How infrastructure impacts performance perception

---

## Future Improvements

- Production-grade hosting
- Redis caching
- Rate limiting
- Improved monitoring (Prometheus/Grafana)
- Better failure handling strategies
- Potential modularization into services

---

## Final Note

This project represents my first serious backend system built from scratch.

It is intentionally simple in functionality, but designed with attention to:

- Correctness
- Concurrency safety
- Performance
- Observability
- Clean structure

It reflects my effort to move beyond tutorials and understand backend engineering in a practical and thoughtful way.

