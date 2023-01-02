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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_choice")
    private Choice userChoice;

    @Column(name = "correct")
    private Boolean correct;

    @ManyToOne
    @JsonIgnoreProperties(value = { "gameRounds" }, allowSetters = true)
    private GameSession gameSessionId;

    @ManyToMany(mappedBy = "rights")
    @JsonIgnoreProperties(value = { "rights", "lefts" }, allowSetters = true)
    private Set<Movie> rightIds = new HashSet<>();

    @ManyToMany(mappedBy = "lefts")
    @JsonIgnoreProperties(value = { "rights", "lefts" }, allowSetters = true)
    private Set<Movie> leftIds = new HashSet<>();

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

    public Set<Movie> getRightIds() {
        return this.rightIds;
    }

    public void setRightIds(Set<Movie> movies) {
        if (this.rightIds != null) {
            this.rightIds.forEach(i -> i.removeRight(this));
        }
        if (movies != null) {
            movies.forEach(i -> i.addRight(this));
        }
        this.rightIds = movies;
    }

    public GameRound rightIds(Set<Movie> movies) {
        this.setRightIds(movies);
        return this;
    }

    public GameRound addRightId(Movie movie) {
        this.rightIds.add(movie);
        movie.getRights().add(this);
        return this;
    }

    public GameRound removeRightId(Movie movie) {
        this.rightIds.remove(movie);
        movie.getRights().remove(this);
        return this;
    }

    public Set<Movie> getLeftIds() {
        return this.leftIds;
    }

    public void setLeftIds(Set<Movie> movies) {
        if (this.leftIds != null) {
            this.leftIds.forEach(i -> i.removeLeft(this));
        }
        if (movies != null) {
            movies.forEach(i -> i.addLeft(this));
        }
        this.leftIds = movies;
    }

    public GameRound leftIds(Set<Movie> movies) {
        this.setLeftIds(movies);
        return this;
    }

    public GameRound addLeftId(Movie movie) {
        this.leftIds.add(movie);
        movie.getLefts().add(this);
        return this;
    }

    public GameRound removeLeftId(Movie movie) {
        this.leftIds.remove(movie);
        movie.getLefts().remove(this);
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
}
