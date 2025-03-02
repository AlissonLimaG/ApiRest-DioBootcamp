package project.dio.projeto_pessoal_dio_bootcamp.controllers.records;

import project.dio.projeto_pessoal_dio_bootcamp.models.Card;

import java.math.BigDecimal;

public record CardRecord(Long id, String number, BigDecimal limit) {

    public CardRecord(Card card){
        this(card.getId(),card.getNumber(),card.getLimit());
    }

    public Card toModel(){
        return new Card(
                this.id,
                this.number,
                this.limit
        );
    }

}
