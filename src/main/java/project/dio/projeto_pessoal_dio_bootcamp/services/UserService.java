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
        if(u != null) throw new UserAlreadyExistsException("User already exists.");
        user.getFeatures().forEach(feature -> feature.setUser(user));
        user.getNews().forEach(news -> news.setUser(user));
        return userRepository.save(user);
    }



    @Transactional
    public User updateUser(String username, UpdateUserRecord updateUser){
        if(updateUser.username() != null && userRepository.existsByUsername(updateUser.username())){
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
