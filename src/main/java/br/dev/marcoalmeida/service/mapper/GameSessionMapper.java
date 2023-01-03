package br.dev.marcoalmeida.service.mapper;

import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.service.api.dto.GameSessionDTO;
import org.springframework.stereotype.Service;

@Service
public class GameSessionMapper {

    public GameSessionDTO gameSessionDTO(GameSession gameSession) {
        return new GameSessionDTO().id(gameSession.getId().intValue()).userId(gameSession.getUserId()).finished(gameSession.getFinished());
    }
}
