package project.dio.projeto_pessoal_dio_bootcamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
//teste
@OpenAPIDefinition(servers = {@Server(url = "/",description = "Default server url")})
@SpringBootApplication
public class DecolaDesafioBanco {

	public static void main(String[] args) {
		SpringApplication.run(DecolaDesafioBanco.class, args);
	}

}
