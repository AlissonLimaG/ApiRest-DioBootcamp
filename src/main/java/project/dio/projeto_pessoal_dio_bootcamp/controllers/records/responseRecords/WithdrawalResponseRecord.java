package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords;

import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.WithdrawalRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record WithdrawalResponseRecord(String name, BigDecimal amountWithdrawn, BigDecimal balance, BigDecimal remainingLimit, String sakeMoment) {

    public WithdrawalResponseRecord(User user, WithdrawalRequestRecord sakeRequest){
        this(
                user.getName(),
                sakeRequest.value(),
                user.getAccount().getBalance(),
                user.getAccount().getLimit().subtract(user.getAccount().getBalance()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
        );
    }
}
