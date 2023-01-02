package br.dev.marcoalmeida.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.springframework.data.domain.Persistable;

/**
 * A GameSession.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@Table(name = "game_session")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameSession implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private Long userId;

    @Transient
    private boolean isPersisted;

    @ManyToOne
    @JsonIgnoreProperties(value = { "gameSessionIds" }, allowSetters = true)
    private GameRound id;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public GameSession id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public GameSession userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public GameSession setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    public GameRound getId() {
        return this.id;
    }

    public void setId(GameRound gameRound) {
        this.id = gameRound;
    }

    public GameSession id(GameRound gameRound) {
        this.setId(gameRound);
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
            ", userId=" + getUserId() +
            "}";
    }
}
