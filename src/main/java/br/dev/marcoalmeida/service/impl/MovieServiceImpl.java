package br.dev.marcoalmeida.service.impl;

import br.dev.marcoalmeida.domain.Movie;
import br.dev.marcoalmeida.repository.MovieRepository;
import br.dev.marcoalmeida.service.MovieService;
import br.dev.marcoalmeida.service.dto.MoviePairDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Movie}.
 */
@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final Logger log = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie save(Movie movie) {
        log.debug("Request to save Movie : {}", movie);
        return movieRepository.save(movie);
    }

    @Override
    public Movie update(Movie movie) {
        log.debug("Request to update Movie : {}", movie);
        return movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> partialUpdate(Movie movie) {
        log.debug("Request to partially update Movie : {}", movie);

        return movieRepository
            .findById(movie.getId())
            .map(existingMovie -> {
                if (movie.getImdbId() != null) {
                    existingMovie.setImdbId(movie.getImdbId());
                }
                if (movie.getTitle() != null) {
                    existingMovie.setTitle(movie.getTitle());
                }
                if (movie.getImdbRating() != null) {
                    existingMovie.setImdbRating(movie.getImdbRating());
                }
                if (movie.getImdbVotes() != null) {
                    existingMovie.setImdbVotes(movie.getImdbVotes());
                }
                if (movie.getScore() != null) {
                    existingMovie.setScore(movie.getScore());
                }

                return existingMovie;
            })
            .map(movieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        log.debug("Request to get all Movies");
        return movieRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Movie> findOne(Long id) {
        log.debug("Request to get Movie : {}", id);
        return movieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Movie : {}", id);
        movieRepository.deleteById(id);
    }

    @Override
    public MoviePairDTO getRandomPair() {
        Set<Movie> titleSet = movieRepository.findAll().stream().collect(Collectors.toSet());

        return new MoviePairDTO(titleSet.stream().skip(0).findFirst().get(), titleSet.stream().skip(1).findFirst().get());
    }
}
