package project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record DepositResponseRecord(String name, BigDecimal balance, BigDecimal avaliableLimit, String depositMoment) {
    public DepositResponseRecord(User user){
        this(
                user.getName(),
                user.getAccount().getBalance(),
                user.getAccount().getLimit().subtract(user.getAccount().getBalance()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
    }
}
