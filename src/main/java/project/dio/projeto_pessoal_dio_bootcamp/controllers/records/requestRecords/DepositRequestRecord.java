package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositRequestRecord(
        @NotNull
        @Positive(message = "the deposit amount must be greater than zero")
        BigDecimal value
) { }
