# Risum Odonto - Backend (API)

Bem-vindo ao repositório do backend do sistema de gestão de clínicas odontológicas Risum Odonto. Este projeto é responsável por toda a lógica de negócios, persistência de dados e exposição de APIs para o frontend da aplicação.

## 🚀 Tecnologias Utilizadas

Este backend foi construído com as seguintes tecnologias e ferramentas:

*   **Linguagem:** Java 17
*   **Framework:** Spring Boot 3.x
*   **Banco de Dados:** PostgreSQL
*   **ORM:** Spring Data JPA / Hibernate
*   **Build Tool:** Maven
*   **Testes:** JUnit 5, Mockito
*   **Documentação API:** SpringDoc OpenAPI (Swagger UI)
*   **Segurança:** Spring Security (JWT, OAuth2, etc.)
*   **Outros:** Lombok, Flyway (para migrações de DB)

## 📦 Estrutura do Projeto

O projeto segue uma arquitetura em camadas, comum em aplicações Spring Boot:
## ⚙️ Configuração e Execução

Siga os passos abaixo para configurar e executar o projeto em seu ambiente local.

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas:

*   Java Development Kit (JDK) 17 ou superior
*   Maven
*   Docker (opcional, para rodar o banco de dados localmente)
*   Um cliente Git

### 1. Clonar o Repositório

git clone git@github.com:btechsolution/api.git
cd api


### 2. Configurar o Banco de Dados

Este projeto utiliza PostgreSQL.

#### Opção A: Usando Docker (Recomendado para desenvolvimento local)

1.  Certifique-se de que o Docker esteja rodando.
2.  Crie um arquivo `docker-compose.yml` na raiz do projeto (se ainda não existir) com a configuração do seu banco de dados. Exemplo para PostgreSQL:
    ```yaml
    version: '3.8'
    services:
      db:
        image: postgres:13
        environment:
          POSTGRES_DB: risum_odonto_db
          POSTGRES_USER: user
          POSTGRES_PASSWORD: password
        ports:
          - "5432:5432"
        volumes:
          - db_data:/var/lib/postgresql/data
    volumes:
      db_data:
    ```
3.  Inicie o contêiner do banco de dados:
    ```bash
    docker-compose up -d
    ```

#### Opção B: Usando um Banco de Dados Local

1.  Instale e configure o PostgreSQL em sua máquina.
2.  Crie um banco de dados chamado `risum_odonto_db`.
3.  Crie um usuário e senha conforme as configurações no `application.properties`.

### 3. Configurar as Propriedades da Aplicação

1.  Abra o arquivo `src/main/resources/application.properties` (ou `application.yml`).
2.  Ajuste as configurações do banco de dados para corresponder ao seu ambiente:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/risum_odonto_db
    spring.datasource.username=user
    spring.datasource.password=password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    # Configurações adicionais, como JWT, CORS, etc.
    ```
    *   **Importante:** Para produção, utilize variáveis de ambiente ou um sistema de gerenciamento de segredos.

### 4. Executar a Aplicação

#### Via Maven (Terminal)


mvn spring-boot:run


#### Via IDE (IntelliJ IDEA, Eclipse, VS Code)

1.  Importe o projeto como um projeto Maven.
2.  Localize a classe principal `RisumOdontoApplication.java`.
3.  Clique com o botão direito e selecione "Run 'RisumOdontoApplication'".

A aplicação estará disponível em `http://localhost:8080` (ou a porta configurada).

## 📄 Documentação da API (Swagger UI)

Após iniciar a aplicação, você pode acessar a documentação interativa da API através do Swagger UI:

*   `http://localhost:8080/swagger-ui.html`

## 🧪 Testes

Para executar os testes unitários e de integração:

mvn test


## 🤝 Contribuição

Contribuições são bem-vindas! Se você deseja contribuir, por favor, siga os seguintes passos:

1.  Faça um fork do projeto.
2.  Crie uma nova branch (`git checkout -b feature/minha-nova-feature`).
3.  Faça suas alterações e commit (`git commit -m 'feat: Adiciona nova funcionalidade X'`).
4.  Envie para a branch (`git push origin feature/minha-nova-feature`).
5.  Abra um Pull Request.

## 📝 Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---
