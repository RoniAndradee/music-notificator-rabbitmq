# Projeto Music Notificator RabbitMQ - Producer & Consumer

## Integrantes
- Pedro Ueda - RA: 01241090
- Ronielli Andrade - RA: 01241040

---

## Visão geral do projeto
O tema do projeto é alertar quando uma nova música é lançada.

Este projeto demonstra o fluxo **Producer → RabbitMQ → Consumer**. 
- A **API Java (Producer)** recebe requisições HTTP POST e envia mensagens para a fila RabbitMQ.
- O **RabbitMQ** gerencia a fila de mensagens.  
- O **Consumer Node.js** consome mensagens da fila, salva em memória e em um arquivo `messages.txt`, e disponibiliza via HTTP GET.

---

## Endpoints

### Producer (API Java)

**URL de publicação:**  
`POST http://localhost:8080/musics`

**Exemplo de JSON a ser enviado:**  
```json
{
  "name": "Shape of You",
  "artist": "Ed Sheeran",
  "album": "Divide"
}
```

**Exemplo de envio usando curl**

`curl -X POST http://localhost:8080/musics \
-H "Content-Type: application/json" \
-d '{
  "name": "So Far Away",
  "artist": "Nightmare",
  "album": "Avenged Sevenfold"
}'`

### Consumer (Node.js)

**URL para consultar mensagens recebidas:**
`GET http://localhost:3000/messages`

**Exemplo de teste usando curl:**

`curl http://localhost:3000/messages`

**Colocar tutorial de rodar o producer** 


**Rodar o Consumer Node.js**

No diretório do consumer:

# Instalar as dependências
`npm install express amqplib`

# Executar o consumer
`node consumer.js`

O consumer ficará escutando a fila example.queue e salvará todas as mensagens recebidas em memória (messages[]) e no arquivo messages.txt.
