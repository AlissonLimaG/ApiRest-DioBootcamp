package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords;

import jakarta.validation.constraints.NotNull;

public record UpdateUserRecord(
        String name,
        String username ,
        String password) {
}
