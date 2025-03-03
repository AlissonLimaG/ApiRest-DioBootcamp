package project.dio.projeto_pessoal_dio_bootcamp.controllers;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;

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
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.requestRecords.*;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.DepositResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.WithdrawalResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.TransferResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.exceptions.requestExceptions.MissingAuthHeadingException;
import project.dio.projeto_pessoal_dio_bootcamp.infra.security.TokenService;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.responseRecords.UserLoginResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.services.UserService;

@RestController
@Tag(name = "UsersController", description = "Controller to simulate the dynamics of using a user's bank account, such as: registration, login, updating and deletion of user data")
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
    public ResponseEntity<UserRecord> saveUser(@RequestBody @Valid UserRecord userRecord){
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
    public ResponseEntity<UserLoginResponseRecord> login(@RequestBody @Valid UserLoginRequestRecord login){
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



    @Operation(summary = "Get a current user", description = "Retrieve a current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "User not exists.")
    })
    @GetMapping()
    public ResponseEntity<UserRecord> findUser(HttpServletRequest request){
        String username = this.validationRequest(request);
        User user = (User) userService.loadUserByUsername(username);
        user.setPassword("");
        UserRecord userResponse = new UserRecord(user);

        return ResponseEntity.ok().body(userResponse);
    }



    @Operation(summary = "Update a current user", description = "Update a current user and return new data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "User not exists.")
    })
    @PutMapping()
    public ResponseEntity<UserRecord> updateUser(@RequestBody UpdateUserRecord updateUser, HttpServletRequest request){
        String username = this.validationRequest(request);
        User user = (User) userService.updateUser(username,updateUser);
        UserRecord userResponse = new UserRecord(user);

        return ResponseEntity.ok().body(userResponse);
    }



    @Operation(summary = "Delete a current user", description = "Delete a current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "User not exists.")
    })
    @DeleteMapping()
    public ResponseEntity<HashMap<String,String>> deleteUser(HttpServletRequest request){
        String username = this.validationRequest(request);
        userService.deleteUser(username);

        HashMap<String,String> response= new HashMap<>();
        response.put("message", "User deleted succsessfully");

        return ResponseEntity.ok(response);
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
