package project.dio.projeto_pessoal_dio_bootcamp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import project.dio.projeto_pessoal_dio_bootcamp.models.User;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByAccount(String account);
    
}
