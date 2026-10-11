# Brainstorm: potencial de aprendizado do Achievements App

Avaliação do plano atual (`planning/v0-plano.md`, revisão 3) do ponto de vista do mercado de trabalho, e ideias de mudança no app para estudar conceitos importantes. É material para discussão: nada aqui altera o plano.

## 1. O que o plano atual já ensina

Visão geral: o v0 é um bom "feijão com arroz" full-stack, e cobre o que aparece em quase toda vaga júnior/pleno de backend Java ou Android.

| Área | O que você pratica | Relevância no mercado |
|---|---|---|
| API REST | Desenho de recursos, verbos e status, validação, Problem Details, versionamento `/v1` | Muito alta |
| Spring Boot | Camadas controller/service/repository, DTOs, perfis, injeção de dependência | Muito alta (vagas Java) |
| Banco | PostgreSQL, JPA/Hibernate, migrações com Flyway, chaves e índices | Muito alta |
| Testes backend | JUnit 5, MockMvc, Testcontainers, pirâmide de testes | Alta (diferencial em entrevistas) |
| Android moderno | Compose, MVVM, StateFlow, coroutines, Hilt, Retrofit, Navigation | Muito alta (vagas Android) |
| Testes Android | ViewModel com Turbine, MockWebServer, Compose UI test | Alta |
| DevOps básico | Docker Compose, GitHub Actions, CI por caminho | Alta |
| Processo | Histórias, PRs, revisão, versionamento por tags | Média/alta |

**Lacunas:** o plano ainda não toca em **segurança/autenticação**, **deploy em nuvem**, **observabilidade**, **mensageria/assincronismo**, **cache**, **paginação**, **offline/sincronização** nem **arquitetura em escala** (modularização). Esses temas costumam separar o pleno do júnior em entrevistas.

## 2. Temas de mercado e como encaixá-los no app

Cada tema está ligado a uma funcionalidade que o justifica. Assim o estudo não vira tecnologia pela tecnologia.

### Segurança e autenticação (prioridade alta)
- **Funcionalidade:** login de verdade e dados por usuário.
- **Conceitos:** Spring Security, OAuth2/OpenID Connect, JWT, resource server, autorização por dono do recurso, armazenamento seguro de token no Android (DataStore + Keystore), refresh token.
- **Sugestão:** usar o **Keycloak** local (Docker) como provedor de identidade. É o que muitas empresas usam e evita escrever login "na mão". Vale **antecipar para logo depois do v0**, porque mexer em segurança mais tarde dói mais.

### Offline-first e sincronização (prioridade alta para Android)
- **Funcionalidade:** registrar conquistas sem internet.
- **Conceitos:** Room como fonte única da verdade, WorkManager, fila de operações pendentes, resolução de conflitos, **optimistic locking** (`@Version` no JPA, `ETag`/`If-Match` na API), idempotência (chave de idempotência no POST).
- É um dos temas mais valorizados em Android sênior e ensina muito sobre consistência de dados.

### Paginação e busca
- **Funcionalidade:** a Timeline com centenas de itens, e a busca (já está no design).
- **Conceitos:** paginação por cursor/keyset vs. offset, **Paging 3** no Android, busca *full-text* do Postgres sobre título e descrição, índices.
- A descrição recém-adicionada é um bom motivo para estudar full-text search.

### Upload de fotos
- **Funcionalidade:** anexar foto a uma conquista (combina bem com o app).
- **Conceitos:** armazenamento de objetos compatível com S3 (**MinIO** local, S3/R2 em produção), URLs pré-assinadas, multipart, compressão e redimensionamento no Android, carregamento de imagens com Coil.

### Assincronismo, eventos e notificações
- **Funcionalidade:** lembrete "um ano atrás hoje" e aviso ao concluir uma meta.
- **Conceitos:** eventos de domínio (Spring `ApplicationEvent`), depois um broker (**RabbitMQ** ou **Kafka**), padrão *outbox*, jobs agendados (`@Scheduled`, ShedLock), push com **Firebase Cloud Messaging**.
- Kafka/RabbitMQ aparecem em muitas vagas backend. Basta um caso de uso pequeno para aprender o essencial.

### Estatísticas e cache
- **Funcionalidade:** tela Look back (totais por mês e por categoria).
- **Conceitos:** agregações SQL (`GROUP BY`, *window functions*), *materialized views*, cache com **Redis** (`@Cacheable`) e invalidação.

### Observabilidade
- **Funcionalidade:** saber o que acontece quando algo quebra.
- **Conceitos:** Actuator, métricas com Micrometer + **Prometheus** + **Grafana**, logs estruturados (JSON) com id de correlação, *tracing* com **OpenTelemetry**. No Android: Crashlytics ou Sentry.

### Deploy e nuvem
- **Funcionalidade:** usar o app de verdade no celular, fora de casa.
- **Conceitos:** imagem Docker otimizada (multi-stage, Jib ou buildpacks), deploy contínuo pelo GitHub Actions, configuração por ambiente e segredos, Postgres gerenciado, HTTPS.
- Opções gratuitas ou quase: Render, Fly.io, Railway, Neon (Postgres) e Oracle Cloud Free Tier. Kubernetes fica como estudo opcional depois, por exemplo com k3d local.

### Arquitetura
- **Android:** modularização (`core:*`, `feature:*`), camada de domínio com casos de uso, convenções de build (*convention plugins*). É o que apps grandes fazem.
- **Backend:** arquitetura hexagonal (ports and adapters) ou "package by feature", com **ArchUnit** para garantir as regras.
- **Contrato:** OpenAPI como fonte da verdade e geração de cliente Retrofit a partir dele (*contract-first*).
- **Cuidado com excesso de engenharia:** vale introduzir cada padrão quando o app sente a dor que ele resolve, e registrar a decisão num ADR (ver abaixo).

### Qualidade e práticas de engenharia
- Cobertura (JaCoCo/Kover), análise estática (**Detekt**, **SonarCloud** gratuito para repositório público), testes de *screenshot* (Roborazzi/Paparazzi), testes de mutação (PIT).
- **ADRs** (Architecture Decision Records): um arquivo curto por decisão. É ótimo para estudo e para mostrar raciocínio em entrevistas.
- *Conventional commits*, versionamento semântico, Dependabot/Renovate, *code review* nos próprios PRs.
- Acessibilidade (TalkBack, contraste), internacionalização (PT/EN) e tema escuro.

### Portfólio
- Repositório público com README caprichado, diagrama de arquitetura, prints/GIF e ADRs vira um **projeto de portfólio** forte para currículo e LinkedIn.
- Se o foco incluir vagas web, um **cliente web** (React ou Angular) consumindo a mesma API é um incremento natural. O backend já está pronto para isso.

## 3. Sugestões concretas de mudança no plano

**Pequenas adições ao v0** (baixo custo, alto valor):
1. `@Version` na conquista (optimistic locking) desde o início.
2. Dockerfile do backend (mesmo sem deploy) e Actuator com health check.
3. ADRs para as decisões já tomadas (monorepo, Gradle, usuário fixo).
4. Detekt/Spotless + Checkstyle/Spotless Java + cobertura no CI.
5. Dependabot e *conventional commits*.
6. Repositório **público** (habilita SonarCloud gratuito e serve de portfólio).

**Roteiro revisado depois do v0**, ajustado ao foco em Java com visão full-stack (respostas do Rafael em 05/10: repositório público, cliente web num segundo momento):

| Versão | Funcionalidade | Conceitos principais | Lado |
|---|---|---|---|
| v0.2 | Login (Keycloak) | Spring Security, OAuth2/OIDC, JWT, autorização por dono | Back + app |
| v0.3 | Categorias + Timeline paginada + busca | Relacionamentos JPA, paginação por cursor, full-text no Postgres, Paging 3 | Back + app |
| v0.4 | Deploy | Imagem Docker, CD no GitHub Actions, nuvem gratuita, segredos, HTTPS | Back |
| v0.5 | Progresso e metas | Regras de domínio, eventos de domínio, arquitetura hexagonal + ArchUnit | Back + app |
| v0.6 | Look back + lembretes | Agregações SQL, cache com Redis, jobs agendados, mensageria (Kafka ou RabbitMQ), padrão outbox, push | Back + app |
| v0.7 | Observabilidade | Micrometer, Prometheus, Grafana, logs estruturados, OpenTelemetry | Back |
| v0.8 | Cliente web | React ou Angular consumindo a mesma API, login via OIDC, CORS | Web |
| v0.9 | Fotos | Armazenamento compatível com S3, URLs pré-assinadas | Back + app + web |
| v1.0 | Offline no Android | Room, WorkManager, sincronização, conflitos, idempotência | App |
| Contínuo | Qualidade e arquitetura | SonarCloud (público), cobertura, ADRs, Dependabot, modularização | Todos |

O que mudou em relação à primeira versão do roteiro: os temas de backend Java (deploy, eventos, mensageria, cache, observabilidade) vieram para antes. O offline, que é mais específico de Android, foi para o fim. O cliente web entra depois do deploy e do login, quando a API já está pública e protegida, que é o cenário real de um front web.

Follow-ups (cadeia de conquistas) podem entrar em qualquer ponto depois do v0.3. É um bom exercício de modelagem em SQL (consultas recursivas com `WITH RECURSIVE`).

## 4. Decisões já tomadas

- Foco: **Java**, com visão **full-stack**.
- Repositório **público** (portfólio e SonarCloud gratuito).
- **Cliente web** num segundo momento (v0.8 no roteiro acima).
