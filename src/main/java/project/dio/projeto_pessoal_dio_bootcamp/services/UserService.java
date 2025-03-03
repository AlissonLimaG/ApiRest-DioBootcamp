package project.dio.projeto_pessoal_dio_bootcamp.services;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.TransferRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.UpdateUserRecord;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.*;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import project.dio.projeto_pessoal_dio_bootcamp.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;


    public User saveUser(User user){
        User u = userRepository.findByUsername(user.getUsername());
        if(u != null) throw new RuntimeException("User already exists.");
        user.getFeatures().forEach(feature -> feature.setUser(user));
        user.getNews().forEach(news -> news.setUser(user));
        return userRepository.save(user);
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new UserNotExistsException("User not found."));
    }



    @Transactional
    public User depositAccount(String username, BigDecimal value){
        User user = (User) loadUserByUsername(username);
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
        User user = (User) loadUserByUsername(username);
        BigDecimal balance = user.getAccount().getBalance();
        if(balance.subtract(value).compareTo(BigDecimal.ZERO) < 0){
            throw new NotBalanceException("insufficient balance");
        }

        user.getAccount().setBalance(balance.subtract(value));
        return userRepository.save(user);
    }



    @Transactional
    public User transferAccount(String username, TransferRequestRecord transferData){
        User userSender = (User) loadUserByUsername(username);
        User userRecipient = (User) loadUserByUsername(transferData.recipientUsername());
        if(userRecipient == null) throw new RecipientNotExistsException("Invalid recipient data.");

        BigDecimal senderBalance = userSender.getAccount().getBalance();
        if(senderBalance.subtract(transferData.value()).compareTo(BigDecimal.ZERO) < 0){
            throw new NotBalanceException("insufficient balance to transfer");
        }

        BigDecimal recipientLimit = userRecipient.getAccount().getLimit();
        BigDecimal recipientBalance = userRecipient.getAccount().getBalance();
        if(recipientBalance.add(transferData.value()).compareTo(recipientLimit) > 0){
            throw new ExceededAccountLimitException("the recipient has no available limit");
        }

        userSender.getAccount().setBalance(senderBalance.subtract(transferData.value()));
        userRecipient.getAccount().setBalance(recipientBalance.add(transferData.value()));
        userRepository.saveAll(List.of(userSender,userRecipient));
        return userSender;
    }


    @Transactional
    public User updateUser(String username, UpdateUserRecord updateUser){
        if(updateUser.username() != null && userRepository.findByUsername(updateUser.username()) != null){
            throw new UserAlreadyExistsException("Username already exists");
        }
        User user = (User) loadUserByUsername(username);
        user.setUsername(updateUser.username() != null ?updateUser.username() :user.getUsername());
        user.setName(updateUser.name() != null ?updateUser.name() :user.getName());
        user.setPassword(updateUser.password() != null ?encoder.encode(updateUser.password()) : user.getPassword());
        return userRepository.save(user);
    }


    @Transactional
    public void deleteUser(String username){
        User user = (User) loadUserByUsername(username);
        userRepository.delete(user);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("Username not exists.");
        return user;
    }
}
