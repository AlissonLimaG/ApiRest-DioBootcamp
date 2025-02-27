package project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions;

public class UserNotExistsException extends RuntimeException{

    public UserNotExistsException(String mesage){
        super(mesage);
    }

}
