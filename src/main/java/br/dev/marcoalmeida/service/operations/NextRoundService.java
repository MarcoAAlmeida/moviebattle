package br.dev.marcoalmeida.service.operations;

import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.domain.enumeration.Choice;
import br.dev.marcoalmeida.service.GameRoundService;
import br.dev.marcoalmeida.service.GameSessionService;
import br.dev.marcoalmeida.service.MovieService;
import br.dev.marcoalmeida.service.api.dto.OpenGameRoundDTO;
import br.dev.marcoalmeida.service.dto.MoviePairDTO;
import br.dev.marcoalmeida.web.api.NextRoundApiDelegate;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NextRoundService implements NextRoundApiDelegate {

    private static int MAX_ATTEMPTS = 10000;

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private GameRoundService gameRoundService;

    @Autowired
    private MovieService movieService;

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
        return gameRounds.size() > 1
            ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            : gameRounds.size() == 1
                ? new ResponseEntity<>(gameRounds.get(0).openGameRoundDTO(), HttpStatus.OK)
                : createNewRound(gameSession);
    }

    private ResponseEntity<OpenGameRoundDTO> createNewRound(GameSession gameSession) {
        Set<MoviePairDTO> usedPairs = gameSessionService.getUsedMoviePairs(gameSession);

        MoviePairDTO newPair;
        int i = 0;
        do {
            newPair = movieService.getRandomPair();
        } while (!usedPairs.contains(newPair) || i < MAX_ATTEMPTS);

        if (i == MAX_ATTEMPTS) {
            return ResponseEntity.internalServerError().build();
        }

        return new ResponseEntity<>(gameRoundService.createRound(gameSession, newPair).openGameRoundDTO(), HttpStatus.OK);
    }
}
