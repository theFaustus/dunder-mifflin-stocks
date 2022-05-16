package inc.dundermifflin.stocks.organizationservice.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * To avoid circular dependencies in KeycloakSecurityConfiguration
 */
@Configuration
public class KeycloakSpringBootConfiguration {

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

}
