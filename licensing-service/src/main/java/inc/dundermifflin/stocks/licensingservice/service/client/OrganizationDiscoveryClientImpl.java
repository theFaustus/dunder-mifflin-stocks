package inc.dundermifflin.stocks.licensingservice.service.client;

import inc.dundermifflin.stocks.licensingservice.config.ClientProperties;
import inc.dundermifflin.stocks.licensingservice.web.dto.OrganizationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
class OrganizationDiscoveryClientImpl implements OrganizationClient {
    private final DiscoveryClient discoveryClient;

    @Override
    public OrganizationDto getOrganization(String organizationId) {
        /*Once we’ve enabled the Spring Discovery Client in the application class via @EnableDiscoveryClient,
        all REST templates managed by the Spring framework will have a Load Balancer enabled interceptor injected
        into those instances. This will change how URLs are created with the RestTemplate class.
        Directly instantiating RestTemplate allows you to avoid this behavior.*/
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");

        if (instances.isEmpty()) return null;
        String serviceUri = String.format("%s/api/v1/organizations/%s", instances.get(0).getUri().toString(), organizationId);

        return restTemplate.exchange(serviceUri, HttpMethod.GET, null, OrganizationDto.class, organizationId).getBody();
    }

    @Override
    public ClientProperties.ClientType type() {
        return ClientProperties.ClientType.DISCOVERY;
    }
}