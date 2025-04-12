# URL Shortener & Redirector

Prosty projekt rozproszonej aplikacji webowej napisanej w Spring Boot i uruchomionej w środowisku Docker.  
Aplikacja składa się z dwóch mikroserwisów:

- `url-shortener` – służy do skracania linków i zapisu do bazy danych
- `url-redirector` – odpowiada za przekierowanie na podstawie skróconego linku

Oba serwisy korzystają ze wspólnej bazy danych PostgreSQL (również uruchomionej w Dockerze).

---

## Technologie

- Java 17
- Spring Boot 3.2.4
- PostgreSQL (kontener)
- Docker & Docker Compose

---

##  Uruchamianie projektu

1. Zbuduj i uruchom cały projekt w Dockerze:

docker-compose up --build
Po uruchomieniu usługi dostępne będą pod:

Serwis	Port	Opis
url-shortener	8080	Skracanie URL
url-redirector	8081	Przekierowanie
postgres	5432	Wewnętrzna baza danych

Przykładowe użycie
1. Skrócenie linku
Request:
GET http://localhost:8080/shorten?url=https://bestservers.fun/&ttl=120
Response:
http://localhost:8081/a1B2c3

2. Przekierowanie
Request:
GET http://localhost:8081/a1B2c3
Response:
Automatyczne przekierowanie do https://bestservers.fun/
```text
Struktura projektu
Rozproszone_aplikacje_lab2/
├── docker-compose.yml
├── url-shortener/
│   ├── Dockerfile
│   └── src/main/java/...
├── url-redirector/
│   ├── Dockerfile
│   └── src/main/java/...
└── README.md
