package br.dev.marcoalmeida.service.dto;

import br.dev.marcoalmeida.domain.Movie;
import br.dev.marcoalmeida.domain.User;
import br.dev.marcoalmeida.service.api.dto.GameSessionDTO;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * A DTO representing a movie pair
 * [ A - A ] is invalid
 * [ A - B ] equals [B - A].
 */
public class MoviePairDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Movie left;
    private Movie right;

    public MoviePairDTO(Movie left, Movie right) throws InvalidParameterException {
        if (left.equals(right)) throw new InvalidParameterException(String.format("[ A - A ] is and invalid pair %s - %s", left, right));

        this.left = left;
        this.right = right;
    }

    public Movie getLeft() {
        return left;
    }

    public void setLeft(Movie left) {
        this.left = left;
    }

    public Movie getRight() {
        return right;
    }

    public void setRight(Movie right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoviePairDTO moviePairDTO = (MoviePairDTO) o;
        return (
            Objects.equals(this.left, moviePairDTO.left) &&
            Objects.equals(this.right, moviePairDTO.right) ||
            (Objects.equals(this.left, moviePairDTO.right) && Objects.equals(this.right, moviePairDTO.left))
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoviePairDTO{" +
            "left='" + left + '\'' +
            ", right='" + right + '\'' +
            "}";
    }
}
