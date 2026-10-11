# Achievements App — plano do v0 e incrementos (revisão 4)

Referência visual: design "Achievements App — Android UX v1" (https://claude.ai/artifact/QtF4UMs6MRNT8vLR6rvibB). Resumo: `design/resumo-design.md`.

**Revisão 4:** práticas de engenharia no v0 (controle de versão por registro, Dockerfile, ADRs, análise estática, cobertura, Dependabot, repositório público) e roteiro pós-v0 com foco em Java.

**Revisão 3:** campo de descrição opcional na conquista.

**Mudanças da revisão 2:** o backend Java/Spring entra já no v0. O v0 passa a incluir editar e excluir (o antigo v0.1). O v0 é limitado em funcionalidade, não em tecnologia (Hilt, Retrofit, Flyway etc. entram desde o início). Há um usuário fixo e nenhum login.

## Arquitetura

```
[App Android]  --HTTP/JSON-->  [API Spring Boot]  --JPA-->  [PostgreSQL]
 Compose + Hilt                  Java 21                      Docker Compose
 Retrofit                        localhost:8080               localhost:5432
```

- **Monorepo** no GitHub com duas pastas, `backend/` e `android/`. Cada uma tem seu workflow de CI, disparado só quando a sua pasta muda. Um repositório facilita versionar o contrato da API junto com quem o usa.
- **Ambiente do v0:** tudo local. O Postgres sobe via `docker compose`, a API roda na máquina e o emulador acessa a API por `http://10.0.2.2:8080`. Num aparelho físico, usa-se o IP da máquina na rede local, configurável no `BuildConfig`.
- **Fonte da verdade:** o backend. No v0 o app funciona só online. Cache local e modo offline (Room) ficam para depois.

### Backend (decisões propostas)

- Java 21, Spring Boot 3, build com **Gradle** (Kotlin DSL), o mesmo do Android, para estudar uma ferramenta só. Maven também funciona, se preferir.
- Spring Web, Spring Data JPA, Bean Validation, **Flyway** para migrações, **PostgreSQL**.
- Erros no padrão **Problem Details** (RFC 9457) via `ProblemDetail`.
- **springdoc-openapi**: Swagger UI em `/swagger-ui.html`, que também serve de documentação do contrato.
- Testes: JUnit 5, MockMvc nos controllers e **Testcontainers** (Postgres real) nos repositórios e na integração.
- Camadas: `controller` → `service` → `repository`, com DTOs separados das entidades.

### Android (decisões propostas)

- Kotlin, Jetpack Compose (Material 3), Navigation Compose.
- **Hilt** para injeção de dependência, **Retrofit + OkHttp + kotlinx.serialization** para a API.
- MVVM: ViewModel + `StateFlow`, repositório que encapsula a API, e estados de carregando, erro e vazio em cada tela.
- Testes: JUnit + Turbine para ViewModels, MockWebServer para a camada de rede, Compose UI test para o fluxo principal.

### Usuário sem login

- O banco já tem a tabela `app_user`, e toda conquista pertence a um usuário (`user_id`). Isso prepara o terreno para a autenticação futura sem precisar migrar dados.
- No v0, uma migração do Flyway cria um único usuário padrão. A API resolve sempre esse usuário (`CurrentUserProvider`, com uma implementação fixa). Quando a autenticação chegar, só essa implementação muda.
- Os endpoints não recebem `userId`: o usuário vem do contexto, como será com autenticação.

## Funcionalidades do v0

**Dentro**
- **Timeline:** lista de conquistas, mais recentes primeiro, agrupadas por mês, com chip da categoria e data. Estados de carregando, vazio e erro (com "tentar de novo"). Puxar para atualizar.
- **Nova conquista:** título (obrigatório), categoria (uma das fixas), data (padrão hoje) e descrição (opcional, texto livre com várias linhas). Salvar envia à API e volta para a Timeline.
- **Detalhe da conquista** (simples): título, categoria, data e descrição, com ações de editar e excluir.
- **Editar:** a mesma tela do formulário, já preenchida.
- **Excluir:** com confirmação. Ao voltar para a Timeline, o item some.
- **Categorias fixas** (Career, Health, Learning, Personal, Finance), criadas no banco por migração e servidas pela API.

**Fora** (incrementos): abas e navegação inferior, CRUD de categorias, progresso e metas, follow-ups, Look back, busca e filtros, login, offline, deploy.

## Contrato da API v0

| Método | Caminho | O que faz |
|---|---|---|
| GET | `/api/v1/categories` | Lista as categorias (id, nome, cor) |
| GET | `/api/v1/achievements` | Lista as conquistas do usuário, ordenadas por data desc. |
| GET | `/api/v1/achievements/{id}` | Detalhe |
| POST | `/api/v1/achievements` | Cria `{title, description, categoryId, achievedOn}` e responde 201 + `Location` |
| PUT | `/api/v1/achievements/{id}` | Atualiza os mesmos campos + `version` (responde 409 se a conquista mudou desde a leitura) |
| DELETE | `/api/v1/achievements/{id}` | Exclui e responde 204 |

Regras: título de 1 a 120 caracteres; descrição opcional, texto puro, até 2000 caracteres (vazia vira `null`); `categoryId` precisa existir; `achievedOn` não pode ser no futuro. Erros de validação respondem 400 com os campos inválidos. Uma conquista inexistente, ou de outro usuário, responde 404.

Modelo: `app_user(id, display_name, created_at)`, `category(id, name, color)`, `achievement(id, user_id, category_id, title, description, achieved_on, version, created_at, updated_at)`.

## Histórias e tarefas do v0 (rascunho para revisão)

### H0 — Base do repositório
Como desenvolvedor, quero um monorepo organizado com CI, para evoluir back e front com segurança.
- [ ] Preservar a tentativa antiga (microsserviços Maven + Kafka) numa branch `legacy` e limpar a `main`
- [ ] Estruturar o repositório `rafa-afonso/achievements` com README, `.gitignore` e as pastas `backend/` e `android/`
- [ ] `docker-compose.yml` com Postgres para desenvolvimento
- [ ] Workflow de CI do backend (build + testes, com Testcontainers) por filtro de caminho
- [ ] Workflow de CI do Android (build + lint + testes unitários) por filtro de caminho
- [ ] README com como subir o ambiente local
- [ ] Repositório **público**, com licença (ex.: MIT)
- [ ] Dependabot (Gradle do backend, Gradle do Android e GitHub Actions)
- [ ] *Conventional commits* e template de PR
- [ ] Pasta `docs/adr/` com os primeiros ADRs: monorepo, Gradle, usuário fixo sem login, app só online
- [ ] SonarCloud ligado ao CI (gratuito para repositório público)

**Pronto quando:** um PR que mexe em cada pasta dispara só o CI correspondente, e os dois passam.

### H1 — Esqueleto do backend
Como desenvolvedor, quero uma API Spring Boot que sobe conectada ao banco.
- [ ] Gerar o projeto Spring Boot (Web, JPA, Validation, Flyway, Postgres, Actuator)
- [ ] Perfil `dev` apontando para o Postgres do Docker Compose
- [ ] springdoc-openapi com Swagger UI
- [ ] Tratamento global de erros com `ProblemDetail`
- [ ] Teste de contexto com Testcontainers
- [ ] Formatação e análise estática do Java (Spotless + Checkstyle ou PMD) no build
- [ ] Cobertura com JaCoCo publicada no CI
- [ ] Dockerfile multi-stage do backend (e serviço `api` opcional no Docker Compose)

**Pronto quando:** `./gradlew bootRun` sobe, `/actuator/health` responde UP, o Swagger abre e a imagem Docker roda.

### H2 — Usuário padrão e categorias (backend)
Como usuário, quero que o app já tenha as categorias prontas, sem precisar de login.
- [ ] Migração: tabelas `app_user` e `category`, com o usuário padrão e as 5 categorias
- [ ] `CurrentUserProvider` com implementação fixa (lendo o id do usuário padrão)
- [ ] `GET /api/v1/categories`
- [ ] Testes: repositório (Testcontainers) e controller (MockMvc)

**Pronto quando:** o endpoint devolve as 5 categorias com as cores do design.

### H3 — CRUD de conquistas (backend)
Como usuário, quero criar, ver, editar e excluir minhas conquistas pela API.
- [ ] Migração: tabela `achievement` (com `description TEXT NULL`) com as chaves estrangeiras e um índice em `(user_id, achieved_on)`
- [ ] Entidade, repositório e service com as regras de validação
- [ ] DTOs de request e response, e o controller com os 5 endpoints
- [ ] Erros: 400 de validação, 404 para inexistente ou de outro usuário
- [ ] *Optimistic locking* com `@Version`: o PUT recebe a versão lida e responde 409 se houve mudança no meio do caminho
- [ ] Testes do service (unitários) e de integração dos endpoints

**Pronto quando:** o fluxo completo funciona pelo Swagger e os testes cobrem casos de sucesso e de erro.

### H4 — Esqueleto do app Android
Como desenvolvedor, quero um app que compila, com DI e rede configuradas.
- [ ] Gerar o projeto Kotlin + Compose (Material 3), com version catalog
- [ ] Configurar Hilt (`@HiltAndroidApp`, módulos de rede)
- [ ] Retrofit + OkHttp (logging em debug) + kotlinx.serialization, com `BASE_URL` por build type (`10.0.2.2:8080` em debug)
- [ ] Liberar HTTP em texto puro só em debug (`network_security_config`)
- [ ] Navigation Compose com as rotas `timeline`, `achievement/{id}`, `new` e `edit/{id}`
- [ ] Spotless (ktlint) e Detekt no build
- [ ] Cobertura com Kover publicada no CI

**Pronto quando:** o app abre, navega entre telas vazias e o CI passa.

### H5 — Tema visual
Como usuário, quero que o app tenha a cara do design.
- [ ] Cores (fundo, superfície, texto, destaque) e fontes (Space Grotesk + DM Sans) no tema
- [ ] Componentes: `CategoryChip`, cartão de conquista, estados vazio e erro

**Pronto quando:** os previews do Compose mostram os componentes como no design.

### H6 — Ver minhas conquistas (Timeline)
Como usuário, quero ver o que já conquistei, das mais recentes para as mais antigas.
- [ ] `AchievementApi` + `AchievementRepository` (mapeia DTO → modelo de domínio e trata erros)
- [ ] `TimelineViewModel` com os estados carregando, vazio, erro e lista agrupada por mês
- [ ] Tela Timeline com puxar para atualizar e botão flutuante "Nova"
- [ ] Testes: agrupamento por mês, ViewModel (Turbine) e repositório (MockWebServer)

**Pronto quando:** com a API rodando, a lista aparece. Com a API parada, aparece o erro com "tentar de novo".

### H7 — Criar e editar conquista
Como usuário, quero cadastrar algo que realizei e corrigir depois, se precisar.
- [ ] `AchievementFormViewModel` com modo criar e modo editar (carrega pelo id)
- [ ] Tela de formulário: título, descrição (campo de várias linhas, opcional), chips de categoria (vindas da API) e seletor de data. Salvar fica desabilitado enquanto o formulário é inválido
- [ ] Mostrar erros de validação vindos da API
- [ ] Tratar o 409 da edição: avisar que a conquista mudou e recarregar
- [ ] Testes do ViewModel (validação, sucesso e erro)

**Pronto quando:** criar e editar refletem na Timeline e persistem no banco.

### H8 — Detalhe e exclusão
Como usuário, quero ver uma conquista e poder excluí-la.
- [ ] Tela de detalhe mostrando a descrição (quando houver), com as ações editar e excluir
- [ ] Diálogo de confirmação de exclusão; ao confirmar, volta para a Timeline atualizada
- [ ] Testes do ViewModel

**Pronto quando:** excluir remove a conquista do banco e da lista.

### H9 — Fechar o v0
- [ ] Teste de UI de ponta a ponta (criar, editar e excluir) contra a API falsa (MockWebServer)
- [ ] README com o passo a passo (subir o banco e a API, rodar o app) e prints das telas
- [ ] Tag `v0.1.0`

## Ordem sugerida

H0 → (H1 → H2 → H3) em paralelo com (H4 → H5) → H6 → H7 → H8 → H9.
O contrato da API (tabela acima) é combinado antes. Assim o app pode começar com MockWebServer enquanto o backend fica pronto.

## Incrementos depois do v0

Ordem com foco em Java e visão full-stack (detalhes e motivação em `planning/brainstorm-aprendizado.md`):

1. **v0.2 Login:** Keycloak + Spring Security (OAuth2/OIDC, JWT), trocando a implementação do `CurrentUserProvider`.
2. **v0.3 Categorias, paginação e busca:** aba Categories, criar categoria, filtros, paginação por cursor, busca full-text, Paging 3.
3. **v0.4 Deploy:** imagem Docker, CD pelo GitHub Actions, nuvem gratuita, segredos e HTTPS.
4. **v0.5 Progresso e metas:** alvo e unidade, "Log progress", eventos de domínio, arquitetura hexagonal + ArchUnit.
5. **v0.6 Look back e lembretes:** agregações SQL, cache com Redis, jobs agendados, mensageria (Kafka ou RabbitMQ) com outbox, push.
6. **v0.7 Observabilidade:** Micrometer, Prometheus, Grafana, logs estruturados, OpenTelemetry.
7. **v0.8 Cliente web:** React ou Angular sobre a mesma API.
8. **v0.9 Fotos:** armazenamento compatível com S3 e URLs pré-assinadas.
9. **v1.0 Offline no Android:** Room, WorkManager e sincronização.

Follow-ups (cadeia de conquistas) entram em qualquer ponto depois do v0.3.
