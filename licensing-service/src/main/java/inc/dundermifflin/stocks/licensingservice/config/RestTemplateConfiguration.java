package inc.dundermifflin.stocks.licensingservice.config;

import inc.dundermifflin.stocks.licensingservice.config.context.UserContextInterceptor;
import org.apache.catalina.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.interceptors(new UserContextInterceptor()).build();
    }
}
