# EnviroWatch

Air quality monitoring platform powered by [OpenAQ](https://openaq.org) open data.

## Project Structure

```
envirowatch/
├── mobile/     # Flutter app (BLoC, Clean Architecture)
├── backend/    # Spring Boot REST API (PostgreSQL)
└── docs/       # Documentation, Swagger, Postman collections
```

## Tech Stack

| Layer    | Technology                          |
|----------|-------------------------------------|
| Mobile   | Flutter, BLoC, GetIt, GoRouter, Dio |
| Backend  | Spring Boot, PostgreSQL, Swagger    |
| Data     | OpenAQ API v3                       |

## Getting Started

### Mobile
```bash
cd mobile
flutter pub get
flutter run
```

### Backend
```bash
cd backend
./gradlew bootRun
```
