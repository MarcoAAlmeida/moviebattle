package br.dev.marcoalmeida.service;

import br.dev.marcoalmeida.domain.Movie;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Movie}.
 */
public interface MovieService {
    /**
     * Save a movie.
     *
     * @param movie the entity to save.
     * @return the persisted entity.
     */
    Movie save(Movie movie);

    /**
     * Updates a movie.
     *
     * @param movie the entity to update.
     * @return the persisted entity.
     */
    Movie update(Movie movie);

    /**
     * Partially updates a movie.
     *
     * @param movie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Movie> partialUpdate(Movie movie);

    /**
     * Get all the movies.
     *
     * @return the list of entities.
     */
    List<Movie> findAll();

    /**
     * Get the "id" movie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Movie> findOne(Long id);

    /**
     * Delete the "id" movie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
