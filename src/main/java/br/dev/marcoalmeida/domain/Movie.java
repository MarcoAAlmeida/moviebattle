package br.dev.marcoalmeida.domain;

import java.io.Serializable;
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
