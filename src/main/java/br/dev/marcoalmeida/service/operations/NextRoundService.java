package br.dev.marcoalmeida.service.operations;

import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.domain.Movie;
import br.dev.marcoalmeida.domain.enumeration.Choice;
import br.dev.marcoalmeida.service.GameRoundService;
import br.dev.marcoalmeida.service.GameSessionService;
import br.dev.marcoalmeida.service.IdempotentPair;
import br.dev.marcoalmeida.service.MovieService;
import br.dev.marcoalmeida.service.api.dto.UnansweredGameRoundDTO;
import br.dev.marcoalmeida.web.api.NextRoundApiDelegate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    public ResponseEntity<UnansweredGameRoundDTO> nextRound(Integer gameSessionId) {
        return gameSessionService
            .findOneWithRounds(gameSessionId.longValue())
            .map(this::fetchNextRound)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private ResponseEntity<UnansweredGameRoundDTO> fetchNextRound(GameSession gameSession) {
        if (gameSession.getFinished()) return ResponseEntity.unprocessableEntity().build();

        List<GameRound> unanswered = gameSession
            .getGameRounds()
            .stream()
            .filter(gameRound -> gameRound.getUserChoice() == Choice.NONE)
            .collect(Collectors.toUnmodifiableList());

        return checkUnansweredSize(gameSession, unanswered);
    }

    private ResponseEntity<UnansweredGameRoundDTO> checkUnansweredSize(GameSession gameSession, List<GameRound> unanswered) {
        return unanswered.size() > 1
            ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            : unanswered.size() == 1
                ? new ResponseEntity<>(unanswered.get(0).openGameRoundDTO(), HttpStatus.OK)
                : createNewRound(gameSession);
    }

    private ResponseEntity<UnansweredGameRoundDTO> createNewRound(GameSession gameSession) {
        if (gameSession.getFinished()) return ResponseEntity.unprocessableEntity().build();

        Set<IdempotentPair<Movie>> usedIdepotentPairs = gameSessionService.getUsedMoviePairs(gameSession);

        IdempotentPair<Movie> newPair;
        int i = 0;
        do {
            newPair = movieService.getRandomPair();
            i++;
        } while (usedIdepotentPairs.contains(newPair) && i < MAX_ATTEMPTS);

        if (i == MAX_ATTEMPTS) {
            return ResponseEntity.internalServerError().build();
        }

        return new ResponseEntity<>(gameRoundService.createRound(gameSession, newPair).openGameRoundDTO(), HttpStatus.OK);
    }
}
