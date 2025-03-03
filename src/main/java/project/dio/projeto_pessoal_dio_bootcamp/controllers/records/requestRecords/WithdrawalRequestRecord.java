package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WithdrawalRequestRecord(
        @NotNull(message = "The withdrawal amount not be null")
        @Positive(message = "The withdrawal amount must be greater than zero")
        BigDecimal value
) { }
