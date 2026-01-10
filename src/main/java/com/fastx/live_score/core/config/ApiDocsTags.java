package com.fastx.live_score.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Crick live score api by Kritik",
                version = "1.0.0",
                description = "Add and customize Players Teams tournament"
        )
)
@Configuration
public class ApiDocsTags {
        public static final String SEARCH = "Search";
}
