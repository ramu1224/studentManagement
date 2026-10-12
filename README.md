# Student Management

A small full stack app to manage students and the courses they are enrolled in.
A student can be enrolled in many courses, and you can create, view, edit and delete both students and courses.

**Tech stack**

- Backend: Java 17, Spring Boot 3, Spring Data JPA, H2 (in-memory database)
- Frontend: Angular 18, RxJS, Bootstrap 5
- Tests: JUnit 5 and Mockito (backend), Jasmine and Karma (frontend)

## What you need installed

- JDK 17 or newer
- Maven
- Node.js 18.19+ (or 20+) and npm

## How to run

Start the backend first, in one terminal:

```bash
cd backend
mvn spring-boot:run
```

It runs on http://localhost:8080. The database is H2 in memory, so there is nothing to install. It starts with a few sample students and courses, and the data is reset every time the backend restarts.
(H2 console, if you want to look at the tables: http://localhost:8080/h2-console, JDBC URL `jdbc:h2:mem:studentdb`, user `sa`, no password.)

Then start the frontend in a second terminal:

```bash
cd frontend
npm install
npm start
```

Open http://localhost:4200 in the browser.

## How to run the tests

```bash
# backend
cd backend
mvn test

# frontend (needs Chrome installed)
cd frontend
npm test
```

## REST endpoints

| Method | URL | What it does |
|--------|-----|--------------|
| GET | /api/students | list all students |
| GET | /api/students/{id} | get one student |
| POST | /api/students | create a student (with the courses they take) |
| PUT | /api/students/{id} | update a student and their courses |
| DELETE | /api/students/{id} | delete a student |
| GET | /api/courses | list all courses |
| GET | /api/courses/{id} | get one course |
| POST | /api/courses | create a course |
| PUT | /api/courses/{id} | update a course |
| DELETE | /api/courses/{id} | delete a course (it is also removed from the students who had it) |

Errors come back as JSON with a `message`. Validation errors also have an `errors` object with one message per field.
Status codes: 400 for invalid data, 404 when something is not found, 409 when the email is already used.

## Notes

- Backend validation is done with Bean Validation annotations on the entities (`@NotBlank`, `@Email`, `@Size`).
- Errors are handled in one place, `GlobalExceptionHandler`, and the services are `@Transactional`.
- On the frontend, the students and courses are kept in `BehaviorSubject`s inside the services, and a small HTTP interceptor shows errors in a banner at the top.
- The Students and Courses tabs use the router. Deleting asks for confirmation in a modal.
