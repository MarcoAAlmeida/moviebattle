package br.dev.marcoalmeida.service.operations;

import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.domain.enumeration.Choice;
import br.dev.marcoalmeida.service.GameRoundService;
import br.dev.marcoalmeida.service.GameSessionService;
import br.dev.marcoalmeida.service.api.dto.OpenGameRoundDTO;
import br.dev.marcoalmeida.web.api.NextRoundApiDelegate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NextRoundService implements NextRoundApiDelegate {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private GameRoundService gameRoundService;

    @Override
    public ResponseEntity<OpenGameRoundDTO> nextRound(Integer gameSessionId) {
        return gameSessionService
            .findOne(gameSessionId.longValue())
            .map(this::fetchNextRound)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private ResponseEntity<OpenGameRoundDTO> fetchNextRound(GameSession gameSession) {
        return gameRoundService
            .findByGameSessionAndUserChoice(gameSession, Choice.NONE)
            .map(rounds -> checkRounds(gameSession, rounds))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private ResponseEntity<OpenGameRoundDTO> checkRounds(GameSession gameSession, List<GameRound> gameRounds) {
        return gameRounds.size() == 0
            ? createNewRound(gameSession)
            : gameRounds.size() == 1
                ? new ResponseEntity<>(gameRounds.get(0).openGameRoundDTO(), HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<OpenGameRoundDTO> createNewRound(GameSession gameSession) {
        return new ResponseEntity<>(new OpenGameRoundDTO().id(1).left("Star Wars").right("Star Trek"), HttpStatus.OK);
    }
}
