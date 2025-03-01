package project.dio.projeto_pessoal_dio_bootcamp.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;

import java.security.AlgorithmConstraints;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String SECRET_KEY = "QueroTrabalharNaAvanade";
    private static final String ISSUER = "Banco-api";

    public String generateToken(User u){
        try{
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(generateCreationDate())
                    .withExpiresAt(generateExpirantionDate())
                    .withSubject(u.getUsername())
                    .sign(algorithm);
        }catch (JWTCreationException ex){
            throw new RuntimeException("Erro ao gerar token JWT.");
        }
    }

    public String validateToken(String  token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT
                    .require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException ex){
            return "";
        }
    }

    public Instant generateExpirantionDate(){
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.ofHours(-3));
    }
    public Instant generateCreationDate(){
        return Instant.now().atZone(ZoneOffset.ofHours(-3)).toInstant();
    }


}
