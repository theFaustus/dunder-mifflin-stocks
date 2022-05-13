package inc.dundermifflin.stocks.licensingservice.service;

import inc.dundermifflin.stocks.licensingservice.config.CommentProperties;
import inc.dundermifflin.stocks.licensingservice.model.License;
import inc.dundermifflin.stocks.licensingservice.model.LicenseType;
import inc.dundermifflin.stocks.licensingservice.repository.LicenseRepository;
import inc.dundermifflin.stocks.licensingservice.service.client.OrganizationClientResolver;
import inc.dundermifflin.stocks.licensingservice.web.dto.LicenseDto;
import inc.dundermifflin.stocks.licensingservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;
    private final MessageSource messageSource;
    private final CommentProperties commentProperties;
    private final OrganizationClientResolver resolver;

    @Override
    public LicenseDto getLicense(String licenseId, String organizationId) {
        LicenseDto licenseDto = LicenseDto.from(licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId).orElseThrow());
        OrganizationDto organization = resolver.getClient().getOrganization(organizationId);
        licenseDto.setOrganizationDto(organization);
        return licenseDto;
    }

    @Override
    @CircuitBreaker(name = "license-service-cb")
    public List<LicenseDto> getLicensesByOrganizationId(String organizationId) throws TimeoutException {
        randomlyRunLong(); //simulate circuit breaker behavior
        List<License> byOrganizationId = licenseRepository.findByOrganizationId(organizationId);
        return byOrganizationId.stream().map(LicenseDto::from).collect(Collectors.toList());
    }

    private void randomlyRunLong() throws TimeoutException {
        Random random = ThreadLocalRandom.current();
        int randomNum = random.nextInt(3) + 1;
        if (randomNum == 3) sleep();
    }

    private void sleep() throws TimeoutException {
        try {
            Thread.sleep(2000);
            throw new java.util.concurrent.TimeoutException("Oops request took too long.");
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public SuccessResponse createLicense(LicenseDto license, String organizationId, Locale locale) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            License entity = new License(license.getLicenseId(), license.getDescription(), license.getOrganizationId(), license.getProductName(), LicenseType.valueOf(license.getLicenseType()), commentProperties.getProperty());
            licenseRepository.save(entity);
            responseMessage = String.format(messageSource.getMessage("license.create.message", null, locale), license.getLicenseId());
        }
        return SuccessResponse.builder().message(responseMessage).build();
    }

    @Override
    public SuccessResponse updateLicense(LicenseDto license, String organizationId, Locale locale) {
        License entity = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, license.getLicenseId()).orElseThrow();
        entity.setLicenseId(license.getLicenseId());
        entity.setOrganizationId(license.getOrganizationId());
        entity.setDescription(license.getDescription());
        entity.setProductName(license.getProductName());
        entity.setLicenseType(LicenseType.valueOf(license.getLicenseType()));
        String responseMessage = String.format(messageSource.getMessage("license.update.message", null, locale), license.getLicenseId());
        return SuccessResponse.builder().message(responseMessage).build();
    }

    @Override
    public SuccessResponse deleteLicense(String licenseId, String organizationId, Locale locale) {
        licenseRepository.delete(licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId).orElseThrow());
        return SuccessResponse.builder().message(String.format(messageSource.getMessage("license.delete.message", null, locale), licenseId, organizationId)).build();
    }

}
