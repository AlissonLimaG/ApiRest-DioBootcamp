package project.dio.projeto_pessoal_dio_bootcamp.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.TransferRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.ExceededAccountLimitException;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.NotBalanceException;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.RecipientNotExistsException;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import project.dio.projeto_pessoal_dio_bootcamp.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @Transactional
    public User depositAccount(String username, BigDecimal value){
        User user = (User) userService.loadUserByUsername(username);
        BigDecimal oldBalance = user.getAccount().getBalance();
        BigDecimal limit = user.getAccount().getLimit();

        if(oldBalance.add(value).compareTo(limit) > 0) {
            BigDecimal remainingLimit = limit.subtract(oldBalance);
            throw new ExceededAccountLimitException("Account limit exceeded, your limit is: $" + remainingLimit);
        }

        user.getAccount().setBalance(oldBalance.add(value));
        return userRepository.save(user);
    }



    @Transactional
    public User withdrawalAccount(String username, BigDecimal value){
        User user = (User) userService.loadUserByUsername(username);
        BigDecimal balance = user.getAccount().getBalance();
        if(balance.subtract(value).compareTo(BigDecimal.ZERO) < 0){
            throw new NotBalanceException("Insufficient balance");
        }

        user.getAccount().setBalance(balance.subtract(value));
        return userRepository.save(user);
    }



    @Transactional
    public User transferAccount(String username, TransferRequestRecord transferData){
        User userSender = (User) userService.loadUserByUsername(username);
        User userRecipient = (User) userService.loadUserByUsername(transferData.recipientUsername());
        if(userRecipient == null) throw new RecipientNotExistsException("Invalid recipient data.");

        BigDecimal senderBalance = userSender.getAccount().getBalance();
        if(senderBalance.subtract(transferData.value()).compareTo(BigDecimal.ZERO) < 0){
            throw new NotBalanceException("insufficient balance to transfer");
        }

        BigDecimal recipientLimit = userRecipient.getAccount().getLimit();
        BigDecimal recipientBalance = userRecipient.getAccount().getBalance();
        if(recipientBalance.add(transferData.value()).compareTo(recipientLimit) > 0){
            throw new ExceededAccountLimitException("The recipient has no available limit");
        }

        userSender.getAccount().setBalance(senderBalance.subtract(transferData.value()));
        userRecipient.getAccount().setBalance(recipientBalance.add(transferData.value()));
        userRepository.save(userSender);
        userRepository.save(userRecipient);
        return userSender;
    }


}
