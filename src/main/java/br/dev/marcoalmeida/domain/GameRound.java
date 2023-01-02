package br.dev.marcoalmeida.domain;

import br.dev.marcoalmeida.domain.enumeration.Choice;
import br.dev.marcoalmeida.service.api.dto.UnansweredGameRoundDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_choice")
    private Choice userChoice;

    @Column(name = "correct")
    private Boolean correct;

    @ManyToOne
    @JsonIgnoreProperties(value = { "gameRounds" }, allowSetters = true)
    private GameSession gameSessionId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rightRounds", "leftRounds" }, allowSetters = true)
    private Movie left;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rightRounds", "leftRounds" }, allowSetters = true)
    private Movie right;

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

    public GameSession getGameSessionId() {
        return this.gameSessionId;
    }

    public void setGameSessionId(GameSession gameSession) {
        this.gameSessionId = gameSession;
    }

    public GameRound gameSessionId(GameSession gameSession) {
        this.setGameSessionId(gameSession);
        return this;
    }

    public Movie getLeft() {
        return this.left;
    }

    public void setLeft(Movie movie) {
        this.left = movie;
    }

    public GameRound left(Movie movie) {
        this.setLeft(movie);
        return this;
    }

    public Movie getRight() {
        return this.right;
    }

    public void setRight(Movie movie) {
        this.right = movie;
    }

    public GameRound right(Movie movie) {
        this.setRight(movie);
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
            ", userChoice='" + getUserChoice() + "'" +
            ", correct='" + getCorrect() + "'" +
            "}";
    }

    public UnansweredGameRoundDTO openGameRoundDTO() {
        return new UnansweredGameRoundDTO().id(this.id.intValue()).left(this.left.getTitle()).right(this.right.getTitle());
    }
}
