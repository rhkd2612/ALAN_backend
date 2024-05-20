package com.inha.endgame.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
                        name = "mumomu",
                        email = "lsb7127@naver.com"
                )
        ),
        tags = {
                @Tag(name = "Common", description = "공통 기능"),
        }
)
@Configuration
@ComponentScan("com.inha.endgame")
public class SwaggerConfig {
}
