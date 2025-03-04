package project.dio.projeto_pessoal_dio_bootcamp.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.UpdateUserRecord;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.UserAlreadyExistsException;
import project.dio.projeto_pessoal_dio_bootcamp.models.Account;
import project.dio.projeto_pessoal_dio_bootcamp.models.Features;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import project.dio.projeto_pessoal_dio_bootcamp.models.enums.UserRole;
import project.dio.projeto_pessoal_dio_bootcamp.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @InjectMocks
    private UserService userService;


    @Nested
    class createUser {

        @Test
        @DisplayName("Cadastra usuário com sucesso")
        void deveCriarUsuarioComSucesso() {
            //arrange
            Account account = new Account(
                    null,
                    "1234567890",
                    "0001",
                    BigDecimal.valueOf(100L),
                    BigDecimal.valueOf(1000L));
            User user = new User(
                    null,
                    UserRole.ROLE_USER,
                    "Alisson",
                    "AlissonLima",
                    encoder.encode("123456789"),
                    account,
                    null,
                    new ArrayList<>(),
                    new ArrayList<>());

            Mockito.doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            //act
            User output = userService.saveUser(user);

            //assert
            var capturedData = userArgumentCaptor.getValue();
            assertNotNull(output);
            assertEquals(user.getName(), capturedData.getName());
            assertEquals(user.getUsername(), capturedData.getUsername());
            assertEquals(user.getPassword(), capturedData.getPassword());
            assertEquals(user.getAccount(), capturedData.getAccount());
            assertEquals(user.getCard(), capturedData.getCard());
            assertEquals(user.getAccount(), capturedData.getAccount());
            assertEquals(user.getNews(), capturedData.getNews());
            assertEquals(user.getFeatures(), capturedData.getFeatures());
        }

        @Test
        @DisplayName("Tem que lançar uma exceção UserAlreadyExists ao tentar cadastrar um username existente")
        void deveLancarExcecao() {
            //ARRANGE
            User user = new User(
                    null,
                    UserRole.ROLE_USER,
                    "Alisson",
                    "AlissonLima",
                    encoder.encode("123456789"),
                    new Account(null, "1234567890", "0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>());
            Mockito.doReturn(user).when(userRepository).findByUsername("AlissonLima");

            //Act-assert
            assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(user));
        }
    }

    @Nested
    class updateUser{
        @Test
        @DisplayName("Deve atualizar usuário com sucesso")
        void atualizaComSucesso(){
            // ARRANGE
            User user = new User(
                    null,
                    UserRole.ROLE_USER,
                    "Alisson",
                    "AlissonLima",
                    encoder.encode("123456789"),
                    new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>());

            String username = "AlissonLima";
            UpdateUserRecord updateUser = new UpdateUserRecord("Alisson", "Alisson", "12345678910");

            Mockito.doReturn(false).when(userRepository).existsByUsername(updateUser.username());
            Mockito.doReturn(user).when(userRepository).findByUsername(username);
            Mockito.doReturn(user).when(userRepository).save(user);

            // ACT
            User output = userService.updateUser(username, updateUser);

            // ASSERT
            assertNotNull(output);
            assertEquals("Alisson", output.getName());
            assertEquals("Alisson", output.getUsername());
            assertEquals(encoder.encode("12345678910"), output.getPassword());
        }

        @Test
        @DisplayName("Não deixa atualizar por que já existe um username.")
        void levantaExcecao(){
            User user = new User(
                    null,
                    UserRole.ROLE_USER,
                    "Alisson",
                    "AlissonLima",
                    encoder.encode("123456789"),
                    new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>());

            String username = "AlissonLima";
            UpdateUserRecord updateUser = new UpdateUserRecord("Alisson", "Alisson", "12345678910");

            Mockito.doReturn(true).when(userRepository).existsByUsername(updateUser.username());

            // ACT - ASSERT
            assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(username,updateUser));

        }


    }

    @Nested
    class deleteUser{

        @Test
        @DisplayName("Deve deletar um usuário com sucesso")
        void deletaComSucesso(){
            //ARRANGE
            User user = new User(
                    null,
                    UserRole.ROLE_USER,
                    "Alisson",
                    "AlissonLima",
                    encoder.encode("123456789"),
                    new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>());
            String username = "Alisson";

            Mockito.doReturn(user).when(userRepository).findByUsername(username);
            Mockito.doNothing().when(userRepository).delete(user);

            //ACT
            userService.deleteUser(username);

            //ASSERT
            Mockito.verify(userRepository, Mockito.times(1)).delete(user);
        }
    }
}