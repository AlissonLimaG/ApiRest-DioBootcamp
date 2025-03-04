package project.dio.projeto_pessoal_dio_bootcamp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.dio.projeto_pessoal_dio_bootcamp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByUsername(String username);
    Boolean existsByUsername(String username);
    
}
