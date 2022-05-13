package inc.dundermifflin.stocks.licensingservice.service;

import inc.dundermifflin.stocks.licensingservice.web.dto.LicenseDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

public interface LicenseService {
    LicenseDto getLicense(String licenseId, String organizationId);

    List<LicenseDto> getLicensesByOrganizationId(String organizationId) throws TimeoutException;

    SuccessResponse createLicense(LicenseDto license, String organizationId, Locale locale);

    SuccessResponse updateLicense(LicenseDto license, String organizationId, Locale locale);

    SuccessResponse deleteLicense(String licenseId, String organizationId, Locale locale);
}
