package br.dev.marcoalmeida.service.impl;

import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.repository.GameSessionRepository;
import br.dev.marcoalmeida.service.GameSessionService;
import br.dev.marcoalmeida.service.IdempotentPair;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GameSession}.
 */
@Service
@Transactional
public class GameSessionServiceImpl implements GameSessionService {

    private final Logger log = LoggerFactory.getLogger(GameSessionServiceImpl.class);

    private final GameSessionRepository gameSessionRepository;

    public GameSessionServiceImpl(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    @Override
    public GameSession save(GameSession gameSession) {
        log.debug("Request to save GameSession : {}", gameSession);
        return gameSessionRepository.save(gameSession);
    }

    @Override
    public GameSession update(GameSession gameSession) {
        log.debug("Request to update GameSession : {}", gameSession);
        return gameSessionRepository.save(gameSession);
    }

    @Override
    public Optional<GameSession> partialUpdate(GameSession gameSession) {
        log.debug("Request to partially update GameSession : {}", gameSession);

        return gameSessionRepository
            .findById(gameSession.getId())
            .map(existingGameSession -> {
                if (gameSession.getUserId() != null) {
                    existingGameSession.setUserId(gameSession.getUserId());
                }

                if (gameSession.getFinished() != null) {
                    existingGameSession.setFinished(gameSession.getFinished());
                }

                return existingGameSession;
            })
            .map(gameSessionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameSession> findAll() {
        log.debug("Request to get all GameSessions");
        return gameSessionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameSession> findOne(Long id) {
        log.debug("Request to get GameSession : {}", id);
        return gameSessionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GameSession : {}", id);
        gameSessionRepository.deleteById(id);
    }

    @Override
    public GameSession createGameSession(String userId) {
        GameSession gameSession = new GameSession();
        gameSession.setUserId(userId);
        gameSession.setFinished(false);
        return this.save(gameSession);
    }

    @Override
    public Set getUsedMoviePairs(GameSession gameSession) {
        return gameSession
            .getGameRounds()
            .stream()
            .map(round -> new IdempotentPair(round.getLeft(), round.getRight()))
            .collect(Collectors.toSet());
    }

    @Override
    public Optional<GameSession> findOneWithRounds(Long id) {
        return gameSessionRepository.findOneWithRounds(id);
    }
}
