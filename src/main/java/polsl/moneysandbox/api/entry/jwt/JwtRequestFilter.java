package polsl.moneysandbox.api.entry.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import polsl.moneysandbox.api.entry.service.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtTokenUtility jwtTokenUtility;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtility.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException | ExpiredJwtException e) {
                e.printStackTrace();
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = this.userDetailsService.loadUserByUsername(username);
            if (Boolean.TRUE.equals(jwtTokenUtility.validateToken(jwtToken, userDetails))) {
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
