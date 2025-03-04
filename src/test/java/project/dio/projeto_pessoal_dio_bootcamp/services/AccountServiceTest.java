package project.dio.projeto_pessoal_dio_bootcamp.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.TransferRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.ExceededAccountLimitException;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.NotBalanceException;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.businessExceptions.RecipientNotExistsException;
import project.dio.projeto_pessoal_dio_bootcamp.models.Account;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import project.dio.projeto_pessoal_dio_bootcamp.models.enums.UserRole;
import project.dio.projeto_pessoal_dio_bootcamp.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;
    @InjectMocks
    private AccountService accountService;

    @Nested
    class depositAccount{
        
        @Test
        @DisplayName("Deve depositar com sucesso.")
        void depositaComSucesso(){
            //ARRANGE
            BigDecimal valueDeposit = BigDecimal.valueOf(100L);
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

            User userOutPut = new User(
                    null,
                    UserRole.ROLE_USER,
                    "Alisson",
                    "AlissonLima",
                    encoder.encode("123456789"),
                    new Account(null,"1234567890","0001", BigDecimal.valueOf(100L).add(valueDeposit), BigDecimal.valueOf(1000L)),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>());

            Mockito.doReturn(user).when(userService).loadUserByUsername(user.getUsername());
            Mockito.doReturn(userOutPut).when(userRepository).save(userOutPut);

            //ACT
            User output = accountService.depositAccount(user.getUsername(), valueDeposit);

            //ASSERT
            assertNotNull(output);
            assertEquals(user.getAccount().getBalance(), output.getAccount().getBalance());
        }

        @Test
        @DisplayName("Não deixa depositar por causa do limite insuficiente e levanta exceção")
        void naoDepositaPorCausaDoLimite(){
            //ARRANGE
            User user = new User(
                    null,
                    UserRole.ROLE_USER,
                    "Alisson",
                    "AlissonLima",
                    encoder.encode("123456789"),
                    new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(200L)),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>());
            BigDecimal valueToDeposit = BigDecimal.valueOf(150L);
            Mockito.doReturn(user).when(userService).loadUserByUsername(user.getUsername());

            //ASSERT-ACT
            assertThrows(ExceededAccountLimitException.class, ()-> accountService.depositAccount(user.getUsername(), valueToDeposit));
        }
    }

    @Nested
    class withdrawalAccount{

        @Test
        @DisplayName("Deve sacar com sucesso")
        void sacaComSucesso(){
            //ARRANGE
            BigDecimal withdrawal = BigDecimal.valueOf(50L);
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
            User userOutPut = new User(
                    null,
                    UserRole.ROLE_USER,
                    "Alisson",
                    "AlissonLima",
                    encoder.encode("123456789"),
                    new Account(null,"1234567890","0001", BigDecimal.valueOf(50L), BigDecimal.valueOf(1000L)),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>());
            Mockito.doReturn(user).when(userService).loadUserByUsername(user.getUsername());
            Mockito.doReturn(userOutPut).when(userRepository).save(user);

            //ACT
            User output = accountService.withdrawalAccount(user.getUsername(), withdrawal);

            //ASSERT
            assertNotNull(output);
            assertEquals(user.getAccount().getBalance(), output.getAccount().getBalance());
        }

        @Test
        @DisplayName("Saldo insuficiente! Deve levantar exceção")
        void naoSaca(){
            //ARRANGE
            BigDecimal withdrawal = BigDecimal.valueOf(150L);
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

            Mockito.doReturn(user).when(userService).loadUserByUsername(user.getUsername());

            //ACT-ASSERT
            assertThrows(NotBalanceException.class, () -> accountService.withdrawalAccount(user.getUsername(),withdrawal));
            Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
        }
    }

    @Nested
    class transferAccount{

        @Test
        @DisplayName("Transfere com sucesso")
        void transfereComSucesso(){
            //ARRANGE
            User sender = new User(null, UserRole.ROLE_USER, "Alisson", "AlissonLima",
                    encoder.encode("123456789"), new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null, new ArrayList<>(), new ArrayList<>());

            User recipient = new User(null, UserRole.ROLE_USER, "Alisson", "LuanaMarques",
                    encoder.encode("123456789"), new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null, new ArrayList<>(), new ArrayList<>());

            BigDecimal transferValue = BigDecimal.valueOf(50L);
            TransferRequestRecord transferRecord = new TransferRequestRecord(transferValue,recipient.getUsername());

            Mockito.doReturn(sender).when(userService).loadUserByUsername(sender.getUsername());
            Mockito.doReturn(recipient).when(userService).loadUserByUsername(recipient.getUsername());
            Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArgument(0));

            //ACT
            User senderOutput = accountService.transferAccount(sender.getUsername(), transferRecord);

            //ASSERT
            assertNotNull(senderOutput);
            assertEquals(BigDecimal.valueOf(50L), senderOutput.getAccount().getBalance());
            assertEquals(BigDecimal.valueOf(150L), recipient.getAccount().getBalance());
            Mockito.verify(userRepository, Mockito.times(2)).save(Mockito.any(User.class));

        }

        @Test
        @DisplayName("Destinatário não encontrado, deve levantar exceção")
        void destinatarioNaoEncontradoLevantaExcecao(){
            //ARRANGE
            User sender = new User(null, UserRole.ROLE_USER, "Alisson", "AlissonLima",
                    encoder.encode("123456789"), new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null, new ArrayList<>(), new ArrayList<>());
            BigDecimal transferValue = BigDecimal.valueOf(50L);
            TransferRequestRecord transferRecord = new TransferRequestRecord(transferValue,"Desconhecido");

            Mockito.doReturn(sender).when(userService).loadUserByUsername(sender.getUsername());
            Mockito.doReturn(null).when(userService).loadUserByUsername(transferRecord.recipientUsername());

            //ACT-ASSERT
            assertThrows(RecipientNotExistsException.class,
                    () -> accountService.transferAccount(sender.getUsername(), transferRecord));
            Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
        }

        @Test
        @DisplayName("Remetente sem saldo, deve levantar exceção")
        void remetenteSemSaldoLevantaExcecao(){
            //ARRANGE
            User sender = new User(null, UserRole.ROLE_USER, "Alisson", "AlissonLima",
                    encoder.encode("123456789"), new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null, new ArrayList<>(), new ArrayList<>());

            User recipient = new User(null, UserRole.ROLE_USER, "Alisson", "LuanaMarques",
                    encoder.encode("123456789"), new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null, new ArrayList<>(), new ArrayList<>());
            BigDecimal transferValue = BigDecimal.valueOf(150L);
            TransferRequestRecord transferRecord = new TransferRequestRecord(transferValue,recipient.getUsername());

            Mockito.doReturn(sender).when(userService).loadUserByUsername(sender.getUsername());
            Mockito.doReturn(recipient).when(userService).loadUserByUsername(recipient.getUsername());

            //ACT-ASSERT
            assertThrows(NotBalanceException.class, () -> accountService.transferAccount(sender.getUsername(), transferRecord));
            Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
        }

        @Test
        @DisplayName("Destinatário sem limite, deve levantar exceção")
        void destinatarioSemLimiteLevantaExcecao(){
            //ARRANGE
            User sender = new User(null, UserRole.ROLE_USER, "Alisson", "AlissonLima",
                    encoder.encode("123456789"), new Account(null,"1234567890","0001", BigDecimal.valueOf(1000L), BigDecimal.valueOf(1000L)),
                    null, new ArrayList<>(), new ArrayList<>());

            User recipient = new User(null, UserRole.ROLE_USER, "Alisson", "LuanaMarques",
                    encoder.encode("123456789"), new Account(null,"1234567890","0001", BigDecimal.valueOf(100L), BigDecimal.valueOf(1000L)),
                    null, new ArrayList<>(), new ArrayList<>());
            BigDecimal transferValue = BigDecimal.valueOf(1000L);
            TransferRequestRecord transferRecord = new TransferRequestRecord(transferValue,recipient.getUsername());

            Mockito.doReturn(sender).when(userService).loadUserByUsername(sender.getUsername());
            Mockito.doReturn(recipient).when(userService).loadUserByUsername(recipient.getUsername());

            //ACT-ASSERT
            assertThrows(ExceededAccountLimitException.class, () -> accountService.transferAccount(sender.getUsername(), transferRecord));
            Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
        }
    }

}