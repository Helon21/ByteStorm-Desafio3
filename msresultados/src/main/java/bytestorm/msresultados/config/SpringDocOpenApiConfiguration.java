package bytestorm.msresultados.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("MICRO SERVIÇO - Resultados")
                                .description("API para gestão de resultados de votações de projetos")
                                .version("v1")
                );
    }

}
