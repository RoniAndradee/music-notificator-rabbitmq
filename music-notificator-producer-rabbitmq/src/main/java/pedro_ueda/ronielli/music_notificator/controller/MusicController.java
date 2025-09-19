package pedro_ueda.ronielli.music_notificator.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedro_ueda.ronielli.music_notificator.dto.MusicDtoRequest;
import pedro_ueda.ronielli.music_notificator.service.SenderMessage;

@RestController
@RequestMapping("/musics")
public class MusicController {

    private final SenderMessage senderMessage;

    public MusicController(SenderMessage senderMessage) {
        this.senderMessage = senderMessage;
    }

    @PostMapping
    public String enviar(@RequestBody MusicDtoRequest musicRequest) {
        senderMessage.enviarMensagem(musicRequest);

        return "Mensagem enviada!";
    }
}
