package com.inha.endgame.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 *  localhost:8080/swagger-ui/index.html
 *
 *  https://tg360.tistory.com/entry/Springdoc-openapi%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-Spring-Boot-%EA%B8%B0%EB%B0%98-API%EC%9D%98-%EB%AC%B8%EC%84%9C-%EC%9E%90%EB%8F%99%ED%99%94
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Doc Example API Document",
                description = "API Document",
                version = "v0.1",
                termsOfService = "http://www.tg360tech.com/terms",
                license = @License(
                        name = "Apache License Version 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                ),
                contact = @Contact(
                        name = "dev",
                        email = "dev@tg360tech.com"
                )
        ),
        tags = {
                @Tag(name = "Common", description = "공통 기능"),
        }
)
@Configuration
@ComponentScan("com.example.test")
public class SwaggerConfig {
}
