package pl.edu.pjwstk.jaz;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pl.edu.pjwstk.jaz.entities.UserEntity;
import pl.edu.pjwstk.jaz.services.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AppAuthentication extends AbstractAuthenticationToken {

    private final UserEntity authenticatedUser;

    public AppAuthentication(UserEntity authenticatedUser) {
        super(toGrantedAuthorities(new HashSet<>(Collections.singleton(authenticatedUser.getRole()))));
        this.authenticatedUser = authenticatedUser;
        setAuthenticated(true);
    }

    private static Collection<? extends GrantedAuthority> toGrantedAuthorities(Set<String> authorities){
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public Object getCredentials() { return authenticatedUser.getPassword(); }

    @Override
    public Object getPrincipal() { return authenticatedUser;}
}
