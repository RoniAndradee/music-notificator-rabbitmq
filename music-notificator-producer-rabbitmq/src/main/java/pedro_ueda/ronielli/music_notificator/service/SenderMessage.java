package pedro_ueda.ronielli.music_notificator.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pedro_ueda.ronielli.music_notificator.dto.MusicDtoRequest;
import pedro_ueda.ronielli.music_notificator.model.Music;

@Service
public class SenderMessage {

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.exchange.name}")
    private String exchangeName;

    public SenderMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensagem(MusicDtoRequest musicRequest) {
        Music music = new Music(
                java.util.UUID.randomUUID(),
                musicRequest.name(),
                musicRequest.artist(),
                musicRequest.album()
        );

        rabbitTemplate.convertAndSend(
                exchangeName,
                "",
                music
        );
        System.out.println("Mensagem enviada: " + music);
    }
}
