package br.dev.marcoalmeida.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "imdb_id")
    private String imdbId;

    @Column(name = "title")
    private String title;

    @Column(name = "imdb_rating")
    private Double imdbRating;

    @Column(name = "imdb_votes")
    private Long imdbVotes;

    @Column(name = "score")
    private Double score;

    @ManyToMany
    @JoinTable(name = "rel_movie__right", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "right_id"))
    @JsonIgnoreProperties(value = { "gameSessionId", "rightIds", "leftIds" }, allowSetters = true)
    private Set<GameRound> rights = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_movie__left", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "left_id"))
    @JsonIgnoreProperties(value = { "gameSessionId", "rightIds", "leftIds" }, allowSetters = true)
    private Set<GameRound> lefts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Movie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbId() {
        return this.imdbId;
    }

    public Movie imdbId(String imdbId) {
        this.setImdbId(imdbId);
        return this;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return this.title;
    }

    public Movie title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getImdbRating() {
        return this.imdbRating;
    }

    public Movie imdbRating(Double imdbRating) {
        this.setImdbRating(imdbRating);
        return this;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public Long getImdbVotes() {
        return this.imdbVotes;
    }

    public Movie imdbVotes(Long imdbVotes) {
        this.setImdbVotes(imdbVotes);
        return this;
    }

    public void setImdbVotes(Long imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public Double getScore() {
        return this.score;
    }

    public Movie score(Double score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Set<GameRound> getRights() {
        return this.rights;
    }

    public void setRights(Set<GameRound> gameRounds) {
        this.rights = gameRounds;
    }

    public Movie rights(Set<GameRound> gameRounds) {
        this.setRights(gameRounds);
        return this;
    }

    public Movie addRight(GameRound gameRound) {
        this.rights.add(gameRound);
        gameRound.getRightIds().add(this);
        return this;
    }

    public Movie removeRight(GameRound gameRound) {
        this.rights.remove(gameRound);
        gameRound.getRightIds().remove(this);
        return this;
    }

    public Set<GameRound> getLefts() {
        return this.lefts;
    }

    public void setLefts(Set<GameRound> gameRounds) {
        this.lefts = gameRounds;
    }

    public Movie lefts(Set<GameRound> gameRounds) {
        this.setLefts(gameRounds);
        return this;
    }

    public Movie addLeft(GameRound gameRound) {
        this.lefts.add(gameRound);
        gameRound.getLeftIds().add(this);
        return this;
    }

    public Movie removeLeft(GameRound gameRound) {
        this.lefts.remove(gameRound);
        gameRound.getLeftIds().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return id != null && id.equals(((Movie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", imdbId='" + getImdbId() + "'" +
            ", title='" + getTitle() + "'" +
            ", imdbRating=" + getImdbRating() +
            ", imdbVotes=" + getImdbVotes() +
            ", score=" + getScore() +
            "}";
    }
}
