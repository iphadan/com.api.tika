package com.api.tika.configs;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("PDFBOX API")
                        .version("1.0")
                        .description("APIs based on the examples given by pdfbox developers, focused on the pdfbox library"))
                         .components(new Components()
                            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                                    .in(SecurityScheme.In.HEADER)
                )
        )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
