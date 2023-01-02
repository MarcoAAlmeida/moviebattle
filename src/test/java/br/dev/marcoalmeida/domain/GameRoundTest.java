package br.dev.marcoalmeida.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.dev.marcoalmeida.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameRoundTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameRound.class);
        GameRound gameRound1 = new GameRound();
        gameRound1.setId(1L);
        GameRound gameRound2 = new GameRound();
        gameRound2.setId(gameRound1.getId());
        assertThat(gameRound1).isEqualTo(gameRound2);
        gameRound2.setId(2L);
        assertThat(gameRound1).isNotEqualTo(gameRound2);
        gameRound1.setId(null);
        assertThat(gameRound1).isNotEqualTo(gameRound2);
    }
}
