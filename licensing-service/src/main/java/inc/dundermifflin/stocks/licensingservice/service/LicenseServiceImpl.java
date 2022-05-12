package inc.dundermifflin.stocks.licensingservice.service;

import inc.dundermifflin.stocks.licensingservice.config.CommentProperties;
import inc.dundermifflin.stocks.licensingservice.model.License;
import inc.dundermifflin.stocks.licensingservice.model.LicenseType;
import inc.dundermifflin.stocks.licensingservice.repository.LicenseRepository;
import inc.dundermifflin.stocks.licensingservice.web.dto.LicenseDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;
    private final MessageSource messageSource;
    private final CommentProperties commentProperties;

    @Override
    public LicenseDto getLicense(String licenseId, String organizationId) {
        return LicenseDto.from(licenseRepository.findByOrganizationIdAndLicenseId(licenseId, organizationId).orElseThrow());
    }

    @Override
    public SuccessResponse createLicense(LicenseDto license, String organizationId, Locale locale) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            License entity = new License(license.getLicenseId(), license.getDescription(), license.getOrganizationId(),  license.getProductName(), LicenseType.valueOf(license.getLicenseType()), commentProperties.getProperty());
            licenseRepository.save(entity);
            responseMessage = String.format(messageSource.getMessage("license.create.message", null, locale), license.getLicenseId());
        }
        return SuccessResponse.builder().message(responseMessage).build();
    }

    @Override
    public SuccessResponse updateLicense(LicenseDto license, String organizationId, Locale locale) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format(messageSource.getMessage("license.update.message", null, locale), license.getLicenseId());
        }
        return SuccessResponse.builder().message(responseMessage).build();
    }

    @Override
    public SuccessResponse deleteLicense(String licenseId, String organizationId, Locale locale) {
        licenseRepository.delete(licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId).orElseThrow());
        return SuccessResponse.builder().message(String.format(messageSource.getMessage("license.delete.message", null, locale), licenseId, organizationId)).build();
    }
}
