package project.dio.projeto_pessoal_dio_bootcamp.controllers.records;

import project.dio.projeto_pessoal_dio_bootcamp.models.Account;

import java.math.BigDecimal;

public record AccountRecord(String number, String agency, BigDecimal balance, BigDecimal limit) {

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
