package br.dev.marcoalmeida.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.dev.marcoalmeida.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameSession.class);
        GameSession gameSession1 = new GameSession();
        gameSession1.setId("id1");
        GameSession gameSession2 = new GameSession();
        gameSession2.setId(gameSession1.getId());
        assertThat(gameSession1).isEqualTo(gameSession2);
        gameSession2.setId("id2");
        assertThat(gameSession1).isNotEqualTo(gameSession2);
        gameSession1.setId(null);
        assertThat(gameSession1).isNotEqualTo(gameSession2);
    }
}
