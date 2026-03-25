# Ecommerce API

Backend RESTful para e-commerce desenvolvido em Java com Spring Boot.  
O projeto implementa autenticacao com JWT em cookie HTTP-only, autorizacao por perfil e modulos de catalogo, carrinho, enderecos, pedidos e analytics.

## Status

Em desenvolvimento.

## Stack Tecnologica

- Java 21
- Spring Boot 4.0.2
- Spring Security
- JWT (`jjwt`)
- Spring Data JPA
- PostgreSQL
- Stripe Java SDK
- Springdoc OpenAPI (Swagger UI)
- Docker Compose (PostgreSQL + pgAdmin)

## Funcionalidades

- Autenticacao (`signup`, `signin`, `signout`) com sessao stateless via JWT em cookie.
- Controle de acesso por perfil (`USER`, `SELLER`, `ADMIN`).
- CRUD de categorias.
- CRUD de produtos, busca por categoria/palavra-chave e upload de imagem.
- Gestao de carrinho por usuario autenticado.
- CRUD de enderecos.
- Criacao e acompanhamento de pedidos.
- Endpoint de analytics para painel administrativo.
- Integracao com Stripe para geracao de `client secret`.

## Arquitetura

```text
src/main/java/com/app/ecommerce
|-- config
|-- controller
|-- exceptions
|-- model
|-- payload
|-- repository
|-- security
|-- service
`-- util
```

Padrao em camadas: `controller -> service -> repository`, com DTOs em `payload` e tratamento global de excecoes.

## Pre-requisitos

- JDK 21
- Docker + Docker Compose (opcional, mas recomendado para banco local)

## Configuracao de Ambiente

Crie/atualize o arquivo `.env` na raiz do projeto com as variaveis abaixo:

```env
# Docker / PostgreSQL
POSTGRES_DB=ecommerce-db
POSTGRES_USER=ecom_user
POSTGRES_PASSWORD=change_me
PGADMIN_DEFAULT_EMAIL=pgadmin4@pgadmin.org
PGADMIN_DEFAULT_PASSWORD=admin

# Aplicacao / Banco
DB_ECOM_URL=jdbc:postgresql://localhost:5434/ecommerce-db
DB_ECOM_USERNAME=ecom_user
DB_ECOM_PASSWORD=change_me

# JPA
JPA_ECOM_DDL_AUTO=update
JPA_ECOM_SHOW_SQL=false

# JWT
JWT_ECOM_SECRET=base64_secret_here
JWT_ECOM_EXPIRATION=43200000
JWT_ECOM_COOKIE_NAME=ecommerceJava

# CORS
FRONTEND_ECOM_URL=http://localhost:5173

# Stripe
STRIPE_SECRET_KEY=sk_test_your_key
```

## Como Executar

### 1. Subir infraestrutura (PostgreSQL + pgAdmin)

```bash
docker compose up -d
```

Servicos:

- PostgreSQL: `localhost:5434`
- pgAdmin: `http://localhost:5050`

### 2. Executar a API

Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

API disponivel em `http://localhost:8080`.

## Documentacao da API

Com a aplicacao em execucao:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Seguranca e Permissoes

Regras principais de acesso:

- Publico: `/api/public/**`, `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/images/**`
- Apenas `ADMIN`: `/api/admin/**`
- `SELLER` ou `ADMIN`: `/api/seller/**`
- Demais rotas: usuario autenticado

## Usuarios Seed

Na inicializacao, o projeto cria/atualiza usuarios para testes:

| Perfil | Usuario | Senha |
|---|---|---|
| USER | `user1` | `password1` |
| SELLER | `seller1` | `password2` |
| ADMIN | `admin` | `adminPass` |

## Endpoints Principais

### Autenticacao

- `POST /api/auth/signup`
- `POST /api/auth/signin`
- `POST /api/auth/signout`
- `GET /api/auth/user`
- `GET /api/auth/sellers`

### Categorias

- `GET /api/public/categories`
- `POST /api/admin/categories`
- `PUT /api/admin/categories/{categoryId}`
- `DELETE /api/admin/categories/{categoryId}`

### Produtos

- `GET /api/public/products`
- `GET /api/public/categories/{categoryId}/products`
- `GET /api/public/products/keyword/{keyword}`
- `POST /api/seller/categories/{categoryId}/product`
- `PUT /api/seller/products/{productId}`
- `DELETE /api/seller/products/{productId}`
- `PUT /api/seller/products/{productId}/image`
- `GET /api/admin/products`
- `GET /api/seller/products`

### Carrinho

- `POST /api/carts/products/{productId}/quantity/{quantity}`
- `GET /api/carts/users/cart`
- `GET /api/carts`
- `PUT /api/cart/products/{productId}/quantity/{operation}`
- `DELETE /api/cart/product/{productId}`

### Enderecos

- `POST /api/addresses`
- `GET /api/addresses`
- `GET /api/addresses/{addressId}`
- `GET /api/addresses/users/address`
- `PUT /api/addresses/{addressId}`
- `DELETE /api/addresses/{addressId}`

### Pedidos e Pagamento

- `POST /api/order/user/payments/{paymentMethod}`
- `POST /api/order/stripe-client-secret`
- `GET /api/admin/orders`
- `GET /api/seller/orders`
- `GET /api/user/orders`
- `PUT /api/seller/orders/{orderId}/status`

### Analytics

- `GET /api/admin/app/analytics`

## Paginacao Padrao

Valores padrao (`AppConstants`):

- `pageNumber=0`
- `pageSize=12`
- `sortOrder=asc`

## Testes

Windows:

```bash
mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```
