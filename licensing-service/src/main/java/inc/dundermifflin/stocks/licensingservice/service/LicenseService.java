package inc.dundermifflin.stocks.licensingservice.service;

import inc.dundermifflin.stocks.licensingservice.model.License;
import inc.dundermifflin.stocks.licensingservice.web.dto.LicenseDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;

import java.util.List;
import java.util.Locale;

public interface LicenseService {
    LicenseDto getLicense(String licenseId, String organizationId);

    List<LicenseDto> getLicenses(String organizationId);

    SuccessResponse createLicense(LicenseDto license, String organizationId, Locale locale);

    SuccessResponse updateLicense(LicenseDto license, String organizationId, Locale locale);

    SuccessResponse deleteLicense(String licenseId, String organizationId, Locale locale);
}
