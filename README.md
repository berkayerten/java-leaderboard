# Leaderboard Project

This project is a **real-time leaderboard system** built in **Java**. It efficiently tracks and displays user scores, supporting live updates and persistent storage.

## Features

- **Submit Scores**: Users can submit their scores via a REST API.
- **Top Rankings**: Retrieve the top N users and their scores.
- **Real-Time Updates**: WebSocket integration for instant leaderboard changes.
- **Persistence**: Scores are stored in Cassandra for durability.
- **Caching**: Redis is used for fast leaderboard queries and updates.
- **Async Communication**: WebSockets and Redis Pub/Sub are used for asynchronous communication between server and clients.


## Getting Started

### Prerequisites

- Java 17+
- Docker (for Redis and Cassandra)

### Access the client:
- Open src/main/resources/static/client.html in your browser.

### API Endpoints
`POST /api/v1/scores`   
Submit a score:
```json
{
  "userId": "userFoo",
  "score": 100
}
```

`GET /api/v1/top/{n}`   
Get the top N users.
```json
[
  {
    "score": 429,
    "userId": "userFoo"
  },
  {
    "score": 424,
    "userId": "userBar"
  }
]
```

The `sorted-set-key` in Redis is used to store leaderboard scores efficiently. In this project, the key `leaderboard:zset` (configured in `application.yml`) represents a Redis Sorted Set data structure. Each user's score is stored as a member of this set, with the score as the sorting value. This allows fast retrieval of top N users by score, as well as quick updates when scores change.   

**sorted-set-key**'s implement `skip list` data structure internally, which provides efficient O(log N) complexity for insertions, deletions, and lookups, making it ideal for leaderboard applications where frequent updates and queries are common.

### Technologies

- Java / SpringBoot
- Redis
- Cassandra
- WebSockets