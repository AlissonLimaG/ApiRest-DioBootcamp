package project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions;

public class ExceededAccountLimitException extends RuntimeException {
    public ExceededAccountLimitException(String message) {
        super(message);
    }
}
