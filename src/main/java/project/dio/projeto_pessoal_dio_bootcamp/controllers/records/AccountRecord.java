package project.dio.projeto_pessoal_dio_bootcamp.controllers.records;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import project.dio.projeto_pessoal_dio_bootcamp.models.Account;

import java.math.BigDecimal;

public record AccountRecord(
        @Size(max = 13, min = 10, message = "Account number must be between 10 and 13 characters")
        String number,
        @Size(max = 4, min = 3, message = "Agency number must be between 3 and 4 characters")
        String agency,
        BigDecimal balance,
        @Positive(message = "The limit must be greater than zero")
        BigDecimal limit) {

    public AccountRecord(Account account){
        this(account.getNumber(),account.getAgency(),account.getBalance(),account.getLimit());
    }

    public Account toModel(){
        return new Account(
                null,
                this.number,
                this.agency,
                this.balance,
                this.limit
        );
    }

}
