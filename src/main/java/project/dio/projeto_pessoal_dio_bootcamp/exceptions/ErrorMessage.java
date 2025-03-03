package project.dio.projeto_pessoal_dio_bootcamp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage{
    private Integer status;
    private String time;
    private String message;
}