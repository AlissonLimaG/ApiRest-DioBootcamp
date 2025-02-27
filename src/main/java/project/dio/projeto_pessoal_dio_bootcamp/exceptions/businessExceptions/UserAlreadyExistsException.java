package project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String mesage){
        super(mesage);
    }

}
