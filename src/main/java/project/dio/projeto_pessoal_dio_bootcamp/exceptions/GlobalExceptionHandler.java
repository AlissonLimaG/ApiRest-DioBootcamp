package project.dio.projeto_pessoal_dio_bootcamp.exceptions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.UserAlreadyExistsException;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.UserNotExistsException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(exception = UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMesage> userAlreadyExists(UserAlreadyExistsException e){
        log.info(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMesage mesage = new ErrorMesage(HttpStatus.CONFLICT.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mesage);
    }

    @ExceptionHandler(exception = UserNotExistsException.class)
    public ResponseEntity<ErrorMesage> userNotExists(UserNotExistsException e){
        log.error(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMesage mesage = new ErrorMesage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mesage);
    }

    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<ErrorMesage> unexpectedException(Exception e){
        log.error(e.getMessage(),e);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ErrorMesage mesage = new ErrorMesage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now().format(formatter), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mesage);
    }


    @Getter
    @AllArgsConstructor
    public class ErrorMesage{
        private Integer status;
        private String time;
        private String mesage;
    }

}

