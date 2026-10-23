# MOTIVA — Sprint 4
## API REST com Spring Boot e Spring Data JPA

**Projeto acadêmico — FIAP Challenge**

### Integrantes

- Erick Gimenez — RM564748
- Henrique Boscoli — RM563651
- João Henrique — RM563578
- Sergio Mirabelo — RM562161
- Tomazzo Canterucci — RM565566

## Sobre o projeto

O MOTIVA é um sistema de monitoramento de vegetação em trechos rodoviários, gerenciamento de equipes de manutenção e controle de intervenções operacionais.

Na Sprint 4, o sistema evolui de uma aplicação de console com persistência JDBC para uma API REST desenvolvida com Spring Boot e Spring Data JPA, mantendo o banco de dados Oracle utilizado na Sprint 3.

## Tecnologias utilizadas

- Java 17
- Spring Boot 4.1.2
- Spring Web
- Spring Data JPA
- Hibernate
- Maven
- Oracle Database
- Oracle JDBC Driver (`ojdbc17`)
- Git e GitHub
- cURL e Postman

## Arquitetura

A aplicação utiliza arquitetura em camadas:

```text
src/main/java/br/com/motiva/
├── MotivaApplication.java
├── model/
├── repository/
├── service/
└── controller/
```

- **Model:** entidades JPA que representam os dados do sistema.
- **Repository:** interfaces `JpaRepository` responsáveis pela persistência e pelas consultas.
- **Service:** regras de negócio, validações e classificação de prioridades.
- **Controller:** endpoints REST e respostas HTTP.

Fluxo da aplicação:

```text
Cliente HTTP → Controller → Service → Repository → Oracle
```

## Banco de dados

O projeto utiliza o banco Oracle da FIAP, reaproveitando a modelagem e as tabelas definidas na Sprint 3.

A conexão é configurada no arquivo `src/main/resources/application.properties`, sem credenciais diretamente no código Java.

Exemplo de configuração:

```properties
spring.datasource.url=${MOTIVA_DB_URL:jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL}
spring.datasource.username=${MOTIVA_DB_USER}
spring.datasource.password=${MOTIVA_DB_PASSWORD}
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
```

## Como executar

### Pré-requisitos

- JDK 17
- Maven
- Acesso ao banco Oracle da FIAP

### 1. Clonar o repositório

```bash
git clone URL_DO_REPOSITORIO
cd NOME_DO_PROJETO
```

### 2. Configurar as variáveis de ambiente

Windows PowerShell:

```powershell
$env:MOTIVA_DB_USER="seu_usuario"
$env:MOTIVA_DB_PASSWORD="sua_senha"
```

Linux/macOS:

```bash
export MOTIVA_DB_USER="seu_usuario"
export MOTIVA_DB_PASSWORD="sua_senha"
```

### 3. Executar a aplicação

```bash
mvn spring-boot:run
```

Ou, utilizando o Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API estará disponível em:

`http://localhost:8080`

## Endpoints da API

### Trechos rodoviários

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/trechos` | Lista todos os trechos |
| GET | `/api/trechos/{id}` | Busca trecho por ID |
| POST | `/api/trechos` | Cadastra um trecho |
| PUT | `/api/trechos/{id}` | Atualiza um trecho |
| DELETE | `/api/trechos/{id}` | Remove um trecho |
| GET | `/api/trechos/densidade/{densidade}` | Filtra por densidade da vegetação |
| GET | `/api/trechos/altura-minima/{cm}` | Busca trechos por altura mínima da grama |

### Equipes de manutenção

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/equipes` | Lista as equipes |
| GET | `/api/equipes/{id}` | Busca equipe por ID |
| POST | `/api/equipes` | Cadastra uma equipe |
| PUT | `/api/equipes/{id}` | Atualiza uma equipe |
| DELETE | `/api/equipes/{id}` | Remove uma equipe |
| GET | `/api/equipes/status/{status}` | Filtra equipes por status |

### Intervenções operacionais

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/intervencoes` | Lista as intervenções |
| GET | `/api/intervencoes/{id}` | Busca intervenção por ID |
| POST | `/api/intervencoes` | Cadastra uma intervenção |
| PUT | `/api/intervencoes/{id}` | Atualiza uma intervenção |
| DELETE | `/api/intervencoes/{id}` | Remove uma intervenção |
| GET | `/api/intervencoes/status/{status}` | Filtra intervenções por status |

### Relatórios de prioridade

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/relatorios` | Gera e persiste um relatório de prioridade |
| GET | `/api/relatorios` | Lista o histórico de relatórios |
| GET | `/api/relatorios/periodo?inicio={inicio}&fim={fim}` | Consulta relatórios por período |

Os parâmetros de data e hora devem utilizar o formato ISO `yyyy-MM-dd'T'HH:mm:ss`.

## Códigos HTTP

| Código | Significado |
|---|---|
| 200 | Requisição executada com sucesso |
| 201 | Recurso criado |
| 204 | Recurso removido com sucesso |
| 400 | Dados inválidos ou erro de validação |
| 404 | Recurso não encontrado |
| 409 | Conflito de integridade ou duplicidade |

## Regras de negócio

### Trechos rodoviários

- `codigo`, `rodovia` e `municipio` são obrigatórios.
- `kmFim` deve ser maior que `kmInicio`.
- `alturaGramaCm` deve ser maior ou igual a zero.
- `densidadeVegetacao` deve ser `BAIXA`, `MEDIA`, `ALTA` ou `CRITICA`.
- `diasSemCorte` deve ser maior ou igual a zero.
- `ultimaVistoria` é obrigatória.

### Equipes de manutenção

- `nome` e `especialidade` são obrigatórios.
- `quantidadeMembros` deve ser maior que zero.
- `status` deve ser `ATIVA` ou `INATIVA`.

### Intervenções operacionais

- A descrição é obrigatória.
- Os tipos aceitos são `ROCAGEM_MECANICA`, `ROCAGEM_MANUAL`, `CAPINA_MANUAL` e `PODA_VEGETACAO`.
- Os status aceitos são `PLANEJADA`, `EM_ANDAMENTO`, `CONCLUIDA` e `CANCELADA`.
- `dataIntervencao` é obrigatória.
- `custo` deve ser maior ou igual a zero.
- `trechoId` e `equipeId` devem corresponder a registros existentes.

### Classificação de prioridade

O motor de prioridade, implementado na camada Service, classifica os trechos de acordo com os critérios definidos no projeto.

| Prioridade | Critérios |
|---|---|
| URGENTE | Altura da grama ≥ 30 cm, densidade `CRITICA` ou pelo menos 30 dias sem corte |
| CRÍTICO | Altura ≥ 25 cm, densidade `ALTA` ou pelo menos 20 dias sem corte, se não for urgente |
| ATENÇÃO | Altura ≥ 18 cm, densidade `MEDIA` ou pelo menos 10 dias sem corte, se não for urgente ou crítico |
| NORMAL | Nenhuma das condições anteriores |

Os relatórios registram os resultados da classificação e os totais por prioridade, além de identificar trechos com altura da grama acima de 30 cm.

## Exemplos de requisições cURL

### Listar trechos

```bash
curl -i http://localhost:8080/api/trechos
```

### Buscar trecho por ID

```bash
curl -i http://localhost:8080/api/trechos/1
```

### Criar trecho

```bash
curl -i -X POST http://localhost:8080/api/trechos \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "TR001",
    "rodovia": "SP-001",
    "municipio": "Sao Paulo",
    "kmInicio": 10.0,
    "kmFim": 15.0,
    "alturaGramaCm": 20.0,
    "densidadeVegetacao": "MEDIA",
    "diasSemCorte": 10,
    "ultimaVistoria": "2026-10-20"
  }'
```

### Atualizar trecho

```bash
curl -i -X PUT http://localhost:8080/api/trechos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "TR001",
    "rodovia": "SP-001",
    "municipio": "Sao Paulo",
    "kmInicio": 10.0,
    "kmFim": 15.0,
    "alturaGramaCm": 15.0,
    "densidadeVegetacao": "BAIXA",
    "diasSemCorte": 5,
    "ultimaVistoria": "2026-10-23"
  }'
```

### Remover trecho

```bash
curl -i -X DELETE http://localhost:8080/api/trechos/1
```

### Consultar trechos por densidade

```bash
curl -i http://localhost:8080/api/trechos/densidade/ALTA
```

### Consultar trechos por altura mínima

```bash
curl -i http://localhost:8080/api/trechos/altura-minima/25
```

### Listar equipes ativas

```bash
curl -i http://localhost:8080/api/equipes/status/ATIVA
```

### Gerar relatório

```bash
curl -i -X POST http://localhost:8080/api/relatorios
```

### Consultar histórico

```bash
curl -i http://localhost:8080/api/relatorios
```

### Consultar relatórios por período

```bash
curl -i "http://localhost:8080/api/relatorios/periodo?inicio=2026-10-01T00:00:00&fim=2026-10-31T23:59:59"
```

## Spring Data JPA e derived queries

Os repositories estendem `JpaRepository`, disponibilizando operações de persistência sem a necessidade de implementar manualmente o SQL.

Exemplos de consultas derivadas utilizadas no projeto:

```java
findByAlturaGramaCmGreaterThanEqual(Double minimo);

findByDensidadeVegetacao(String densidadeVegetacao);

findByStatus(String status);

findByDataGeracaoBetween(
    LocalDateTime inicio,
    LocalDateTime fim
);
```

O Spring Data JPA interpreta os nomes dos métodos e gera as consultas de acordo com as propriedades das entidades.

## Perguntas de reflexão

### 1. Por que o Repository é uma interface? Quem implementa os métodos e quando?

O Repository é uma interface porque define as operações de acesso aos dados sem precisar implementar manualmente cada método. O Spring Data JPA gera uma implementação em tempo de execução, permitindo utilizar operações como `findAll()`, `findById()` e `save()`.

### 2. O pattern DAO da Sprint 3 morreu na migração?

Não. O conceito de separar o acesso aos dados das regras de negócio continua existindo. Na Sprint 3, essa responsabilidade era implementada manualmente com classes DAO e JDBC. Na Sprint 4, os repositories do Spring Data JPA assumem esse papel, reduzindo a quantidade de código necessário.

### 3. Por que validar `alturaGramaCm >= 0` no Service e não no Controller?

Porque essa é uma regra de negócio que deve ser respeitada independentemente da forma de acesso ao sistema. O Service centraliza a validação, evitando duplicação e garantindo que diferentes partes da aplicação apliquem a mesma regra.

### 4. Onde está o SQL do `findByTipo()` e quem o gerou?

O SQL não precisa ser escrito manualmente. O Spring Data JPA interpreta o nome do método, identifica a propriedade correspondente na entidade e gera a consulta em tempo de execução por meio do mecanismo de persistência JPA.

## Evidências de funcionamento

A entrega inclui registros de requisições executadas com sucesso para demonstrar as funcionalidades da API.

As evidências devem contemplar operações CRUD, consultas derivadas e geração e consulta de relatórios, utilizando o Postman ou cURL.

## Versionamento

O projeto utiliza Git e GitHub para controle de versão, com commits incrementais durante o desenvolvimento.

Exemplos de mensagens de commit:

```text
feat: configure Spring Boot and Oracle connection
feat: map domain entities with JPA
feat: implement repositories and derived queries
feat: implement business services
feat: expose REST CRUD endpoints
feat: persist and query priority reports
docs: document API endpoints and execution
```

Arquivos gerados, configurações locais e credenciais não devem ser versionados.

## Conclusão

A Sprint 4 transforma o MOTIVA em uma API REST utilizando Spring Boot e Spring Data JPA, mantendo a persistência no Oracle e as regras de negócio desenvolvidas nas sprints anteriores. A arquitetura em camadas melhora a organização do código, facilita a manutenção e permite que o sistema seja consumido por diferentes clientes HTTP.

**FIAP Challenge — MOTIVA | Sprint 4**
