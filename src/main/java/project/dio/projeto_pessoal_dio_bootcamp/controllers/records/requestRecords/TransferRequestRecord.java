package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestRecord(
        @Positive(message = "the transfer amount must be greater than zero")
        BigDecimal value,
        String recipientUsername
) { }
