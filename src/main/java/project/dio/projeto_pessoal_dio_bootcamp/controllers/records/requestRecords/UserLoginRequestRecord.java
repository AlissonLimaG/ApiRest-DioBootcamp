package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserLoginRequestRecord(
        @NotEmpty(message = "Username not be empty")
        @NotNull(message = "Username not be null")
        String username,
        @NotEmpty(message = "Password not be empty")
        @NotNull(message = "Password not be null")
        String password) {
}
