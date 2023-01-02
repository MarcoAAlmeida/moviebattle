package br.dev.marcoalmeida.repository;

import br.dev.marcoalmeida.domain.GameSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    @Query("SELECT gs FROM GameSession gs LEFT JOIN FETCH gs.gameRounds WHERE gs.id = :id")
    Optional<GameSession> findOneWithRounds(Long id);
}
