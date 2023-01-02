package br.dev.marcoalmeida.domain;

import br.dev.marcoalmeida.domain.enumeration.Choice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A GameRound.
 */
@Entity
@Table(name = "game_round")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameRound implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "game_session_id")
    private String gameSessionId;

    @Column(name = "right_imdb_id")
    private String rightImdbId;

    @Column(name = "left_imdb_id")
    private String leftImdbId;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_choice")
    private Choice userChoice;

    @Column(name = "correct")
    private Boolean correct;

    @OneToMany(mappedBy = "id")
    @JsonIgnoreProperties(value = { "id" }, allowSetters = true)
    private Set<GameSession> gameSessionIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameRound id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameSessionId() {
        return this.gameSessionId;
    }

    public GameRound gameSessionId(String gameSessionId) {
        this.setGameSessionId(gameSessionId);
        return this;
    }

    public void setGameSessionId(String gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public String getRightImdbId() {
        return this.rightImdbId;
    }

    public GameRound rightImdbId(String rightImdbId) {
        this.setRightImdbId(rightImdbId);
        return this;
    }

    public void setRightImdbId(String rightImdbId) {
        this.rightImdbId = rightImdbId;
    }

    public String getLeftImdbId() {
        return this.leftImdbId;
    }

    public GameRound leftImdbId(String leftImdbId) {
        this.setLeftImdbId(leftImdbId);
        return this;
    }

    public void setLeftImdbId(String leftImdbId) {
        this.leftImdbId = leftImdbId;
    }

    public Choice getUserChoice() {
        return this.userChoice;
    }

    public GameRound userChoice(Choice userChoice) {
        this.setUserChoice(userChoice);
        return this;
    }

    public void setUserChoice(Choice userChoice) {
        this.userChoice = userChoice;
    }

    public Boolean getCorrect() {
        return this.correct;
    }

    public GameRound correct(Boolean correct) {
        this.setCorrect(correct);
        return this;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Set<GameSession> getGameSessionIds() {
        return this.gameSessionIds;
    }

    public void setGameSessionIds(Set<GameSession> gameSessions) {
        if (this.gameSessionIds != null) {
            this.gameSessionIds.forEach(i -> i.setId(null));
        }
        if (gameSessions != null) {
            gameSessions.forEach(i -> i.setId(this));
        }
        this.gameSessionIds = gameSessions;
    }

    public GameRound gameSessionIds(Set<GameSession> gameSessions) {
        this.setGameSessionIds(gameSessions);
        return this;
    }

    public GameRound addGameSessionId(GameSession gameSession) {
        this.gameSessionIds.add(gameSession);
        gameSession.setId(this);
        return this;
    }

    public GameRound removeGameSessionId(GameSession gameSession) {
        this.gameSessionIds.remove(gameSession);
        gameSession.setId(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameRound)) {
            return false;
        }
        return id != null && id.equals(((GameRound) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameRound{" +
            "id=" + getId() +
            ", gameSessionId='" + getGameSessionId() + "'" +
            ", rightImdbId='" + getRightImdbId() + "'" +
            ", leftImdbId='" + getLeftImdbId() + "'" +
            ", userChoice='" + getUserChoice() + "'" +
            ", correct='" + getCorrect() + "'" +
            "}";
    }
}
