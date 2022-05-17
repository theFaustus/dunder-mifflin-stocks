package inc.dundermifflin.stocks.licensingservice.service;

import inc.dundermifflin.stocks.licensingservice.config.context.UserContextHolder;
import inc.dundermifflin.stocks.licensingservice.config.properties.CommentProperties;
import inc.dundermifflin.stocks.licensingservice.model.License;
import inc.dundermifflin.stocks.licensingservice.model.LicenseType;
import inc.dundermifflin.stocks.licensingservice.model.Organization;
import inc.dundermifflin.stocks.licensingservice.repository.LicenseRepository;
import inc.dundermifflin.stocks.licensingservice.repository.OrganizationRedisRepository;
import inc.dundermifflin.stocks.licensingservice.service.client.OrganizationClientResolver;
import inc.dundermifflin.stocks.licensingservice.web.dto.LicenseDto;
import inc.dundermifflin.stocks.licensingservice.web.dto.OrganizationDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
    private final OrganizationRedisRepository organizationRedisRepository;
    private final MessageSource messageSource;
    private final CommentProperties commentProperties;
    private final OrganizationClientResolver resolver;

    @Override
    public void invalidateOrganizationCache(String organizationId){
        organizationRedisRepository.deleteById(organizationId);
    }

    @Override
    public LicenseDto getLicense(String licenseId, String organizationId) {
        LicenseDto licenseDto = LicenseDto.from(licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId).orElseThrow());
        getOrganization(organizationId).ifPresent(licenseDto::setOrganization);
        return licenseDto;
    }

    private Optional<OrganizationDto> getOrganization(String organizationId) {
        try {
            Optional<Organization> redisOrg = organizationRedisRepository.findById(organizationId);
            if (redisOrg.isPresent()) {
                log.info("Successfully retrieved redis entry for organization id {}", organizationId);
                return Optional.of(OrganizationDto.from(redisOrg.get()));
            } else {
                log.info("Retrieving organization from organization-service via HTTP for id {}", organizationId);
                OrganizationDto org = resolver.getClient().getOrganization(organizationId);
                cacheOrganization(org);
                return Optional.of(org);
            }
        } catch (Exception e) {
            log.trace(e.getMessage());
            return Optional.empty();
        }
    }

    private void cacheOrganization(OrganizationDto org) {
        if (org != null) {
            Organization organization = new Organization(org.getId(), org.getName(), org.getContactName(), org.getContactEmail(), org.getContactPhone(), org.getCreatedAt(), org.getUpdatedAt());
            organizationRedisRepository.save(organization);
        }
    }

    @Override
    @Retry(name = "license-service-retry")
    @CircuitBreaker(name = "license-service-cb")
    @RateLimiter(name = "license-service-rlimit")
    @Bulkhead(name = "license-service-bkh", type = Bulkhead.Type.SEMAPHORE)
    public List<LicenseDto> getLicensesByOrganizationId(String organizationId) throws TimeoutException {
        randomlyRunLong(); //simulate circuit breaker behavior
        List<License> byOrganizationId = licenseRepository.findByOrganizationId(organizationId);
        log.info("Retrieved correlation id {}", UserContextHolder.getContext().getCorrelationId());
        return byOrganizationId.stream().map(LicenseDto::from).collect(Collectors.toList());
    }

    private List<LicenseDto> getFallbackLicenses(String organizationId, Throwable t) {
        LicenseDto licenseDto = new LicenseDto();
        licenseDto.setId("n/a");
        licenseDto.setLicenseId("n/a");
        licenseDto.setOrganizationId(organizationId);
        licenseDto.setDescription("n/a");
        licenseDto.setProductName("n/a");
        licenseDto.setLicenseType("n/a");
        return List.of(licenseDto);
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
    @Retry(name = "license-service-retry")
    @CircuitBreaker(name = "license-service-cb")
    @RateLimiter(name = "license-service-rlimit")
    @Bulkhead(name = "license-service-bkh", type = Bulkhead.Type.SEMAPHORE)
    public SuccessResponse createLicense(LicenseDto license, String organizationId, Locale locale) {
        String responseMessage = null;
        if (license != null) {
            Optional<OrganizationDto> organization = getOrganization(organizationId);
            if (organization.isPresent()) {
                license.setOrganizationId(organizationId);
                License entity = new License(license.getLicenseId(), license.getDescription(), license.getOrganizationId(), license.getProductName(), LicenseType.valueOf(license.getLicenseType()), commentProperties.getProperty());
                licenseRepository.save(entity);
                responseMessage = String.format(messageSource.getMessage("license.create.message", null, locale), license.getLicenseId());
            } else {
                responseMessage = String.format(messageSource.getMessage("license.create.failed.message", null, locale), organizationId);
            }
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
