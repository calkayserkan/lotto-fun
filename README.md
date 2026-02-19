🎯 LottoFun – Microservice Lottery System
1. Overview

LottoFun is a microservice-based lottery backend system developed using Spring Boot.
The system allows users to register, participate in draws, purchase tickets, and evaluate prizes.

The architecture is containerized using Docker Compose and follows a distributed service design.

2. Domain Model Design

Core Entities
🧑 User (Auth Service)

Field	Type	Description

id	Long	Unique identifier
username	String	Unique username
password	String	Encrypted password
balance	BigDecimal	User wallet balance
createdAt	LocalDateTime	Registration timestamp

🎟 Draw (Draw Service)

Field	Type	Description
id	Long	Unique draw id
status	Enum	DRAW_OPEN / DRAW_FINALIZED
winningNumbers	String	Generated winning numbers
drawDate	LocalDateTime	Scheduled draw date
ticketPrice	BigDecimal	Ticket cost
jackpotPool	BigDecimal	Total prize pool

🎫 Ticket (Ticket Service)

Field	Type	Description
id	Long	Ticket id
userId	Long	Owner user
drawId	Long	Related draw
selectedNumbers	String	Chosen numbers
purchaseDate	LocalDateTime	Purchase timestamp
ticketStatus	Enum	PENDING / WON / LOST
matchCount	Integer	Matched numbers
prizeType	Enum	NONE / SMALL / MEDIUM / JACKPOT
ticketNumber	String	Unique ticket code

Relationships

One User → Many Tickets
One Draw → Many Tickets
Services communicate via REST
TicketPurchasedEvent is published via RabbitMQ

3. API Specification
🔐 Auth Service (Port 8080)

Register
POST /auth/register
Request:
{
  "username": "user1",
  "password": "1234"
}

Login
POST /auth/login
Response:
{
  "token": "JWT_TOKEN"
}

Get User Info
GET /users/user-info/{username}

Decrease Balance
POST /users/decrease-balance?userId=1&amount=10

🎯 Draw Service (Port 8081)

Create Draw
POST /draws/create

Get Draw Info
GET /draws/draw-info/{id}

List Draws (Pageable)
GET /draws/list/pageable?page=0&size=10&sort=id,desc

🎫 Ticket Service (Port 8083)

Purchase Ticket
POST /tickets/purchase
Request:
{
  "drawId": 1,
  "selectedNumbers": "1,2,3,4,5"
}

Process:

Validate user
Validate draw status
Check balance
Decrease balance
Create ticket
Publish TicketPurchasedEvent


4. Draw Logic & Prize Distribution
Draw Logic

Draw must be in DRAW_OPEN state to allow ticket purchase.
After draw date, winning numbers are generated.
Draw status becomes DRAW_FINALIZED.
Prize Distribution Logic

Match count determines prize type:

Match Count	Prize Type
0-1	NONE
2	SMALL
3-4	MEDIUM
5	JACKPOT
Jackpot pool is distributed among winning tickets.

5. Architecture & Design
   
Microservices

Auth Service
Draw Service
Ticket Service
PostgreSQL
RabbitMQ

Communication
REST communication between services
JWT authentication
RabbitMQ for event publishing


High-Level Architecture
Client
   |
   v
Ticket Service
   |         \
   v          v
Draw Service  Auth Service
        \       /
         v     v
        PostgreSQL


RabbitMQ is used for asynchronous ticket events.

6. Testing Approach
   
Manual Testing
Swagger UI for endpoint validation
Postman Collection provided
JWT authentication tested via Swagger

Integration Testing
Cross-service communication verified
Docker environment used for end-to-end validation

7. Setup Instructions
   
Requirements
Docker
Docker Compose

Run the Application
./mvnw clean package -DskipTests
docker compose up --build

Service Ports
Service	Port
Auth Service	8080
Draw Service	8081
Ticket Service	8083
RabbitMQ	15672
Stop Containers
docker compose down

8. Notes
Each service has its own database schema.
Services communicate using internal Docker network names.
Basic transactional consistency is ensured within services.
Event-driven architecture is partially implemented via RabbitMQ.

