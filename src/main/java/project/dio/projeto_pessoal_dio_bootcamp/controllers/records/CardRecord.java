package project.dio.projeto_pessoal_dio_bootcamp.controllers.records;

import project.dio.projeto_pessoal_dio_bootcamp.models.Card;

import java.math.BigDecimal;

public record CardRecord(String number, BigDecimal limit) {

    public CardRecord(Card card){
        this(card.getNumber(),card.getLimit());
    }

    public Card toModel(){
        return new Card(
                null,
                this.number,
                this.limit
        );
    }

}
