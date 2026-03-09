# 🚀 Projeto Base — Spring Boot Starter Template

Projeto base reutilizável para iniciar rapidamente novas aplicações **Spring Boot**. Contém toda a infraestrutura, abstrações e configurações que são comuns a qualquer projeto, permitindo focar diretamente nas regras de negócio.

> **Autor:** Willians Silva Santos (`br.com.wss`)

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Stack Tecnológica](#-stack-tecnológica)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Camada Base (Abstrações)](#-camada-base-abstrações)
- [Segurança](#-segurança)
- [Módulo de Exemplo — Account](#-módulo-de-exemplo--account)
- [Configuração](#-configuração)
- [Como Usar](#-como-usar)
- [Docker](#-docker)
- [Swagger / OpenAPI](#-swagger--openapi)

---

## 🎯 Visão Geral

Este repositório serve como **template/boilerplate** para novos projetos Spring Boot. Em vez de configurar segurança, banco de dados, migrations, tratamento de exceções e camadas base do zero a cada novo projeto, basta clonar este repositório e começar a implementar as regras de negócio.

### O que já vem pronto:

- ✅ Abstrações genéricas (Entity, DTO, Repository, Business/Service, Converter)
- ✅ CRUD completo com soft delete e campos de auditoria
- ✅ Autenticação e autorização com JWT + OAuth2 Resource Server
- ✅ Tratamento global de exceções (`ExceptionsHandler`)
- ✅ Configuração do Swagger/OpenAPI para documentação automática
- ✅ Flyway para versionamento de migrations do banco de dados
- ✅ MapStruct para conversão entre entidades e DTOs
- ✅ Lombok para redução de boilerplate
- ✅ JaCoCo para relatórios de cobertura de testes
- ✅ Docker Compose com PostgreSQL 16
- ✅ Profile de teste com H2 em memória
- ✅ Validações customizadas (ex: CPF/CNPJ)
- ✅ Contexto de usuário autenticado (`UserContext`)
- ✅ Módulo Account como implementação de referência

---

## 🛠 Stack Tecnológica

| Tecnologia | Versão | Função |
|---|---|---|
| **Java** | 21 | Linguagem |
| **Spring Boot** | 4.0.0 | Framework principal |
| **Spring Security** | — | Autenticação e autorização |
| **Spring Data JPA** | — | Persistência de dados |
| **OAuth2 Resource Server** | — | Validação de tokens JWT |
| **PostgreSQL** | 16 | Banco de dados (produção) |
| **H2** | — | Banco de dados (testes) |
| **Flyway** | — | Migrations do banco de dados |
| **MapStruct** | 1.6.3 | Mapeamento Entity ↔ DTO |
| **Lombok** | — | Redução de boilerplate |
| **Springdoc OpenAPI** | 2.8.14 | Swagger UI / Documentação |
| **JaCoCo** | 0.8.14 | Cobertura de testes |
| **Docker** | — | Containerização |
| **Maven** | — | Build e gerenciamento de dependências |

---

## 📁 Estrutura do Projeto

```
src/main/java/br/com/wss/
├── base/                          # 🏗 Camada de abstrações reutilizáveis
│   ├── BaseEntity.java            #   Entidade base (UUID, audit, soft delete)
│   ├── BaseDTO.java               #   DTO base
│   ├── BaseRepository.java        #   Repository genérico
│   ├── BaseBusiness.java          #   Interface de serviço (contrato)
│   ├── AbstractBusinessImpl.java  #   Implementação abstrata do CRUD
│   ├── BaseConverter.java         #   Conversão Entity ↔ DTO (MapStruct)
│   ├── PageImpl.java              #   Wrapper de paginação
│   └── TransactionType.java       #   Enum (INSERT, UPDATE, DELETE)
│
├── config/                        # ⚙️ Configurações
│   ├── WebSecurityConfig.java     #   Regras de segurança HTTP
│   ├── SecurityBeansConfig.java   #   Beans de segurança (encoders, etc.)
│   └── SwaggerConfig.java         #   Configuração do Swagger/OpenAPI
│
├── exception/                     # ❌ Tratamento de exceções
│   ├── BusinessException.java     #   Exceção de negócio customizada
│   ├── BusinessExceptionDto.java  #   DTO de resposta de erro
│   ├── ExceptionsHandler.java     #   Handler global (@ControllerAdvice)
│   └── GenericMessages.java       #   Mensagens genéricas
│
├── filters/                       # 🔐 Contexto do usuário autenticado
│   ├── JwtToken.java              #   Utilitário de token JWT
│   ├── UserContext.java           #   Componente de contexto do usuário
│   └── UserContextDetails.java   #   Detalhes do usuário logado
│
├── scheduleds/                    # ⏰ Tarefas agendadas
│   └── ScheduledService.java
│
└── projeto/                       # 📦 Módulo de exemplo (Account)
    ├── ProjetoApplication.java    #   Classe main
    ├── business/                  #   Interfaces de serviço
    ├── business/impl/             #   Implementações de serviço
    ├── converters/                #   MapStruct converters
    ├── dtos/                      #   DTOs de request/response
    ├── entities/                  #   Entidades JPA
    ├── enums/                     #   Enumerações
    ├── repositories/              #   Repositórios Spring Data
    ├── resources/                 #   Controllers REST
    └── validation/                #   Validações customizadas
```

---

## 🏗 Camada Base (Abstrações)

### `BaseEntity`

Todas as entidades herdam de `BaseEntity`, que fornece automaticamente:

| Campo | Tipo | Descrição |
|---|---|---|
| `uid` | `String` (UUID) | Identificador único gerado automaticamente |
| `deleted` | `Boolean` | Flag de soft delete |
| `createdAt` | `LocalDateTime` | Data de criação |
| `createdByUid` | `String` | UID de quem criou |
| `createdByName` | `String` | Nome de quem criou |
| `updatedAt` | `LocalDateTime` | Data da última atualização |
| `updatedByUid` | `String` | UID de quem atualizou |
| `updatedByName` | `String` | Nome de quem atualizou |
| `deletedAt` | `LocalDateTime` | Data da exclusão lógica |
| `deletedByUid` | `String` | UID de quem excluiu |
| `deletedByName` | `String` | Nome de quem excluiu |

### `AbstractBusinessImpl`

Implementação abstrata que fornece CRUD completo com:

- **`insert()`** — Criação com preenchimento automático dos campos de auditoria
- **`update()`** — Atualização preservando dados de criação
- **`delete()`** — Soft delete (marca como `deleted = true`)
- **`merge()`** — Decide automaticamente entre insert, update ou delete
- **`findByUid()`** — Busca por UID filtrando registros excluídos
- **Hooks extensíveis:** `validate()`, `setDependencies()`, `afterOperation()`

---

## 🔐 Segurança

- **JWT + OAuth2 Resource Server** — Autenticação stateless via tokens JWT
- **Roles e Authorities** — Controle de acesso por `SCOPE_ROLE_ADM`, etc.
- **`UserContext`** — Componente que extrai os dados do usuário autenticado a partir do token JWT
- **Swagger liberado** — Endpoints de documentação acessíveis sem autenticação

---

## 📦 Módulo de Exemplo — Account

O módulo `Account` serve como **implementação de referência** de como criar novos módulos seguindo a arquitetura do projeto:

1. **Entity** (`Account.java`) — Herda de `BaseEntity`
2. **DTO** (`AccountDTO.java`) — Herda de `BaseDTO`
3. **Repository** (`AccountRepository.java`) — Estende `BaseRepository`
4. **Business Interface** (`AccountBusiness.java`) — Define o contrato
5. **Business Impl** (`AccountBusinessImpl.java`) — Implementa a lógica
6. **Converter** (`AccountConverter.java`) — MapStruct para conversão
7. **Resource** (`AccountResource.java`) — Endpoints REST
8. **Validation** (`TaxNumberValidator.java`) — Validação customizada de CPF/CNPJ

Também inclui autenticação com `AuthenticationResource` e `AuthenticationBusiness`.

---

## ⚙️ Configuração

### Variáveis de Ambiente (`.env`)

```env
DB_URL=jdbc:postgresql://localhost:5432/barbersync_db
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
PASS_JWT=sua_chave_secreta_jwt
```

### Profiles

| Profile | Banco | Uso |
|---|---|---|
| `default` | PostgreSQL | Produção / Desenvolvimento |
| `test` | H2 (memória) | Testes automatizados |

---

## 🚀 Como Usar

### 1. Clone o repositório

```bash
git clone git@github.com:Willians-S-S/projeto-base-spring-boot.git
cd projeto-base-spring-boot
```

### 2. Configure as variáveis de ambiente

Copie o `.env.example` (ou crie um `.env`) com as variáveis necessárias.

### 3. Suba o banco de dados

```bash
docker compose up -d
```

### 4. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8085/api/v1`

### 5. Para criar um novo módulo

Siga o padrão do módulo `Account`:

1. Crie a **Entity** estendendo `BaseEntity`
2. Crie o **DTO** estendendo `BaseDTO`
3. Crie o **Repository** estendendo `BaseRepository`
4. Crie a **Business Interface** estendendo `BaseBusiness`
5. Crie a **Business Impl** estendendo `AbstractBusinessImpl`
6. Crie o **Converter** com MapStruct
7. Crie o **Resource** (Controller REST)
8. Registre as rotas em `WebSecurityConfig`

---

## 🐳 Docker

```bash
# Subir PostgreSQL
docker compose up -d

# Subir ambiente de testes
docker compose -f docker-compose-test.yaml up -d
```

---

## 📖 Swagger / OpenAPI

Com a aplicação rodando, acesse:

- **Swagger UI:** [http://localhost:8085/api/v1/swagger-ui.html](http://localhost:8085/api/v1/swagger-ui.html)
- **API Docs (JSON):** [http://localhost:8085/api/v1/v3/api-docs](http://localhost:8085/api/v1/v3/api-docs)

---

## 📄 Licença

Projeto pessoal de uso livre para fins de estudo e desenvolvimento.
