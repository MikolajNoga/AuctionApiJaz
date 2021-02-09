package pl.edu.pjwstk.jaz;

import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("*")
public class ExampleFilter extends HttpFilter {
    private final UserSession userSession;

    public ExampleFilter(UserSession userSession) { this.userSession = userSession; }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isUserLogged() || isSiteAllowed(request))
            chain.doFilter(request, response);
        else
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private boolean isSiteAllowed(HttpServletRequest request){
        //return false;
        return request.getRequestURI().equals("/api/auth0/is-ready");
    }

    private boolean isUserLogged(){ return userSession.isLoggedIn();}
}
