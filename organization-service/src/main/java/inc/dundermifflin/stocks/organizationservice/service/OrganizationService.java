package inc.dundermifflin.stocks.organizationservice.service;

import inc.dundermifflin.stocks.organizationservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.organizationservice.web.error.SuccessResponse;

import java.util.Locale;

public interface OrganizationService {
    OrganizationDto findById(String organizationId);

    SuccessResponse createOrganization(OrganizationDto organization, Locale locale);

    SuccessResponse updateOrganization(OrganizationDto organization, String organizationId, Locale locale);

    SuccessResponse deleteOrganization(String organizationId, Locale locale);
}
