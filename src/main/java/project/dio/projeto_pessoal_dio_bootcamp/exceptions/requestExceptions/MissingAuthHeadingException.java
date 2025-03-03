package project.dio.projeto_pessoal_dio_bootcamp.exceptions.requestExceptions;

public class MissingAuthHeadingException extends RuntimeException {
    public MissingAuthHeadingException(String message) {
        super(message);
    }
}
