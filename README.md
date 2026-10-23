MOTIVA — Sprint 4
Migração para Spring Boot, Spring Data JPA e API REST
Projeto acadêmico — FIAP Challenge
Integrantes
- Erick Gimenez — RM564748
- Henrique Boscoli — RM563651
- João Henrique — RM563578
- Sergio Mirabelo — RM562161
- Tomazzo Canterucci — RM565566
Sobre o projeto
O MOTIVA é uma aplicação para monitoramento de vegetação e gerenciamento de roçagem em trechos rodoviários. Nesta sprint, o protótipo com persistência JDBC e DAO da Sprint 3 foi migrado para uma API REST com Spring Boot e Spring Data JPA, mantendo o Oracle e a modelagem do banco desenvolvida anteriormente.

A API permite gerenciar trechos rodoviários, equipes de manutenção e intervenções operacionais, além de gerar relatórios de prioridade e consultar seu histórico.
Tecnologias
- Java 17
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA / Hibernate
- Maven
- Oracle Database e driver ojdbc17
- Git e GitHub
- cURL ou Postman para testar os endpoints
Arquitetura
A aplicação segue o fluxo Controller → Service → Repository → Oracle.
src/main/java/br/com/motiva/
├── MotivaApplication.java
├── model/
│   ├── TrechoRodovia.java
│   ├── EquipeManutencao.java
│   ├── IntervencaoOperacional.java
│   └── RelatorioPrioridade.java
├── repository/
│   ├── TrechoRodoviaRepository.java
│   ├── EquipeManutencaoRepository.java
│   ├── IntervencaoOperacionalRepository.java
│   └── RelatorioPrioridadeRepository.java
├── service/
│   ├── TrechoRodoviaService.java
│   ├── EquipeManutencaoService.java
│   ├── IntervencaoOperacionalService.java
│   └── GeradorRelatorio.java
└── controller/
    ├── TrechoRodoviaController.java
    ├── EquipeManutencaoController.java
    ├── IntervencaoOperacionalController.java
    ├── RelatorioPrioridadeController.java
    └── GlobalExceptionHandler.java
- Model: entidades JPA que representam as tabelas do banco.
- Repository: interfaces JpaRepository e consultas derivadas, sem SQL manual nos controllers ou services.
- Service: validações, regras de negócio e geração do relatório de prioridade.
- Controller: endpoints REST e códigos de resposta HTTP.
- GlobalExceptionHandler: tratamento global de erros de validação e integridade.
Banco de dados e configuração
A aplicação reutiliza as tabelas Oracle da Sprint 3. Os scripts SQL estão na pasta sql/:
- sql/seu-script-criacao.sql
- sql/seu-script-dados.sql
- sql/script-dao.sql
- sql/script-sql.sql
Execute os scripts necessários no ambiente Oracle da FIAP, de acordo com a estrutura do banco utilizada pelo grupo. A aplicação está configurada com spring.jpa.hibernate.ddl-auto=none, portanto o Hibernate não cria nem altera automaticamente as tabelas.

A configuração principal fica em src/main/resources/application.properties. As credenciais devem ser fornecidas por variáveis de ambiente, nunca incluídas no repositório.
Variáveis de ambiente
No Windows PowerShell:
$env:MOTIVA_DB_USER = "seu_usuario"
$env:MOTIVA_DB_PASSWORD = "sua_senha"
# Opcional: sobrescreve a URL padrão configurada na aplicação
$env:MOTIVA_DB_URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"
No Linux/macOS:
export MOTIVA_DB_USER="seu_usuario"
export MOTIVA_DB_PASSWORD="sua_senha"
# Opcional
export MOTIVA_DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"
Confirme com a FIAP o formato de conexão e os dados de acesso válidos para o seu ambiente.
Segurança: não versione arquivos locais com credenciais. O arquivo application-local.properties é ignorado pelo .gitignore; mantenha-o fora dos commits e não o compartilhe. Se uma senha real já foi incluída em um ZIP, commit ou repositório remoto, troque-a antes de publicar o projeto.

Como executar
Pré-requisitos
- JDK 17
- Acesso ao banco Oracle da FIAP
- Maven instalado ou Maven Wrapper disponível
Na raiz do projeto, configure as variáveis de ambiente e execute:
mvn spring-boot:run
Ou use o Maven Wrapper:
./mvnw spring-boot:run
No Windows:
.\mvnw.cmd spring-boot:run
Por padrão, a API fica disponível em http://localhost:8080.
Endpoints
Trechos rodoviários
Método	Endpoint	Descrição	Respostas principais
GET	/api/trechos	Lista todos os trechos	200 OK
GET	/api/trechos/{id}	Busca um trecho por ID	200 OK, 404 Not Found
POST	/api/trechos	Cadastra um trecho	201 Created, 400 Bad Request, 409 Conflict
PUT	/api/trechos/{id}	Atualiza um trecho	200 OK, 400 Bad Request, 404 Not Found
DELETE	/api/trechos/{id}	Remove um trecho	204 No Content, 404 Not Found, 409 Conflict
GET	/api/trechos/densidade/{densidade}	Filtra por densidade da vegetação	200 OK
GET	/api/trechos/altura-minima/{cm}	Busca trechos com altura maior ou igual ao valor informado	200 OK


Equipes de manutenção
Método	Endpoint	Descrição	Respostas principais
GET	/api/equipes	Lista as equipes	200 OK
GET	/api/equipes/{id}	Busca equipe por ID	200 OK, 404 Not Found
POST	/api/equipes	Cadastra uma equipe	201 Created, 400 Bad Request, 409 Conflict
PUT	/api/equipes/{id}	Atualiza uma equipe	200 OK, 400 Bad Request, 404 Not Found
DELETE	/api/equipes/{id}	Remove uma equipe	204 No Content, 404 Not Found, 409 Conflict
GET	/api/equipes/status/{status}	Filtra por status (ATIVA ou INATIVA)	200 OK


Intervenções operacionais
Método	Endpoint	Descrição	Respostas principais
GET	/api/intervencoes	Lista as intervenções	200 OK
GET	/api/intervencoes/{id}	Busca intervenção por ID	200 OK, 404 Not Found
POST	/api/intervencoes	Cadastra uma intervenção	201 Created, 400 Bad Request, 409 Conflict
PUT	/api/intervencoes/{id}	Atualiza uma intervenção	200 OK, 400 Bad Request, 404 Not Found
DELETE	/api/intervencoes/{id}	Remove uma intervenção	204 No Content, 404 Not Found, 409 Conflict
GET	/api/intervencoes/status/{status}	Filtra por status da intervenção	200 OK


Relatórios de prioridade
Método	Endpoint	Descrição	Respostas principais
POST	/api/relatorios	Gera relatório dos trechos atuais e persiste no histórico	201 Created
GET	/api/relatorios	Lista o histórico de relatórios	200 OK
GET	/api/relatorios/periodo?inicio={inicio}&fim={fim}	Consulta relatórios entre duas datas e horários	200 OK, 400 Bad Request


Para a consulta por período, inicio e fim devem usar o formato ISO yyyy-MM-dd'T'HH:mm:ss, por exemplo 2026-10-01T00:00:00.
Códigos de erro
- 400 Bad Request: dados inválidos ou regra de negócio violada. O corpo retornado tem o formato {"erro":"mensagem"}.
- 404 Not Found: registro não encontrado para o ID informado.
- 409 Conflict: operação que viola uma restrição de integridade do banco, como chave duplicada ou exclusão de registro ainda referenciado.
Regras de negócio
Trechos rodoviários
- codigo, rodovia e municipio são obrigatórios.
- kmFim deve ser maior que kmInicio.
- alturaGramaCm deve ser maior ou igual a zero.
- densidadeVegetacao deve ser BAIXA, MEDIA, ALTA ou CRITICA.
- diasSemCorte deve ser maior ou igual a zero.
- ultimaVistoria é obrigatória.
Equipes de manutenção
- nome e especialidade são obrigatórios.
- quantidadeMembros deve ser maior que zero.
- status deve ser ATIVA ou INATIVA.
Intervenções operacionais
- A descrição é obrigatória.
- Tipos aceitos: ROCAGEM_MECANICA, ROCAGEM_MANUAL, CAPINA_MANUAL e PODA_VEGETACAO.
- Status aceitos: PLANEJADA, EM_ANDAMENTO, CONCLUIDA e CANCELADA.
- dataIntervencao é obrigatória.
- custo deve ser maior ou igual a zero.
- trechoId e equipeId devem corresponder a registros existentes.
Classificação de prioridade
O GeradorRelatorio, na camada Service, classifica cada trecho usando os critérios definidos na Sprint 3:
Prioridade	Critério
URGENTE	Altura da grama ≥ 30 cm, densidade CRITICA ou pelo menos 30 dias sem corte
CRÍTICO	Altura ≥ 25 cm, densidade ALTA ou pelo menos 20 dias sem corte, se não for urgente
ATENÇÃO	Altura ≥ 18 cm, densidade MEDIA ou pelo menos 10 dias sem corte, se não for urgente ou crítico
NORMAL	Nenhuma das condições anteriores


O relatório também registra os totais por prioridade e identifica trechos com altura da grama acima de 30 cm, considerados sujeitos à multa conforme a regra adotada pelo projeto.
Exemplos de requisições cURL
Os arquivos JSON de exemplo ficam na pasta exemplos/. Execute os comandos a partir da raiz do projeto. No Windows PowerShell, utilize curl.exe.
1. Criar um trecho
curl.exe -i -X POST http://localhost:8080/api/trechos \
  -H "Content-Type: application/json" \
  -d "@exemplos/trecho.json"
2. Listar trechos
curl.exe -i http://localhost:8080/api/trechos
3. Buscar trecho por ID
curl.exe -i http://localhost:8080/api/trechos/1
4. Atualizar um trecho
curl.exe -i -X PUT http://localhost:8080/api/trechos/1 \
  -H "Content-Type: application/json" \
  -d "@exemplos/trecho-atualizado.json"
5. Testar validação com altura negativa
curl.exe -i -X POST http://localhost:8080/api/trechos \
  -H "Content-Type: application/json" \
  -d "@exemplos/trecho-invalido.json"
A requisição deve retornar 400 Bad Request porque alturaGramaCm não pode ser negativa.
6. Consultar derived queries
curl.exe -i http://localhost:8080/api/trechos/densidade/ALTA
curl.exe -i http://localhost:8080/api/trechos/altura-minima/25
curl.exe -i http://localhost:8080/api/equipes/status/ATIVA
curl.exe -i http://localhost:8080/api/intervencoes/status/PLANEJADA
7. Criar equipe e intervenção
curl.exe -i -X POST http://localhost:8080/api/equipes \
  -H "Content-Type: application/json" \
  -d "@exemplos/equipe.json"

curl.exe -i -X POST http://localhost:8080/api/intervencoes \
  -H "Content-Type: application/json" \
  -d "@exemplos/intervencao.json"
O exemplo de intervenção depende da existência dos IDs de trecho e equipe informados no arquivo exemplos/intervencao.json. Ajuste-os para IDs existentes no seu banco.
8. Gerar e consultar relatórios
curl.exe -i -X POST http://localhost:8080/api/relatorios
curl.exe -i http://localhost:8080/api/relatorios
curl.exe -i "http://localhost:8080/api/relatorios/periodo?inicio=2026-10-01T00:00:00&fim=2026-12-31T23:59:59"
9. Remover um trecho
curl.exe -i -X DELETE http://localhost:8080/api/trechos/ID_DO_TRECHO
Substitua ID_DO_TRECHO por um ID existente. A remoção pode retornar 409 Conflict caso existam intervenções vinculadas ao trecho.
Derived queries do Spring Data JPA
Os repositories declaram métodos cujas consultas são construídas pelo Spring Data JPA a partir dos nomes dos métodos, por exemplo:
- findByAlturaGramaCmGreaterThanEqual(Double minimo)
- findByDensidadeVegetacao(String densidadeVegetacao)
- findByStatus(String status)
- findByDataGeracaoBetween(LocalDateTime inicio, LocalDateTime fim)
O framework interpreta as propriedades e condições declaradas e gera a consulta durante a execução. Assim, não é necessário escrever SQL manual para essas operações.
Perguntas de reflexão
1. Por que o Repository é uma interface? Quem implementa os métodos e quando?
O Repository é uma interface porque declara as operações disponíveis sem precisar implementar manualmente o acesso ao banco. O Spring Data JPA cria uma implementação em tempo de execução quando a aplicação é inicializada, permitindo que o código utilize métodos como findAll(), findById() e save().
2. O pattern DAO da Sprint 3 morreu na migração?
Não. A responsabilidade de separar o acesso aos dados das regras de negócio continua existindo. Na Sprint 3, essa responsabilidade era implementada manualmente com DAO e JDBC. Na Sprint 4, os repositories do Spring Data JPA cumprem esse papel e reduzem a quantidade de código necessário.
3. Por que validar alturaGramaCm >= 0 no Service e não no Controller?
Porque é uma regra de negócio do domínio, e não uma regra exclusiva do protocolo HTTP. Ao mantê-la no Service, a validação também pode ser aplicada caso a lógica seja chamada por outra parte da aplicação, sem depender de um controller específico. O Controller deve se concentrar em receber a requisição e devolver a resposta HTTP.
4. Onde está o SQL do findByTipo() e quem o gerou?
O SQL não é escrito manualmente no método. O Spring Data JPA interpreta o nome do método e gera a consulta a partir da propriedade correspondente da entidade. No projeto atual, os repositories usam métodos como findByStatus, findByDensidadeVegetacao e findByAlturaGramaCmGreaterThanEqual.
Evidências de funcionamento
Para a entrega, registrem pelo menos seis requisições reais funcionando, incluindo operações CRUD e relatório. Sugestão:
1. GET /api/trechos
2. POST /api/trechos
3. PUT /api/trechos/{id}
4. DELETE /api/trechos/{id}
5. POST /api/relatorios
6. GET /api/relatorios
7. GET /api/relatorios/periodo?inicio=...&fim=...
Adicionem ao repositório as capturas de tela do Postman ou terminal em uma pasta como docs/evidencias/. Os exemplos deste README documentam como testar a API, mas não substituem evidências de execução real.
Git e boas práticas
O projeto utiliza .gitignore para excluir arquivos gerados (target/), configurações locais de IDE e o arquivo application-local.properties.

Exemplos de mensagens de commit:
feat: configure Spring Boot and Oracle connection
feat: map domain entities with JPA
feat: implement repositories and derived queries
feat: implement business services
feat: expose REST CRUD endpoints
feat: persist and query priority reports
docs: document API endpoints and execution
Nunca inclua senhas reais, tokens ou credenciais no código ou no histórico do Git.
Conclusão
A Sprint 4 transforma o MOTIVA em uma API REST organizada em camadas, com persistência por Spring Data JPA, validações centralizadas nos Services, consultas derivadas e geração de relatórios históricos. A solução mantém as regras de negócio desenvolvidas nas sprints anteriores e disponibiliza as funcionalidades por meio de endpoints HTTP.
