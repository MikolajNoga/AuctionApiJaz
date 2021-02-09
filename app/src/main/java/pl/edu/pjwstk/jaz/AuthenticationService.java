package pl.edu.pjwstk.jaz;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.pjwstk.jaz.services.UserService;

@Component
public class AuthenticationService {

    private final UserService userService;

    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public boolean login(String username, String password){
        boolean login = userService.login(username,password);
        if (login){
            var user = userService.findUserByUsername(username).get();
            SecurityContextHolder.getContext().setAuthentication(new AppAuthentication(user));
        }
        return login;
    }
}
