package inc.dundermifflin.stocks.licensingservice.web.dto;

import inc.dundermifflin.stocks.licensingservice.model.License;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class LicenseDto extends RepresentationModel<LicenseDto> {
    private String id;
    private String licenseId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenseType;
    private OrganizationDto organization;

    public static LicenseDto from(License license){
        LicenseDto licenseDto = new LicenseDto();
        licenseDto.setId(license.getId());
        licenseDto.setLicenseId(license.getLicenseId());
        licenseDto.setOrganizationId(license.getOrganizationId());
        licenseDto.setDescription(license.getDescription());
        licenseDto.setProductName(license.getProductName());
        licenseDto.setLicenseType(license.getLicenseType().name());
        return licenseDto;
    }

}
