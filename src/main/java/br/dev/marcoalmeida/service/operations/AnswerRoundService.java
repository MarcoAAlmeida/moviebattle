package br.dev.marcoalmeida.service.operations;

import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.domain.enumeration.Choice;
import br.dev.marcoalmeida.service.GameRoundService;
import br.dev.marcoalmeida.service.api.dto.AnsweredGameRoundDTO;
import br.dev.marcoalmeida.service.mapper.AnsweredGameRoundMapper;
import br.dev.marcoalmeida.web.api.AnswerRoundApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnswerRoundService implements AnswerRoundApiDelegate {

    @Autowired
    GameRoundService gameRoundService;

    @Autowired
    AnsweredGameRoundMapper answeredGameRoundMapper;

    @Override
    public ResponseEntity<AnsweredGameRoundDTO> answerRound(Integer gameRoundId, String userChoice) {
        return gameRoundService
            .findOne(gameRoundId.longValue())
            .map(gameRound -> registerAnswer(gameRound, userChoice))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private ResponseEntity<AnsweredGameRoundDTO> registerAnswer(GameRound gameRound, String choice) {
        gameRound.userChoice(Choice.valueOf(choice));
        gameRound.setCorrect(true);

        return ResponseEntity.ok(answeredGameRoundMapper.answeredGameRoundDTO(gameRoundService.update(gameRound)));
    }
}
