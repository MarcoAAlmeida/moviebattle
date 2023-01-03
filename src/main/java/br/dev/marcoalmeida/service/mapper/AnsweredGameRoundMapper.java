package br.dev.marcoalmeida.service.mapper;

import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.service.api.dto.AnsweredGameRoundDTO;
import br.dev.marcoalmeida.service.api.dto.GameSessionDTO;
import org.springframework.stereotype.Service;

@Service
public class AnsweredGameRoundMapper {

    public AnsweredGameRoundDTO answeredGameRoundDTO(GameRound gameRound) {
        return new AnsweredGameRoundDTO()
            .id(gameRound.getId().intValue())
            .left(gameRound.getLeft().getTitle())
            .right(gameRound.getRight().getTitle())
            .correct(gameRound.getCorrect())
            .userChoice(gameRound.getUserChoice().toString());
    }
}
