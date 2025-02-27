package project.dio.projeto_pessoal_dio_bootcamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(servers = {@Server(url = "/",description = "Default server url")})
@SpringBootApplication
public class ProjetoPessoalDioBootcampApplication {

	public static void main(String[] args) {
		System.out.println("PG_URL: " + System.getenv("PG_URL"));
		System.out.println("PG_USERNAME: " + System.getenv("PG_USERNAME"));
		System.out.println("PG_PASSWORD: " + System.getenv("PG_PASSWORD"));
		SpringApplication.run(ProjetoPessoalDioBootcampApplication.class, args);
	}

}
