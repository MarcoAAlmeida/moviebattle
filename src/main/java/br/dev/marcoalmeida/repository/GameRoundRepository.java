package br.dev.marcoalmeida.repository;

import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.domain.enumeration.Choice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameRound entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
    Optional<List<GameRound>> findAllByGameSessionIdAndUserChoice(GameSession gameSession, Choice userChoice);
}
