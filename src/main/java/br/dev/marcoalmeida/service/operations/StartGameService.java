package br.dev.marcoalmeida.service.operations;

import br.dev.marcoalmeida.service.GameSessionService;
import br.dev.marcoalmeida.service.UserService;
import br.dev.marcoalmeida.service.api.dto.GameSessionDTO;
import br.dev.marcoalmeida.service.mapper.GameSessionMapper;
import br.dev.marcoalmeida.web.api.StartGameApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    GameSessionMapper gameSessionMapper;

    @Override
    public ResponseEntity<GameSessionDTO> startGame() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(gameSessionMapper.gameSessionDTO(gameSessionService.createGameSession(authentication.getName())));
    }
}
