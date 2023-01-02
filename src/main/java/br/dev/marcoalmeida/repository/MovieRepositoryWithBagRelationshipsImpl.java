package br.dev.marcoalmeida.repository;

import br.dev.marcoalmeida.domain.Movie;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MovieRepositoryWithBagRelationshipsImpl implements MovieRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Movie> fetchBagRelationships(Optional<Movie> movie) {
        return movie.map(this::fetchRights).map(this::fetchLefts);
    }

    @Override
    public Page<Movie> fetchBagRelationships(Page<Movie> movies) {
        return new PageImpl<>(fetchBagRelationships(movies.getContent()), movies.getPageable(), movies.getTotalElements());
    }

    @Override
    public List<Movie> fetchBagRelationships(List<Movie> movies) {
        return Optional.of(movies).map(this::fetchRights).map(this::fetchLefts).orElse(Collections.emptyList());
    }

    Movie fetchRights(Movie result) {
        return entityManager
            .createQuery("select movie from Movie movie left join fetch movie.rights where movie is :movie", Movie.class)
            .setParameter("movie", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Movie> fetchRights(List<Movie> movies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, movies.size()).forEach(index -> order.put(movies.get(index).getId(), index));
        List<Movie> result = entityManager
            .createQuery("select distinct movie from Movie movie left join fetch movie.rights where movie in :movies", Movie.class)
            .setParameter("movies", movies)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Movie fetchLefts(Movie result) {
        return entityManager
            .createQuery("select movie from Movie movie left join fetch movie.lefts where movie is :movie", Movie.class)
            .setParameter("movie", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Movie> fetchLefts(List<Movie> movies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, movies.size()).forEach(index -> order.put(movies.get(index).getId(), index));
        List<Movie> result = entityManager
            .createQuery("select distinct movie from Movie movie left join fetch movie.lefts where movie in :movies", Movie.class)
            .setParameter("movies", movies)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
