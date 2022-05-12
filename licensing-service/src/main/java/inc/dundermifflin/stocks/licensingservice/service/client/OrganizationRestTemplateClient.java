package inc.dundermifflin.stocks.licensingservice.service.client;

import inc.dundermifflin.stocks.licensingservice.config.ClientProperties;
import inc.dundermifflin.stocks.licensingservice.web.dto.OrganizationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
class OrganizationRestTemplateClient implements OrganizationClient{
    private final RestTemplate restTemplate;

    @Override
    public OrganizationDto getOrganization(String organizationId) {
        /*When using a Load Balancer backed RestTemplate, builds the target URL with the Eureka service ID*/
        /*http://{applicationId}/v1/organization/{organizationId}*/
        ResponseEntity<OrganizationDto> restExchange =
                restTemplate.exchange(
                        "http://organization-service/api/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, OrganizationDto.class, organizationId);

        return restExchange.getBody();
    }

    @Override
    public ClientProperties.ClientType type() {
        return ClientProperties.ClientType.REST;
    }
}