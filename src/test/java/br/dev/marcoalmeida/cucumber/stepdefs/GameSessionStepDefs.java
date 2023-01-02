package br.dev.marcoalmeida.cucumber.stepdefs;

import br.dev.marcoalmeida.security.AuthoritiesConstants;
import br.dev.marcoalmeida.web.rest.UserResource;
import io.cucumber.java.Before;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class GameSessionStepDefs extends StepDefs {

    @Autowired
    private UserResource userResource;

    private MockMvc userResourceMock;

    @Before
    public void setup() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
        User principal = new User("username", "", true, true, true, true, grantedAuthorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            principal,
            principal.getPassword(),
            principal.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        this.userResourceMock = MockMvcBuilders.standaloneSetup(userResource).build();
    }
}
