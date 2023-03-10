package br.dev.marcoalmeida.service;

import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.domain.Movie;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link GameSession}.
 */
public interface GameSessionService {
    /**
     * Save a gameSession.
     *
     * @param gameSession the entity to save.
     * @return the persisted entity.
     */
    GameSession save(GameSession gameSession);

    /**
     * Updates a gameSession.
     *
     * @param gameSession the entity to update.
     * @return the persisted entity.
     */
    GameSession update(GameSession gameSession);

    /**
     * Partially updates a gameSession.
     *
     * @param gameSession the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GameSession> partialUpdate(GameSession gameSession);

    /**
     * Get all the gameSessions.
     *
     * @return the list of entities.
     */
    List<GameSession> findAll();

    /**
     * Get the "id" gameSession.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameSession> findOne(Long id);

    /**
     * Delete the "id" gameSession.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    GameSession createGameSession(String userId);

    Set<IdempotentPair<Movie>> getUsedMoviePairs(GameSession gameSession);

    Optional<GameSession> findOneWithRounds(Long id);
}
