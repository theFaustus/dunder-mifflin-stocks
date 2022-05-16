package inc.dundermifflin.stocks.licensingservice.service.client;

import inc.dundermifflin.stocks.licensingservice.config.ClientProperties;
import inc.dundermifflin.stocks.licensingservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
class OrganizationRestTemplateClient implements OrganizationClient{
//    private final RestTemplate restTemplate;
    @LoadBalanced
    private final KeycloakRestTemplate keycloakRestTemplate;
    @Override
    public OrganizationDto getOrganization(String organizationId) {
        /*When using a Load Balancer backed RestTemplate, builds the target URL with the Eureka service ID*/
        /*http://{applicationId}/v1/organization/{organizationId}*/
        ResponseEntity<OrganizationDto> restExchange =
                keycloakRestTemplate.exchange(
                        "http://organization-service/api/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, OrganizationDto.class, organizationId);

        return restExchange.getBody();
    }

    @Override
    public SuccessResponse createOrganization(OrganizationDto organizationDto) {
        return keycloakRestTemplate.exchange("http://organization-service/api/v1/organizations/", HttpMethod.POST, new HttpEntity<>(organizationDto), SuccessResponse.class).getBody();

    }

    @Override
    public ClientProperties.ClientType type() {
        return ClientProperties.ClientType.REST;
    }
}