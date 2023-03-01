package ru.relex.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.relex.config.CustomHttpHeader;
import ru.relex.exception.JwtExpiredException;
import ru.relex.exception.TokenIsNotValidException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.relex.jwt.JwtTokenProvider;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

            if (token != null) {
                jwtTokenProvider.validateToken(token);

                Authentication auth = jwtTokenProvider.getAuth(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (TokenIsNotValidException e) {
            handlerExceptionResolver.resolveException(
                    (HttpServletRequest) servletRequest,
                    (HttpServletResponse) servletResponse,
                    null,
                    e
            );
            throw new TokenIsNotValidException(e.getMessage());
        } catch (JwtExpiredException e) {
            handlerExceptionResolver.resolveException(
                    (HttpServletRequest) servletRequest,
                    (HttpServletResponse) servletResponse,
                    null,
                    e
            );
            throw new JwtExpiredException(e.getMessage());
        }
    }
}
