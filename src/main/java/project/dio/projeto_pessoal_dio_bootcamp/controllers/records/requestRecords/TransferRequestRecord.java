package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestRecord(
        @Positive(message = "the transfer amount must be greater than zero")
        BigDecimal value,
        @NotNull(message = "The recipient username no be null")
        @NotEmpty(message = "The recipient username not be empty")
        String recipientUsername
) { }
