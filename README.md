<h1>SCA - Sistema de Controle de Almoxarifado</h1>

<p>SCA (Sistema de Controle de Almoxarifado) é uma aplicação que simula um WMS (Warehouse Management System) voltado para o contexto de um almoxarifado.</p>

<h2>🎯 Objetivo</h2>

<ul>
  <li>Controlar estoque</li>
  <li>Controlar entrada e saída de produtos</li>
  <li>Atender requisições internas</li>
  <li>Registro e auditoria de movimentações</li>
</ul>

## Como executar

### Pré-requisitos

- Docker instalado

### 1. Clone o repositório

```bash
git clone https://github.com/vitormate/sca.git
cd sca
```

### 2. Suba os containers

```bash
docker-compose up
```

O Docker baixa automaticamente a imagem da API e o banco de dados PostgreSQL — nenhuma configuração adicional necessária.

## Documentação (Swagger)

Acesse: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

<h2>⚙️ Funcionalidades</h2>

<ul type="none" >
  <li>✅ Cadastro de produtos</li>
  <li>✅ Cadastro de posições</li>
  <li>✅ Cadastro de estoque</li>
  <li>✅ Controle de entrada de produtos</li>
  <li>✅ Controle de saída de produtos</li>
  <li>✅ Controle de ordens de coleta</li>
  <li>✅ Registro de movimentações</li>
  <li>✅ Controle de movimentação interna</li>
</ul>

<h2>💻 Tecnologias Utilizadas</h2>

<ul>
  <li>Java</li>
  <li>Spring Boot</li>
  <li>PostgreSQL</li>
  <li>Flyway</li>
  <li>Lombok</li>
  <li>Swagger / OpenAPI</li>
  <li>JUnit 5</li>
  <li>Mockito</li>
  <li>Docker</li>
</ul>

<h2>🏛️ Arquitetura</h2>

O projeto segue uma arquitetura em camadas:

- Controller → Responsável pelos endpoints da API
- Service → Orquestra lógica e regras de negócio
- Entity → Regras de negócio das entidades
- Repository → Persistência de dados

<h2>📦 Entidades</h2>

O sistema é composto pelas seguintes entidades principais:

- **Product** → Produto armazenado
- **Position** → Local físico no almoxarifado
- **Stock** → Quantidade de um produto em uma posição
- **PickingOrder** → Ordem de coleta
- **PickingProduct** → Item dentro da ordem de coleta
- **MovementStock** → Registro de auditoria de movimentações

<h2>🔄 Fluxo das Principais Funcionalidades</h2>

<h3>Fluxo de Entrada de Produtos</h3>

<h4>🎯 Objetivo</h4>

Registrar a entrada de produtos no estoque e garantir rastreabilidade da movimentação.

1. Usuário informa:

   * Código do produto
   * Código da posição
   * Quantidade recebida

2. Sistema valida:

   * Se o produto existe
   * Se a posição é válida

3. Sistema atualiza:

   * Quantidade disponível no estoque

4. Sistema registra movimentação:

   * Tipo: `IN`(Entrada)
   * Produto
   * Posição
   * Quantidade
   * Usuário responsável
   * Data/hora

6. Operação finalizada com sucesso

---

<h3>📤 Fluxo de Saída de Produtos (Picking)</h3>

<h4>🎯 Objetivo</h4>

Atender uma requisição interna através de uma ordem de coleta.

---

🧾 1. Criação da Ordem de Coleta

1. Usuário cria uma Picking Order informando:

   * Produtos
   * Quantidades solicitadas

2. Sistema:

   * Cria a ordem com status `CREATED`(Criado)
   * Associa os itens a ordem de coleta (PickingProducts)
   * Define status inicial dos itens como `WAITING`(Aguardando)

---

📦 2. Processo de Coleta

1. Separador assume a ordem

2. Seleciona um item da ordem

3. Envia posição

4. Sistema:

   * Busca estoque pela posição
   * Valida se o produto da posição é o correto
   * Verifica quantidade disponível

5. Sistema executa coleta:

   * Se estoque ≥ necessário → coleta total
   * Se estoque < necessário → coleta parcial

6. Sistema atualiza:

   * `collectedAmount`
   * Status do item (`PARTIAL` ou `COMPLETED`)
   * Quantidade no estoque

7. Sistema registra movimentação:

   * Tipo: `OUT`(Saída)
   * Produto
   * Posição
   * Quantidade coletada na operação
   * Usuário
   * Data/hora

---

✅ 3. Finalização Automática da Ordem

Após cada coleta:

1. Sistema verifica:

   * Todos os itens estão com o status de coleta `COMPLETED`(Completo)?

2. Se sim:

   * Atualiza status da ordem para `FINISHED`
   * Registra data de finalização

---

<h2>🚀 Executando o projeto</h2>

1. Clonar o repositório

    * git clone git@github.com:vitormate/sca.git

2. Criar o banco PostgreSQL

3. Configurar application.properties

    * spring.datasource.url=...
    * spring.datasource.username=...
    * spring.datasource.password=...

4. Rodar aplicação
