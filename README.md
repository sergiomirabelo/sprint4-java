# MOTIVA — Sprint 4
## Migração para Spring Boot, Spring Data JPA e API REST

## 📌 Sobre o projeto

O **MOTIVA** é um sistema desenvolvido para monitoramento de trechos rodoviários, gerenciamento de equipes de manutenção e classificação de intervenções operacionais de acordo com o nível de prioridade.

Nesta Sprint 4, o sistema evolui de uma aplicação de console com persistência utilizando JDBC puro para uma **API REST desenvolvida com Spring Boot e Spring Data JPA**, mantendo o banco de dados Oracle e a modelagem estabelecida nas sprints anteriores.

O objetivo é modernizar a arquitetura, reduzir o código necessário para acesso a dados e disponibilizar as funcionalidades do sistema por meio de requisições HTTP.

## 🎯 Objetivos

- Migrar a persistência de JDBC puro para Spring Data JPA.
- Mapear as entidades utilizando anotações JPA.
- Implementar repositories com `JpaRepository` e derived queries.
- Organizar a aplicação em camadas: Model, Repository, Service e Controller.
- Implementar operações CRUD completas para trechos rodoviários, equipes e intervenções.
- Disponibilizar o motor de prioridade por meio de uma API REST.
- Persistir e consultar relatórios históricos no Oracle.
- Aplicar boas práticas de desenvolvimento, validação e tratamento de erros.

## 🛠️ Tecnologias utilizadas

| Tecnologia | Finalidade |
|---|---|
| Java 17 | Linguagem de programação |
| Spring Boot 4.1.x | Framework da aplicação |
| Spring Web | Criação dos endpoints REST |
| Spring Data JPA | Persistência e acesso a dados |
| Hibernate | Implementação ORM utilizada pelo JPA |
| Maven | Gerenciamento de dependências e build |
| Oracle Database | Banco de dados relacional |
| Oracle JDBC Driver (`ojdbc17`) | Conexão do framework com o Oracle |
| Git e GitHub | Versionamento do código |
| cURL / Postman | Testes da API |

## 🏗️ Arquitetura do projeto

A aplicação utiliza uma arquitetura em camadas, separando as responsabilidades de cada componente.

```text
src/
└── main/
    ├── java/
    │   └── br/com/motiva/
    │       ├── MotivaApplication.java
    │       ├── model/
    │       │   ├── TrechoRodovia.java
    │       │   ├── EquipeManutencao.java
    │       │   ├── IntervencaoOperacional.java
    │       │   └── ...
    │       ├── repository/
    │       │   ├── TrechoRodoviaRepository.java
    │       │   ├── EquipeManutencaoRepository.java
    │       │   └── ...
    │       ├── service/
    │       │   ├── TrechoRodoviaService.java
    │       │   ├── EquipeManutencaoService.java
    │       │   ├── IntervencaoService.java
    │       │   └── GeradorRelatorio.java
    │       └── controller/
    │           ├── TrechoRodoviaController.java
    │           ├── EquipeManutencaoController.java
    │           ├── IntervencaoController.java
    │           └── RelatorioController.java
    └── resources/
        └── application.properties
```

*Observação: os nomes e a organização das classes devem ser ajustados à implementação efetiva do projeto.*

### Responsabilidades das camadas

**Model**

Contém as entidades que representam os dados do sistema e são mapeadas para as tabelas do Oracle por meio de anotações como `@Entity`, `@Table` e `@Id`.

**Repository**

Contém interfaces que estendem `JpaRepository`, permitindo operações de persistência e consultas sem a necessidade de implementar manualmente o SQL.

**Service**

Concentra as regras de negócio, validações e lógica de classificação de prioridade das intervenções.

**Controller**

Expõe os endpoints REST, recebe as requisições HTTP, chama a camada Service e retorna as respostas com os códigos HTTP apropriados.

**Fluxo de comunicação:**

```text
Cliente HTTP
     |
     v
Controller
     |
     v
Service
     |
     v
Repository
     |
     v
Banco Oracle
```

## 🗄️ Banco de dados

O projeto reutiliza o banco de dados Oracle da Sprint 3, preservando as tabelas, os relacionamentos e as sequences já definidos na modelagem anterior.

As entidades JPA devem ser configuradas para corresponder à estrutura real do banco, incluindo os nomes das tabelas, colunas e sequences existentes.

A configuração de conexão é realizada pelo arquivo `application.properties`, sem credenciais diretamente no código Java.

Exemplo de configuração:

```properties
spring.datasource.url=${DB_URL:jdbc:oracle:thin:@//oracle.fiap.com.br:1521/ORCL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
```

> **Importante:** configure as variáveis de ambiente `DB_USERNAME` e `DB_PASSWORD` antes de executar a aplicação. A URL e o formato de conexão devem corresponder às informações fornecidas pela FIAP. O `ddl-auto=none` evita que o Hibernate tente criar ou alterar automaticamente a estrutura do banco.

As entidades também devem utilizar `@SequenceGenerator` com os nomes reais das sequences da Sprint 3, conforme a estratégia de geração de identificadores adotada no banco.

## 🔌 Endpoints da API REST

A API disponibiliza operações de consulta, criação, atualização e remoção para as principais entidades do sistema.

### Trechos rodoviários

| Método | Endpoint | Descrição | Resposta esperada |
|---|---|---|---|
| GET | `/api/trechos` | Lista todos os trechos | 200 OK |
| GET | `/api/trechos/{id}` | Busca um trecho por ID | 200 OK / 404 Not Found |
| POST | `/api/trechos` | Cadastra um trecho | 201 Created / 400 Bad Request |
| PUT | `/api/trechos/{id}` | Atualiza um trecho | 200 OK / 404 Not Found |
| DELETE | `/api/trechos/{id}` | Remove um trecho | 204 No Content / 404 Not Found |

### Equipes de manutenção

| Método | Endpoint | Descrição | Resposta esperada |
|---|---|---|---|
| GET | `/api/equipes` | Lista as equipes | 200 OK |
| GET | `/api/equipes/{id}` | Busca uma equipe por ID | 200 OK / 404 Not Found |
| POST | `/api/equipes` | Cadastra uma equipe | 201 Created / 400 Bad Request |
| PUT | `/api/equipes/{id}` | Atualiza uma equipe | 200 OK / 404 Not Found |
| DELETE | `/api/equipes/{id}` | Remove uma equipe | 204 No Content / 404 Not Found |

### Intervenções operacionais

| Método | Endpoint | Descrição | Resposta esperada |
|---|---|---|---|
| GET | `/api/intervencoes` | Lista as intervenções | 200 OK |
| GET | `/api/intervencoes/{id}` | Busca uma intervenção por ID | 200 OK / 404 Not Found |
| POST | `/api/intervencoes` | Cadastra uma intervenção | 201 Created / 400 Bad Request |
| PUT | `/api/intervencoes/{id}` | Atualiza uma intervenção | 200 OK / 404 Not Found |
| DELETE | `/api/intervencoes/{id}` | Remove uma intervenção | 204 No Content / 404 Not Found |

### Relatórios de prioridade

| Método | Endpoint | Descrição | Resposta esperada |
|---|---|---|---|
| POST | `/api/relatorios` | Gera um relatório e persiste o histórico | 200 OK ou 201 Created |
| GET | `/api/relatorios` | Consulta o histórico de relatórios | 200 OK |
| GET | `/api/relatorios/periodo?inicio={inicio}&fim={fim}` | Consulta relatórios por período | 200 OK / 400 Bad Request |

Os endpoints de relatório devem utilizar os trechos atualmente cadastrados, executar o motor de prioridade na camada Service e persistir o resultado no histórico.

Os formatos de data, os campos de resposta e os códigos exatos dos endpoints devem corresponder à implementação final.

## 🚦 Motor de prioridade

O sistema classifica os trechos rodoviários de acordo com o nível de vegetação registrado, utilizando as regras definidas na Sprint 2.

Exemplo de classificação:

| Nível de vegetação | Classificação |
|---|---|
| 80 cm ou mais | URGENTE |
| 50 cm ou mais e abaixo de 80 cm | CRÍTICO |
| 25 cm ou mais e abaixo de 50 cm | ATENÇÃO |
| Abaixo de 25 cm | NORMAL |

Os limites acima são ilustrativos e devem respeitar os valores efetivamente definidos na Sprint 2.

A classificação é executada na camada `Service`, mantendo a lógica de negócio independente dos controllers e dos detalhes de persistência.

## 🔎 Spring Data JPA e derived queries

Os repositories utilizam interfaces que estendem `JpaRepository`, disponibilizando operações comuns de persistência sem a implementação manual de cada método.

Exemplos de consultas derivadas:

```java
List<TrechoRodovia> findByNivelVegetacaoCmGreaterThanEqual(
    Double minimo
);

List<IntervencaoOperacional> findByTipo(String tipo);
```

Os nomes dos métodos são interpretados pelo Spring Data JPA, que gera as consultas necessárias com base nas propriedades das entidades.

Os métodos devem ser declarados no repository correspondente e utilizar nomes de atributos compatíveis com as classes reais do projeto.

## ▶️ Como executar o projeto

### Pré-requisitos

- JDK 17 instalado.
- Maven instalado ou Maven Wrapper disponível.
- Acesso ao banco Oracle da FIAP.
- Credenciais válidas para conexão com o banco.
- Git instalado, caso o projeto seja clonado do GitHub.

### 1. Clonar o repositório

```bash
git clone URL_DO_REPOSITORIO
cd NOME_DO_PROJETO
```

Substitua os valores acima pela URL e pelo nome reais do repositório.

### 2. Configurar as variáveis de ambiente

No Windows PowerShell:

```powershell
$env:DB_USERNAME="SEU_USUARIO"
$env:DB_PASSWORD="SUA_SENHA"
```

No Linux:

```bash
export DB_USERNAME="SEU_USUARIO"
export DB_PASSWORD="SUA_SENHA"
```

Utilize suas credenciais locais sem adicioná-las aos arquivos versionados.

### 3. Executar a aplicação

Com Maven instalado:

```bash
mvn spring-boot:run
```

Ou, se o projeto possuir Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows, o comando equivalente é:

```powershell
.\mvnw.cmd spring-boot:run
```

Por padrão, a API Spring Boot utiliza a porta `8080`, salvo configuração diferente no projeto.

### 4. Acessar a API

```text
http://localhost:8080/api/trechos
```

O endpoint poderá ser acessado por um navegador, cURL ou Postman, desde que a aplicação esteja em execução e a conexão com o Oracle esteja configurada corretamente.

## 🧪 Exemplos de requisições cURL

Os exemplos abaixo ilustram como consumir a API. Os nomes e tipos dos atributos JSON devem ser adaptados às entidades reais do MOTIVA.

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
    "nome": "Trecho de teste",
    "nivelVegetacaoCm": 30.0
  }'
```

### Atualizar trecho

```bash
curl -i -X PUT http://localhost:8080/api/trechos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Trecho atualizado",
    "nivelVegetacaoCm": 20.0
  }'
```

### Remover trecho

```bash
curl -i -X DELETE http://localhost:8080/api/trechos/1
```

### Gerar relatório de prioridade

```bash
curl -i -X POST http://localhost:8080/api/relatorios
```

### Consultar histórico de relatórios

```bash
curl -i http://localhost:8080/api/relatorios
```

### Consultar relatórios por período

```bash
curl -i "http://localhost:8080/api/relatorios/periodo?inicio=2026-01-01&fim=2026-12-31"
```

Os exemplos de relatório pressupõem que os endpoints aceitem essas requisições sem corpo JSON e que o período seja informado no formato `yyyy-MM-dd`. Ajuste-os conforme os contratos implementados.

## ✅ Validações e tratamento de erros

A aplicação deve preservar as regras de negócio definidas nas sprints anteriores, incluindo a validação de valores de vegetação não negativos e a consistência dos dados recebidos.

Como melhoria de qualidade, podem ser utilizados:

- Bean Validation com `@Valid`, `@NotNull` e `@PositiveOrZero`.
- DTOs para separar os contratos HTTP das entidades JPA.
- `@ControllerAdvice` para centralizar o tratamento de exceções.
- Respostas HTTP consistentes para dados inválidos, recursos inexistentes e operações concluídas.

Os controllers devem encaminhar as operações para a camada Service, sem concentrar regras de negócio ou executar SQL manualmente.

## 📸 Evidências de funcionamento

A entrega deve conter o registro de pelo menos seis requisições funcionando, demonstrando operações CRUD e funcionalidades de relatório.

Sugestão de evidências:

1. Listagem de trechos com `GET`.
2. Busca de trecho por ID com `GET`.
3. Criação de trecho com `POST`.
4. Atualização de trecho com `PUT`.
5. Remoção de trecho com `DELETE`.
6. Geração de relatório com `POST`.
7. Consulta do histórico de relatórios.
8. Consulta de relatórios por período.

As capturas de tela devem mostrar a requisição realizada e a resposta obtida no Postman ou no terminal.

## 🌿 Versionamento com Git

O projeto deve utilizar commits incrementais para registrar a evolução do desenvolvimento.

Exemplos de mensagens:

```text
feat: configure Spring Boot and Oracle connection
feat: map domain entities with JPA
feat: implement repositories and derived queries
feat: implement business services
feat: expose REST CRUD endpoints
feat: persist and query priority reports
docs: document endpoints and execution
```

O `.gitignore` deve excluir arquivos gerados, configurações locais da IDE e outros artefatos que não precisam ser versionados.

Exemplo:

```gitignore
target/
.idea/
.vscode/
*.iml
.env
application-local.properties
```

**Nunca versione senhas reais, tokens ou outras credenciais.**

## 📚 Perguntas de reflexão

### 1. Por que o Repository é uma interface? Quem implementa seus métodos e quando?

O Repository é uma interface porque define os métodos de acesso a dados sem precisar implementar manualmente cada operação. O Spring Data JPA fornece uma implementação em tempo de execução, criando um objeto que executa as operações solicitadas quando a aplicação utiliza o repository.

### 2. O pattern DAO da Sprint 3 morreu na migração?

Não. O conceito de separar o acesso a dados das regras de negócio continua existindo. Na Sprint 3, essa responsabilidade era implementada manualmente com classes DAO e JDBC. Na Sprint 4, o Spring Data JPA assume grande parte desse trabalho por meio dos repositories, reduzindo o código necessário para persistência.

### 3. Por que validar `nivelVegetacao >= 0` no Service e não no Controller?

Porque essa é uma regra de negócio que deve ser respeitada independentemente de como o sistema é acessado. O Service centraliza a validação e evita que diferentes controllers ou outras partes da aplicação implementem a mesma regra de maneiras inconsistentes. A Bean Validation também pode ser utilizada para validar os dados de entrada da API.

### 4. Onde está o SQL do `findByTipo()` e quem o gerou?

O SQL não precisa ser escrito manualmente no método. O Spring Data JPA interpreta o nome `findByTipo()`, identifica a propriedade `tipo` da entidade e gera a consulta necessária em tempo de execução, utilizando o mecanismo de persistência JPA e o Hibernate.

## 🏁 Conclusão

A Sprint 4 representa a evolução do MOTIVA para uma arquitetura baseada em APIs REST e persistência com ORM. A utilização do Spring Boot e do Spring Data JPA reduz a complexidade do acesso ao banco, melhora a separação de responsabilidades e facilita a integração do sistema com outros clientes.

A migração preserva o domínio e as regras de negócio desenvolvidos nas sprints anteriores, modernizando a forma de disponibilizar e persistir as funcionalidades do sistema.

---

**Projeto acadêmico — FIAP Challenge | MOTIVA | Sprint 4**

Desenvolvido com Java 17, Spring Boot, Spring Data JPA, Maven e Oracle Database.
