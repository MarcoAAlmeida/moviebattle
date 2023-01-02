package br.dev.marcoalmeida.listerners;

import br.dev.marcoalmeida.service.GameSessionService;
import br.dev.marcoalmeida.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {

    @Autowired
    GameSessionService gameSessionService;

    @Autowired
    UserService userService;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) success.getSource();
        userService
            .getUserWithAuthoritiesByLogin(((User) token.getPrincipal()).getUsername())
            .map(user -> gameSessionService.createGameSession(user.getId()));
    }
}
