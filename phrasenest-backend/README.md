# PhraseNest Backend

A modular Spring Boot backend for an application focused on idioms, phrases,
phrasal verbs, proverbs, and common English expressions.

## Requirements

- Java 21
- Docker Desktop
- Maven 3.9+

## Start PostgreSQL

```bash
docker compose up -d postgres
```

## Run the backend

```bash
./mvnw spring-boot:run
```

If the Maven wrapper is not present, use:

```bash
mvn spring-boot:run
```

## Test the public endpoint

```bash
curl http://localhost:8080/api/v1/public/system
```

## Health endpoint

```bash
curl http://localhost:8080/actuator/health
```

## Current modules

- expression
- category
- search
- ai
- user
- favorite
- collection
- learning
- popularity
- admin
- shared
- system

## Next milestone

Create the expression catalog schema:

- expression_types
- expressions
- expression_aliases
- expression_examples
- categories
- expression_categories
- common_mistakes
- expression_relations
