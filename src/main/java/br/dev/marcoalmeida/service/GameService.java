package br.dev.marcoalmeida.service;

import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.domain.User;
import br.dev.marcoalmeida.service.api.dto.GameSessionDTO;
import br.dev.marcoalmeida.web.api.StartGameApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GameService implements StartGameApiDelegate {

    @Autowired
    GameSessionService gameSessionService;

    @Autowired
    UserService userService;

    public ResponseEntity<GameSessionDTO> startGame() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userService
            .getUserWithAuthoritiesByLogin(authentication.getName())
            .map(this::creteNewGameSession)
            .orElse(ResponseEntity.badRequest().build());
    }

    private ResponseEntity<GameSessionDTO> creteNewGameSession(User user) {
        GameSession gameSession = new GameSession();
        gameSession.setUserId(user.getId());

        return new ResponseEntity<>(gameSessionService.save(gameSession).toDTO(), HttpStatus.OK);
    }
}
