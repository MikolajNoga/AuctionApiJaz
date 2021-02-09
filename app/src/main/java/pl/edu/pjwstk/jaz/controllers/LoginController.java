package pl.edu.pjwstk.jaz.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.services.UserService;
import pl.edu.pjwstk.jaz.exceptions.UnauthorizedException;
import pl.edu.pjwstk.jaz.AuthenticationService;
import pl.edu.pjwstk.jaz.requests.LoginRequest;
import pl.edu.pjwstk.jaz.UserSession;

@RestController
public class LoginController {
    private final AuthenticationService authenticationService;
    private final UserSession userSession;

    public LoginController(AuthenticationService authenticationService, UserSession userSession, UserService userService){
        this.authenticationService = authenticationService;
        this.userSession = userSession;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest){
        var isLogged = authenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (!isLogged) {
            throw new UnauthorizedException();
        }
        userSession.logIn();
    }
}
