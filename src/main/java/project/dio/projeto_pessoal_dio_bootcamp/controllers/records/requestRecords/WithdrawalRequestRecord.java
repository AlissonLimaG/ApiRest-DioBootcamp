package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WithdrawalRequestRecord(
        @Positive(message = "the withdrawal amount must be greater than zero")
        BigDecimal value
) { }
