## 🛒 Ecommerce API

🚧 **Status do Projeto: Em desenvolvimento**

API RESTful de e-commerce desenvolvida com **Spring Boot**, implementando autenticação **JWT via cookie HTTP-only**, controle de acesso por perfil e arquitetura em camadas.

O projeto simula um backend completo de loja virtual, incluindo gerenciamento de produtos, categorias, carrinho, endereços e pedidos.

---

## 🚀 Stack Tecnológica

- Java 21
- Spring Boot 4
- Spring Security + JWT (jjwt)
- Spring Data JPA
- PostgreSQL
- Springdoc OpenAPI (Swagger UI)
- Docker Compose (PostgreSQL + pgAdmin)

---

## 🧩 Arquitetura

O projeto segue uma arquitetura em camadas, com separação clara de responsabilidades:

```
src/main/java/com/app/ecommerce
├── config        # Configurações (Security, CORS, Beans)
├── controller    # Camada de entrada (REST Controllers)
├── exceptions    # Tratamento global de exceções
├── model         # Entidades JPA
├── payload       # DTOs (Request / Response)
├── repository    # Interfaces JPA
├── security      # JWT, filtros e autenticação
├── service       # Regras de negócio
└── util          # Classes utilitárias
```

---

## 🔐 Funcionalidades

### Autenticação & Segurança
- Cadastro de usuário
- Login com geração de JWT
- Logout (invalidação de cookie)
- Usuário autenticado
- Controle de acesso por perfil (USER / SELLER / ADMIN)

### Categorias
- CRUD completo
- Paginação e ordenação

### Produtos
- CRUD completo
- Busca por categoria
- Busca por palavra-chave
- Upload de imagem
- Controle de acesso por perfil

### Carrinho
- Adicionar item
- Atualizar quantidade
- Remover item
- Listar carrinho do usuário autenticado

### Endereços
- CRUD completo
- Listagem por usuário

### Pedidos
- Criação de pedido
- Escolha do método de pagamento

---

## ⚙️ Pré-requisitos

- Java 21
- Maven 3.9+
- Docker e Docker Compose (opcional, para banco de dados)

---

## 🌱 Variáveis de Ambiente

Defina no arquivo `.env` ou nas variáveis do sistema:

```env
# Banco de Dados
DB_ECOM_URL=jdbc:postgresql://localhost:5434/seu_banco
DB_ECOM_USERNAME=seu_usuario
DB_ECOM_PASSWORD=sua_senha

# JPA
JPA_ECOM_DDL_AUTO=update
JPA_ECOM_SHOW_SQL=false

# JWT
JWT_ECOM_SECRET=uma_chave_muito_forte
JWT_ECOM_EXPIRATION=86400000
JWT_ECOM_COOKIE_NAME=ecommerce

# Docker Compose
POSTGRES_DB=seu_banco
POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha
PGADMIN_DEFAULT_EMAIL=pgadmin4@pgadmin.org
PGADMIN_DEFAULT_PASSWORD=admin
```

---

## 🐳 Executando o Projeto

### 1️⃣ Subir banco com Docker

```bash
docker compose up -d
```

Serviços disponíveis:

- PostgreSQL → `localhost:5434`
- pgAdmin → `http://localhost:5050`

---

### 2️⃣ Rodar a API

**Windows**
```bash
mvnw.cmd spring-boot:run
```

**Linux/macOS**
```bash
./mvnw spring-boot:run
```

A aplicação será iniciada em:

```
http://localhost:8080
```

---

## 📚 Documentação da API

Com a aplicação em execução:

- Swagger UI  
  `http://localhost:8080/swagger-ui/index.html`

- OpenAPI JSON  
  `http://localhost:8080/v3/api-docs`

---

## 👤 Usuários Seed (Criação Automática)

Na inicialização, são criados usuários padrão para testes:

| Perfil  | Usuário  | Senha       |
|---------|----------|------------|
| USER    | user1    | password1 |
| SELLER  | seller1  | password2 |
| ADMIN   | admin    | adminPass |

---

## 🔎 Principais Endpoints

### 🔐 Autenticação — `/api/auth`

- `POST /signin`
- `POST /signup`
- `POST /signout`
- `GET /user`

---

### 📂 Categorias

- `GET /api/public/categories`
- `POST /api/public/categories`
- `PUT /api/public/categories/{categoryId}`
- `DELETE /api/admin/categories/{categoryId}`

---

### 📦 Produtos

- `GET /api/public/product`
- `GET /api/public/categories/{categoryId}/products`
- `GET /api/public/products/keyword/{keyword}`
- `POST /api/admin/categories/{categoryId}/product`
- `PUT /api/admin/products/{productId}`
- `DELETE /api/admin/products/{productId}`
- `PUT /api/products/{productId}/image`

---

### 🛒 Carrinho

- `POST /api/carts/products/{productId}/quantity/{quantity}`
- `GET /api/carts/users/cart`
- `GET /api/carts`
- `PUT /api/cart/products/{productId}/quantity/{operation}`
- `DELETE /api/cart/product/{productId}`

---

### 📍 Endereços

- `POST /api/addresses`
- `GET /api/addresses`
- `GET /api/addresses/{addressId}`
- `GET /api/addresses/users/address`
- `PUT /api/addresses/{addressId}`
- `DELETE /api/addresses/{addressId}`

---

### 🧾 Pedidos

- `POST /api/order/user/payments/{paymentMethod}`

---

## 🧪 Testes

```bash
mvnw.cmd test
```

---

## 📌 Observações Técnicas

- Autenticação **stateless** com JWT armazenado em cookie HTTP-only.
- Rotas protegidas exigem autenticação, exceto:
    - `/api/auth/**`
    - Swagger
    - Endpoints públicos
- Paginação padrão:
    - `pageNumber=0`
    - `pageSize=50`
- Tratamento global de exceções com `@ControllerAdvice`.
