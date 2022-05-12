package inc.dundermifflin.stocks.licensingservice.web;

import inc.dundermifflin.stocks.licensingservice.model.License;
import inc.dundermifflin.stocks.licensingservice.service.LicenseService;
import inc.dundermifflin.stocks.licensingservice.web.dto.LicenseDto;
import inc.dundermifflin.stocks.licensingservice.web.error.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations/{organizationId}/licenses")
public class LicenseController {
    private final LicenseService licenseService;

    @GetMapping("/{licenseId}")
    public ResponseEntity<LicenseDto> getLicense(@PathVariable("organizationId") String organizationId, @PathVariable("licenseId") String licenseId) {
        LicenseDto license = licenseService.getLicense(organizationId, licenseId);
        license.add(linkTo(methodOn(LicenseController.class)
                        .getLicense(organizationId, license.getLicenseId()))
                        .withSelfRel(),
                linkTo(methodOn(LicenseController.class)
                        .createLicense(organizationId, license, null))
                        .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .updateLicense(organizationId, license, null))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
                        .deleteLicense(organizationId, license.getLicenseId(), null))
                        .withRel("deleteLicense"));
        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateLicense(@PathVariable("organizationId") String organizationId, @RequestBody LicenseDto request,
                                                @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.updateLicense(request, organizationId, locale));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createLicense(@PathVariable("organizationId") String organizationId, @RequestBody LicenseDto request,
                                                @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.createLicense(request, organizationId, locale));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<SuccessResponse> deleteLicense(@PathVariable("organizationId") String organizationId, @PathVariable("licenseId") String licenseId,
                                                         @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, organizationId, locale));
    }
}
