package project.dio.projeto_pessoal_dio_bootcamp.services;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import project.dio.projeto_pessoal_dio_bootcamp.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User saveUser(User user){
        User u = userRepository.findByAccount_Number(user.getAccount().getNumber());
        if(u != null) throw new RuntimeException("Usuário já existe");
        user.getFeatures().forEach(feature -> feature.setUser(user));
        user.getNews().forEach(news -> news.setUser(user));
        return userRepository.save(user);
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuário não existe."));
    }
    

}
