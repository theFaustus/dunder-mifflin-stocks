package inc.dundermifflin.stocks.organizationservice.service;

import inc.dundermifflin.stocks.organizationservice.model.Organization;
import inc.dundermifflin.stocks.organizationservice.repository.OrganizationRepository;
import inc.dundermifflin.stocks.organizationservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.organizationservice.web.error.SuccessResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final MessageSource messageSource;

    @Override
    @CircuitBreaker(name = "organization-service-cb")
    public OrganizationDto findById(String organizationId) {
        return OrganizationDto.from(organizationRepository.findById(organizationId).orElseThrow());
    }

    @Override
    public SuccessResponse createOrganization(OrganizationDto organization, Locale locale) {
        String responseMessage = null;
        if (organization != null) {
            Organization entity = new Organization(organization.getName(), organization.getContactName(), organization.getContactPhone(),  organization.getContactEmail());
            organizationRepository.save(entity);
            responseMessage = String.format(messageSource.getMessage("organization.create.message", null, locale), organization.getId());
        }
        return SuccessResponse.builder().message(responseMessage).build();
    }

    @Override
    public SuccessResponse updateOrganization(OrganizationDto organization, String organizationId, Locale locale) {
        Organization entity = organizationRepository.findById(organizationId).orElseThrow();
        entity.setName(organization.getName());
        entity.setContactName(organization.getContactName());
        entity.setContactEmail(organization.getContactEmail());
        entity.setContactPhone(organization.getContactPhone());
        String responseMessage = String.format(messageSource.getMessage("organization.update.message", null, locale), organization.getId());
        return SuccessResponse.builder().message(responseMessage).build();
    }

    @Override
    public SuccessResponse deleteOrganization(String organizationId, Locale locale) {
        organizationRepository.delete(organizationRepository.findById(organizationId).orElseThrow());
        return SuccessResponse.builder().message(String.format(messageSource.getMessage("organization.delete.message", null, locale), organizationId)).build();
    }
}
