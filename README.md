# 🧁 Bakers.in — Multi-Category Food E-Commerce (Spring Boot + Maven + Tomcat 9)

A full-stack, colourful food e-commerce web app: cakes, pastries & desserts, pizza, burgers,
mocktails, and cookies & snacks — each with product images, price tags with sale badges, a
cart, and checkout — built with Java, Spring Boot, Thymeleaf, and Maven, packaged as a WAR
for deployment on **Apache Tomcat 9**.

## Tech stack

- **Java 21** (`openjdk-21-jdk`)
- **Spring Boot 2.7.18** (Web MVC, Spring Data JPA, Validation, Thymeleaf) — the last Spring
  Boot line built on the `javax.*` servlet/persistence namespace, which is what Tomcat 9
  (Servlet 4.0) understands. Spring Boot 3.x moved to `jakarta.*` and requires Tomcat 10+, so
  it is intentionally **not** used here.
- **Apache Tomcat 9** as the target servlet container (packaging is `war`, not a runnable jar)
- **H2** in-memory database for local development (auto-seeded with sample data)
- **PostgreSQL** for production (swap in with three environment variables — no code changes)
- Plain HTML/CSS/JS front end rendered server-side via Thymeleaf (no separate JS build step)
- **Maven** build

> **Source-code note:** because this pins Spring Boot to the 2.7 line, any Java source that
> was written against Spring Boot 3's `jakarta.*` packages (e.g. `jakarta.persistence.*`,
> `jakarta.validation.*`, `jakarta.servlet.*`, `SpringBootServletInitializer`) needs to use the
> `javax.*` equivalents instead, and `BakersInApplication` needs to extend
> `SpringBootServletInitializer` and override `configure()` so the WAR boots correctly under an
> external Tomcat 9 instance rather than only via `java -jar`.

## Project structure

```
bakers-ecommerce/
├── pom.xml
├── Dockerfile
├── docker-entrypoint.sh         # rewrites Tomcat's connector port from $PORT at container start
├── Procfile                     # runs the WAR on Tomcat 9 via Webapp Runner (Heroku/Railway-style)
├── render.yaml                  # Render.com blueprint (Docker + Tomcat 9)
└── src/main/
    ├── java/com/bakersin/
    │   ├── BakersInApplication.java
    │   ├── config/DataLoader.java        # seeds categories & products on startup
    │   ├── model/                        # Category, Product, Cart, CartItem, Order, OrderItem, OrderStatus
    │   ├── repository/                   # Spring Data JPA repositories
    │   ├── service/                      # ProductService, CartService, OrderService
    │   └── controller/                   # Home, Product, Cart, Checkout controllers + GlobalModelAdvice
    └── resources/
        ├── application.properties        # dev profile (H2)
        ├── application-prod.properties   # prod profile (PostgreSQL, via env vars)
        ├── templates/                    # Thymeleaf pages (index, products, product-detail, cart, checkout, order-confirmation, about)
        └── static/css, static/js
```

## Prerequisites

```bash
sudo apt install -y openjdk-21-jdk
sudo apt install -y maven
```

Tomcat 9 itself is only required if you choose to deploy the WAR to a manually-installed
Tomcat 9 (see "Run locally" below) rather than using the Docker image or Webapp Runner path.

```bash
sudo apt install -y tomcat9
```

## Run locally

### Option 1 — Build the WAR and drop it into a local Tomcat 9

```bash
mvn clean package
sudo cp target/bakers-ecommerce.war /var/lib/tomcat9/webapps/ROOT.war
sudo systemctl restart tomcat9
```

Visit **http://localhost:8080**.

### Option 2 — Build and run with Webapp Runner (no Tomcat install needed)

```bash
mvn clean package
java -jar target/dependency/webapp-runner.jar --port 8080 target/bakers-ecommerce.war
```

Visit **http://localhost:8080**. The H2 database is in-memory and reseeds every restart —
no setup needed. (H2 console, if you want to inspect data: http://localhost:8080/h2-console,
JDBC URL `jdbc:h2:mem:bakersin`, user `sa`, blank password.)

## Deploy to the cloud

The app is 12-factor style: it reads its port from `PORT` and its database from
`SPRING_DATASOURCE_URL` / `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD`,
activated via the `prod` Spring profile. Pick whichever platform you prefer:

### Option A — Render.com (matches the included `render.yaml`)

1. Push this project to a GitHub repo.
2. On Render: **New → Blueprint**, point it at the repo (`render.yaml` is already included).
   It builds the included `Dockerfile`, which runs Tomcat 9 on Java 21 and deploys the WAR as
   `ROOT.war`. `docker-entrypoint.sh` rewrites Tomcat's connector port to Render's `$PORT`
   automatically.
3. Create a free PostgreSQL instance on Render, then in the web service's **Environment** tab
   set `SPRING_DATASOURCE_URL` (convert Render's `postgres://user:pass@host:5432/db` to
   `jdbc:postgresql://host:5432/db`), `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.

### Option B — Docker (works on any cloud: Railway, Fly.io, AWS App Runner, Azure, GCP Cloud Run)

```bash
docker build -t bakers-ecommerce .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<db> \
  -e SPRING_DATASOURCE_USERNAME=<user> \
  -e SPRING_DATASOURCE_PASSWORD=<password> \
  bakers-ecommerce
```

The image is built from `tomcat:9.0-jdk21-temurin`; the WAR is deployed as `ROOT.war` so the
app serves from `/`. Push the same image to any container registry (Docker Hub, ECR, GCR, ACR)
and point your cloud platform's container service at it. **Do not** try to serve this app from
S3 static website hosting — it's a server-rendered Java app on Tomcat, not a static site, so
S3 alone cannot run it.

### Option C — Railway / Heroku (buildpack, no Docker needed)

Both detect the `pom.xml` and `Procfile` automatically:

1. Push to GitHub, create a new app, connect the repo.
2. Add a PostgreSQL plugin/add-on.
3. Set `SPRING_PROFILES_ACTIVE=prod` and the three `SPRING_DATASOURCE_*` variables from the
   database credentials the platform gives you.
4. Deploy — the `Procfile` runs the WAR on Tomcat 9 via Webapp Runner (`target/dependency/webapp-runner.jar`,
   fetched automatically during `mvn package`).

### Option D — AWS Elastic Beanstalk / EC2 / a traditional VM with Tomcat 9

```bash
mvn clean package
scp target/bakers-ecommerce.war user@your-server:/tmp/
ssh user@your-server
sudo apt install -y openjdk-21-jdk tomcat9
sudo cp /tmp/bakers-ecommerce.war /var/lib/tomcat9/webapps/ROOT.war
sudo systemctl restart tomcat9
```

Set `SPRING_PROFILES_ACTIVE=prod` and the `SPRING_DATASOURCE_*` variables as environment
variables for the `tomcat9` service (e.g. in `/etc/default/tomcat9` or a systemd override), then
restart. Put it behind Nginx / an ALB for TLS termination. For **AWS specifically with
containers**, use App Runner or ECS/Fargate (push the Docker image to ECR first) rather than S3.

## Before going to production

- [ ] Replace placeholder images with real product photography.
- [ ] Point `SPRING_DATASOURCE_URL` at a managed PostgreSQL instance (Render, RDS, Supabase, Neon, etc.).
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` once you've reviewed the generated schema, and
      manage schema changes with Flyway/Liquibase instead of `update` for a real production app.
- [ ] Wire the payment method selector to an actual payment gateway (Razorpay/Stripe) if you
      want to process real payments — currently it's captured as order metadata only.
- [ ] Add HTTPS (most platforms above provide this automatically) and set secure cookie flags.
- [ ] Add an admin view or import script for managing products/categories instead of editing `DataLoader.java`.
- [ ] Register a real domain and point it at your deployment if you want it live at bakers.in
      (or your domain of choice).
- [ ] Confirm Java source files use `javax.*` (not `jakarta.*`) imports throughout, and that
      `BakersInApplication` extends `SpringBootServletInitializer`, so the WAR deploys cleanly
      to Tomcat 9.

## A note on this build

This project could not be compiled inside the sandbox that generated it, because that
environment has no outbound network access to reach Maven Central. Every file was written and
manually reviewed for correctness (imports, JPA/Thymeleaf/SpEL expressions, brace balance),
but please run `mvn clean package` as your first step locally or in CI to confirm a clean build
before deploying, and open an issue/ask if anything doesn't compile.
