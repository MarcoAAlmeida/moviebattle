package br.dev.marcoalmeida.service.operations;

import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.domain.User;
import br.dev.marcoalmeida.service.GameSessionService;
import br.dev.marcoalmeida.service.UserService;
import br.dev.marcoalmeida.service.api.dto.GameSessionDTO;
import br.dev.marcoalmeida.web.api.StartGameApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StartGameService implements StartGameApiDelegate {

    @Autowired
    GameSessionService gameSessionService;

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<GameSessionDTO> startGame() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userService
            .getUserWithAuthoritiesByLogin(authentication.getName())
            .map(user -> new ResponseEntity<>(gameSessionService.createGameSession(user.getId()).toDTO(), HttpStatus.OK))
            .orElse(ResponseEntity.badRequest().build());
    }
}
