package br.dev.marcoalmeida.repository;

import br.dev.marcoalmeida.domain.GameRound;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameRound entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, Long> {}
