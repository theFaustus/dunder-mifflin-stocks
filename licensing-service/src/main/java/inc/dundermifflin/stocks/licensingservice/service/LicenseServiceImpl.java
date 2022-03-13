package inc.dundermifflin.stocks.licensingservice.service;

import inc.dundermifflin.stocks.licensingservice.model.License;
import inc.dundermifflin.stocks.licensingservice.model.LicenseType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
@RequiredArgsConstructor
class LicenseServiceImpl implements LicenseService {

    private final MessageSource messageSource;

    @Override
    public License getLicense(String licenseId, String organizationId) {
        License license = new License();
        license.setId(new Random().nextInt(1000));
        license.setLicenseId(licenseId);
        license.setOrganizationId(organizationId);
        license.setDescription("Software product");
        license.setProductName("DMstock");
        license.setLicenseType(LicenseType.FULL);
        return license;
    }

    @Override
    public String createLicense(License license, String organizationId, Locale locale) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format(messageSource.getMessage("license.create.message", null, locale), license.getLicenseId());
        }
        return responseMessage;
    }

    @Override
    public String updateLicense(License license, String organizationId, Locale locale) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format(messageSource.getMessage("license.update.message", null, locale), license.getLicenseId());
        }
        return responseMessage;
    }

    @Override
    public String deleteLicense(String licenseId, String organizationId, Locale locale) {
        String responseMessage = null;
        responseMessage = String.format(messageSource.getMessage("license.delete.message", null, locale), licenseId, organizationId);
        return responseMessage;
    }
}
