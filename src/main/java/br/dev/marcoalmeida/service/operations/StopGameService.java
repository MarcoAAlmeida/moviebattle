package br.dev.marcoalmeida.service.operations;

import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.service.GameSessionService;
import br.dev.marcoalmeida.service.api.dto.GameSessionDTO;
import br.dev.marcoalmeida.service.mapper.GameSessionMapper;
import br.dev.marcoalmeida.web.api.StopGameApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StopGameService implements StopGameApiDelegate {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private GameSessionMapper gameSessionMapper;

    @Override
    public ResponseEntity<GameSessionDTO> stopGame(Integer gameSessionId) {
        return gameSessionService
            .findOne(gameSessionId.longValue())
            .map(this::update)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private ResponseEntity<GameSessionDTO> update(GameSession gameSession) {
        if (gameSession.getFinished()) return ResponseEntity.unprocessableEntity().build();

        gameSession.setFinished(true);
        gameSessionService.update(gameSession);
        return ResponseEntity.ok(gameSessionMapper.gameSessionDTO(gameSession));
    }
}
