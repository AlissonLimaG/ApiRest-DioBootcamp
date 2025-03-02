package project.dio.projeto_pessoal_dio_bootcamp.controllers;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import project.dio.projeto_pessoal_dio_bootcamp.infra.security.TokenService;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.UserLoginRequestRecord;
import project.dio.projeto_pessoal_dio_bootcamp.controllers.records.UserLoginResponseRecord;
import project.dio.projeto_pessoal_dio_bootcamp.services.UserService;

@RestController
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
    public ResponseEntity<User> saveUser(@RequestBody User user){
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User userCreated = userService.saveUser(user);
        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(userCreated.getId())
                        .toUri();
        return ResponseEntity.created(location).body(userCreated);
    }

    @PostMapping("/auth")
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
                return ResponseEntity.badRequest().build();
            }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.findById(id));
    }


}
