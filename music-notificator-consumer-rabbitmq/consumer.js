const express = require("express");
const amqp = require("amqplib");
const fs = require("fs");
const app = express();
const PORT = 3000;

let messages = [];
const QUEUE = "example.queue"; 
const FILE_PATH = "received_messages.txt";

async function connectRabbit() {
    try {
        const connection = await amqp.connect("amqp://myuser:secret@localhost:5672");
        const channel = await connection.createChannel();
        await channel.assertQueue(QUEUE, { durable: true });

        console.log(" [*] Aguardando mensagens...");

        channel.consume(
            QUEUE,
            (msg) => {
                if (msg !== null) {
                    const content = msg.content.toString();
                    console.log(" [x] Recebido:", content);
                    messages.push(content);

                    fs.appendFile(FILE_PATH, content + "\n", (err) => {
                        if (err) {
                            console.error("Erro ao salvar a mensagem no arquivo:", err);
                        }
                    });

                    channel.ack(msg);
                }
            },
            { noAck: false }
        );
    } catch (err) {
        console.error("Erro ao conectar no RabbitMQ:", err);
    }
}

app.get("/messages", (req, res) => {
    res.json(messages);
});

app.listen(PORT, () => {
    console.log(`Consumer rodando em http://localhost:${PORT}`);
    connectRabbit();
});
