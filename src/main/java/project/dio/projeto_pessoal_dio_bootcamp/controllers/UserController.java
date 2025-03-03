package project.dio.projeto_pessoal_dio_bootcamp.controllers;
import java.net.URI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.UserRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.DepositRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.WithdrawalRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.TransferRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.DepositResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.WithdrawalResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.TransferResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.requestExceptions.MissingAuthHeadingException;
import project.dio.projeto_pessoal_dio_bootcamp.infra.security.TokenService;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.UserLoginRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.UserLoginResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.services.UserService;

@RestController
@Tag(name = "UsersController", description = "Controller to simulate the dynamics of using a user's bank account, such as: registration, login, deposit, withdrawals, etc.")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private TokenService tokenService;



    @PostMapping
    @Operation(summary = "Register a user", description = "Register a new user and return some of their data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully!"),
            @ApiResponse(responseCode = "409", description = "User already exists.")
    })
    public ResponseEntity<UserRecord> saveUser(@RequestBody UserRecord userRecord){
        User user = userRecord.toModel();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User userCreated = userService.saveUser(user);
        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(userCreated.getId())
                        .toUri();
        return ResponseEntity.created(location).body(new UserRecord(userCreated));
    }



    @Operation(summary = "User login", description = "Authenticates a user and returns their token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login sucessful."),
            @ApiResponse(responseCode = "401", description = "Invalid credentials.")
    })
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseRecord> login(@RequestBody UserLoginRequestRecord login){
            try{
                var usernamePassword = new UsernamePasswordAuthenticationToken(
                        login.username(),
                        login.password()
                );
                var authenticate = this.authenticationManager.authenticate(usernamePassword);
                String token = tokenService.generateToken((User) authenticate.getPrincipal());
                return ResponseEntity.ok(new UserLoginResponseRecord(login.username(),token));

            }catch (BadCredentialsException e){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
    }



    @Operation(summary = "Get a user by id", description = "Retrieve a specific user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "User not exists.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.findById(id));
    }



    @Operation(summary = "Deposit money into the account", description = "Deposits an amount into a specific user's account and returns informations of deposit and the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "403", description = "Missing authorization header."),
            @ApiResponse(responseCode = "422", description = "Account limit exceeded"),
            @ApiResponse(responseCode = "400", description = "User not exists.")
    })
    @PostMapping("/deposit")
    public ResponseEntity<DepositResponseRecord> accountDeposit(@RequestBody @Valid DepositRequestRecord depositRequest, HttpServletRequest request){
        String username = this.validationRequest(request);

        User user = userService.depositAccount(username, depositRequest.value());
        return ResponseEntity.ok(new DepositResponseRecord(user));
    }



    @Operation(summary = "Sake money out the account", description = "Sake an amount into a specific user's account and returns informations of sake and the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "403", description = "Missing authorization header."),
            @ApiResponse(responseCode = "422", description = "Not balance in account"),
            @ApiResponse(responseCode = "400", description = "User not exists.")
    })
    @PostMapping("/withdrawal")
    public ResponseEntity<WithdrawalResponseRecord> accountWithdrawal(@RequestBody @Valid WithdrawalRequestRecord withdrawalRequest, HttpServletRequest request){
        String username = this.validationRequest(request);

        User user = userService.withdrawalAccount(username, withdrawalRequest.value());
        return ResponseEntity.ok(new WithdrawalResponseRecord(user, withdrawalRequest));
    }



    @Operation(summary = "Transfer money", description = "Transfer money for user especified for username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful."),
            @ApiResponse(responseCode = "403", description = "Missing authorization header."),
            @ApiResponse(responseCode = "422", description = "Not balance in sender account."),
            @ApiResponse(responseCode = "422", description = "Not limit in recipient account."),
            @ApiResponse(responseCode = "400", description = "Sender not exists."),
            @ApiResponse(responseCode = "400", description = "Recipient not exists.")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseRecord> transfer(@RequestBody @Valid TransferRequestRecord transferRequest, HttpServletRequest request){
        String username = this.validationRequest(request);

        User userSender = userService.transferAccount(username,transferRequest);
        return ResponseEntity.ok(new TransferResponseRecord(userSender,transferRequest));
    }



    // Metodo para validar o cabeçalho da requisição, checar se o token existe e se o username existe.
    public String validationRequest(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) throw new MissingAuthHeadingException("Missing authorization header.");

        String username = tokenService.validateToken(authHeader.replaceAll("Bearer ", ""));
        if(username == null) ResponseEntity.badRequest().build();

        return username;
    }
}
