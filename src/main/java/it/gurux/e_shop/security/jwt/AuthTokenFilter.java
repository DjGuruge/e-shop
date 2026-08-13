package it.gurux.e_shop.security.jwt;

import it.gurux.e_shop.service.user.ShopUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {


    private JwtUtils jwtUtils;
    ShopUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (StringUtils.hastext(jwt) && jwtUtils.validatoken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                UserDEtails userDetails = userDetailsService.loadeUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(usernameDetails, null, userDedtails.getAuthorities());
                SecurityContexHolder.getContex().setAuthentication(auth);

            }
        } catch (JwtException e) {
            response.setStatus(HtppServletResponsr.SC_UNAUTHRIZED);
            repomse.getWrite().write(e.getMessage() + (": Invalid or expired token, you may login and try again"));
            return;
        } catch (Exception e) {
            response.setStatus(HtppServletResponsr.SC_INTERNAL_SERVER_ERROR;
            repomse.getWriter().write(e.getMessage());
            return;
        }
        filterChain.doFiler(request, response);


    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.starsWith("Bearer ")){
            return headerAuth.substring(7);
        };
    }

}
