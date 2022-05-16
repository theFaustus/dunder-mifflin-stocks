package inc.dundermifflin.stocks.licensingservice.service.client;

import inc.dundermifflin.stocks.licensingservice.config.ClientProperties;
import inc.dundermifflin.stocks.licensingservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient("organization-service")
public interface OrganizationFeignClient extends OrganizationClient{
    @GetMapping(value = "/api/v1/organizations/{organizationId}", consumes = "application/json")
    OrganizationDto getOrganization(@PathVariable("organizationId") String organizationId);

    @PostMapping(value = "/api/v1/organizations", consumes = "application/json")
    SuccessResponse createOrganization(@RequestBody OrganizationDto request);

    @Override
    default ClientProperties.ClientType type(){
        return ClientProperties.ClientType.FEIGN;
    }
}