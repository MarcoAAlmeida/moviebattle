package br.dev.marcoalmeida.service;

import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.domain.enumeration.Choice;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link GameRound}.
 */
public interface GameRoundService {
    /**
     * Save a gameRound.
     *
     * @param gameRound the entity to save.
     * @return the persisted entity.
     */
    GameRound save(GameRound gameRound);

    /**
     * Updates a gameRound.
     *
     * @param gameRound the entity to update.
     * @return the persisted entity.
     */
    GameRound update(GameRound gameRound);

    /**
     * Partially updates a gameRound.
     *
     * @param gameRound the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GameRound> partialUpdate(GameRound gameRound);

    /**
     * Get all the gameRounds.
     *
     * @return the list of entities.
     */
    List<GameRound> findAll();

    /**
     * Get the "id" gameRound.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameRound> findOne(Long id);

    /**
     * Delete the "id" gameRound.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<List<GameRound>> findByGameSessionAndUserChoice(GameSession gameSession, Choice userChoice);
}
