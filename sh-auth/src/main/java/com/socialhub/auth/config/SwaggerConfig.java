package com.socialhub.auth.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.*;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI authServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("AuthService API")
                        .description("Authentication Service for Social Hub Platform")
                        .version("v1.0.0")
                        .contact(new Contact().name("Azim Memon")))
                .externalDocs(new ExternalDocumentation()
                        .description("AuthService Documentation")
                        .url("https://github.com/azimmemon2002/socialhub"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", 
                            new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}