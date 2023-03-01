package ru.relex.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.relex.config.CustomHttpHeader;

import java.io.IOException;

public class FormatResponseFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getHeader("accept").equals("application/xml")) {
            CustomHttpHeader.setHttpHeaders(MediaType.APPLICATION_XML);
        }
        if (request.getHeader("accept").equals("application/json")) {
            CustomHttpHeader.setHttpHeaders(MediaType.APPLICATION_JSON);
        }

        filterChain.doFilter(request, response);
    }
}
