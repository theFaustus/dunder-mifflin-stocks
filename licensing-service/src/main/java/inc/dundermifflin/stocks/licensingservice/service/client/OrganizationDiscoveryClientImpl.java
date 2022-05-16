package inc.dundermifflin.stocks.licensingservice.service.client;

import inc.dundermifflin.stocks.licensingservice.config.ClientProperties;
import inc.dundermifflin.stocks.licensingservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
class OrganizationDiscoveryClientImpl implements OrganizationClient {
    private final DiscoveryClient discoveryClient;
    @LoadBalanced
    private final KeycloakRestTemplate keycloakRestTemplate;

    @Override
    public OrganizationDto getOrganization(String organizationId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");

        if (instances.isEmpty()) return null;
        String serviceUri = String.format("%s/api/v1/organizations/%s", instances.get(0).getUri().toString(), organizationId);

        return keycloakRestTemplate.exchange(serviceUri, HttpMethod.GET, null, OrganizationDto.class, organizationId).getBody();
    }

    @Override
    public SuccessResponse createOrganization(OrganizationDto organizationDto) {
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");

        if (instances.isEmpty()) return null;
        String serviceUri = String.format("%s/api/v1/organizations", instances.get(0).getUri().toString());

        return keycloakRestTemplate.exchange(serviceUri, HttpMethod.POST, new HttpEntity<>(organizationDto), SuccessResponse.class).getBody();
    }

    @Override
    public ClientProperties.ClientType type() {
        return ClientProperties.ClientType.DISCOVERY;
    }
}