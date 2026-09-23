package kata.tkachev.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        redirectStrategy.sendRedirect(request, response, admin ? "/admin" : "/user");
    }
}
