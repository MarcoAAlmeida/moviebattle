package br.dev.marcoalmeida.service.impl;

import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.repository.GameRoundRepository;
import br.dev.marcoalmeida.service.GameRoundService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GameRound}.
 */
@Service
@Transactional
public class GameRoundServiceImpl implements GameRoundService {

    private final Logger log = LoggerFactory.getLogger(GameRoundServiceImpl.class);

    private final GameRoundRepository gameRoundRepository;

    public GameRoundServiceImpl(GameRoundRepository gameRoundRepository) {
        this.gameRoundRepository = gameRoundRepository;
    }

    @Override
    public GameRound save(GameRound gameRound) {
        log.debug("Request to save GameRound : {}", gameRound);
        return gameRoundRepository.save(gameRound);
    }

    @Override
    public GameRound update(GameRound gameRound) {
        log.debug("Request to update GameRound : {}", gameRound);
        return gameRoundRepository.save(gameRound);
    }

    @Override
    public Optional<GameRound> partialUpdate(GameRound gameRound) {
        log.debug("Request to partially update GameRound : {}", gameRound);

        return gameRoundRepository
            .findById(gameRound.getId())
            .map(existingGameRound -> {
                if (gameRound.getGameSessionId() != null) {
                    existingGameRound.setGameSessionId(gameRound.getGameSessionId());
                }
                if (gameRound.getRightImdbId() != null) {
                    existingGameRound.setRightImdbId(gameRound.getRightImdbId());
                }
                if (gameRound.getLeftImdbId() != null) {
                    existingGameRound.setLeftImdbId(gameRound.getLeftImdbId());
                }
                if (gameRound.getUserChoice() != null) {
                    existingGameRound.setUserChoice(gameRound.getUserChoice());
                }
                if (gameRound.getCorrect() != null) {
                    existingGameRound.setCorrect(gameRound.getCorrect());
                }

                return existingGameRound;
            })
            .map(gameRoundRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameRound> findAll() {
        log.debug("Request to get all GameRounds");
        return gameRoundRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameRound> findOne(Long id) {
        log.debug("Request to get GameRound : {}", id);
        return gameRoundRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GameRound : {}", id);
        gameRoundRepository.deleteById(id);
    }
}
