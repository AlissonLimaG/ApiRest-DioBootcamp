package project.dio.projeto_pessoal_dio_bootcamp.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.ErrorMessage;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.GlobalExceptionHandler;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.UserNotExistsException;
import project.dio.projeto_pessoal_dio_bootcamp.repositories.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = getToken(request);
        if(token != null){
            String username = tokenService.validateToken(token);
            UserDetails user = userRepository.findByUsername(username);
            if(user == null) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(new ObjectMapper().writeValueAsString(
                        new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                                "User not Exists")
                ));
                return;
            }
            var authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }

    private String getToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
