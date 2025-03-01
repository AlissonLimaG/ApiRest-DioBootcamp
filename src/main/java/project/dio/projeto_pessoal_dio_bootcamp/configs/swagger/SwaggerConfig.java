package project.dio.projeto_pessoal_dio_bootcamp.configs.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(
                new SecurityRequirement()
                        .addList("Bearer Authentication"))
                        .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                            .info(new Info().title("DecolaTech - Desafio 1")
                                    .description("API para simular uma conta de banco.\n\n" +
                                                 "Código aprimorado por [Alisson Lima](https://www.linkedin.com/in/alisson-gp-lima/).\n\n" +
                                                 "Código fonte disponível em [Github](https://github.com/AlissonLimaG/DecolaTech-Desafio-1).")
                                    .version("1.0"));
    }

}
