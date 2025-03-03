package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords;

import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.TransferRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record TransferResponseRecord(String sender, BigDecimal transferBalance, BigDecimal balance, String recipient, String transferMoment) {
    public TransferResponseRecord(User userSender, TransferRequestRecord transferRequest){
        this(
                userSender.getName(),
                transferRequest.value(),
                userSender.getAccount().getBalance(),
                transferRequest.recipientUsername(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
        );
    }
}
