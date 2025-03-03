package project.dio.projeto_pessoal_dio_bootcamp.exceptions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.*;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.requestExceptions.MissingAuthHeadingException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    //EXCEÇÕES DE NEGÓCIO
    @ExceptionHandler(exception = UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> userAlreadyExists(UserAlreadyExistsException e){
        log.info(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMessage message = new ErrorMessage(HttpStatus.CONFLICT.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(exception = UserNotExistsException.class)
    public ResponseEntity<ErrorMessage> userNotExists(UserNotExistsException e){
        log.error(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now().format(formatter), e.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(exception = BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> userNotExists(BadCredentialsException e){
        log.error(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMessage message = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> invalidArguments(MethodArgumentNotValidException e){
        log.error(e.getMessage(),e);
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .toList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now().format(formatter), String.join("; ",errors));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(exception = ExceededAccountLimitException.class)
    public ResponseEntity<ErrorMessage> exceededLimit(ExceededAccountLimitException e){
        log.error(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMessage message = new ErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(message);
    }

    @ExceptionHandler(exception = NotBalanceException.class)
    public ResponseEntity<ErrorMessage> notBalance(NotBalanceException e){
        log.error(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMessage message = new ErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(message);
    }

    @ExceptionHandler(exception = RecipientNotExistsException.class)
    public ResponseEntity<ErrorMessage> recipientNotExists(RecipientNotExistsException e){
        log.error(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    //EXCEÇÕES DE REQUISIÇÃO
    @ExceptionHandler(exception = MissingAuthHeadingException.class)
    public ResponseEntity<ErrorMessage> missingAuthHeaderException(MissingAuthHeadingException e){
        log.error(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMessage message = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    //GERAL
    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<ErrorMessage> unexpectedException(Exception e){
        log.error(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

}

