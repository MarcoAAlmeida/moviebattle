package br.dev.marcoalmeida.domain;

import br.dev.marcoalmeida.service.api.dto.GameSessionDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A GameSession.
 */
@Entity
@Table(name = "game_session")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(mappedBy = "gameSessionId")
    @JsonIgnoreProperties(value = { "gameSessionId", "left", "right" }, allowSetters = true)
    private Set<GameRound> gameRounds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public GameSession userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<GameRound> getGameRounds() {
        return this.gameRounds;
    }

    public void setGameRounds(Set<GameRound> gameRounds) {
        if (this.gameRounds != null) {
            this.gameRounds.forEach(i -> i.setGameSessionId(null));
        }
        if (gameRounds != null) {
            gameRounds.forEach(i -> i.setGameSessionId(this));
        }
        this.gameRounds = gameRounds;
    }

    public GameSession gameRounds(Set<GameRound> gameRounds) {
        this.setGameRounds(gameRounds);
        return this;
    }

    public GameSession addGameRound(GameRound gameRound) {
        this.gameRounds.add(gameRound);
        gameRound.setGameSessionId(this);
        return this;
    }

    public GameSession removeGameRound(GameRound gameRound) {
        this.gameRounds.remove(gameRound);
        gameRound.setGameSessionId(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameSession)) {
            return false;
        }
        return id != null && id.equals(((GameSession) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameSession{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            "}";
    }

    public GameSessionDTO toDTO() {
        return new GameSessionDTO().id(this.id.intValue());
    }
}
